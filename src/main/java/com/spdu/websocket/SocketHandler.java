package com.spdu.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            for (WebSocketSession webSocketSession : sessions) {
                webSocketSession.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            for (WebSocketSession webSocketSession : sessions) {
                webSocketSession.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }
}
