package com.icaro.api_petshop.tutor.service;

import com.icaro.api_petshop.exceptions.EmailAlreadyExistsException;
import com.icaro.api_petshop.exceptions.InvalidCredentialsException;
import com.icaro.api_petshop.pet.repository.PetRepository;
import com.icaro.api_petshop.tutor.dto.TutorRequestDTO;
import com.icaro.api_petshop.tutor.dto.TutorResponseDTO;
import com.icaro.api_petshop.tutor.model.Tutor;
import com.icaro.api_petshop.tutor.repository.TutorRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TutorServiceTest {

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    TutorService tutorService;

    @Test
    @DisplayName("should return the tutor dto after creating the entity")
    void createTutorSuccess() {

        TutorRequestDTO dto = new TutorRequestDTO(
                "test",
                "test@test.com",
                "test1234"
        );

        Tutor savedTutor = new Tutor("test", "test@test.com", "hash1234");

        when(passwordEncoder.encode(dto.password()))
                .thenReturn("hash1234");

        when(tutorRepository.save(any(Tutor.class)))
                .thenReturn(savedTutor);

        TutorResponseDTO dtoResult = tutorService.createTutor(dto);

        verify(tutorRepository).save(any(Tutor.class));
        verify(passwordEncoder).encode(dto.password());

        assertThat(dtoResult).isNotNull();
        assertThat(dto.name()).isEqualTo(dtoResult.name());
        assertThat(dto.email()).isEqualTo(dtoResult.email());
    }

    @Test
    @DisplayName("should throw EmailAlreadyExistsException")
    void createTutorExistingEmail() {

        TutorRequestDTO dto = new TutorRequestDTO(
                "test1",
                "test@test.com",
                "test1234"
        );

        Tutor existingTutor = new Tutor("test2", "test@test.com", "test1234");

        when(tutorRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(existingTutor));

        assertThatThrownBy(
                () -> tutorService.createTutor(dto)
        ).isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("email already exists");
    }

    @Test
    @DisplayName("should return tutor info after login")
    void loginValidationSuccess() {

        String email = "test@test.com";
        String password = "test1234";

        Tutor tutor = new Tutor("test", email, "hash1234");

        when(tutorRepository.findByEmailAndActiveTrue(email))
                .thenReturn(Optional.of(tutor));
        when(passwordEncoder.matches(password, "hash1234"))
                .thenReturn(true);

        Tutor tutorResult = tutorService.loginValidation(email, password);

        verify(tutorRepository).findByEmailAndActiveTrue(email);
        verify(passwordEncoder).matches(password, "hash1234");

        assertThat(tutorResult).isNotNull();
        assertThat(tutorResult.getName()).isEqualTo(tutor.getName());
        assertThat(tutorResult.getEmail().equals(email)).isTrue();
    }

    @Test
    @DisplayName("should throw InvalidCredentialsException caused by tutor not found")
    void loginValidationTutorNotFound() {

        assertThatThrownBy(
                () -> tutorService.loginValidation("non@exists.com", "test1234")
        ).isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("invalid credentials");
    }

    @Test
    @DisplayName("should throw InvalidCredentialsException caused by wrong password")
    void loginValidationWrongPassword() {

        Tutor tutor = new Tutor("test", "test@test.com", "hash1234");

        when(tutorRepository.findByEmailAndActiveTrue("test@test.com"))
                .thenReturn(Optional.of(tutor));

        assertThatThrownBy(
                () -> tutorService.loginValidation("test@test.com", "wrongPassword")
        ).isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("invalid credentials");
    }

    @Test
    @DisplayName("should throw InvalidCredentialsException caused by tutor found but inactive")
    void loginValidationInactiveTutor() {

        Tutor tutor = new Tutor("test", "test@test.com", "test1234");
        tutor.setActive(false);

        when(tutorRepository.findByEmailAndActiveTrue("test@test.com"))
                .thenReturn(Optional.of(tutor));

        assertThatThrownBy(
                () -> tutorService.loginValidation("test@test.com", "test1234")
        ).isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("invalid credentials");
    }
}