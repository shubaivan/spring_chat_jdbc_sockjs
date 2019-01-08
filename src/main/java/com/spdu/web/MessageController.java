package com.spdu.web;

import com.spdu.web.websocket.SocketHandler;
import com.spdu.web.websocket.kop.AppConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

@RestController
public class MessageController {
    @RequestMapping(value = "/send")
    public void sendMessage() {
        System.out.println("PING");
        SocketHandler socketHandler = AppConfig.configSocketHandler;
        socketHandler.sendMess(new TextMessage("PING"));
    }

    @GetMapping(value = "/sout")
    public void soutMessage() {
        System.out.println("PING");
    }
}
