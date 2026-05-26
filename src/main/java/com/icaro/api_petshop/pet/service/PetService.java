package com.icaro.api_petshop.pet.service;

import com.icaro.api_petshop.exceptions.UnauthorizedException;
import com.icaro.api_petshop.pet.dto.PetRequestDTO;
import com.icaro.api_petshop.pet.dto.PetResponseDTO;
import com.icaro.api_petshop.pet.dto.PetUpdateDTO;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.repository.PetRepository;
import com.icaro.api_petshop.tutor.model.Tutor;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {

    private final PetRepository petRepository;

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

    public PetResponseDTO createPet(Tutor tutor, PetRequestDTO dto) {

        Pet pet = new Pet(
                tutor,
                dto.name(),
                dto.type(),
                dto.sex(),
                dto.breed(),
                dto.size(),
                dto.age());

        Pet saved = petRepository.save(pet);
        return toResponseDTO(saved);
    }

    public Pet findPetOwner(Long id, Tutor tutor) {

        Pet pet = petRepository.findByIdAndActiveTrue(id).orElseThrow(
                () -> new EntityNotFoundException("pet not found")
        );

        if (!pet.isActive()){
            throw new EntityNotFoundException("pet not found");
        }
        if (!pet.getTutor().getId().equals(tutor.getId())) {
            throw new UnauthorizedException("this pet does not belong to this tutor");
        }
        return pet;
    }

    public void deletePet(Long id, Tutor tutor) {

        Pet pet = findPetOwner(id, tutor);

        pet.setActive(false);
    }

    public PetResponseDTO updatePet(Long id, Tutor tutor, PetUpdateDTO dto) {

     Pet pet = findPetOwner(id, tutor);

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

     return toResponseDTO(pet);
    }
}