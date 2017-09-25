package com.spring.chatroom.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.spring.chatroom.model.Request;
import com.spring.chatroom.model.RequestCode;
import com.spring.chatroom.topic.RoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// Todo: Avoid session fixation

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
                    roomManager.joinRoom(request, session);
                    break;
                case LEAVE_ROOM:
                    roomManager.leaveRoom(request, session);
                    break;
                case SEND_MESSAGE:
                    roomManager.sendMessage(request, session);
                    break;
                case VIEW_MEMBERS:
                    roomManager.viewMembers(request, session);
                    break;
                case VIEW_ROOMS:
                    roomManager.viewRooms(request, session);
                    break;
                case MY_ROOMS:
                    roomManager.yourRooms(request, session);
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

}