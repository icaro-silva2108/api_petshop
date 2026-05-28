package com.icaro.api_petshop.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icaro.api_petshop.auth.service.JwtService;
import com.icaro.api_petshop.config.JwtAuthenticationFilter;
import com.icaro.api_petshop.config.SecurityConfig;
import com.icaro.api_petshop.exceptions.InvalidCredentialsException;
import com.icaro.api_petshop.tutor.dto.TutorLoginDTO;
import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.tutor.service.TutorService;

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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {JwtAuthenticationFilter.class, SecurityConfig.class}
        )
)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private TutorService tutorService;

    @MockitoBean
    private JwtService jwtService;

    private TutorLoginDTO loginDTO;
    private Tutor tutor;
    @BeforeEach
    void setup() {

        loginDTO = new TutorLoginDTO("test@test.com", "password123");
        tutor = new Tutor("test", "test@test.com", "hash1234");
    }

    @Test
    @DisplayName("should validate tutor successfully")
    @WithMockUser
    void signinSuccess() throws Exception {

        String requestBody = objectMapper.writeValueAsString(loginDTO);

        when(tutorService.loginValidation(loginDTO.email(), loginDTO.password()))
                .thenReturn(tutor);

        when(jwtService.generateToken(tutor))
                .thenReturn("token123");

        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf())
            )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.token").value("token123"));

    }

    @Test
    @DisplayName("should assert IvalidCredentialsException")
    @WithMockUser
    void signinInvalidCredentials() throws Exception {

        String requestBody = objectMapper.writeValueAsString(loginDTO);

        when(tutorService.loginValidation(loginDTO.email(), loginDTO.password()))
                .thenThrow(new InvalidCredentialsException());


        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf())
            )
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("invalid credentials"));

    }
}