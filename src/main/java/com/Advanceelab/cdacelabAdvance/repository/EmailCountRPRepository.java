package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Advanceelab.cdacelabAdvance.entity.EmailCountRP;

public interface EmailCountRPRepository extends JpaRepository<EmailCountRP, Long>{

	EmailCountRP findByEmailAddress(String emailAddress);
}
