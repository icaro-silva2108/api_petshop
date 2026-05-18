package com.icaro.api_petshop.auth.controller;

import com.icaro.api_petshop.auth.dto.AuthResponseDTO;
import com.icaro.api_petshop.auth.service.JwtService;
import com.icaro.api_petshop.tutor.dto.TutorLoginDTO;
import com.icaro.api_petshop.tutor.dto.TutorResponseDTO;
import com.icaro.api_petshop.tutor.model.Tutor;
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
    private final JwtService jwtService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> signingTutor(
            @RequestBody @Valid TutorLoginDTO dto) {

        Tutor tutor = tutorService.loginValidation(dto.email(), dto.password());
        AuthResponseDTO response = new AuthResponseDTO(
                jwtService.generateToken(tutor),
                new TutorResponseDTO(
                        tutor.getId(),
                        tutor.getName(),
                        tutor.getEmail()
                        )
        );

        return ResponseEntity.ok(response);
    }
}