package com.spdu.web.controllers;

import com.spdu.web.websocket.SocketHandler;
import com.spdu.web.websocket.kop.AppConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

@RestController
public class MessageController {

    @RequestMapping(value = "/send")
    public void sendMessage() {
        SocketHandler socketHandler = AppConfig.configSocketHandler;
        socketHandler.sendMess(new TextMessage("PING"));
    }
}
