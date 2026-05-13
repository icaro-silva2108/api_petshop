package com.icaro.api_petshop.pet.dto;

import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;
import com.icaro.api_petshop.pet.model.enums.AnimalType;

import jakarta.validation.constraints.Min;

public record PetUpdateDTO(

    String name,

    AnimalType type,

    AnimalSex sex,

    String breed,

    AnimalSize size,

    @Min(value = 0, message = "age must be positive")
    Integer age
) {}