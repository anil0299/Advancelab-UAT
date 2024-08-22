package com.Advanceelab.cdacelabAdvance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.CourseTopics;
@Repository
public interface CourseTopicsRepository extends JpaRepository<CourseTopics, Integer> {
	
	@Query(value = "select id from p_course_topics where topic_name=?1",nativeQuery = true)
	public int findByTopicName(String topicName);

}