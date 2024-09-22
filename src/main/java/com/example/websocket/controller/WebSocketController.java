package com.example.websocket.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
public class WebSocketController extends TextWebSocketHandler {

    // Use a Set to keep track of all active WebSocket sessions
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); // Add new session to the set
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session); // Remove session when closed
    }

    // @KafkaListener annotation is used to mark the listenKafka method as a Kafka message listener.
    @KafkaListener(topics = "websocket-topic", groupId = "websocket-group")
    public void listenKafka(String message) throws IOException {
        // Broadcast message to all active sessions
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}


