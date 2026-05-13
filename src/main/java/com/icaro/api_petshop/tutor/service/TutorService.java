package com.icaro.api_petshop.tutor.service;

import com.icaro.api_petshop.exceptions.InvalidCredentialsException;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.repository.PetRepository;
import com.icaro.api_petshop.tutor.dto.TutorRequestDTO;
import com.icaro.api_petshop.tutor.dto.TutorResponseDTO;
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
    private final PetRepository petRepository;
    private final PasswordEncoder passwordEncoder;

    private TutorResponseDTO toResponseDTO(Tutor tutor) {

        return new TutorResponseDTO(
                tutor.getId(),
                tutor.getName(),
                tutor.getEmail());
    }

    public void loginValidation(String email, String password) {

        Tutor tutor = tutorRepository.findByEmail(email).orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(password, tutor.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
    }

    public TutorResponseDTO createTutor(TutorRequestDTO dto) {

        String passwordHash = passwordEncoder.encode(dto.password());
        Tutor tutor = new Tutor(dto.name(), dto.email(), passwordHash);

        tutorRepository.save(tutor);
        return toResponseDTO(tutor);
    }

    public Tutor findByEmail(String email) {

        return tutorRepository.findByEmail(email).orElseThrow(EmailNotFound::new);
    }

    public TutorResponseDTO updateTutor(String email, TutorUpdateDTO dto) {

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

        tutorRepository.save(tutor);
        return toResponseDTO(tutor);
    }

    public void deleteTutor(String email) {

        Tutor tutor = findByEmail(email);
        petRepository.deleteByTutor(tutor);
        tutorRepository.deleteByEmail(tutor);
    }

    public List<Pet> getTutorPets(String email) {

        return tutorRepository.findPetsByTutorEmail(email);
    }
}