package com.icaro.api_petshop.pet.repository;

import com.icaro.api_petshop.tutor.model.Tutor;

import com.icaro.api_petshop.pet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    void deleteByTutor(Tutor tutor);
}