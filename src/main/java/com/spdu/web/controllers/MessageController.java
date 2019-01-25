package com.spdu.web.controllers;

import com.spdu.web.websocket.SocketHandler;
import com.spdu.web.websocket.kop.AppConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.TextMessage;

@Controller
public class MessageController {

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public void sendMessage() {
        System.out.println("PING");
        SocketHandler socketHandler = AppConfig.configSocketHandler;
        socketHandler.sendMess(new TextMessage("PING"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/sout")
    public void soutMessage() {
        System.out.println("PING");
    }
}
