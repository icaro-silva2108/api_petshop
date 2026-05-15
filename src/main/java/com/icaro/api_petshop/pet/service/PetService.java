package com.icaro.api_petshop.pet.service;

import com.icaro.api_petshop.exceptions.UnauthorizedException;
import com.icaro.api_petshop.pet.dto.PetRequestDTO;
import com.icaro.api_petshop.pet.dto.PetResponseDTO;
import com.icaro.api_petshop.pet.dto.PetUpdateDTO;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.repository.PetRepository;
import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.tutor.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {

    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;

    private PetResponseDTO toResponseDTO(Pet pet) {
        return new PetResponseDTO(
                pet.getId(),
                pet.getTutor().getId(),
                pet.getName(),
                pet.getType(),
                pet.getSex(),
                pet.getBreed(),
                pet.getSize(),
                pet.getAge());
    }

    public PetResponseDTO createPet(PetRequestDTO dto) {

        Tutor tutor = tutorRepository.findById(dto.tutorId()).orElseThrow(
                () -> new EntityNotFoundException("tutor not found to the given id")
        );

        Pet pet = new Pet(
                tutor,
                dto.name(),
                dto.type(),
                dto.sex(),
                dto.breed(),
                dto.size(),
                dto.age());

        petRepository.save(pet);
        return toResponseDTO(pet);
    }

    public void deletePet(Long id, String email) {

        Tutor tutor = tutorRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("tutor not found")
        );

        Pet pet = petRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("pet not found")
        );

        if (!pet.getTutor().getId().equals(tutor.getId())) {
            throw new UnauthorizedException("this pet does not belong to this tutor");
        }

        petRepository.delete(pet);
    }

    public PetResponseDTO updatePet(Long id, String tutorEmail, PetUpdateDTO dto) {

     Tutor tutor = tutorRepository.findByEmail(tutorEmail).orElseThrow(
             () -> new EntityNotFoundException("tutor not found")
     );

     Pet pet = petRepository.findById(id).orElseThrow(
             () -> new EntityNotFoundException("pet not found")
     );

     if (!pet.getTutor().getId().equals(tutor.getId())) {
         throw new UnauthorizedException("this pet does not belong to this tutor");
     }

     if (dto.name() != null && !dto.name().isBlank()) {
         pet.setName(dto.name());
     }
     if (dto.type() != null) {
         pet.setType(dto.type());
     }
     if (dto.sex() != null) {
         pet.setSex(dto.sex());
     }
     if (dto.breed() != null && !dto.breed().isBlank()) {
         pet.setBreed(dto.breed());
     }
     if (dto.size() != null) {
         pet.setSize(dto.size());
     }
     if (dto.age() != null) {
         pet.setAge(dto.age());
     }

     petRepository.save(pet);
     return toResponseDTO(pet);
    }
}