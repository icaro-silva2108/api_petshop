package com.icaro.api_petshop.models;

import com.icaro.api_petshop.enums.AppointmentStatus;
import com.icaro.api_petshop.enums.AppointmentType;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Appointment {


    private final UUID id;
    private Tutor petTutor;
    private Pet pet;
    private AppointmentType serviceType;
    private AppointmentStatus status;
    private LocalDateTime scheduledDateTime;
    private final Instant createdAt;

    public Appointment(
        Tutor petTutor,
        Pet pet,
        AppointmentType serviceType,
        LocalDateTime scheduledDateTime

    ) {

        if (scheduledDateTime.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("appointment cannot be scheduled in the past");
        }

        this.id = UUID.randomUUID();
        this.petTutor = Objects.requireNonNull(petTutor, "appointment must have a tutor");
        this.pet = Objects.requireNonNull(pet, "appointment must have a pet");
        this.serviceType = Objects.requireNonNull(serviceType, "appointment must have a service type");
        this.status = AppointmentStatus.SCHEDULED;
        this.scheduledDateTime = Objects.requireNonNull(scheduledDateTime, "appointment must have a scheduled datetime");
        this.createdAt = Instant.now();
    }

    //GETTERS

    public UUID getId() {return id;}

    public Tutor getPetTutor() {return petTutor;}

    public Pet getPet() {return pet;}

    public AppointmentType getServiceType() {return serviceType;}

    public AppointmentStatus getStatus() {return status;}

    public LocalDateTime getScheduledDateTime() {return scheduledDateTime;}

    public Instant getCreatedAt() {return createdAt;}

    //SETTERS

    public void setPetTutor(Tutor petTutor) {this.petTutor = petTutor;}

    public void setPet(Pet pet) {this.pet = pet;}

    public void setServiceType(AppointmentType serviceType) {this.serviceType = serviceType;}

    public void setStatus(AppointmentStatus status) {this.status = status;}

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {this.scheduledDateTime = scheduledDateTime;}
}