package com.icaro.api_petshop.appointment.service;

import com.icaro.api_petshop.appointment.dto.AppointmentRequestDTO;
import com.icaro.api_petshop.appointment.dto.AppointmentResponseDTO;
import com.icaro.api_petshop.appointment.model.Appointment;
import com.icaro.api_petshop.appointment.model.enums.AppointmentType;
import com.icaro.api_petshop.appointment.repository.AppointmentRepository;
import com.icaro.api_petshop.exceptions.InvalidDateException;
import com.icaro.api_petshop.exceptions.UnauthorizedException;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;
import com.icaro.api_petshop.pet.model.enums.AnimalType;
import com.icaro.api_petshop.pet.repository.PetRepository;

import com.icaro.api_petshop.tutor.model.Tutor;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    @Autowired
    AppointmentService appointmentService;

    Tutor tutor;
    Pet pet;
    Appointment appointment;
    AppointmentRequestDTO appointmentDTO;
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

        ReflectionTestUtils.setField(tutor, "id", 1L);
        ReflectionTestUtils.setField(pet, "id", 1L);
        ReflectionTestUtils.setField(appointment, "id", 1L);

        appointmentDTO = new AppointmentRequestDTO(
                tutor.getId(),
                pet.getId(),
                AppointmentType.VETERINARY,
                LocalDateTime.of(2060, 1, 1, 10, 0, 0));
    }

    @Test
    @DisplayName("should return the appointment dto after creating the entity")
    void createAppointmentSuccess() {

        when(petRepository.findById(1L))
                .thenReturn(Optional.of(pet));

        when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(appointment);

        AppointmentResponseDTO appointmentResult = appointmentService.createAppointment(tutor, appointmentDTO);

        assertThat(appointmentResult.id()).isEqualTo(appointment.getId());
        assertThat(appointmentResult.tutorId()).isEqualTo(tutor.getId());
        assertThat(appointmentResult.petId()).isEqualTo(pet.getId());
    }

    @Test
    @DisplayName("should throw EntityNotFoundException caused by pet not found to the given dto")
    void createAppointmentPetNotFound() {

        when(petRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> appointmentService.createAppointment(tutor, appointmentDTO)
        ).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("pet not found");
    }

    @Test
    @DisplayName("should throw EntityNotFoundException caused by inactive pet")
    void createAppointmentPetInactive() {

        pet.setActive(false);

        when(petRepository.findById(1L))
                .thenReturn(Optional.of(pet));

        assertThatThrownBy(
                () -> appointmentService.createAppointment(tutor, appointmentDTO)
        ).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("pet not found");
    }

    @Test
    @DisplayName("should throw UnauthorizedException caused by tutor creating not owned pet appointment")
    void createAppointmentPetNotOwned() {

        Tutor differentTutor = new Tutor("test2", "test2@test2.com", "hash4321");

        when(petRepository.findById(1L))
                .thenReturn(Optional.of(pet));

        assertThatThrownBy(
                () -> appointmentService.createAppointment(differentTutor, appointmentDTO)
        ).isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("this pet does not belong to this tutor");
    }

    @Test
    @DisplayName("should throw InvalidDateException caused by creating appointment in the past")
    void createAppointmentInvalidDate() {

        AppointmentRequestDTO pastDateAppointmentDTO = new AppointmentRequestDTO(
                tutor.getId(),
                pet.getId(),
                AppointmentType.VETERINARY,
                LocalDateTime.now().minusDays(1));

        when(petRepository.findById(1L))
                .thenReturn(Optional.of(pet));

        assertThatThrownBy(
                () -> appointmentService.createAppointment(tutor, pastDateAppointmentDTO)
        ).isInstanceOf(InvalidDateException.class)
                .hasMessageContaining("appointment can not be scheduled in the past");
    }
}