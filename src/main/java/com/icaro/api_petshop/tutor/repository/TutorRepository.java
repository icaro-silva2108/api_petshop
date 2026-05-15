package com.icaro.api_petshop.tutor.repository;

import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.tutor.model.Tutor;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByEmail(String email);

    void deleteByEmail(Tutor tutor);

    @Query("SELECT p FROM Pet p WHERE p.tutor.email = :email")
    List<Pet> findPetsByTutorEmail(@Param("email") String email);
}