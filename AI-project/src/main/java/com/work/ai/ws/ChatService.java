package com.work.ai.ws;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.volcengine.ark.runtime.model.Usage;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import com.work.ai.constants.ApiResult;
import com.work.ai.constants.ResultCodeEnum;
import com.work.ai.entity.bo.SysUserRemainingDO;
import com.work.ai.entity.bo.UserDO;
import com.work.ai.enums.RoleTypeEnum;
import com.work.ai.mapper.AiRoleMapper;
import com.work.ai.mapper.ImChatMapper;
import com.work.ai.mapper.SysUserRemainingMapper;
import com.work.ai.utils.DouBaoUtil;
import com.work.ai.entity.bo.ImChatDO;
import com.work.ai.mapper.UserMapper;
import com.work.ai.utils.Base64IdUtil;
import com.work.ai.utils.IDUtil;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class ChatService {

    @Autowired
    ImChatMapper imChatMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SysUserRemainingMapper sysUserRemainingMapper;
    @Autowired
    AiRoleMapper aiRoleMapper;


    public void openConnect(Session session, String openId) {
        try {
            if (StrUtil.isEmpty(openId)) {
                session.close();
            }
            UserDO userDO = userMapper.selectByOpenId(openId);
            if (userDO == null) {
                session.close();
            } else {
                ChatWebSocket.sessionMap.put(openId,session);
            }
        } catch (Exception e) {
            log.info("异常:",e);
        }
    }

    @Transactional
    public void handle(String request, Session session, String openId) {
        if (StrUtil.isEmpty(openId)) {
            return;
        }
        // 获取用户
        UserDO userDO = userMapper.selectByOpenId(openId);
        if (userDO == null) {
            return;
        }
        // 检查余额
        Long number = sysUserRemainingMapper.selectNumberByOpenId(openId);

        if (number == null) {
            SysUserRemainingDO sysUserRemainingDO = new SysUserRemainingDO();
            sysUserRemainingDO.setOpenId(openId);
            sysUserRemainingDO.setCreateTime(new Date());
            sysUserRemainingDO.setIncreaseTime(new Date());
            sysUserRemainingDO.setUpdateTime(new Date());
            sysUserRemainingDO.setNumber(10L);
            sysUserRemainingMapper.insert(sysUserRemainingDO);
        } else {
            // 余额不足
            if (number <= 0) {
                sendFailMessage(openId, request,"生成失败，额度不足。");
                return;
            }
        }

        // 获取答案 推送数据
        getResponse(request,openId);
        // 扣减余额
        sysUserRemainingMapper.deductionNumber(openId,1);
    }

    /**
     * 获取答案
     *
     * @param openId
     * @param request
     */
    private void getResponse(String request,String openId) {
        JSONObject jsonObject = JSONObject.parseObject(request);
        String question = jsonObject.getString("question");
        String messageId = jsonObject.getString("messageId");
        String type = jsonObject.getString("type");
        // 获取AI角色
        String roleKeyWorld = getRoleKey(type);
        // 历史数据
        Map<String,String> map = new HashMap<>();
        if (StrUtil.isNotEmpty(messageId)) {
            handleHistoryChat(map,type,messageId);
        } else {
            messageId = IDUtil.generatorSnowId();
        }
        AtomicLong promptTokens = new AtomicLong(0L);
        AtomicLong completionTokens = new AtomicLong(0L);
        AtomicLong totalTokens = new AtomicLong(0L);
        String shortId = Base64IdUtil.generateShortId();
        AtomicBoolean allSuccess = new AtomicBoolean(true);
        StringBuilder sb = new StringBuilder();
        List<String> error = new ArrayList<>();
        // 调用AI模型获取答案, 发送给前端
        Flowable<ChatCompletionChunk> chunkFlowable = DouBaoUtil.streamingChat2(map, roleKeyWorld,question);
        String finalMessageId = messageId;
        chunkFlowable.doOnError(throwable -> {
            log.info("获取答案失败:{}", throwable);
            error.add(ExceptionUtil.getMessage(throwable));
            allSuccess.set(false);
        }).doOnComplete(() -> {
            record(openId ,shortId, finalMessageId,type ,
                    question ,sb.toString(),allSuccess, error,
                    promptTokens.longValue(),completionTokens.longValue(), totalTokens.longValue());
        }).blockingForEach(
                choice -> {
                    Usage usage = choice.getUsage();
                    if (usage != null) {
                        promptTokens.set(usage.getPromptTokens());
                        completionTokens.set(usage.getCompletionTokens());
                        totalTokens.set(usage.getTotalTokens());
                    }
                    if (choice.getChoices().size() > 0) {
                        Object content = choice.getChoices().get(0).getMessage().getContent();
                        String message = String.valueOf(content);
                        sendMessage(openId, finalMessageId,type, shortId, message);
                        sb.append(message);
                    }
                });
    }


    private String getRoleKey(String type) {
        String roleKey;
        if (StrUtil.isNotEmpty(type) && !RoleTypeEnum.AI.getType().equals(type)) {
            roleKey = aiRoleMapper.getRoleKey(type);
        } else {
            roleKey = RoleTypeEnum.AI.getRoleKey();
        }
        return roleKey;
    }

    private void handleHistoryChat(Map<String,String> map, String type, String messageId) {
        List<ImChatDO> imChatDOS = imChatMapper.selectMessage(type, messageId);
        if (CollUtil.isNotEmpty(imChatDOS)) {
            for (ImChatDO chatDO : imChatDOS) {
                map.put(chatDO.getQuestion(), chatDO.getAnswer());
            }
        }
    }

    /**
     * 发送消息
     *
     * @param openId
     * @param shortId
     * @param message
     */
    private void sendMessage(String openId,String messageId,String type, String shortId, String message) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageId",messageId);
            jsonObject.put("type",type);
            jsonObject.put("q", shortId);
            jsonObject.put("a", message);
            ApiResult<JSONObject> data = ApiResult.data(jsonObject);
            pushMessage(openId,data);
        } catch (Exception e) {
            log.error("推送数据给客户端失败:", e);
        }
    }

    private void sendFailMessage(String openId, String request, String errorMessage) {
        JSONObject jsonObject = JSONObject.parseObject(request);
        JSONObject error = new JSONObject();
        error.put("type",jsonObject.getString("type"));
        ApiResult data = ApiResult.data(ResultCodeEnum.FAIL.getCode(), error, errorMessage);
        pushMessage(openId,data);
    }

    private void pushMessage(String openId, ApiResult data) {
        try {
            Session session = ChatWebSocket.sessionMap.get(openId);
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(JSON.toJSONString(data)); // 逐字发送
            }
        } catch (Exception e) {
            log.error("推送数据给客户端失败:", e);
        }
    }

    /**
     * 请求信息记录
     *
     * @param openId
     * @param question
     * @param message
     * @param success
     * @param error
     */
    private void record(String openId,String shortId,String messageId ,String type, String question, String message, AtomicBoolean success, List<String> error,Long promptTokens,Long completionTokens, Long totalTokens) {
        ImChatDO imChatDO = new ImChatDO();
        imChatDO.setOpenId(openId);
        imChatDO.setShortId(shortId);
        imChatDO.setMessageId(messageId);
        imChatDO.setQuestion(question);
        imChatDO.setAnswer(message);
        imChatDO.setType(type);
        imChatDO.setStatus(success.get() ? 0 : 1);
        imChatDO.setPromptTokens(promptTokens);
        imChatDO.setCompletionTokens(completionTokens);
        imChatDO.setTotalTokens(totalTokens);
        imChatDO.setErrorMessage(CollUtil.isEmpty(error)? null: JSONArray.toJSONString(error));
        imChatDO.setCreateTime(new Date());
        imChatMapper.insert(imChatDO);
    }
}
