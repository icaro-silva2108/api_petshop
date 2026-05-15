package com.icaro.api_petshop.appointment.service;

import com.icaro.api_petshop.appointment.dto.AppointmentRequestDTO;
import com.icaro.api_petshop.appointment.dto.AppointmentResponseDTO;
import com.icaro.api_petshop.appointment.model.Appointment;
import com.icaro.api_petshop.appointment.model.enums.AppointmentStatus;
import com.icaro.api_petshop.appointment.repository.AppointmentRepository;
import com.icaro.api_petshop.exceptions.InvalidDateException;
import com.icaro.api_petshop.exceptions.UnauthorizedException;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.repository.PetRepository;
import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.tutor.repository.TutorRepository;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;

    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {

        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getPetTutor().getId(),
                appointment.getPetTutor().getName(),
                appointment.getPet().getName(),
                appointment.getPet().getId(),
                appointment.getServiceType(),
                appointment.getStatus(),
                appointment.getScheduledDateTime(),
                appointment.getCreatedAt()
        );
    }

    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {

        Tutor tutor = tutorRepository.findById(dto.tutorId()).orElseThrow(
                () -> new EntityNotFoundException("tutor not found")
        );

        Pet pet = petRepository.findById(dto.petId()).orElseThrow(
                () -> new EntityNotFoundException("pet not found")
        );

        if (!pet.getTutor().getId().equals(tutor.getId())) {
            throw new UnauthorizedException("this pet does not belong to this tutor");
        }

        if (dto.scheduledDateTime().isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("appointment can not be scheduled in the past");
        }

        Appointment appointment = new Appointment(
                tutor,
                pet,
                dto.serviceType(),
                dto.scheduledDateTime()
        );

        appointmentRepository.save(appointment);
        return toResponseDTO(appointment);
    }

    public void cancelAppointment(Long tutorId, Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new EntityNotFoundException("appointment not found")
        );

        if (!appointment.getPetTutor().getId().equals(tutorId)) {

            throw new UnauthorizedException("appointment does not belong to this tutor");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELED) {

            throw new IllegalStateException("appointment is already canceled");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {

            throw new IllegalStateException("completed appointment can not be canceled");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> listAppointmentByPet(Long petId) {

        return appointmentRepository.findByPet(petId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> listAppointmentByTutor(Long tutorId) {

        return appointmentRepository.findByPetTutor(tutorId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AppointmentResponseDTO rescheduleAppointment(Long tutorId, Long appointmentId, LocalDateTime rescheduledDateTime) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new EntityNotFoundException("appointment not found")
        );

        if (!appointment.getPetTutor().getId().equals(tutorId)) {

            throw new UnauthorizedException("appointment does not belong to this tutor");
        }
        if (rescheduledDateTime.isBefore(LocalDateTime.now())) {

            throw new InvalidDateException("can not reschedule in the past");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELED) {

            throw new IllegalStateException("can not reschedule a canceled appointment");
        }

        appointment.setStatus(AppointmentStatus.RESCHEDULED);
        appointment.setScheduledDateTime(rescheduledDateTime);
        appointmentRepository.save(appointment);
        return toResponseDTO(appointment);
    }

    public void completeAppointment(Long tutorId, Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new EntityNotFoundException("appointment not found")
        );

        if (!appointment.getPetTutor().getId().equals(tutorId)) {

            throw new UnauthorizedException("appointment does not belong to this tutor");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELED) {

            throw new IllegalStateException("can not complete a canceled appointment");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {

            throw new IllegalStateException("appointment already completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }
}