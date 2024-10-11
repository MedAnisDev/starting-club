package com.example.startingclubbackend.security.config.webSocket;

import com.example.startingclubbackend.model.chat.ChatMessage;
import com.example.startingclubbackend.model.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.SessionEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageTemplate ;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent disconnectEvent){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        String userName = (String) headerAccessor.getSessionAttributes().get("username");

        if(userName !=null){
            log.info("user with userName {} has disconected", userName);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .Sender(userName)
                    .build();
            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }

    }
}
