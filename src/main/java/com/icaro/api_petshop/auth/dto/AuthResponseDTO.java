package com.icaro.api_petshop.auth.dto;

import com.icaro.api_petshop.tutor.dto.TutorResponseDTO;

public record AuthResponseDTO(

    String token,
    TutorResponseDTO tutorResponse
) {}