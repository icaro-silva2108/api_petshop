package com.icaro.api_petshop.pet.controller;

import com.icaro.api_petshop.config.JwtAuthenticationFilter;
import com.icaro.api_petshop.exceptions.UnauthorizedException;
import com.icaro.api_petshop.pet.dto.PetRequestDTO;
import com.icaro.api_petshop.pet.dto.PetResponseDTO;
import com.icaro.api_petshop.pet.model.Pet;
import com.icaro.api_petshop.pet.model.enums.AnimalSex;
import com.icaro.api_petshop.pet.model.enums.AnimalSize;
import com.icaro.api_petshop.pet.model.enums.AnimalType;
import com.icaro.api_petshop.pet.service.PetService;
import com.icaro.api_petshop.tutor.model.Tutor;

import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.test.util.ReflectionTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = PetController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        JwtAuthenticationFilter.class
                }
        )
)
@ActiveProfiles("test")
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petService;

    private Tutor tutor;
    private Pet pet;
    private PetRequestDTO petRequestDTO;
    private PetResponseDTO petResponseDTO;
    @BeforeEach
    void setup() {

        tutor = new Tutor("test", "test@test.com", "hash1234");
        pet = new Pet(tutor, "dog", AnimalType.DOG, AnimalSex.MALE, "breed", AnimalSize.SMALL, 1);

        ReflectionTestUtils.setField(tutor, "id", 1L);
        ReflectionTestUtils.setField(pet, "id", 1L);

        petRequestDTO = new PetRequestDTO(
                "dog",
                AnimalType.DOG,
                AnimalSex.MALE,
                "breed",
                AnimalSize.SMALL, 1);
        petResponseDTO = new PetResponseDTO(
                pet.getId(),
                tutor.getId(),
                "dog",
                AnimalType.DOG,
                AnimalSex.MALE,
                "breed",
                AnimalSize.SMALL,
                1);

    }

    @Test
    @DisplayName("should create pet successfully 201 created")
    void createPetSuccess() throws Exception {

        String requestBody = objectMapper.writeValueAsString(petRequestDTO);

        when(petService.createPet(any(Tutor.class), any(PetRequestDTO.class)))
                .thenReturn(petResponseDTO);

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf())
                .with(user(tutor))
                )

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(pet.getId()))

                .andExpect(jsonPath("$.tutorId").exists())
                .andExpect(jsonPath("$.tutorId").value(tutor.getId()));

        verify(petService, times(1)).createPet(any(Tutor.class), any(PetRequestDTO.class));
    }

    @Test
    @DisplayName("should block the request from a non authenticated user 401 unauthorized")
    void createPetUnauthorized() throws Exception {

        String requestBody = objectMapper.writeValueAsString(petRequestDTO);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should set pet active to false by soft delete")
    void deletePetSuccess() throws Exception {

        doNothing().when(petService).deletePet(pet.getId(), tutor);

        mockMvc.perform(delete("/pets/1")
                .with(csrf())
                .with(user(tutor)))

                .andExpect(status().isNoContent());

        verify(petService, times(1)).deletePet(any(Long.class), any(Tutor.class));
    }

    @Test
    @DisplayName("should return 404 when pet not found")
    void deletePetNotFound() throws Exception {

        doThrow(new EntityNotFoundException("pet not found"))
                .when(petService).deletePet(pet.getId(), tutor);

        mockMvc.perform(delete("/pets/1")
                .with(csrf())
                .with(user(tutor)))

                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 403 forbidden trying to delete pet not owned")
    void deletePetNotOwned() throws Exception {

        doThrow(new UnauthorizedException("this pet does not belong to this tutor"))
                .when(petService).deletePet(pet.getId(), tutor);

        mockMvc.perform(delete("/pets/1")
                .with(csrf())
                .with(user(tutor)))

                .andExpect(status().isForbidden())
                .andExpect(content().string("this pet does not belong to this tutor"));
    }
}