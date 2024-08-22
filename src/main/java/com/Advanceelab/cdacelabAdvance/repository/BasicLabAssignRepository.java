package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.BasicLabSubmission;

@Repository
public interface BasicLabAssignRepository extends JpaRepository<BasicLabSubmission, Long>{

}
