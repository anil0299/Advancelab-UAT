package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.ReleaseNote1;

@Repository
public interface ReleaseNote1Repo extends JpaRepository<ReleaseNote1, Integer>{

	void save(ReleaseNote1Repo releaseNote);
	
}
