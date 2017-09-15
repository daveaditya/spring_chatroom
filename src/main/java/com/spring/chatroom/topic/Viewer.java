package com.spring.chatroom.topic;

import com.spring.chatroom.model.Response;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


// Represents a single person's session
@SuppressWarnings({"WeakerAccess"})
public class Viewer {

    private String viewerName;
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

    public String getSessionId() {
        return this.wbSession.getId();
    }

    public WebSocketSession getWbSession() {
        return wbSession;
    }

    public void setWbSession(WebSocketSession wbSession) {
        this.wbSession = wbSession;
    }

    public void sendMessage(Response response) throws IOException {
        this.wbSession.sendMessage(new TextMessage(response.toString()));
    }

}