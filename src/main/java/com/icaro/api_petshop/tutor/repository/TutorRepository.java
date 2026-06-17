package com.icaro.api_petshop.tutor.repository;

import com.icaro.api_petshop.tutor.model.Tutor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByEmail(String email);

    @Query("SELECT t FROM Tutor t WHERE t.email = :email AND t.active = true")
    Optional<Tutor> findByEmailAndActiveTrue(@Param("email") String email);
}