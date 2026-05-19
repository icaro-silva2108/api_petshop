package com.icaro.api_petshop.pet.repository;

import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.pet.model.Pet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByTutorIdAndActiveTrue(Long tutorId);

    Optional<Pet> findByIdAndActiveTrue(Long id);
}