package com.icaro.api_petshop.models;

import com.icaro.api_petshop.enums.AnimalSex;
import com.icaro.api_petshop.enums.AnimalSize;
import com.icaro.api_petshop.enums.AnimalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

public class Pet {

    private final UUID id;
    private Tutor tutor;
    private String name;
    private AnimalType type;
    private AnimalSex sex;
    private String breed;
    private int age;
    private AnimalSize size;

    public Pet(
            Tutor tutor,
            String name,
            AnimalType type,
            AnimalSex sex,
            String breed,
            int age,
            AnimalSize size
    ) {
        this.id = UUID.randomUUID();
        this.tutor = Objects.requireNonNull(tutor, "pet must have a tutor");
        this.name = Objects.requireNonNull(name, "pet must have a name");
        this.type = Objects.requireNonNull(type, "pet must have a type");
        this.sex = Objects.requireNonNull(sex, "pet must have a sex");
        this.breed = Objects.requireNonNull(breed, "pet must have a breed");
        this.age = age;
        this.size = Objects.requireNonNull(size, "pet must have a size");

        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        if (breed.isBlank()) {
            throw new IllegalArgumentException("breed cannot be empty");
        }
    }

    //GETTERS

    public UUID getId() {return id;}

    public Tutor getTutor() {return tutor;}

    public String getName() {return name;}

    public AnimalType getType() {return type;}

    public AnimalSex getSex() {return sex;}

    public String getBreed() {return breed;}

    public int getAge() {return age;}

    public AnimalSize getSize() {return size;}


    //SETTERS

    public void setTutor(Tutor tutor) {this.tutor = tutor;}

    public void setName(String name) {this.name = name;}

    public void setType(AnimalType type) {this.type = type;}

    public void setSex(AnimalSex sex) {this.sex = sex;}

    public void setBreed(String breed) {this.breed = breed;}

    public void setAge(int age) {this.age = age;}

    public void setSize(AnimalSize size) {this.size = size;}
}