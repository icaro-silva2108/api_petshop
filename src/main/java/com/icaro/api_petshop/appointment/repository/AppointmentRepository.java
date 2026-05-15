package com.icaro.api_petshop.appointment.repository;

import com.icaro.api_petshop.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPet(Long petId);

    List<Appointment> findByPetTutor(Long tutorId);
}