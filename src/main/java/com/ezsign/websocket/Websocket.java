package com.ezsign.websocket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/loading/{webSocketKey}")
public class Websocket {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("webSocketKey") String webSocketKey) throws IOException {
        sessions.put(webSocketKey, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("webSocketKey") String webSocketKey) throws IOException {
        sessions.remove(webSocketKey);
    }

    public static void sendMessage(String webSocketKey, String message) {
        Session session = sessions.get(webSocketKey);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
