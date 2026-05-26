package com.icaro.api_petshop.appointment.repository;

import com.icaro.api_petshop.appointment.model.Appointment;
import com.icaro.api_petshop.appointment.model.enums.AppointmentType;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;
import com.icaro.api_petshop.pet.model.enums.AnimalType;
import com.icaro.api_petshop.pet.repository.PetRepository;
import com.icaro.api_petshop.tutor.model.Tutor;

import com.icaro.api_petshop.tutor.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AppointmentRepositoryTest {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    TutorRepository tutorRepository;

    Tutor tutor;
    Pet pet;
    Appointment appointment;
    @BeforeEach
    void setup() {

        tutor = new Tutor("test", "test@test.com", "hash1234");
        pet = new Pet(tutor, "dog", AnimalType.DOG, AnimalSex.MALE, "breed", AnimalSize.SMALL, 1);
        appointment = new Appointment(
                tutor,
                pet,
                AppointmentType.VETERINARY,
                LocalDateTime.of(2060, 1, 1, 10, 0,0)
        );

        tutorRepository.save(tutor);
        petRepository.save(pet);
        appointmentRepository.save(appointment);
    }

    @Test
    @DisplayName("should return the appointment list trough the given pet id")
    void findAppointmentByPetIdSuccess() {

        List<Appointment> appointmentList = appointmentRepository.findByPetId(pet.getId());

        assertThat(appointmentList.isEmpty()).isFalse();
        assertThat(appointmentList.getFirst().getPetTutor()).isEqualTo(tutor);
        assertThat(appointmentList.getFirst().getPet()).isEqualTo(pet);
    }

    @Test
    @DisplayName("should return an empty appointment list trough the non existing given id")
    void findAppointmentByPetIdEmpty() {

        List<Appointment> appointmentList = appointmentRepository.findByPetId(1000L);

        assertThat(appointmentList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("should return the appointment list trough the given tutor id")
    void findAppointmentByPetTutorIdSuccess() {

        List<Appointment> appointmentList = appointmentRepository.findByPetTutorId(tutor.getId());

        assertThat(appointmentList.isEmpty()).isFalse();
        assertThat(appointmentList.getFirst().getPetTutor()).isEqualTo(tutor);
        assertThat(appointmentList.getFirst().getPet()).isEqualTo(pet);
    }

    @Test
    @DisplayName("should return the appointment list trough the given tutor id")
    void findAppointmentByPetTutorIdEmpty() {

        List<Appointment> appointmentList = appointmentRepository.findByPetTutorId(1000L);

        assertThat(appointmentList.isEmpty()).isTrue();
    }
}