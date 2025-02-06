package com.edu.harran.social.request;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String chatId;
    private String content;
}
