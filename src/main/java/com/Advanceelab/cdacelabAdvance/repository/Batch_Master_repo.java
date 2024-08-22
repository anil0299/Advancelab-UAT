package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.Batch_Master;

@Repository
public interface Batch_Master_repo extends JpaRepository<Batch_Master, Integer>{

	@Query("SELECT u.exer_id FROM Batch_Master u WHERE u.batch_no = :batch_no")
	List<Integer> findExerIdByBatchNo(@Param("batch_no") Integer batch_no);
	
	
	@Query(value="SELECT EXISTS(SELECT u FROM Batch_Master u WHERE u.batch_no = :batch_no AND u.exer_id = :exer_id)",nativeQuery =true)
	Boolean checkExercise(@Param("batch_no") Integer batch_no, @Param("exer_id") Integer exerId);

	@Query(value = "SELECT COUNT(*) FROM Batch_Master WHERE batch_no=?1",nativeQuery = true)
	Integer findDtlsByBatch_no(Integer getbatch);
}
