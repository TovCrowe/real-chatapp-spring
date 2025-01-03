# WebSocket Implementation Guide

## Configuration

The configuration class is marked with two important annotations:

```java
@Configuration  // Indicates this is a Spring configuration class
@EnableWebSocketMessagesBroker  // Enables websocket message handling with a broker
```

A message broker is software that enables applications to communicate with each other. To implement the configuration:

1. Implement `WebSocketMessageBrokerConfigurer` interface
2. Override the following methods:

### configureMessageBroker

```java
@Override
public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");  // Route where user messages go
    config.setApplicationDestinationPrefixes("/app");  // Messages starting with /app handled by controllers
}
```

### registerStompEndpoints

This method registers STOMP (Simple Text Oriented Messaging Protocol) endpoints. STOMP makes it easier for clients to communicate with the server.

```java
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").withSockJS();  // Registers /ws endpoint with SockJS fallback
}
```

SockJS provides WebSocket behavior for browsers that don't support WebSockets, ensuring connectivity even with outdated browsers or apps.

## Controller Implementation

Create a WebSocket controller:

```java
@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;  // Used to send messages to clients

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/messages")  // Binds to /app/messages
    public void handleMessage(/* parameters */) {
        // Handle incoming messages
    }
}
```

## Client Implementation

### MyStompClient Class

```java
public class MyStompClient {
    private StompSession stompSession;  // Allows connection to STOMP servers
    private String username;

    public MyStompClient(String username) {
        this.username = username;
        // Configure transport
    }

    // Configure message conversion
    public void configureMessageConverter() {
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        // Converts Java objects to JSON data automatically
    }
}
```

### StompSession Handler

Create a class extending `StompSessionHandlerAdapter`:

```java
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        // Subscribe to messages after connection
        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;  // Convert JSON to Message object
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload instanceof Message) {
                    Message message = (Message) payload;
                    // Handle message
                }
            }
        });
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        // Handle transport errors
    }
}
```

Key Components:
- StompSession: Manages connection to STOMP servers
  - Sends messages
  - Subscribes to routes
  - Manages connections
- Transport: Protocol for client-server data transfer
- Message Conversion:
  - Serialization: Format data for WebSocket
  - Deserialization: Interpret incoming WebSocket data

The `StompFrameHandler` enables:
1. `getPayloadType`: Specifies expected payload type
2. `handleFrame`: Processes incoming messages for subscribed destinations

