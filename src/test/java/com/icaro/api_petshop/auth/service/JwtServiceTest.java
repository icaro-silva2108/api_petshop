package com.icaro.api_petshop.auth.service;

import com.icaro.api_petshop.tutor.model.Tutor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class JwtServiceTest {

    @Autowired
    @InjectMocks
    JwtService jwtService;

    private Tutor tutor;
    @BeforeEach
    void setup() {

        tutor = new Tutor("test", "test@test.com", "hash1234");
    }

    @Test
    @DisplayName("should generate token successfully")
    void generateTokenSuccess() {

        String token = jwtService.generateToken(tutor);

        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("should extract email from successfully")
    void extractEmailTokenSuccess() {

        String token = jwtService.generateToken(tutor);

        String email = jwtService.extractEmail(token);

        assertThat(email).isNotNull();
        assertThat(email).isNotBlank();
        assertThat(email).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("should return different tokens for different tutors")
    void generateDifferentTokens() {

        Tutor differentTutor = new Tutor("test2", "test2@test2.com", "hash4321");

        String token1 = jwtService.generateToken(tutor);
        String token2 = jwtService.generateToken(differentTutor);

        assertThat(token1).isNotEqualTo(token2);
    }
}