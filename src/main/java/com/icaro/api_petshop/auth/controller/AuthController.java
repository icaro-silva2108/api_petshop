package com.icaro.api_petshop.auth.controller;

import com.icaro.api_petshop.tutor.dto.TutorLoginDTO;
import com.icaro.api_petshop.tutor.dto.TutorResponseDTO;
import com.icaro.api_petshop.tutor.service.TutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TutorService tutorService;

    @PostMapping("/signin")
    public ResponseEntity<TutorResponseDTO> signingTutor(
            @RequestBody @Valid TutorLoginDTO dto) {

        TutorResponseDTO response = tutorService.loginValidation(dto.email(), dto.password());

        return ResponseEntity.ok(response);
    }
}