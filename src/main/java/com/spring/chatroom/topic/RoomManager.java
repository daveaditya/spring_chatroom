package com.spring.chatroom.topic;


import com.spring.chatroom.model.Message;
import com.spring.chatroom.model.Request;
import com.spring.chatroom.model.Response;
import com.spring.chatroom.model.ResponseCode;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


// Manages multiple topics
@SuppressWarnings({"unused", "WeakerAccess", "JavaDoc"})
public class RoomManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomManager.class);

    // Stores currently created rooms
    private final ConcurrentMap<String, Room> currentRooms = new ConcurrentHashMap<>();

    // Stores the JSESSIONID as key and a list of WebSocketSession Id and roomName pairs
    private final ConcurrentMap<String, List<Pair<String, String>>> sessionToRooms = new ConcurrentHashMap<>();

    // A Simple Date Formatter object to help in formatting date
    private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("HH:mm:ss");


    /**
     * Returns true if there are any rooms other than the one passed
     *
     * @param roomName Name of the present room
     * @return
     */
    private boolean areRoomsThereExcept(String roomName) {
        return !(currentRooms.get(roomName) != null && currentRooms.size() == 1);
    }


    /**
     * Returns the room corresponding to given name
     *
     * @param roomName Name of the desired room
     * @return
     */
    public Room getRoomByName(String roomName) {
        Room present = currentRooms.get(roomName);
        if (present == null) {
            present = new Room(roomName);
            currentRooms.put(roomName, present);
        }
        return present;
    }


    /**
     * Returns a list of current active rooms
     *
     * @return
     */
    public List<String> getCurrentRoomNames() {
        List<String> roomNames = new ArrayList<>();
        for (Room r : currentRooms.values()) {
            roomNames.add(r.getRoomName());
        }
        return roomNames;
    }


    /**
     * Returns the list of rooms except the given onces
     *
     * @param exceptRoom Name of room that should not be included
     * @return
     */
    public List<String> getCurrentRoomsNamesExcept(String exceptRoom) {
        List<String> roomNames = new ArrayList<>();
        for (Room r : currentRooms.values()) {
            if (!r.getRoomName().equalsIgnoreCase(exceptRoom)) {
                roomNames.add(r.getRoomName());
            }
        }
        return roomNames;
    }


    /**
     * Adds a pair of websocket session id and room name to corresponding http session id in -> sessionToRooms
     *
     * @param httpSessionId
     * @param webSocketSessionId
     * @param roomName
     */
    private void addSessionAndRoom(String httpSessionId, String webSocketSessionId, String roomName) {
        if (sessionToRooms.containsKey(httpSessionId)) {
            List<Pair<String, String>> lrooms = sessionToRooms.get(httpSessionId);
            if (lrooms == null) {
                lrooms = new ArrayList<>();
            }
            lrooms.add(new Pair<>(webSocketSessionId, roomName));
        } else {
            List<Pair<String, String>> lrooms = new ArrayList<>();
            lrooms.add(new Pair<>(httpSessionId, roomName));
            sessionToRooms.put(httpSessionId, lrooms);
        }
    }


    /**
     * Removes a pair of websocket session id and room name from the corresponsing http session id from -> sessionToRooms
     *
     * @param httpSessionId
     * @param webSocketSessionId
     * @param roomName
     */
    private void removeFromSessionAndRoom(String httpSessionId, String webSocketSessionId, String roomName) {
        if (sessionToRooms.containsKey(httpSessionId)) {
            List<Pair<String, String>> lrooms = sessionToRooms.get(httpSessionId);
            for (int i = 0; i < sessionToRooms.size(); i++) {
                Pair<String, String> pair = lrooms.get(i);
                if (pair.getValue().equalsIgnoreCase(roomName)) {
                    lrooms.remove(i);
                    break;
                }
            }
        }
    }


    private List<Pair<String, String>> getSessionRoomsList(String httpSessionId) {
        return sessionToRooms.get(httpSessionId);
    }


    /**
     * @param request
     * @param session
     */
    public void joinRoom(Request request, WebSocketSession session) {
        if (getRoomByName(request.getRoomName()).addViewer(request, session)) {
            addSessionAndRoom(
                    session.getAttributes().get("HTTP.SESSION.ID").toString(),
                    session.getId(),
                    request.getRoomName()
            );
        }
    }


    /**
     * Sends a list of rooms to the viewer, that he / she
     * is present in
     *
     * @param request
     * @param session
     */
    public void yourRooms(Request request, WebSocketSession session) {
        List<String> rooms = new ArrayList<>();
        for (Pair<String, String> pair : getSessionRoomsList(session.getAttributes().get("HTTP.SESSION.ID").toString())) {
            rooms.add(pair.getValue());
        }
        Room userRoom = getRoomByName(request.getRoomName());
        Response response = new Response();
        response.setResponseCode(ResponseCode.YOUR_ROOM_LIST.getCode());
        response.setResponseDesc(ResponseCode.YOUR_ROOM_LIST.getDescription());
        response.setSessionId(session.getId());
        response.setMessage(new Message(
                request.getRoomName(),
                rooms,
                DATE_FORMATTER.format(new Date())
        ));
        userRoom.sendTo(userRoom.getViewerBySId(session.getId()), response);
    }


    /**
     * Sends the list of rooms to the viewer from given room
     *
     * @param request
     * @param session
     */
    public void viewRooms(Request request, WebSocketSession session) {
        String fromRoom = request.getRoomName();
        Viewer viewer = getRoomByName(request.getRoomName()).getViewerBySId(session.getId());
        Response response = new Response();
        if (areRoomsThereExcept(fromRoom)) {
            response.setResponseCode(ResponseCode.ROOM_LIST.getCode());
            response.setResponseDesc(ResponseCode.ROOM_LIST.getDescription());
            response.setSessionId(viewer.getSessionId());
            response.setMessage(
                    new Message(
                            fromRoom,
                            getCurrentRoomsNamesExcept(fromRoom),
                            DATE_FORMATTER.format(new Date())
                    )
            );
        } else {
            response.setResponseCode(ResponseCode.NO_ROOMS.getCode());
            response.setResponseDesc(ResponseCode.NO_ROOMS.getDescription());
            response.setSessionId(viewer.getSessionId());
        }
        this.getRoomByName(fromRoom).sendTo(viewer, response);
    }


    /**
     * Removes the requester from the said room
     *
     * @param request
     * @param session
     */
    public void leaveRoom(Request request, WebSocketSession session) {
        if (getRoomByName(request.getRoomName()).removeViewer(session.getId())) {
            removeFromSessionAndRoom(
                    session.getAttributes().get("HTTP.SESSION.ID").toString(),
                    session.getId(),
                    request.getRoomName()
            );
        }
    }


    /**
     * Sends back message to the the requester that the message was received
     *
     * @param request
     * @param session
     */
    public void sendMessage(Request request, WebSocketSession session) {
        Room room = getRoomByName(request.getRoomName());
        room.sendMessage(request, room.getViewerBySId(session.getId()));
    }


    /**
     * Send a list of members of the requester's room to the requester
     *
     * @param request
     * @param session
     */
    public void viewMembers(Request request, WebSocketSession session) {
        Room room = getRoomByName(request.getRoomName());
        room.viewMembers(room.getViewerBySId(session.getId()));
    }

}