package com.icaro.api_petshop.tutor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record TutorRequestDTO(

    @NotBlank(message = "name is required")
    @Size(min = 2, max = 100)
    String name,

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    String email,

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password must have at least 8 characters")
    String password
) {}