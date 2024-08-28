package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Advanceelab.cdacelabAdvance.entity.LoginAttempt;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Integer>{

	LoginAttempt findByUserId(int userId);
}
