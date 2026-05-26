package com.icaro.api_petshop.pet.repository;

import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;
import com.icaro.api_petshop.pet.model.enums.AnimalType;
import com.icaro.api_petshop.tutor.model.Tutor;

import com.icaro.api_petshop.tutor.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    Tutor tutor;
    Pet pet;
    @BeforeEach
    void setup() {
        tutor = new Tutor("test", "test@test.com", "hash1234");
        pet = new Pet(tutor, "dog", AnimalType.DOG, AnimalSex.MALE, "breed", AnimalSize.SMALL, 1);
    }

    @Test
    @DisplayName("should return the the tutor pet named 'dog'")
    void findByTutorSuccess() {

        tutorRepository.save(tutor);
        petRepository.save(pet);

        List<Pet> petList = petRepository.findByTutorIdAndActiveTrue(tutor.getId());

        assertThat(petList.isEmpty()).isFalse();
        assertThat(petList.getFirst().getName()).isEqualTo("dog");
        assertThat(petList.getFirst().getTutor()).isEqualTo(tutor);
    }

    @Test
    @DisplayName("should return the pet through the given pet id")
    void findByIdSuccess() {

        tutorRepository.save(tutor);
        petRepository.save(pet);

        Optional<Pet> petFound = petRepository.findByIdAndActiveTrue(pet.getId());

        assertThat(petFound).isPresent();
    }

    @Test
    @DisplayName("should not return inactive tutor pets")
    void findByTutorInactive() {

        tutor.setActive(false);

        tutorRepository.save(tutor);
        petRepository.save(pet);

        List<Pet> petList = petRepository.findByTutorIdAndActiveTrue(tutor.getId());

        assertThat(petList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("should not return inactive pet through the given pet id")
    void findByIdInactive() {

        pet.setActive(false);

        tutorRepository.save(tutor);
        petRepository.save(pet);

        Optional<Pet> petFound = petRepository.findByIdAndActiveTrue(pet.getId());

        assertThat(petFound).isEmpty();
    }
}