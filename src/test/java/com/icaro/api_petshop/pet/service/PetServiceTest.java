package com.icaro.api_petshop.pet.service;

import com.icaro.api_petshop.exceptions.UnauthorizedException;
import com.icaro.api_petshop.pet.dto.PetRequestDTO;
import com.icaro.api_petshop.pet.dto.PetResponseDTO;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;
import com.icaro.api_petshop.pet.model.enums.AnimalType;
import com.icaro.api_petshop.pet.repository.PetRepository;
import com.icaro.api_petshop.tutor.model.Tutor;

import com.icaro.api_petshop.tutor.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Autowired
    @InjectMocks
    PetService petService;

    Tutor tutor;
    Pet savedPet;
    PetRequestDTO petDTO;
    @BeforeEach
    void setup() {

        tutor = new Tutor("test", "test@test.com", "hash1234");
        savedPet = new Pet(tutor, "dog", AnimalType.DOG, AnimalSex.MALE, "breed", AnimalSize.SMALL, 1);
        savedPet.setActive(true);

        ReflectionTestUtils.setField(tutor, "id", 1L);
        ReflectionTestUtils.setField(savedPet, "id", 1L);

        petDTO = new PetRequestDTO(
            "dog",
            AnimalType.DOG,
            AnimalSex.MALE,
            "breed",
            AnimalSize.SMALL,
            1
        );
    }

    @Test
    @DisplayName("should return the pet dto after creating the entity")
    void createPetSuccess() {

        PetRequestDTO petDTO = new PetRequestDTO(
                "dog",
                AnimalType.DOG,
                AnimalSex.MALE,
                "breed",
                AnimalSize.SMALL,
                1
        );

        when(petRepository.save(any(Pet.class)))
                .thenReturn(savedPet);

        PetResponseDTO petResult = petService.createPet(tutor, petDTO);

        verify(petRepository).save(any(Pet.class));

        assertThat(petResult).isNotNull();
        assertThat(petResult.tutorId()).isEqualTo(savedPet.getTutor().getId());
    }

    @Test
    @DisplayName("should set active pet to false")
    void deletePetSuccess() {

        when(petRepository.findByIdAndActiveTrue(1L))
                .thenReturn(Optional.of(savedPet));

        petService.deletePet(1L, tutor);

        verify(petRepository, times(1)).findByIdAndActiveTrue(1L);

        assertThat(savedPet.isActive()).isFalse();
    }

    @Test
    @DisplayName("should throw UnauthorizedException caused by tutor deleting not owned pet")
    void deletePetNotOwnedTutor() {

        Tutor differentTutor = new Tutor("test", "test@test2.com", "hash123");
        ReflectionTestUtils.setField(differentTutor, "id", 2L);
        tutorRepository.save(differentTutor);

        when(petRepository.findByIdAndActiveTrue(1L))
                .thenReturn(Optional.of(savedPet));

        assertThatThrownBy(
                () -> petService.deletePet(1L, differentTutor)
        ).isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("this pet does not belong to this tutor");
    }

    @Test
    @DisplayName("should throw EntityNotFoundException caused by pet not found")
    void deletePetNotFound() {

        when(petRepository.findByIdAndActiveTrue(2L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> petService.deletePet(2L, tutor)
        ).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("pet not found");

        verify(petRepository, times(1)).findByIdAndActiveTrue(2L);
    }
}