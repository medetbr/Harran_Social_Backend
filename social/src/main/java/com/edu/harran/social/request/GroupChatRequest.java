package com.edu.harran.social.request;

import lombok.Data;

import java.util.List;

@Data
public class GroupChatRequest {
    private List<String> users;
    private String chatName;
    private String chatImage;
    private String admin;
}
