package com.work.plat.ws;

import com.alibaba.fastjson.JSONObject;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import com.work.plat.utils.Base64IdUtil;
import com.work.plat.utils.DouBaoUtil;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/chat/{openId}")
public class ChatWebSocket {

    private Map<String, Session> userMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "openId") String openId) {
        // 连接打开逻辑
        log.info("用户:{}建立连接", openId);
        userMap.put(openId, session);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam(value = "openId") String openId) {
        // 收到客户端发送消息
        log.info("openId:{} message:{}",openId, message);
        JSONObject request = JSONObject.parseObject(message);
        String question = request.getString("question");
        String shortId = Base64IdUtil.generateShortId();
        // 调用AI模型获取答案, 发送给前端
        Flowable<ChatCompletionChunk> chunkFlowable = DouBaoUtil.streamingChat2(question);
        chunkFlowable.doOnError(throwable ->
                log.info("获取答案失败:{}",throwable)
        ).blockingForEach(
                        choice -> {
                            if (choice.getChoices().size() > 0) {
                                Object content = choice.getChoices().get(0).getMessage().getContent();
                                sendMessage(session,shortId,content);
                            }
                        });
    }

    private void sendMessage(Session session, String shortId, Object content) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("q", shortId);
            jsonObject.put("a",String.valueOf(content));
            session.getBasicRemote().sendText(jsonObject.toJSONString()); // 逐字发送
        } catch (Exception e) {
            log.error("推送数据给客户端失败:",e);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam(value = "openId") String openId) {
        // 连接关闭逻辑
        log.info("用户:{}关闭连接", openId);
        userMap.remove(openId);
    }
}

