package com.icaro.api_petshop.tutor.repository;

import com.icaro.api_petshop.tutor.model.Tutor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByEmail(String email);
}