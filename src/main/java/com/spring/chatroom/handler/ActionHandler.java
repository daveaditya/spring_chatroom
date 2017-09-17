package com.spring.chatroom.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.spring.chatroom.model.Request;
import com.spring.chatroom.model.RequestCode;
import com.spring.chatroom.model.Response;
import com.spring.chatroom.model.ResponseCode;
import com.spring.chatroom.topic.Room;
import com.spring.chatroom.topic.RoomManager;
import com.spring.chatroom.topic.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


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

    // Maps different request actions
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            LOGGER.debug("GOT ... " + message.getPayload());
            Request request = GSON.fromJson(message.getPayload(), Request.class);
            LOGGER.debug("GOT ... " + request.toString());
            switch (RequestCode.fromAction(request.getAction())) {
                case JOIN_ROOM:
                    joinRoom(request, session);
                    break;
                case LEAVE_ROOM:
                    leaveRoom(request, session);
                    break;
                case SEND_MESSAGE:
                    sendMessage(request, session);
                    break;
                default:
                    // Show error
                    LOGGER.info("Some error in request ... {}", request.toString());
                    break;
            }
        } catch (JsonSyntaxException exc) {
            exc.printStackTrace();
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


    // Removes the member from room
    private void leaveRoom(Request request, WebSocketSession session) {
        roomManager.getRoomByName(request.getRoomName()).removeViewer(session.getId());
    }


    // Sends 'message received' response and broadcasts the message
    private void sendMessage(Request request, WebSocketSession session) {
        // Acknowledge message received
        Room room = roomManager.getRoomByName(request.getRoomName());
        Response response = new Response();
        response.setResponseCode(ResponseCode.MESSAGE_RECEIVED.getCode());
        response.setSessionId(session.getId());
        response.setResponseDesc(ResponseCode.MESSAGE_RECEIVED.getDescription());
        response.setMessage(request.getMessage());
        room.sendTo(room.getViewerBySId(session.getId()), response);

        // Forward message to others
        response.setResponseCode(ResponseCode.FORWARD_MESSAGE.getCode());
        response.setResponseDesc(ResponseCode.FORWARD_MESSAGE.getDescription());
        room.sendExcept(room.getViewerBySId(session.getId()), response);
    }

}