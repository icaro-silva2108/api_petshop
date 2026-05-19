package com.icaro.api_petshop.tutor.controller;

import com.icaro.api_petshop.pet.dto.PetResponseDTO;
import com.icaro.api_petshop.tutor.dto.TutorRequestDTO;
import com.icaro.api_petshop.tutor.dto.TutorResponseDTO;
import com.icaro.api_petshop.tutor.dto.TutorUpdateDTO;
import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.tutor.service.TutorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutors")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    @GetMapping("/me")
    public ResponseEntity<TutorResponseDTO> me(@AuthenticationPrincipal Tutor tutor) {

        TutorResponseDTO tutorDTO = new TutorResponseDTO(
                tutor.getId(),
                tutor.getName(),
                tutor.getEmail()
        );

        return ResponseEntity.ok(tutorDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<TutorResponseDTO> signupTutor(@RequestBody @Valid TutorRequestDTO dto) {

        TutorResponseDTO response = tutorService.createTutor(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<TutorResponseDTO> updateTutor(
            @AuthenticationPrincipal Tutor tutor,
            @RequestBody @Valid TutorUpdateDTO dto) {

        TutorResponseDTO response = tutorService.updateTutor(tutor, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> cancelRegister(@AuthenticationPrincipal Tutor tutor) {

        tutorService.deleteTutor(tutor);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/pets")
    public ResponseEntity<List<PetResponseDTO>> getTutorPets(@AuthenticationPrincipal Tutor tutor) {

        return ResponseEntity.ok(tutorService.getTutorPets(tutor));
    }
}