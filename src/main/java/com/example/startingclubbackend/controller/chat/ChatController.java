package com.example.startingclubbackend.controller.chat;

import com.example.startingclubbackend.model.chat.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;


@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        return chatMessage ;
    }


    @MessageMapping("/chat.addUser")
    @SendTo("topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor accessor
    ){
        // add username to web socket Session
        accessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage ;
    }


}
