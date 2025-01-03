package com.chatapp.project.Client;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.chatapp.project.Models.Message;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/message", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers){
                return Message.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload){
                try {
                    if(payload instanceof Message){
                        Message message = (Message) payload;
                        System.out.println("Recive message from user :" + message.getUser() + " Message : " + message.getMessage() );
                    } else {
                        System.out.println("Recive Unexpected payload type :" + payload.getClass());
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }


}
