package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.Passwordhistory;

@Repository
public interface PasswordhistoryRepository extends JpaRepository<Passwordhistory, Integer>{

}
