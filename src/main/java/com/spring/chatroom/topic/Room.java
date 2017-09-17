package com.spring.chatroom.topic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spring.chatroom.model.Message;
import com.spring.chatroom.model.Response;
import com.spring.chatroom.model.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


// Represents a single topic
@SuppressWarnings({"WeakerAccess", "unused", "JavaDoc", "Convert2Lambda"})
public class Room {

    private static final Logger LOGGER = LoggerFactory.getLogger(Room.class);
    private final Gson GSON = new GsonBuilder().create();
    private final ConcurrentMap<String, Viewer> peopleInRoom = new ConcurrentHashMap<>();

    private final String roomName;


    public Room(final String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() {
        return roomName;
    }

    public Viewer getViewerBySId(String sessionId) {
        return peopleInRoom.get(sessionId);
    }

    public Collection<Viewer> getViewers() {
        return peopleInRoom.values();
    }


    /**
     * Add viewer to current room
     *
     * @param viewer
     */
    public void addViewer(Viewer viewer) {
        LOGGER.info("{} added to {} ... {}", viewer.getViewerName(), roomName, viewer.getSessionId());
        peopleInRoom.put(viewer.getSessionId(), viewer);

        // notify viewer of successful join
        Response response = new Response();
        response.setResponseCode(ResponseCode.JOIN_SUCCESSFUL.getCode());
        response.setResponseDesc(ResponseCode.JOIN_SUCCESSFUL.getDescription());
        response.setSessionId(viewer.getSessionId());
        sendTo(viewer, response);

        // notify others of viewer join
        response.setResponseCode(ResponseCode.SOMEONE_JOINED.getCode());
        response.setResponseDesc(ResponseCode.SOMEONE_JOINED.getDescription());
        response.setMessage(new Message(
                        this.roomName,
                        viewer.getViewerName() + " has joined the room",
                        new SimpleDateFormat("HH:mm").format(new Date())
                )
        );
        sendExcept(viewer, response);
    }


    /**
     * Remove viewer from current room
     *
     * @param sessionId
     */
    public void removeViewer(String sessionId) {
        Viewer toRemove = peopleInRoom.remove(sessionId);
        Response response = new Response();

        if (toRemove != null) {
            // notify viewer of left successful
            response.setResponseCode(ResponseCode.LEFT_SUCCESSFULLY.getCode());
            response.setResponseDesc(ResponseCode.LEFT_SUCCESSFULLY.getDescription());
            response.setSessionId(sessionId);
            sendTo(toRemove, response);
            toRemove.close();

            // Notify others of viewer removal
            Message msg = new Message();
            msg.setFrom(roomName);
            msg.setMessage(toRemove.getViewerName() + " left the room.");
            msg.setTime(new SimpleDateFormat("HH:mm").format(new Date()));
            response.setResponseCode(ResponseCode.SOMEONE_LEFT.getCode());
            response.setResponseDesc(ResponseCode.SOMEONE_LEFT.getDescription());
            response.setMessage(msg);
            sendToAll(response);
        } else {

            // notify viewer of left failed
            response.setResponseCode(ResponseCode.LEFT_UNSUCCESSFUL.getCode());
            response.setResponseDesc(ResponseCode.LEFT_UNSUCCESSFUL.getDescription());
            sendTo(getViewerBySId(sessionId), response);
        }
    }


    /**
     * Sends the provided response to everyone in room except the given one
     *
     * @param exception
     */
    public void sendExcept(Viewer exception, Response response) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Viewer person : peopleInRoom.values()) {
                        if (!person.getSessionId().equalsIgnoreCase(exception.getSessionId())) {
                            person.sendMessage(GSON.toJson(response));
                        }
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });
        thread.start();
    }


    /**
     * Send message to a particular viewer
     *
     * @param viewer
     */
    public void sendTo(Viewer viewer, Response response) {
        try {
            viewer.sendMessage(GSON.toJson(response));
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }


    /**
     * Sends message to all the members in the room
     *
     * @param response
     */
    public void sendToAll(Response response) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Viewer person : getViewers()) {
                        person.sendMessage(GSON.toJson(response));
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });
        thread.start();
    }

}