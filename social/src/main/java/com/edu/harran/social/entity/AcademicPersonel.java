package com.edu.harran.social.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "academic_personels")
public class AcademicPersonel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( nullable = false)
    private String name;
    @Column(nullable = false,unique = true)
    private String e_mail;

    @ManyToOne
    private Department department;

    private Boolean isDeleted = false;

    private String academicPersonelId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public AcademicPersonel() {
        this.academicPersonelId = UUID.randomUUID().toString();
    }

}
