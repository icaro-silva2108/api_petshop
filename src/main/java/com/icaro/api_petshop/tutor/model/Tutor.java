package com.icaro.api_petshop.tutor.model;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "tutor")
@Entity
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "email", unique = true)
    @Setter
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    public Tutor(String name, String email, String passwordHash) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.email = Objects.requireNonNull(email, "email cannot be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "password hash cannot be null");

        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("email cannot be empty");
        }
        if (passwordHash.isBlank()) {
            throw new IllegalArgumentException("password hash cannot be empty");
        }
    }
}