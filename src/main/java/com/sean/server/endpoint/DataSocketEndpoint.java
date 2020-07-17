package com.sean.server.endpoint;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint("/rawdata")
public class DataSocketEndpoint {
    private static final List<Session> wsConnection = new CopyOnWriteArrayList<>();

    @OnMessage
    public void handleMessage(Session session, String message) {
        log.debug("session: {}, message: {}", session.getId(), message);
        String mergeRawData = mergeBreezeData(message);
        sendMessageToBreezePa(session, mergeRawData);
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
    public void sendMessageToBreezePa(Session session, String message) {
        for (Session conn : wsConnection) {
            if (conn.isOpen() && !conn.getId().equals(session.getId())) {
                conn.getAsyncRemote().sendText(message);
            }
        }
    }

    private String mergeBreezeData(String message) {
        return message;
    }
}
