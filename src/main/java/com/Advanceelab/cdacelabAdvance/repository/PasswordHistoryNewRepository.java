package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Advanceelab.cdacelabAdvance.entity.PasswordHistoryNew;

public interface PasswordHistoryNewRepository extends JpaRepository<PasswordHistoryNew, Long>{

	List<PasswordHistoryNew> findAllByEmailAddress(String emailAddress);
}
