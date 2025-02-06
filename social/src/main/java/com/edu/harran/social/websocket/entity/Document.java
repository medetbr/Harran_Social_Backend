package com.edu.harran.social.websocket.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String originalName;
    private Long fileSize;
    private String page;
    @OneToOne
    private Message message;
}
