package com.icaro.api_petshop.appointment.controller;

import com.icaro.api_petshop.appointment.dto.AppointmentRequestDTO;
import com.icaro.api_petshop.appointment.dto.AppointmentRescheduleDTO;
import com.icaro.api_petshop.appointment.dto.AppointmentResponseDTO;
import com.icaro.api_petshop.appointment.service.AppointmentService;
import com.icaro.api_petshop.tutor.model.Tutor;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> scheduleAppointment(
            @AuthenticationPrincipal Tutor tutor,
            @RequestBody @Valid AppointmentRequestDTO dto) {

        AppointmentResponseDTO response = appointmentService.createAppointment(tutor, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{appointment-id}/cancel")
    public ResponseEntity<Void> cancelAppointment(
            @AuthenticationPrincipal Tutor tutor,
            @PathVariable("appointment-id") Long appointmentId) {

        appointmentService.cancelAppointment(tutor, appointmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pet/{pet-id}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentByPet(@PathVariable("pet-id") Long petId) {

        List<AppointmentResponseDTO> response = appointmentService.listAppointmentByPet(petId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tutor")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentByTutor(@AuthenticationPrincipal Tutor tutor) {

        List<AppointmentResponseDTO> response = appointmentService.listAppointmentByTutor(tutor);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{appointment-id}/reschedule")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(
            @AuthenticationPrincipal Tutor tutor,
            @PathVariable("appointment-id") Long appointmentId,
            @RequestBody @Valid AppointmentRescheduleDTO dto
            ) {

        AppointmentResponseDTO response = appointmentService.rescheduleAppointment(tutor, appointmentId, dto.rescheduleDateTime());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{appointment-id}/complete")
    public ResponseEntity<Void> completeAppointment(
            @AuthenticationPrincipal Tutor tutor,
            @PathVariable("appointment-id") Long appointmentId
            ) {

        appointmentService.completeAppointment(tutor, appointmentId);
        return ResponseEntity.noContent().build();
    }

}
