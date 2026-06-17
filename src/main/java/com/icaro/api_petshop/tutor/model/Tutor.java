package com.icaro.api_petshop.tutor.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "tutor")
@Entity
public class Tutor implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "email", unique = true)
    @Setter
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Setter
    @Column(name = "active")
    private boolean active = true;

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

    public void changePassword(String newPasswordHash) {
        this.passwordHash = Objects.requireNonNull(newPasswordHash, "password hash cannot be null");
        if (newPasswordHash.isBlank()) {
            throw new IllegalArgumentException("password hash cannot be empty");
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}