package com.icaro.api_petshop.appointment.dto;

import com.icaro.api_petshop.appointment.model.enums.AppointmentType;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record AppointmentRequestDTO(

    @NotNull(message = "tutor id is required")
    Long tutorId,

    @NotNull(message = "pet id is required")
    Long petId,

    @NotNull(message = "appointment type is required")
    AppointmentType serviceType,

    @NotNull(message = "scheduled date and time required")
    @Future(message = "scheduled date and time must be in the future")
    LocalDateTime scheduledDateTime
) {}