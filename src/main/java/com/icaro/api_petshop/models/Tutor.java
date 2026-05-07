package com.icaro.api_petshop.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Tutor {

    private final UUID id;
    private String name;
    private String email;
    private final List<Pet> petList = new ArrayList<>();

    public Tutor(String name, String email) {
        this.id = UUID.randomUUID();
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.email = Objects.requireNonNull(email, "email cannot be null");

        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("email cannot be empty");
        }
    }

    //GETTERS

    public UUID getId() {return id;}

    public String getName() {return name;}

    public String getEmail() {return email;}

    public List<Pet> getPetList() {return petList;}

    //SETTERS

    public void setName(String name) {this.name = name;}

    public void setEmail(String email) {this.email = email;}
}