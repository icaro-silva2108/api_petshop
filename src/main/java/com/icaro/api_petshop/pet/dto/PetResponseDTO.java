package com.icaro.api_petshop.pet.dto;

import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;
import com.icaro.api_petshop.pet.model.enums.AnimalType;
import com.icaro.api_petshop.tutor.model.Tutor;

public record PetResponseDTO(

    Long id,
    Long tutorId,
    String name,
    AnimalType type,
    AnimalSex sex,
    String breed,
    AnimalSize size,
    Integer age
) {}