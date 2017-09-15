package com.spring.chatroom.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


public class MessageHttpInterceptor implements HandshakeInterceptor {

    private Map<String, WebSocketSession> sessionMap = new HashMap<>();

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        HttpSession httpSession = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getSession();
        sessionMap.putIfAbsent(httpSession.getId(), null);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
