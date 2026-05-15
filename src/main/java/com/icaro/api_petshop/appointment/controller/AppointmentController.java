package com.icaro.api_petshop.appointment.controller;

import com.icaro.api_petshop.appointment.dto.AppointmentRequestDTO;
import com.icaro.api_petshop.appointment.dto.AppointmentRescheduleDTO;
import com.icaro.api_petshop.appointment.dto.AppointmentResponseDTO;
import com.icaro.api_petshop.appointment.service.AppointmentService;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> scheduleAppointment(@RequestBody @Valid AppointmentRequestDTO dto) {

        AppointmentResponseDTO response = appointmentService.createAppointment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{tutor-id}/{appointment-id}/cancel")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable("tutor-id") Long tutorId,
            @PathVariable("appointment-id") Long appointmentId) {

        appointmentService.cancelAppointment(tutorId, appointmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pet/{pet-id}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentByPet(@PathVariable("pet-id") Long petId) {

        List<AppointmentResponseDTO> response = appointmentService.listAppointmentByPet(petId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tutor/{tutor-id}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentByTutor(@PathVariable("tutor-id") Long tutorId) {

        List<AppointmentResponseDTO> response = appointmentService.listAppointmentByTutor(tutorId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{tutor-id}/{appointment-id}/reschedule")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(
            @PathVariable("tutor-id") Long tutorId,
            @PathVariable("appointment-id") Long appointmentId,
            @RequestBody @Valid AppointmentRescheduleDTO dto
            ) {

        AppointmentResponseDTO response = appointmentService.rescheduleAppointment(tutorId, appointmentId, dto.rescheduleDateTime());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{tutor-id}/{appointment-id}/complete")
    public ResponseEntity<Void> completeAppointment(
            @PathVariable("tutor-id") Long tutorId,
            @PathVariable("appointment-id") Long appointmentId
            ) {

        appointmentService.completeAppointment(tutorId, appointmentId);
        return ResponseEntity.noContent().build();
    }

}
