package com.spdu.web.websocket;

import com.spdu.bll.models.CustomUserDetails;
import com.spdu.domain_models.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getUser();
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
        String userName = cud.getUser().getUserName();
        logger.info("Received a new web socket connection " + userName);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("User Disconnected : " + username);

            Message chatMessage = new Message();
            chatMessage.setText(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}

