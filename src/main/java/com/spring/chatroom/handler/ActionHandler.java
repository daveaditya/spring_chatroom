package com.spring.chatroom.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.spring.chatroom.model.Request;
import com.spring.chatroom.model.RequestCode;
import com.spring.chatroom.topic.Room;
import com.spring.chatroom.topic.RoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@SuppressWarnings({"unused", "SpringAutowiredFieldsWarningInspection", "SpringJavaAutowiredMembersInspection"})
public class ActionHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionHandler.class.getName());
    private static final Gson GSON = new GsonBuilder().create();

    @Autowired
    private RoomManager roomManager;


    // Maps different request actions
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            LOGGER.debug("GOT ... " + message.getPayload());
            Request request = GSON.fromJson(message.getPayload(), Request.class);
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
                case VIEW_MEMBERS:
                    viewMembers(request, session);
                    break;
                case VIEW_ROOMS:
                    viewRooms(request, session);
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


    // Methods to handle different actions

    // Add member to room
    private void joinRoom(Request request, WebSocketSession session) {
        roomManager.getRoomByName(request.getRoomName()).addViewer(request, session);
    }


    // Rejoins the room if already joined
    private void rejoinRoom(Request request, WebSocketSession session) {

    }


    // Removes the member from room
    private void leaveRoom(Request request, WebSocketSession session) {
        roomManager.getRoomByName(request.getRoomName()).removeViewer(session.getId());
    }


    // Sends 'message received' response and broadcasts the message
    private void sendMessage(Request request, WebSocketSession session) {
        Room room = roomManager.getRoomByName(request.getRoomName());
        room.sendMessage(request, room.getViewerBySId(session.getId()));
    }


    // Sends the current room members to the requesting viewer
    private void viewMembers(Request request, WebSocketSession session) {
        Room room = roomManager.getRoomByName(request.getRoomName());
        room.viewMembers(room.getViewerBySId(session.getId()));
    }


    // Sends the other ongoing rooms to the requesting viewer
    private void viewRooms(Request request, WebSocketSession session) {
        roomManager.viewRooms(request.getRoomName(), roomManager.getRoomByName(request.getRoomName()).getViewerBySId(session.getId()));
    }

}