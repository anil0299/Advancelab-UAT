package com.Advanceelab.cdacelabAdvance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Advanceelab.cdacelabAdvance.repository.Batch_Master_repo;

@Service
public class Batch_Service {

	@Autowired 
	Batch_Master_repo batch_master_Repo;
	
	public List<Integer> getBatchExerIdByBatchno(Integer batch_no){

		return batch_master_Repo.findExerIdByBatchNo(batch_no);
	}
	
}
