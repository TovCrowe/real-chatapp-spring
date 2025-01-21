package com.chatapp.project.Client;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.chatapp.project.Models.Message;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private String user;
    private MessageListener messageListener;
    public MyStompSessionHandler(MessageListener messageListener, String user){
        this.messageListener = messageListener;
        this.user = user;
    }

    @Override
    public void afterConnected(@NotNull StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Client Connected");
        session.send("/app/connect", user);

        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("asdasdasda");
                try {
                    if (payload instanceof Message) {
                        Message message = (Message) payload;
                        messageListener.onMessageReceive(message);
                        System.out.println(123);
                        System.out.println("Received message: " + message.getUser() + ": " + message.getMessage());
                    } else {
                        System.out.println("Received unexpected payload type: " + payload.getClass());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Client Subscribe to /topic/messages");


        session.subscribe("/topic/users", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return new ArrayList<String>().getClass();
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if(payload instanceof ArrayList){
                        ArrayList<String> activeUsers = (ArrayList<String>) payload;
                        messageListener.onActiveUsersUpdated(activeUsers);
                        System.out.println("Received active users: " + activeUsers.toString());
                    } else {
                        System.out.println("Received unexpected payload type: " + payload.getClass());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Usesr subrrvsiiev");
    }
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
         exception.printStackTrace();
    }


}
