package com.icaro.api_petshop.appointment.model;

import com.icaro.api_petshop.appointment.model.enums.AppointmentStatus;
import com.icaro.api_petshop.appointment.model.enums.AppointmentType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.tutor.model.Tutor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "appointment")
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor petTutor;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Enumerated(EnumType.STRING)
    @Setter
    private AppointmentType serviceType;

    @Enumerated(EnumType.STRING)
    @Setter
    private AppointmentStatus status;

    @Column(name = "scheduled_date_time")
    @Setter
    private LocalDateTime scheduledDateTime;

    @Column(name = "created_at")
    private Instant createdAt;

    public Appointment(
        Tutor petTutor,
        Pet pet,
        AppointmentType serviceType,
        LocalDateTime scheduledDateTime

    ) {

        if (scheduledDateTime.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("appointment cannot be scheduled in the past");
        }

        this.petTutor = Objects.requireNonNull(petTutor, "appointment must have a tutor");
        this.pet = Objects.requireNonNull(pet, "appointment must have a pet");
        this.serviceType = Objects.requireNonNull(serviceType, "appointment must have a service type");
        this.status = AppointmentStatus.SCHEDULED;
        this.scheduledDateTime = Objects.requireNonNull(scheduledDateTime, "appointment must have a scheduled datetime");
        this.createdAt = Instant.now();
    }
}