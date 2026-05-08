package com.icaro.api_petshop.pet.model;

import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;
import com.icaro.api_petshop.pet.model.enums.AnimalType;

import java.util.Objects;

import com.icaro.api_petshop.tutor.model.Tutor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Table(name = "pet")
@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @Column(name = "name")
    @Setter
    private String name;

    @Enumerated(EnumType.STRING)
    @Setter
    private AnimalType type;

    @Enumerated(EnumType.STRING)
    @Setter
    private AnimalSex sex;

    @Column(name = "breed")
    @Setter
    private String breed;

    @Setter
    @Enumerated(EnumType.STRING)
    private AnimalSize size;

    @Setter
    @Column(name = "age")
    private Integer age;

    public Pet(
            Tutor tutor,
            String name,
            AnimalType type,
            AnimalSex sex,
            String breed,
            AnimalSize size,
            Integer age
    ) {
        this.tutor = Objects.requireNonNull(tutor, "pet must have a tutor");
        this.name = Objects.requireNonNull(name, "pet must have a name");
        this.type = Objects.requireNonNull(type, "pet must have a type");
        this.sex = Objects.requireNonNull(sex, "pet must have a sex");
        this.breed = Objects.requireNonNull(breed, "pet must have a breed");
        this.size = Objects.requireNonNull(size, "pet must have a size");
        this.age = age;

        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        if (breed.isBlank()) {
            throw new IllegalArgumentException("breed cannot be empty");
        }
    }
}