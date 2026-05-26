package com.icaro.api_petshop.tutor.service;

import com.icaro.api_petshop.exceptions.EmailAlreadyExistsException;
import com.icaro.api_petshop.exceptions.InvalidCredentialsException;
import com.icaro.api_petshop.pet.dto.PetResponseDTO;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.repository.PetRepository;
import com.icaro.api_petshop.tutor.dto.TutorRequestDTO;
import com.icaro.api_petshop.tutor.dto.TutorResponseDTO;
import com.icaro.api_petshop.tutor.dto.TutorUpdateDTO;
import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.tutor.repository.TutorRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
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

    private PetResponseDTO toPetResponseDTO(Pet pet) {
        return new PetResponseDTO(
                pet.getId(),
                pet.getTutor().getId(),
                pet.getName(),
                pet.getType(),
                pet.getSex(),
                pet.getBreed(),
                pet.getSize(),
                pet.getAge()
        );
    }

    public Tutor loginValidation(String email, String password) {

        Tutor tutor = tutorRepository.findByEmail(email).orElseThrow(InvalidCredentialsException::new);

        if (!tutor.isActive()) {
            throw new InvalidCredentialsException();
        }
        if (!passwordEncoder.matches(password, tutor.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return tutor;
    }

    public TutorResponseDTO createTutor(TutorRequestDTO dto) {

        String passwordHash = passwordEncoder.encode(dto.password());

        if (tutorRepository.findByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        Tutor tutor = new Tutor(dto.name(), dto.email(), passwordHash);

        Tutor saved = tutorRepository.save(tutor);
        return toResponseDTO(saved);
    }

    public TutorResponseDTO updateTutor(Tutor tutor, TutorUpdateDTO dto) {

        if (dto.name() != null && !dto.name().isBlank()) {
            tutor.setName(dto.name());
        }
        if (dto.email() != null && !dto.email().isBlank()) {

            if (tutorRepository.findByEmail(dto.email()).isPresent()) {
                throw new EmailAlreadyExistsException();
            }
            tutor.setEmail(dto.email());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            tutor.changePassword(passwordEncoder.encode(dto.password()));
        }

        return toResponseDTO(tutor);
    }

    public void deleteTutor(Tutor tutor) {

        petRepository.findByTutorIdAndActiveTrue(tutor.getId()).forEach(pet -> pet.setActive(false));
        tutor.setActive(false);
    }

    @Transactional(readOnly = true)
    public List<PetResponseDTO> getTutorPets(Tutor tutor) {

        return petRepository.findByTutorIdAndActiveTrue(tutor.getId())
                .stream()
                .map(this::toPetResponseDTO)
                .toList();
    }
}