package com.chatapp.project.Client;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.chatapp.project.Models.Message;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private String username;

    public MyStompSessionHandler(String username) {
        this.username = username;
    }
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Attempting subscription...");

        try {
            StompSession.Subscription subscription = session.subscribe("/topic/message", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    System.out.println("Getting payload type for headers: " + headers);
                    return Message.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    System.out.println("Frame received - Headers: " + headers);
                    try {
                        if (payload instanceof Message) {
                            Message message = (Message) payload;
                            System.out.println("Message received: " + message.getMessage());
                        } else {
                            System.out.println("Unexpected payload: " + payload);
                        }
                    } catch (Exception e) {
                        System.err.println("Error handling frame: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

            System.out.println("Subscription successful: " + subscription.getSubscriptionId());
        } catch (Exception e) {
            System.err.println("Subscription failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
         exception.printStackTrace();
    }


}
