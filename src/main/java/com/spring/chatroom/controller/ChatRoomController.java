package com.spring.chatroom.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spring.chatroom.topic.RoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@SuppressWarnings({"unused"})
public class ChatRoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomController.class);
    private static final Gson GSON = new GsonBuilder().create();
    private static final RoomManager roomManager = new RoomManager();
    private static final String ROOM_NAMES = "ROOM_NAMES";

    @RequestMapping("/")
    public String goHome(ModelMap modelMap) {
        modelMap.addAttribute(ROOM_NAMES, roomManager.getCurrentRoomNames());
        return "home";
    }

}