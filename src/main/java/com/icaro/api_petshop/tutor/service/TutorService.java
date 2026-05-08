package com.icaro.api_petshop.tutor.service;

import com.icaro.api_petshop.tutor.model.Tutor;

public class TutorService {

    public Tutor registerTutor(String name, String email, String passwordHash) {
        return new Tutor(name, email, passwordHash);
    }
}