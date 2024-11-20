package com.example.startingclubbackend.model.chat;

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
