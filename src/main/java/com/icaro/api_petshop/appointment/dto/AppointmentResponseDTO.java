package com.icaro.api_petshop.appointment.dto;

import com.icaro.api_petshop.appointment.model.enums.AppointmentStatus;
import com.icaro.api_petshop.appointment.model.enums.AppointmentType;

import java.time.LocalDateTime;
import java.time.Instant;

public record AppointmentResponseDTO(

    Long id,
    Long tutorId,
    String tutorName,
    String petName,
    Long petId,
    AppointmentType serviceType,
    AppointmentStatus status,
    LocalDateTime scheduledDateTime,
    Instant createdAt
) {}