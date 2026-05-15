package com.icaro.api_petshop.pet.controller;


import com.icaro.api_petshop.pet.dto.PetRequestDTO;
import com.icaro.api_petshop.pet.dto.PetResponseDTO;
import com.icaro.api_petshop.pet.dto.PetUpdateDTO;
import com.icaro.api_petshop.pet.service.PetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetResponseDTO> registerPet(@RequestBody @Valid PetRequestDTO dto) {

        PetResponseDTO response = petService.createPet(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{tutor-email}/{id}")
    public ResponseEntity<Void> cancelPetRegister(
            @PathVariable("tutor-email") String email,
            @PathVariable("id") Long id) {

        petService.deletePet(id, email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tutor-email}/{id}")
    public ResponseEntity<PetResponseDTO> updatePet(
            @PathVariable("tutor-email") String email,
            @PathVariable("id") Long id,
            @RequestBody @Valid PetUpdateDTO dto) {

        PetResponseDTO response = petService.updatePet(id, email, dto);
        return ResponseEntity.ok(response);
    }
}