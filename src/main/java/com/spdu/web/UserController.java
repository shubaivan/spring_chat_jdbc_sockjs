package com.spdu.web;

import com.spdu.websocket.SocketHandler;
import com.spdu.websocket.WebSocketConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.socket.TextMessage;

@Controller
public class UserController {

    @RequestMapping(value = "/send")
    public void sendMessage() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebSocketConfig.class);
        context.refresh();
        SocketHandler socketHandler = context.getBean(SocketHandler.class);
        socketHandler.sendMess(new TextMessage("PING"));
    }
}
