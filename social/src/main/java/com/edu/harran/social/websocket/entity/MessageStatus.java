package com.edu.harran.social.websocket.entity;

import com.edu.harran.social.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class MessageStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime deliveredDate;
    private LocalDateTime readDate;
    @ManyToOne
    private Message message;
    @ManyToOne
    private User user;

}
