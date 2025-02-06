package com.edu.harran.social.entity;

import com.edu.harran.social.websocket.entity.Chat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, unique = true)
    private String name;
    @Column(unique = true)
    private String e_mail;
    @ManyToOne(optional = false)
    private Program program;
    @OneToMany(mappedBy = "department")
    private List<AcademicPersonel> academicalPersonels;
    @OneToMany(mappedBy = "department")
    private List<Student> students;
    private Boolean isDeleted = false;
    private String departmentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "department")
    private List<Chat> chat;
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

    public Department() {
        this.departmentId = UUID.randomUUID().toString();
    }

}
