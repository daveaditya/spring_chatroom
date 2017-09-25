package com.spring.chatroom.topic;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


// Represents a single person's session
@SuppressWarnings({"WeakerAccess"})
public class Viewer implements AutoCloseable {

    private String viewerName;

    private String jSessionId;

    private WebSocketSession wbSession;


    public Viewer(String viewerName, WebSocketSession wbSession) {
        this.viewerName = viewerName;
        this.wbSession = wbSession;
    }

    public String getViewerName() {
        return viewerName;
    }

    public void setViewerName(String viewerName) {
        this.viewerName = viewerName;
    }

    public String getjSessionId() {
        return jSessionId;
    }

    public void setjSessionId(String jSessionId) {
        this.jSessionId = jSessionId;
    }

    public String getSessionId() {
        return this.wbSession.getId();
    }

    public WebSocketSession getWbSession() {
        return wbSession;
    }

    public void setWbSession(WebSocketSession wbSession) {
        this.wbSession = wbSession;
    }

    public void sendMessage(String msg) throws IOException {
        this.wbSession.sendMessage(new TextMessage(msg));
    }

    @Override
    public String toString() {
        return "Viewer{" +
                "viewerName='" + viewerName + '\'' +
                ", jSessionId='" + jSessionId + '\'' +
                ", wbSession=" + wbSession +
                '}';
    }

    @Override
    public void close() {
        try {
            this.wbSession.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

}