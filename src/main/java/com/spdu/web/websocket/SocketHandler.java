package com.spdu.web.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        sendMessageLogic(message);
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        sendMessageLogic(message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    public void sendMess(TextMessage message) {
        sendMessageLogic(message);
    }

    private void sendMessageLogic(AbstractWebSocketMessage abstractWebSocketMessage) {
        try {
            for (WebSocketSession webSocketSession : sessions) {
                try {
                    if (webSocketSession.isOpen()) {
                        webSocketSession.sendMessage(abstractWebSocketMessage);
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                    webSocketSession.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
