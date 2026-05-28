package com.icaro.api_petshop.tutor.controller;

import com.icaro.api_petshop.config.JwtAuthenticationFilter;
import com.icaro.api_petshop.config.SecurityConfig;
import com.icaro.api_petshop.exceptions.EmailAlreadyExistsException;
import com.icaro.api_petshop.tutor.dto.TutorRequestDTO;
import com.icaro.api_petshop.tutor.dto.TutorResponseDTO;
import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.tutor.service.TutorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TutorController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
@ActiveProfiles("test")
public class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TutorService tutorService;

    private TutorRequestDTO requestDTO;
    private TutorResponseDTO responseDTO;
    @BeforeEach
    void setup() {

        requestDTO = new TutorRequestDTO("test", "test@test.com", "password123");
        responseDTO = new TutorResponseDTO(1L, "test", "test@test.com");
    }

    @Test
    @DisplayName("should create tutor successfully returning 201 created")
    @WithMockUser
    void signupTutorSuccess() throws Exception {

        String requestBody = objectMapper.writeValueAsString(requestDTO);

        when(tutorService.createTutor(any(TutorRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/tutors/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf())
            )
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1L))

                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value("test"))

                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value("test@test.com")
        );

        verify(tutorService, times(1))
                .createTutor(any(TutorRequestDTO.class));
    }

    @Test
    @DisplayName("should throw 409 conflict caused by email already existent")
    @WithMockUser
    void signupTutorEmailExistent() throws Exception {

        String requestBody = objectMapper.writeValueAsString(requestDTO);

        when(tutorService.createTutor(any()))
                .thenThrow(new EmailAlreadyExistsException());

        mockMvc.perform(post("/tutors/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf())
            )
                .andExpect(status().isConflict())
                .andExpect(content().string("email already exists")
        );
    }

    @Test
    @DisplayName("should return the tutor info successfully")
    @WithMockUser(username = "test@test.com")
    void meSuccess() throws Exception {

        Tutor tutor = new Tutor("test", "test@test.com", "hash1234");
        ReflectionTestUtils.setField(tutor, "id", 1L);

        mockMvc.perform(get("/tutors/me")
                .with(user(tutor))
            )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1L))

                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value("test@test.com")
        );
    }
}