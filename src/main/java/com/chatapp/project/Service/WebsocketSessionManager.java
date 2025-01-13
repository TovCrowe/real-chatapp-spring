package com.chatapp.project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebsocketSessionManager {
    private final ArrayList<String> activeUsers = new ArrayList<>();
    private final SimpMessagingTemplate  messagingTemplate;
    @Autowired

    public WebsocketSessionManager(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void addUsers(String username){
        activeUsers.add(username);
    }
    public void removeUsers(String username){
        activeUsers.remove(username);
    }
    public void broadcastActiveUsers(){
        messagingTemplate.convertAndSend("/topic/users", activeUsers);
        System.out.println("Broadcasting active users in /topic/users : " + activeUsers.toString());
    }
}
