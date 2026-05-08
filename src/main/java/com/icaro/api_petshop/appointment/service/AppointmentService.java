package com.icaro.api_petshop.appointment.service;

import com.icaro.api_petshop.appointment.model.enums.AppointmentType;
import com.icaro.api_petshop.appointment.model.Appointment;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.tutor.model.Tutor;

import java.time.LocalDateTime;

public class AppointmentService {

    public Appointment createAppointment(
            Tutor petTutor,
            Pet pet,
            AppointmentType type,
            LocalDateTime scheduledDateTime
    ) {
        return new Appointment(
          petTutor,
          pet,
          type,
          scheduledDateTime
        );
    }
}