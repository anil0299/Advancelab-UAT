package com.Advanceelab.cdacelabAdvance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

	User findByUsername(String username);
	
	@Query(value ="Select p.batch FROM User p WHERE p.username= :username")
	int findBatchbyUsername(@Param("username") String username);
	
	@Query(value ="Select p.id FROM User p WHERE p.username= :username")
	Long FindUserIdbyUsername(@Param("username") String username);
	
	@Query(value ="select * from loginuser where role=?1",nativeQuery = true)
	User findByRole1(String role);
	
	List<User> findByRole(String role);

	 int countByBatch(int batch); 
	
	@Query(value = "SELECT username FROM loginuser WHERE role = 'TEACHER' ", nativeQuery = true)
	List<String> findUsername();

	List<User> findByUsernameInAndRole(List<String> asList, String string);

	boolean existsByUsernameOrEmailAddress(String emailAddress, String emailAddress2);

	void deleteByEmailAddress(String emailAddress);

	User findByEmailAddress(String emailAddress);

	boolean existsByUsername(String username);
	
	@Query(value = "select email_address from loginuser where username =:username", nativeQuery = true)
	String findEmailAddressByUsername(String username);

	@Query(value = "select email_address from loginuser where username =:username", nativeQuery = true)
	List<String> findAllEmailAddressByUsername(String username);
}
