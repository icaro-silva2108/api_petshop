package com.icaro.api_petshop.pet.repository;

import com.icaro.api_petshop.pet.model.Pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p WHERE p.tutor.id = :tutorId AND p.tutor.active = true AND p.active = true")
    List<Pet> findByTutorIdAndActiveTrue(@Param("tutorId") Long tutorId);

    @Query("SELECT p FROM Pet p WHERE p.id = :id AND p.active = true")
    Optional<Pet> findByIdAndActiveTrue(Long id);
}