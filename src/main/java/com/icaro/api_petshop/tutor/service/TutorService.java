package com.icaro.api_petshop.tutor.service;

import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.tutor.dto.TutorUpdateDTO;
import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.tutor.repository.TutorRepository;
import com.icaro.api_petshop.exceptions.EmailNotFound;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;
    private final PasswordEncoder passwordEncoder;

    public Tutor createTutor(String name, String email, String password) {

        String passwordHash = passwordEncoder.encode(password);
        Tutor tutor = new Tutor(name, email, passwordHash);
        return tutorRepository.save(tutor);
    }

    public Tutor findByEmail(String email) {

        return tutorRepository.findByEmail(email).orElseThrow(EmailNotFound::new);
    }

    public Tutor updateTutor(String email, TutorUpdateDTO dto) {

        Tutor tutor = findByEmail(email);

        if (dto.name() != null && !dto.name().isBlank()) {
            tutor.setName(dto.name());
        }
        if (dto.email() != null && !dto.email().isBlank()) {
            tutor.setEmail(dto.email());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            tutor.changePassword(passwordEncoder.encode(dto.password()));
        }

        return tutorRepository.save(tutor);
    }

    public void deleteTutor(String email) {

        Tutor tutor = findByEmail(email);
        tutorRepository.deleteByEmail(tutor);
    }

    public List<Pet> getTutorPets(String email) {

        return tutorRepository.findPetsByTutorEmail(email);
    }
}