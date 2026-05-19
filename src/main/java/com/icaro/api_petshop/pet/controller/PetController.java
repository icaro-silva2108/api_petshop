package com.icaro.api_petshop.pet.controller;


import com.icaro.api_petshop.pet.dto.PetRequestDTO;
import com.icaro.api_petshop.pet.dto.PetResponseDTO;
import com.icaro.api_petshop.pet.dto.PetUpdateDTO;
import com.icaro.api_petshop.pet.service.PetService;
import com.icaro.api_petshop.tutor.model.Tutor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetResponseDTO> registerPet(
            @AuthenticationPrincipal Tutor tutor,
            @RequestBody @Valid PetRequestDTO dto) {

        PetResponseDTO response = petService.createPet(tutor, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelPetRegister(
            @AuthenticationPrincipal Tutor tutor,
            @PathVariable("id") Long id) {

        petService.deletePet(id, tutor);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(
            @AuthenticationPrincipal Tutor tutor,
            @PathVariable("id") Long id,
            @RequestBody @Valid PetUpdateDTO dto) {

        PetResponseDTO response = petService.updatePet(id, tutor, dto);
        return ResponseEntity.ok(response);
    }
}