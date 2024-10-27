package com.work.plat.ws;

import com.alibaba.fastjson.JSONObject;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChunk;
import com.work.plat.utils.Base64IdUtil;
import com.work.plat.utils.DouBaoUtil;
import com.work.plat.utils.SpringContextUtil;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Slf4j
@Component
@ServerEndpoint("/chat/{openId}")
public class ChatWebSocket {


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "openId") String openId) {
        // 连接打开逻辑
        log.info("用户:{}建立连接", openId);
        ChatService chatService = (ChatService) SpringContextUtil.getBean(ChatService.class);
        chatService.openConnect(session, openId);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam(value = "openId") String openId) {
        // 收到客户端发送消息
        log.info("openId:{} message:{}", openId, message);
        ChatService chatService = (ChatService) SpringContextUtil.getBean(ChatService.class);
        chatService.handle(message, session, openId);
    }


    @OnClose
    public void onClose(Session session, @PathParam(value = "openId") String openId) {
        // 连接关闭逻辑
        log.info("用户:{}关闭连接", openId);
    }
}

