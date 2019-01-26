package com.spdu.web.websocket;

import com.spdu.web.websocket.kop.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    public SocketHandler socketHandler;

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        socketHandler = new SocketHandler();
        registry.addHandler(mySocketHandler(), "/all").setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxBinaryMessageBufferSize(1024000);
        return container;
    }

    @Bean
    public SocketHandler mySocketHandler() {
        AppConfig.configSocketHandler = socketHandler;
        return socketHandler;
    }
}