package com.icaro.api_petshop.tutor.repository;

import com.icaro.api_petshop.tutor.model.Tutor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;


@DataJpaTest
@ActiveProfiles("test")
class TutorRepositoryTest {

    @Autowired
    private TutorRepository tutorRepository;

    @Test
    @DisplayName("should return the tutor found by email")
    void findByEmailSuccess() {

        String email = "test@test.com";

        Tutor tutor = new Tutor();
        tutor.setName("test");
        tutor.setEmail(email);

        tutorRepository.save(tutor);

        Optional<Tutor> foundTutor = tutorRepository.findByEmail(email);

        assertThat(foundTutor).isPresent();
        assertThat(foundTutor.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("should not find any tutor through the given email")
    void findByEmailNotFound() {

        String email = "test@test.com";

        Optional<Tutor> foundTutor = tutorRepository.findByEmail(email);

        assertThat(foundTutor).isEmpty();
    }
}