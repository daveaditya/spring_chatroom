package com.spring.chatroom.topic;


import com.spring.chatroom.model.Message;
import com.spring.chatroom.model.Response;
import com.spring.chatroom.model.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Sends the list of rooms to the viewer from given room
     *
     * @param fromRoom Name of the room viewer corresponds to
     */
    public void viewRooms(String fromRoom, Viewer viewer) {
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

}
