package com.spring.chatroom.config;

import com.spring.chatroom.handler.ActionHandler;
import com.spring.chatroom.topic.RoomManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Controller
@Configuration
@EnableWebSocket
public class MySocketConfigurer implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor = new HttpSessionHandshakeInterceptor();
        httpSessionHandshakeInterceptor.setCopyAllAttributes(true);
        registry.addHandler(getActionHandler(), "/app/chat").setAllowedOrigins("*").addInterceptors(httpSessionHandshakeInterceptor);
        registry.addHandler(getActionHandler(), "/app/chat").setAllowedOrigins("*").addInterceptors(httpSessionHandshakeInterceptor).withSockJS();
    }

    @Bean
    public WebSocketHandler getActionHandler() {
        return new ActionHandler();
    }


    @Bean
    public RoomManager roomManager() {
        return new RoomManager();
    }

}
