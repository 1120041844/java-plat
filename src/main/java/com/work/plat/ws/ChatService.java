package com.work.plat.ws;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import com.work.plat.entity.bo.ImChatDO;
import com.work.plat.entity.bo.SysUserRemainingDO;
import com.work.plat.entity.bo.UserDO;
import com.work.plat.mapper.ImChatMapper;
import com.work.plat.mapper.SysUserRemainingMapper;
import com.work.plat.mapper.UserMapper;
import com.work.plat.utils.Base64IdUtil;
import com.work.plat.utils.DouBaoUtil;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class ChatService {

    @Autowired
    ImChatMapper imChatMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SysUserRemainingMapper sysUserRemainingMapper;

    @Transactional
    public void handle(String request, Session session, String openId) {
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
            sysUserRemainingDO.setNumber(10L);
            sysUserRemainingMapper.insert(sysUserRemainingDO);
        } else {
            // 余额不足
            if (number <= 0) return;
        }

        // 获取答案 推送数据
        getResponse(request,session,openId);
        // 扣减余额
        sysUserRemainingMapper.deductionNumber(openId,1);
    }

    /**
     * 获取答案
     *
     * @param session
     * @param request
     */
    private void getResponse(String request,Session session,String openId) {
        AtomicBoolean allSuccess = new AtomicBoolean(true);
        StringBuilder sb = new StringBuilder();
        List<String> error = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(request);
        String question = jsonObject.getString("question");
        String shortId = Base64IdUtil.generateShortId();
        // 调用AI模型获取答案, 发送给前端
        Flowable<ChatCompletionChunk> chunkFlowable = DouBaoUtil.streamingChat2(question);
        chunkFlowable.doOnError(throwable -> {
            log.info("获取答案失败:{}", throwable);
            error.add(ExceptionUtil.getMessage(throwable));
            allSuccess.set(false);
        }).doOnComplete(() -> {
            record(openId ,shortId,request ,sb.toString(),allSuccess, error);
        }).blockingForEach(
                choice -> {
                    if (choice.getChoices().size() > 0) {
                        Object content = choice.getChoices().get(0).getMessage().getContent();
                        String message = String.valueOf(content);
                        sendMessage(session, shortId, message);
                        sb.append(message);
                    }
                });
    }

    /**
     * 发送消息
     *
     * @param session
     * @param shortId
     * @param message
     */
    private void sendMessage(Session session, String shortId, String message) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("q", shortId);
            jsonObject.put("a", message);
            session.getBasicRemote().sendText(jsonObject.toJSONString()); // 逐字发送
        } catch (Exception e) {
            log.error("推送数据给客户端失败:", e);
        }
    }

    /**
     * 请求信息记录
     *
     * @param openId
     * @param request
     * @param message
     * @param success
     * @param error
     */
    private void record(String openId,String shortId,String request, String message, AtomicBoolean success, List<String> error) {
        ImChatDO imChatDO = new ImChatDO();
        imChatDO.setOpenId(openId);
        imChatDO.setShortId(shortId);
        imChatDO.setQuestion(request);
        imChatDO.setAnswer(message);
        imChatDO.setStatus(success.get() ? 0 : 1);
        imChatDO.setErrorMessage(CollUtil.isEmpty(error)? null: JSONArray.toJSONString(error));
        imChatDO.setCreateDate(new Date());
        imChatMapper.insert(imChatDO);
    }
}
