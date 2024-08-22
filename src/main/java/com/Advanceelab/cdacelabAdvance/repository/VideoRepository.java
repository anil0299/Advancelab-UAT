package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long>{

}
