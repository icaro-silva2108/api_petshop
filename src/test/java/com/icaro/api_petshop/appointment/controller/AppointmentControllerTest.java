package com.icaro.api_petshop.appointment.controller;

import com.icaro.api_petshop.appointment.dto.AppointmentRequestDTO;
import com.icaro.api_petshop.appointment.dto.AppointmentResponseDTO;
import com.icaro.api_petshop.appointment.model.enums.AppointmentStatus;
import com.icaro.api_petshop.appointment.model.enums.AppointmentType;
import com.icaro.api_petshop.config.JwtAuthenticationFilter;
import com.icaro.api_petshop.exceptions.InvalidDateException;
import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.appointment.service.AppointmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AppointmentController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        JwtAuthenticationFilter.class
                }
        )
)
@ActiveProfiles("test")
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService appointmentService;

    private Tutor tutor;
    private AppointmentRequestDTO appointmentRequestDTO;
    private AppointmentResponseDTO appointmentResponseDTO;
    @BeforeEach
    void setup() {

        tutor = new Tutor("test", "test@test.com", "hash1234");
        ReflectionTestUtils.setField(tutor, "id", 1L);

        appointmentRequestDTO = new AppointmentRequestDTO(
                tutor.getId(),
                1L,
                AppointmentType.VETERINARY,
                LocalDateTime.of(2060, 1, 1, 10, 0, 0)
        );
        appointmentResponseDTO = new AppointmentResponseDTO(
                1L,
                tutor.getId(),
                "test",
                "dog",
                1L,
                AppointmentType.VETERINARY,
                AppointmentStatus.SCHEDULED,
                LocalDateTime.of(2060, 1, 1, 10, 0, 0),
                Instant.now()
        );
    }

    @Test
    @DisplayName("should create appointment successfully created 201")
    void createAppointmentSuccess() throws Exception {

        String requestBody = objectMapper.writeValueAsString(appointmentRequestDTO);

        when(appointmentService.createAppointment(any(Tutor.class), any(AppointmentRequestDTO.class)))
                .thenReturn(appointmentResponseDTO);

        mockMvc.perform(post("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf())
                .with(user(tutor)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1L))

                .andExpect(jsonPath("$.tutorId").exists())
                .andExpect(jsonPath("$.tutorId").value(1L))

                .andExpect(jsonPath("$.petId").exists())
                .andExpect(jsonPath("$.petId").value(1L));


        verify(appointmentService, times(1))
                .createAppointment(any(Tutor.class), any(AppointmentRequestDTO.class));
    }

    @Test
    @DisplayName("should return 400 for scheduling in the past")
    void createAppointmentSchedulePast() throws Exception {

        String requestBody = objectMapper.writeValueAsString(appointmentRequestDTO);

        when(appointmentService.createAppointment(any(Tutor.class), any(AppointmentRequestDTO.class)))
                .thenThrow(new InvalidDateException("appointment can not be scheduled in the past"));

        mockMvc.perform(post("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf())
                .with(user(tutor)))

                .andExpect(status().isBadRequest())
                .andExpect(content().string("appointment can not be scheduled in the past"));

        verify(appointmentService, times(1))
                .createAppointment(any(Tutor.class), any(AppointmentRequestDTO.class));
    }

    @Test
    @DisplayName("should return the list of appointments through the given tutor")
    void getAppointmentByTutorSuccess() throws Exception {

        List<AppointmentResponseDTO> appointmentList = List.of(
                new AppointmentResponseDTO(
                        1L,
                        tutor.getId(),
                        "test",
                        "dog1",
                        1L,
                        AppointmentType.VETERINARY,
                        AppointmentStatus.SCHEDULED,
                        LocalDateTime.of(2060, 1, 1, 10, 0, 0),
                        Instant.now()),

                appointmentResponseDTO = new AppointmentResponseDTO(
                        2L,
                        tutor.getId(),
                        "test",
                        "dog2",
                        2L,
                        AppointmentType.VETERINARY,
                        AppointmentStatus.SCHEDULED,
                        LocalDateTime.of(2060, 2, 2, 12, 30, 0),
                        Instant.now())
        );

        when(appointmentService.listAppointmentByTutor(tutor))
                .thenReturn(appointmentList);

        mockMvc.perform(get("/appointments/tutor")
                .with(csrf())
                .with(user(tutor)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));;
    }
}