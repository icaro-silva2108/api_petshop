package com.icaro.api_petshop.pet.dto;

import com.icaro.api_petshop.pet.model.enums.AnimalType;
import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequestDTO(

    @NotNull(message = "tutor id is required")
    Long tutorId,

    @NotBlank(message = "name is required")
    String name,

    @NotNull(message = "animal type is required")
    AnimalType type,

    @NotNull(message = "animal sex is required")
    AnimalSex sex,

    @NotBlank(message = "breed is required")
    String breed,

    @NotNull(message = "size is required")
    AnimalSize size,

    @NotNull(message = "age is required")
    @Min(value = 0, message = "age must be positive")
    Integer age
) {}