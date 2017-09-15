package com.spring.chatroom.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.spring.chatroom.model.Request;
import com.spring.chatroom.model.Response;
import com.spring.chatroom.topic.RoomManager;
import com.spring.chatroom.topic.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;


@SuppressWarnings({"unused", "SpringAutowiredFieldsWarningInspection", "SpringJavaAutowiredMembersInspection"})
public class ActionHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionHandler.class.getName());
    private static final Gson GSON = new GsonBuilder().create();

    @Autowired
    private RoomManager roomManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Request request = GSON.fromJson(message.getPayload(), Request.class);
        switch (request.getAction()) {
            case "joinRoom":
                joinRoom(request, session);
                break;
            case "leaveRoom":
                break;
            case "viewMates":
                break;
            case "sendMessage":
                sendMessage(request, session);
                break;
            default:
                // Show error
                LOGGER.info("Some error in request ... {}", request.toString());
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }


    // Methods to handle different actions

    // Add member to room
    private void joinRoom(Request request, WebSocketSession session) {
        Viewer newViewer = new Viewer(request.getNickName(), session);
        roomManager.getRoomByName(request.getRoomName()).addViewer(newViewer);
    }

    private void sendMessage(Request request, WebSocketSession session) {
        try {
            roomManager.getRoomByName(request.getRoomName()).sendToAll(request.getMessage());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

}