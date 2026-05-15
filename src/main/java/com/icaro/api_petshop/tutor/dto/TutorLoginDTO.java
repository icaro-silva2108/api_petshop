package com.icaro.api_petshop.tutor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TutorLoginDTO(

    @NotBlank(message = "email can not be empty")
    @Email
    String email,

    @NotBlank(message = "password can not be empty")
    @Size(min = 8, message = "password must have at least 8 characters")
    String password
) {}