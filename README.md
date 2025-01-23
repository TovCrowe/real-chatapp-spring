# WebSocket Chat Application

This project demonstrates how to create a real-time chat application using Spring Boot and WebSocket. It includes a WebSocket server, a client package for managing connections, and a graphical user interface (GUI) for interaction. The application uses STOMP (Simple Text Oriented Messaging Protocol) for message communication.

## Table of Contents
1. [Overview](#overview)
2. [WebSocket Configuration](#websocket-configuration)
3. [WebSocket Controller](#websocket-controller)
4. [Client Package](#client-package)
5. [Client GUI](#client-gui)
6. [Connecting WebSocket to the GUI](#connecting-websocket-to-the-gui)
7. [Usage](#usage)
8. [Installation](#installation)

---

## Overview

This application demonstrates a real-time communication system using WebSocket. Key features include:

- STOMP support for message handling.
- A WebSocket server implemented using `WebSocketMessageBrokerConfigurer`.
- Client-side implementation with `StompSession` for connecting, sending, and subscribing to topics.
- A GUI that enables users to send messages and view active users.

---

## WebSocket Configuration

The WebSocket server is configured in a class annotated with:

- `@Configuration`: Indicates this is a Spring configuration class.
- `@EnableWebSocketMessageBroker`: Enables WebSocket message handling using a message broker.

### Key Methods:
1. **`configureMessageBroker`**:
   - Enables a simple broker with `enableSimpleBroker("/topic")`.
   - Sets the application destination prefix with `setApplicationDestinationPrefixes("/app")`.

2. **`registerStompEndpoints`**:
   - Registers STOMP endpoints such as `/ws`.
   - Configures SockJS fallback options for browsers that do not support WebSocket.

---

## WebSocket Controller

The controller is annotated with `@Controller` to handle WebSocket messages. It includes:

- A `SimpMessagingTemplate` for sending messages to clients.
- Methods annotated with `@MessageMapping` to handle incoming messages:
  - `@MessageMapping("/messages")` binds to `/app/messages` to process messages.

---

## Client Package

### `MyStompClient`

This class manages the WebSocket client connection. It includes:

- A `StompSession` for handling communication.
- Methods for sending and subscribing to messages.

### Key Steps:
1. **Serialization/Deserialization**:
   - Configured using `setMessageConverter(new MappingJackson2MessageConverter())`.

2. **Session Management**:
   - Extends `StompSessionHandlerAdapter`.
   - Overrides:
     - `afterConnected`: Automatically subscribes to `/topic/message`.
     - `handleFrame`: Processes incoming messages.

---

## Client GUI

The GUI provides a user-friendly interface for sending and receiving messages. Key components include:

- Input fields for messages.
- Display panel for active users and chat messages.

### Integration with `MyStompClient`:
- Automatically connects the WebSocket when initialized.
- Sends messages using `sendMessage()`.

---

## Connecting WebSocket to the GUI

### Message Listener:
- A custom interface `MessageListener` is implemented in the GUI class.
- Contains methods:
  - `onMessageReceived`: Updates the message panel with new messages.
  - `onActiveUsersUpdated`: Updates the active users list.

### Subscribing to Topics:
- Subscribed to `/topic/usernames` for real-time updates on active users.
- Updates the GUI with incoming data.

---

## Usage

1. Run the Spring Boot application to start the WebSocket server.
2. Launch the client GUI to connect to the server.
3. Send and receive messages in real-time.
4. View the list of active users.

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/TovCrowe/real-chatapp-spring.git