package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Advanceelab.cdacelabAdvance.entity.ResetPassword;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long>{

	ResetPassword findByEmailAddress(String emailAddress);
	List<ResetPassword> findAllByEmailAddress(String emailAddress);
}
