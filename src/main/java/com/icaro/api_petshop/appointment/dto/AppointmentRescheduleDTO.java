package com.icaro.api_petshop.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRescheduleDTO(

    @NotNull(message = "reschedule date and time is required")
    @Future(message = "reschedule date and time must be in the future")
    LocalDateTime rescheduleDateTime
) {}
