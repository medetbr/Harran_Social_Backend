package com.edu.harran.social.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "programs")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, unique = true)
    private String name;
    @Column(unique = true)
    private String e_mail;
    @ManyToOne(optional = false)
    private Academic academic;
    @OneToMany(mappedBy = "program")
    private List<Department> department;
    private Boolean isDeleted = false;
    @Column(unique = true,nullable = true,name = "program_id")
    private String programId;

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

    public Program() {
        this.programId = UUID.randomUUID().toString();
    }
}
