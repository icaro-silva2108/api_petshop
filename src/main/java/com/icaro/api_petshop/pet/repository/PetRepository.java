package com.icaro.api_petshop.pet.repository;

import com.icaro.api_petshop.tutor.model.Tutor;

import com.icaro.api_petshop.pet.model.Pet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Transactional
    void deleteByTutor(Tutor tutor);
}