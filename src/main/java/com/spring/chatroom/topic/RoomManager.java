package com.spring.chatroom.topic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


// Manages multiple topics
@SuppressWarnings({"unused", "WeakerAccess"})
public class RoomManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomManager.class);

    // Stores currently created rooms
    private final ConcurrentMap<String, Room> currentRooms = new ConcurrentHashMap<>();


    public Room getRoomByName(String roomName) {
        Room present = currentRooms.get(roomName);
        if (present == null) {
            present = new Room(roomName);
            currentRooms.put(roomName, present);
        }
        return present;
    }


    public List<String> getCurrentRoomNames() {
        List<String> roomNames = new ArrayList<>();
        for (Room r :
                currentRooms.values()) {
            roomNames.add(r.getRoomName());
        }
        return roomNames;
    }

}
