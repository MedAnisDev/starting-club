package com.example.startingclubbackend.controller.chat;

import com.example.startingclubbackend.model.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;


@Controller
@Slf4j
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/chatroom/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        log.info("Received message: {}  || sender name {} " , chatMessage.getContent() , chatMessage.getSender());
        return chatMessage ;
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/chatroom/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor accessor
    ){
        // add username to web socket Session
        accessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage ;
    }

    @MessageMapping("/chat.keepAlive")
    public void keepAlive(@Payload String message) {
    }


}
