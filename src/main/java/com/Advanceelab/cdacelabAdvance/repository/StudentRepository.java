package com.Advanceelab.cdacelabAdvance.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.User;

@Repository
public interface StudentRepository extends JpaRepository<StudentDtls, Integer> {
	
	@Query(value ="select * from student_dtls where role=?1",nativeQuery = true)
	StudentDtls findByRole1(String role);
	
	@Query(value = "select * from student_dtls where class_name= ?1",nativeQuery = true)
	List<StudentDtls> findStudentDtlsByClassName(String id);

	public boolean existsByEmailAddress(String email);
	
	public boolean existsByLabemail(String labmail);
	
	List<StudentDtls> findByApproved(boolean approved);
	
	@Query(value = "SELECT * FROM student_dtls WHERE approved = :approved AND role = :role", nativeQuery = true)
    List<StudentDtls> findByApprovedAndRole(@Param("approved") boolean approved, @Param("role") String role);

	@Query(value = "select u.category_certificate FROM student_dtls u WHERE u.id = :id", nativeQuery = true)
	public byte[] findCertificate(@Param("id") int id);

	public StudentDtls findByEmailAddress(String emailAddress);

	public List<StudentDtls> findByApprovedAndRegistrationDateGreaterThanEqual(boolean b, LocalDate registrationDate);

	// for Registered Student and Comletion Status

	@Query(value = "SELECT * FROM student_dtls WHERE approved=true ", nativeQuery = true)
	public List<StudentDtls> getRegisteredStudent();

//for state
	@Query(value = "SELECT State, COUNT(*) as count FROM student_dtls WHERE approved=:approved AND role=:role  GROUP BY State", nativeQuery = true)
	List<Object[]> getStateCount(@Param("approved")boolean approved,@Param("role") String role);

	default HashMap<String, Integer> getStateCountMap() {
		List<Object[]> rows = getStateCount(true, "USER");
		HashMap<String, Integer> map = new HashMap<>();
		for (Object[] row : rows) {
			map.put((String) row[0], ((Number) row[1]).intValue());
		}
		return map;
	}

//for category

//	@Query(value = "SELECT category, COUNT(*) as count FROM student_dtls WHERE approved=:approved AND role=:role   GROUP BY category", nativeQuery = true)
//	List<Object[]> getCategoryCount(@Param("approved")boolean approved,@Param("role") String role);
//
//	default HashMap<String, Integer> getCategoryCountMap() {
//		List<Object[]> rows = getCategoryCount(true,"USER");
//		HashMap<String, Integer> map = new HashMap<>();
//		for (Object[] row : rows) {
//			map.put((String) row[0], ((Number) row[1]).intValue());
//		}
//		return map;
//	}
	
	
	
	
	
	@Query(value = "SELECT category, COUNT(*) as count FROM student_dtls WHERE approved = :approved AND role = :role GROUP BY category", nativeQuery = true)
    List<Object[]> getCategoryCount(@Param("approved") boolean approved, @Param("role") String role);

    // Method to get category and gender counts
    @Query(value = "SELECT category, gender, COUNT(*) as count FROM student_dtls WHERE approved = :approved AND role = :role GROUP BY category, gender", nativeQuery = true)
    List<Object[]> getCategoryGenderCount(@Param("approved") boolean approved, @Param("role") String role);

    default HashMap<String, Integer> getCategoryCountMap() {
        List<Object[]> rows = getCategoryCount(true,"USER");
        HashMap<String, Integer> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put((String) row[0], ((Number) row[1]).intValue());
        }
        return map;
    }

    default HashMap<String, HashMap<String, Integer>> getCategoryGenderCountMap() {
        List<Object[]> rows = getCategoryGenderCount(true, "USER");
        HashMap<String, HashMap<String, Integer>> map = new HashMap<>();

        for (Object[] row : rows) {
            String category = (String) row[0];
            String gender = (String) row[1];
            int count = ((Number) row[2]).intValue();

            map.putIfAbsent(category, new HashMap<>());
            map.get(category).put(gender, count);
        }
        return map;
    }

	
	
		
	
	
	
	

	// for state-wise and category wise

	/*
	 * @Query(value =
	 * "SELECT State,COUNT(CASE WHEN category= 'SC' THEN 1 END) AS SC_Count,COUNT(CASE WHEN category= 'ST' THEN 1 END) AS ST_Count, COUNT(CASE WHEN category= 'EWS' THEN 1 END) AS EWS_Count FROM student_dtls WHERE approved=true GROUP BY State"
	 * , nativeQuery = true) List<Object[]> getStateCategoryCount();
	 */
//	@Query(value = "SELECT State,\r\n" + "COUNT(CASE WHEN category= 'SC' THEN 1 END) AS SC_Count,\r\n"
//			+ "COUNT(CASE WHEN category= 'ST' THEN 1 END) AS ST_Count,\r\n"
//			+ "COUNT(CASE WHEN category= 'EWS' THEN 1 END) AS EWS_Count,\r\n"
//			+ "SUM(CASE WHEN category= 'SC' THEN 1 ELSE 0 END) + SUM(CASE WHEN category= 'ST' THEN 1 ELSE 0 END) + SUM(CASE WHEN category= 'EWS' THEN 1 ELSE 0 END) AS Total_Count\r\n"
//			+ "FROM student_dtls\r\n" + "WHERE approved=:approved AND role=:role  \r\n" + "GROUP BY State;", nativeQuery = true)
//	List<Object[]> getStateCategoryCount(@Param("approved")boolean approved,@Param("role") String role);
	
	
	
	
	
    @Query(value = "SELECT State,\r\n" +
            "COUNT(CASE WHEN category = 'SC' AND gender = 'Male' THEN 1 END) AS SC_Male_Count,\r\n" +
            "COUNT(CASE WHEN category = 'SC' AND gender = 'Female' THEN 1 END) AS SC_Female_Count,\r\n" +
            "COUNT(CASE WHEN category = 'SC' THEN 1 END) AS SC_Total,\r\n" +

            "COUNT(CASE WHEN category = 'ST' AND gender = 'Male' THEN 1 END) AS ST_Male_Count,\r\n" +
            "COUNT(CASE WHEN category = 'ST' AND gender = 'Female' THEN 1 END) AS ST_Female_Count,\r\n" +
            "COUNT(CASE WHEN category = 'ST' THEN 1 END) AS ST_Total,\r\n" +

            "COUNT(CASE WHEN category = 'EWS' AND gender = 'Male' THEN 1 END) AS EWS_Male_Count,\r\n" +
            "COUNT(CASE WHEN category = 'EWS' AND gender = 'Female' THEN 1 END) AS EWS_Female_Count,\r\n" +
            "COUNT(CASE WHEN category = 'EWS' THEN 1 END) AS EWS_Total,\r\n" +

            "SUM(CASE WHEN category IN ('SC', 'ST', 'EWS') THEN 1 ELSE 0 END) AS Total_Count\r\n" +
            "FROM student_dtls\r\n" +
            "WHERE approved = :approved AND role = :role\r\n" +
            "GROUP BY State;", nativeQuery = true)
List<Object[]> getStateCategoryAndGenderCount(@Param("approved") boolean approved, @Param("role") String role);

	
	
	
	

	// for completion
	/*
	 * @Query(value =
	 * "SELECT COUNT(*) AS num_occurrences , CAST(CASE WHEN COUNT(*) = 1 THEN 20 WHEN COUNT(*) = 2 THEN 40 WHEN COUNT(*) = 3 THEN 60 WHEN COUNT(*) = 4 THEN 80 WHEN COUNT(*) = 5 THEN 100 ELSE 0 END AS INTEGER) AS percentage FROM exercise_submission"
	 * , nativeQuery = true) List<Object[]> getPercentCount();
	 */

	@Query(value = "SELECT username, COUNT(username) as noa FROM exercise_submission GROUP BY username", nativeQuery = true)
	List<Object[]> getPercentCount();

	@Query(value = "Select DISTINCT state FROM student_dtls", nativeQuery = true)
	List<String> findDistinctState();

	@Query(value = "Select DISTINCT u.college FROM student_dtls u WHERE u.state = :state", nativeQuery = true)
	List<String> findCollegesByState(@Param("state") String state);

	@Query(value = "SELECT u.email_address FROM student_dtls u WHERE u.college = :college", nativeQuery = true)
	List<String> findEmailAddress(@Param("college") String college);

	public List<StudentDtls> findByCollege(String college);

	public List<StudentDtls> findByEmailAddressIn(List<String> asList);

//	public List<StudentDtls> findByApprovedAndClassNameIsNull(boolean b);

//	public List<StudentDtls> findByApprovedAndClassNameIsNullAndBatchEquals(boolean b, int batch);	
	
//	
//	public List<StudentDtls> findByCollegeAndApprovedAndClassNameIsNullAndBatchEquals(String college, boolean b,int batch);
	
	public List<StudentDtls> findByBatchEquals(@Min(1) int batch);

	public StudentDtls findByLabemail(String labemail);
	
	@Query(value = "SELECT COUNT(batch) FROM student_dtls WHERE batch <> 0", nativeQuery = true)
	int findlenofbatch();

	@Query("SELECT MAX(e2.batch) FROM StudentDtls e2")
	public Integer findCurrentBatchNumber();

	public int countByBatch(int currentBatch);

	@Query(value = "SELECT batch FROM student_dtls", nativeQuery = true)
	List<Integer> findListofBatch();

	
	@Query(value = "SELECT * FROM student_dtls WHERE approved=true AND class_name= ?1", nativeQuery = true)
	public List<StudentDtls> getTeacherStatus(String className);

	@Query(value = "SELECT userid, COUNT(username) as noi FROM exercise_submission GROUP BY userid", nativeQuery = true)
	List<Object[]> getPercentCount1();

	@Query(value = "SELECT * FROM student_dtls WHERE role=:role AND approved = :approved", nativeQuery = true)
	public List<StudentDtls> listofStudentByRole(@Param("role") String role,@Param("approved") Boolean approved);
	
	@Query(value= "SELECT * FROM student_dtls WHERE batch = :batchid AND approved = :approved AND class_name= :classname AND role = :roleuser", nativeQuery = true)
	public List<StudentDtls> findByApprovedAndClassNameIsNullAndBatchEqualsAndRoleEquals(@Param("batchid") int batchid, @Param("approved") boolean approved,@Param("classname") String classname, @Param("roleuser") String roleuser);
	
	@Query(value = "SELECT * FROM student_dtls WHERE college=college AND approved=true AND batch=batch AND role=USER", nativeQuery = true)
	public List<StudentDtls> findByCollegeAndApprovedAndClassNameIsNullAndBatchEqualsRoleEquals(@Param("college") String college,@Param("b") boolean b,@Param("batch") int batch,@Param("string") String string);

	
	@Query(value = "SELECT * FROM student_dtls WHERE batch = :batchid AND approved = :approved AND college = :colleges AND class_name = :classname OR class_name IS NULL AND role = :roleuser", nativeQuery = true)
	public List<StudentDtls> findCollegeBybatchByroleByclassname(@Param("batchid") int batchid, @Param("approved") boolean approved, @Param("roleuser") String roleuser, @Param("colleges") String colleges, @Param("classname") String classname);
	
	@Query(value="SELECT * FROM student_dtls Where approved=:approved AND role=:role",nativeQuery = true)
	public List<StudentDtls> findByApproveAndRole(@Param("approved")boolean approved,@Param("role") String role);
	
	@Query(value = "SELECT COUNT(class_name) FROM student_dtls WHERE class_name=:className", nativeQuery = true)
	public int findCountByClassName(String className);

	@Query(value="SELECT email_address FROM student_dtls WHERE class_name=:className",nativeQuery = true)
	List<String> findEmailAddressByClass(String className);

	@Query(value = "select * from student_dtls where state= ?1",nativeQuery = true)
	List<StudentDtls> findStudentDtlsByState(String state);

	@Query(value = "select * from student_dtls where category = ?1",nativeQuery = true)
	List<StudentDtls> findByCategories(String categories);
	
	@Query(value = "select id from student_dtls WHERE approved=:approved",nativeQuery = true)
	List<Integer> findIdByApprovedStatus(@Param("approved")boolean approved);
	
	@Query("SELECT DISTINCT s.batch FROM StudentDtls s WHERE s.batch != 0")
    List<Integer> findDistinctBatches();
	
	List<StudentDtls> findByBatch(int batch);
	
	Page<StudentDtls> findByApproved(boolean approved, Pageable pageable);
	
	Page<StudentDtls> findByEmailAddressContainingIgnoreCaseAndApproved(String emailAddress, boolean approved, Pageable pageable);
	
	Page<StudentDtls> findByApprovedAndRole(boolean approved,String role,Pageable pageable);
	
	Page<StudentDtls> findByEmailAddressContainingIgnoreCaseAndApprovedAndRole(String emailAddress,boolean approved,String role,Pageable pageable);
	
	@Query("SELECT CONCAT(s.firstName, ' ', s.lastName) " + "FROM StudentDtls s WHERE s.labemail = :labemail")
	List<String> findFullNamesByLabemail(String labemail);
	
	boolean existsByMobileNumber(String mobile);

	@Query("SELECT mobileNumber FROM StudentDtls s WHERE s.labemail = :labemail")
	String findMobileNumberByLabemail(String labemail);

	@Query("SELECT s FROM StudentDtls s " +
		       "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
		       "LOWER(CAST(s.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.fatherName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.qualification) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.emailAddress) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.mobileNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "CAST(s.dob AS string) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.category) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.gender) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.state) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.college) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.registrationDate AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.approvedDate AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.validTill AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "(:searchTerm IS NOT NULL AND :searchTerm <> '' AND LOWER(CAST(s.batch AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))) " +
		       "AND s.approved = true " +
		       "AND s.role = 'USER'")
	Page<StudentDtls> searchStudents(@Param("searchTerm") String searchTerm, Pageable pageable);
	
	@Query("SELECT s FROM StudentDtls s " +
		       "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
		       "LOWER(CAST(s.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.emailAddress) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.college) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.state) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
		       "AND s.approved = true " +
		       "AND s.role = 'USER'")
	Page<StudentDtls> searchStudentsBasicDetails(@Param("searchTerm") String searchTerm, Pageable pageable);

	
	@Query("SELECT s FROM StudentDtls s " +
		       "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
		       "LOWER(CAST(s.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.emailAddress) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
		       "AND s.approved = true " +
		       "AND s.role = 'USER'")
	Page<StudentDtls> searchStudentsIdNameAndEmail(@Param("searchTerm") String searchTerm, Pageable pageable);
	
	@Query("SELECT s FROM StudentDtls s " +
		       "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
		       "LOWER(s.labemail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.registrationDate AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
		       "AND s.approved = true " +
		       "AND s.role = 'USER'")
	Page<StudentDtls> searchLabEmailAndRegistrationDate(@Param("searchTerm") String searchTerm, Pageable pageable);
	
	@Query("SELECT s FROM StudentDtls s " +
		       "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
		       "LOWER(s.labemail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.registrationDate AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
		       "AND s.approved = true " +
		       "AND s.role = 'USER'" +
		       "AND s.registrationDate >= CAST(:startDate AS date) " +
		       "AND s.registrationDate <= CAST(:endDate AS date)")
	Page<StudentDtls> searchLabEmailAndRegistrationDate(@Param("searchTerm") String searchTerm, LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	int countByApprovedAndRole(Boolean status, String role);
	
	@Query("SELECT s FROM StudentDtls s " +
		       "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
		       "LOWER(CAST(s.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.emailAddress) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.qualification) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.college) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.state) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.registrationDate AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
		       "AND s.approved = false " +
		       "AND s.role = 'USER'")
	Page<StudentDtls> searchPendingStudents(@Param("searchTerm") String searchTerm, Pageable pageable);
	
	@Query("SELECT s FROM StudentDtls s " +
		       "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
		       "LOWER(CAST(s.id AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.emailAddress) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.qualification) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.batch AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.className) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.college) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(s.state) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.registrationDate AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.approvedDate AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(CAST(s.validTill AS string)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
		       "AND s.approved = true " +
		       "AND s.role = 'USER'")
	Page<StudentDtls> searchApprovedStudents(@Param("searchTerm") String searchTerm, Pageable pageable);

}
