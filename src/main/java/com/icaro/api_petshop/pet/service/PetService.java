package com.icaro.api_petshop.pet.service;

import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;
import com.icaro.api_petshop.pet.model.enums.AnimalType;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.tutor.model.Tutor;

public class PetService {

    public Pet registerPet(
            Tutor tutor,
            String name,
            AnimalType type,
            AnimalSex sex,
            String breed,
            int age,
            AnimalSize size
    ) {
        return new Pet(
                tutor,
                name,
                type,
                sex,
                breed,
                size, age
        );
    }
}