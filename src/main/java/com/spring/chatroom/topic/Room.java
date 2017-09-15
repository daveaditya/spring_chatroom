package com.spring.chatroom.topic;

import com.spring.chatroom.model.Message;
import com.spring.chatroom.model.Response;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


// Represents a single topic
public class Room {

    private final String roomName;
    private final ConcurrentMap<String, Viewer> peopleInRoom = new ConcurrentHashMap<>();


    public Room(final String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public Collection<Viewer> getViewers() {
        return peopleInRoom.values();
    }

    /**
     * Add viewer to current room
     * @param viewer
     */
    public void addViewer(Viewer viewer) {
        peopleInRoom.put(viewer.getSessionId(), viewer);
        try {
            Response respToViewer = new Response();
            respToViewer.setResponseCode(200);
            respToViewer.setResponseDesc("JOINED SUCCESSFULLY");
            sendExcept(viewer, new Response());
            sendTo(viewer, respToViewer);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }


    /**
     * Remove viewer from current room
     * @param viewer
     */
    public void removeViewer(Viewer viewer) {
        peopleInRoom.remove(viewer.getSessionId());
        try {
            sendExcept(viewer, new Response());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }


    /**
     * Sends the provided response to everyone in room except the given one
     * @param exception
     * @throws IOException
     */
    private void sendExcept(Viewer exception, Response response) throws IOException {
        for (Viewer person : peopleInRoom.values()) {
            if (!person.getSessionId().equalsIgnoreCase(exception.getSessionId())) {
                person.sendMessage(response);
            }
        }
    }


    /**
     * Send message to a particular viewer
     * @param viewer
     * @throws IOException
     */
    public void sendTo(Viewer viewer, Response response) throws IOException {
        viewer.sendMessage(response);
    }


    /**
     * Sends message to all the members in the room
     * @param msg
     * @throws IOException
     */
    public void sendToAll(Message msg) throws IOException {
        Response response = new Response();
        response.setResponseCode(201);
        response.setResponseDesc("OK");
        response.setMessage(msg);
        for (Viewer person : getViewers()) {
            person.sendMessage(response);
        }
    }

}