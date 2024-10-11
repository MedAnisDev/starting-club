package com.example.startingclubbackend.model.chat;

import com.example.startingclubbackend.model.user.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String Sender ;
    private MessageType type;
}
