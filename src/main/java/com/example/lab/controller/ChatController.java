package com.example.lab.controller;

import com.example.lab.model.ChatMessage;
import com.example.lab.model.Condition;
import com.example.lab.model.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat.info/{info}")
    @SendTo("/topic/info")
    public Info systemInfo(@DestinationVariable("info") String info, @Payload Info infoMessage){
        //System.out.println("info:"+info);
        Info inf = new Info();
        inf.setInformation(info+"##"+infoMessage.getInformation());
        return inf;
    }

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.condition")
    public void conditionalMessage(@Payload Condition condition){
        System.out.println(condition.getValue()+":"+condition.getMessage());
        if (condition.getValue().equalsIgnoreCase(ChatMessage.MessageType.CHAT.name())){
            ChatMessage message = new ChatMessage();
            message.setSender("Unknown");
            message.setType(ChatMessage.MessageType.CHAT);
            message.setContent(condition.getMessage());
            messagingTemplate.convertAndSend("/topic/public", message);
        }
        else if(condition.getValue().equalsIgnoreCase(ChatMessage.MessageType.JOIN.name())){
            Info inf = new Info();
            inf.setInformation("JOIN"+"##"+condition.getMessage());
            messagingTemplate.convertAndSend("/topic/info", inf);
        }
        else{
            Info inf = new Info();
            inf.setInformation("ITSOUL"+"##"+condition.getMessage());
            messagingTemplate.convertAndSend("/topic/soul", inf);
        }

    }

}
