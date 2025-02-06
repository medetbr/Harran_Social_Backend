package com.edu.harran.social.config;

import com.edu.harran.social.websocket.service.MessageStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    public static final Map<String, String> activeSessions = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageStatusService messageStatusService;

    @EventListener
    public void onApplicationEvent(SessionConnectedEvent event) {
        Message<?> message = event.getMessage();
        Object connectMessage = message.getHeaders().get("simpConnectMessage");
        if (connectMessage instanceof Message) {
            Message<?> connectMessageObject = (Message<?>) connectMessage;
            //System.out.println("Connect Message Headers: " + connectMessageObject.getHeaders());

            // 'nativeHeaders' başlıklarını al
            Map<String, Object> nativeHeaders = (Map<String, Object>) connectMessageObject.getHeaders().get("nativeHeaders");
            if (nativeHeaders != null) {
                System.out.println("Native Headers: " + nativeHeaders);

                // Kullanıcı adını 'user_name' başlığından al
                String userId = ((List<String>) nativeHeaders.get("user_id")).get(0); // Kullanıcı adını alın

                if (userId != null) {
                    if (!activeSessions.containsValue(userId)) {
                        messageStatusService.updateMessageDeliveredDateRealTime(userId);
                        String sessionId = StompHeaderAccessor.wrap(message).getSessionId();
                        activeSessions.put(sessionId, userId);
                        System.out.println("User Connected: " + userId);
                        List<String> userIds = new ArrayList<>(activeSessions.values());
                        messagingTemplate.convertAndSend("/topic/userStatus", userIds);
                    }
                }
            }
        }
    }

    @EventListener
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String userId = activeSessions.remove(sessionId);

        List<String> userIds = new ArrayList<>(activeSessions.values());
        messagingTemplate.convertAndSend("/topic/userStatus", userIds);

        System.out.println("User Disconnected: " + (userId));
    }

    public boolean isUserOnline(String userId) {
        return activeSessions.containsValue(userId);
    }

}
