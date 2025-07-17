package com.cms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") 
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String role; // e.g., STUDENT, ADMIN, WARDEN

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
