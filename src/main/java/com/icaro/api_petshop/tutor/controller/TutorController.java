package com.icaro.api_petshop.tutor.controller;

import com.icaro.api_petshop.pet.dto.PetResponseDTO;
import com.icaro.api_petshop.tutor.dto.TutorRequestDTO;
import com.icaro.api_petshop.tutor.dto.TutorResponseDTO;
import com.icaro.api_petshop.tutor.dto.TutorUpdateDTO;
import com.icaro.api_petshop.tutor.service.TutorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutors")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    @PostMapping("/signup")
    public ResponseEntity<TutorResponseDTO> signupTutor(@RequestBody @Valid TutorRequestDTO dto) {

        TutorResponseDTO response = tutorService.createTutor(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{email}")
    public ResponseEntity<TutorResponseDTO> updateTutor(
            @PathVariable("email") String email,
            @RequestBody @Valid TutorUpdateDTO dto) {

        TutorResponseDTO response = tutorService.updateTutor(email, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> cancelRegister(@PathVariable("email") String email) {

        tutorService.deleteTutor(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{email}/pets")
    public ResponseEntity<List<PetResponseDTO>> getTutorPets(@PathVariable("email") String email) {

        return ResponseEntity.ok(tutorService.getTutorPets(email));
    }
}