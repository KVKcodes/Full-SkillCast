package org.killer.springer.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StreamWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> rooms = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("New WebSocket connection established: " + session.getId());
        sessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("WebSocket connection closed: " + session.getId());
        sessions.remove(session.getId());
        
        // Remove from rooms and notify all clients about updated stream list
        boolean wasStreamer = rooms.entrySet().removeIf(entry -> entry.getValue().equals(session.getId()));
        if (wasStreamer) {
            broadcastStreamsList();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("WebSocket transport error for session " + session.getId() + ": " + exception.getMessage());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
        String type = (String) payload.get("type");
        
        switch (type) {
            case "get-streams":
                // Send list of active streams
                Map<String, Object> streamsList = Map.of(
                    "type", "streams-list",
                    "streams", rooms.entrySet().stream()
                        .map(entry -> Map.of("roomId", entry.getKey()))
                        .collect(Collectors.toList())
                );
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(streamsList)));
                break;

            case "start-stream":
                String roomId = (String) payload.get("roomId");
                System.out.println("Starting stream in room: " + roomId);
                rooms.put(roomId, session.getId());
                break;
                
            case "join-stream":
                roomId = (String) payload.get("roomId");
                String streamerId = rooms.get(roomId);
                System.out.println("Viewer joining room: " + roomId + ", streamer: " + streamerId);
                if (streamerId != null) {
                    WebSocketSession streamerSession = sessions.get(streamerId);
                    if (streamerSession != null) {
                        Map<String, Object> viewerJoined = Map.of(
                            "type", "viewer-joined",
                            "viewerId", session.getId()
                        );
                        streamerSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(viewerJoined)));
                    }
                } else {
                    System.out.println("No streamer found for room: " + roomId);
                }
                break;
                
            case "offer":
                String viewerId = (String) payload.get("viewerId");
                WebSocketSession viewerSession = sessions.get(viewerId);
                if (viewerSession != null) {
                    Map<String, Object> offer = Map.of(
                        "type", "offer",
                        "offer", payload.get("offer"),
                        "streamerId", session.getId()
                    );
                    viewerSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(offer)));
                }
                break;
                
            case "answer": {
                String answerStreamerId = (String) payload.get("streamerId");
                WebSocketSession streamerSession = sessions.get(answerStreamerId);
                if (streamerSession != null) {
                    Map<String, Object> answer = Map.of(
                        "type", "answer",
                        "answer", payload.get("answer"),
                        "viewerId", session.getId()
                    );
                    streamerSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(answer)));
                }
            }
                break;
                
            case "ice-candidate":
                String peerId = (String) payload.get("peerId");
                WebSocketSession peerSession = sessions.get(peerId);
                if (peerSession != null) {
                    Map<String, Object> candidate = Map.of(
                        "type", "ice-candidate",
                        "candidate", payload.get("candidate"),
                        "fromId", session.getId()
                    );
                    peerSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(candidate)));
                }
                break;
        }
    }

    private void broadcastStreamsList() {
        Map<String, Object> streamsList = Map.of(
            "type", "streams-list",
            "streams", rooms.entrySet().stream()
                .map(entry -> Map.of("roomId", entry.getKey()))
                .collect(Collectors.toList())
        );
        String message = objectMapper.writeValueAsString(streamsList);
        sessions.values().forEach(session -> {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
} 