package com.work.ai.ws;

import com.work.ai.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
@ServerEndpoint("/chat/{openId}")
public class ChatWebSocket {

    public static final ConcurrentMap<String, Session> sessionMap = new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "openId") String openId) {
        // 连接打开逻辑
        log.info("用户:{}建立连接", openId);
        ChatService chatService = (ChatService) SpringContextUtil.getBean(ChatService.class);
        chatService.openConnect(session, openId);
    }

    @OnMessage
    public void onMessage(@PathParam(value = "openId") String openId, String message, Session session) {
        // 收到客户端发送消息
        log.info("openId:{} message:{}", openId, message);
        ChatService chatService = (ChatService) SpringContextUtil.getBean(ChatService.class);
        chatService.handle(message, session, openId);
    }


    @OnClose
    public void onClose(Session session, @PathParam(value = "openId") String openId) {
        // 连接关闭逻辑
        log.info("用户:{}关闭连接", openId);
        sessionMap.remove(openId);
    }

    @OnError
    public void onError(Session session, @PathParam(value = "openId") String openId,Throwable error) {
        log.error("sessionId:{}, openId:{}",session.getId(),openId);
        log.error("连接异常:",error);
    }
}

