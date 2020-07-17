package com.sean.server.endpoint;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
@Slf4j
@ServerEndpoint("/breezedata")
public class BreezeDataSocketEndpoint {
    
    private static final List<Session> wsConnection = new CopyOnWriteArrayList<>();

    @OnMessage
    public void handleMessage(String message) {
        log.info("message: {}", message);
        String mergeRawData = mergeBreezeData(message);
        sendMessageToBreezePa(mergeRawData);
    }

    @OnOpen
    public void onOpen(Session session) {
        log.debug("session:{}, connecting...", session.getId());
        wsConnection.add(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        log.debug("session:{}, error, close.", session.getId());
        wsConnection.remove(session);
    }

    @OnClose
    public void connectionClosed(Session session) throws Exception {
        log.debug("session:{}, closed.", session.getId());
        wsConnection.remove(session);
    }

    /**
     * 给所有其他在线client发送消息
     */
    public void sendMessageToBreezePa(String message) {
        for (Session conn : wsConnection) {
            conn.getAsyncRemote().sendText(message);
        }
    }

    private String mergeBreezeData(String message) {
        return message;
    }
}
