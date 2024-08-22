package com.Advanceelab.cdacelabAdvance.service;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.Advanceelab.cdacelabAdvance.entity.AdvanceLabUserVmDetails;
import com.Advanceelab.cdacelabAdvance.entity.ExerciseSubmission;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.entity.User;
import com.Advanceelab.cdacelabAdvance.entity.Vm_Master;
import com.Advanceelab.cdacelabAdvance.repository.AdvanceLabUserVmDetailsRepository;
import com.Advanceelab.cdacelabAdvance.repository.ExerciseSubmission_Repo;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;
import com.Advanceelab.cdacelabAdvance.repository.UserRepository;
import com.Advanceelab.cdacelabAdvance.repository.Vm_MasterRepo;
import com.Advanceelab.cdacelabAdvance.security.RequestParameterValidationUtility;
import com.jayway.jsonpath.JsonPath;

@Service
public class AdvanceLabService {
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private Vm_MasterRepo vm_MasterRepo;
	
	@Autowired
	private AdvanceLabUserVmDetailsRepository advanceLabUserVmDetailsRepository;
	
	@Autowired
	private ExerciseSubmission_Repo exerciseSubmission_Repo;
	
	private final HttpClient httpClient;

	public AdvanceLabService() {
        this.httpClient = HttpClient.newHttpClient();
	}
	
	public CompletableFuture<Void> cloneVm(String uuid, String vmName, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v3/vms/" + uuid + "/clone"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Basic " + password)
                        .method("POST", HttpRequest.BodyPublishers.ofString("{\n  \"override_spec\": {\n    \"name\": \"" + vmName + "\"\n   }\n   }"))
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                return null; // Handle exception gracefully, you may throw an exception or return a specific value
            }
			return null;
        });
    }
	
	
	 public CompletableFuture<String> searchUuid(String vmName, String password) {
	        return CompletableFuture.supplyAsync(() -> {
	            try {
	                HttpRequest request = HttpRequest.newBuilder()
	                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v3/vms/list"))
	                        .header("Content-Type", "application/json")
	                        .header("Authorization", "Basic " + password)
	                        .method("POST", HttpRequest.BodyPublishers.ofString("{\n  \"kind\": \"vm\",\n  \"offset\": 0,\n  \"length\": 123,\n  \"filter\": \"vm_name==" + vmName + "\",\n  \"sort_order\": \"ASCENDING\",\n  \"sort_attribute\": \"ASCENDING\"\n}"))
	                        .build();
	                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	                String responseBody = response.body();
	                return JsonPath.read(responseBody, "$.entities[0].metadata.uuid");
	            } catch (Exception ex) {
	                ex.printStackTrace();
	                return null;
	            }
	        });
	    }
	 
	 public CompletableFuture<String> searchIp(String vmName, String password) {
	        return CompletableFuture.supplyAsync(() -> {
		            try {
		                HttpRequest request = HttpRequest.newBuilder()
		                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v3/vms/list"))
		                        .header("Content-Type", "application/json")
		                        .header("Authorization", "Basic " + password)
		                        .method("POST", HttpRequest.BodyPublishers.ofString("{\n  \"kind\": \"vm\",\n  \"offset\": 0,\n  \"length\": 123,\n  \"filter\": \"vm_name==" + vmName + "\",\n  \"sort_order\": \"ASCENDING\",\n  \"sort_attribute\": \"ASCENDING\"\n}"))
		                        .build();
		                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		                String responseBody = response.body();
		                String ip = JsonPath.read(responseBody, "$.entities[0].status.resources.nic_list[0].ip_endpoint_list.[0].ip");
		                return ip;
		            } catch (Exception ex) {
	                    return null;
		            }
	        });
	    }
	 
	 public CompletableFuture<Void> powerOn(String UUID, String auth) {
	        return CompletableFuture.runAsync(() -> {
	            try {
	                HttpRequest request = HttpRequest.newBuilder()
	                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v0.8/vms/set_power_state/fanout"))
	                        .header("Content-Type", "application/json")
	                        .header("Authorization", "Basic YWRtaW46Q2RhY0AxMjMhQCM=")
	                        .method("POST", HttpRequest.BodyPublishers.ofString("[{\"generic_dto\":{\"transition\":\"on\",\"uuid\":\"" + UUID + "\"},\"cluster_uuid\":\"0005a833-66d0-008c-62e3-08f1ea7d7714\"}]"))
	                        .build();
	                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	                System.out.println(response.body());
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        });
	    }
	 
	 public CompletableFuture<Void> deleteVm(String vmUuid) {
	        return CompletableFuture.runAsync(() -> {
	            try {
	                HttpRequest request = HttpRequest.newBuilder()
	                        .uri(URI.create("https://nmegh02.prd.dcservices.in/api/nutanix/v3/vms/" + vmUuid))
	                        .header("Content-Type", "application/json")
	                        .header("Authorization", "Basic YWRtaW46Q2RhY0AxMjMhQCM=")
	                        .method("DELETE", HttpRequest.BodyPublishers.noBody())
	                        .build();
	                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	                System.out.println(response.body());
	                System.out.println("Deleted Vm with uuid " + vmUuid);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        });
	    }
	
	public String generateUserPass(String username) {
	        int atIndex = username.indexOf('@');
	        StudentDtls studentDtls = studentRepo.findByLabemail(username);
	        LocalDate dateOfBirth = studentDtls.getDob();
	        String fName = studentDtls.getFirstName();
	        String hciPassword = generateHciPassword(fName, dateOfBirth);
	        return Base64.getEncoder().encodeToString((username + ":" + hciPassword).getBytes());
	    }

	public String generateHciPassword(String fName, LocalDate dateOfBirth) {
	        if (fName.length() >= 3) {
	            fName = fName.substring(0, 3);
	            String hciPassword = fName + "@@" +
	                    String.format("%02d%02d", dateOfBirth.getDayOfMonth(), dateOfBirth.getMonthValue());
	            return hciPassword.substring(0, 1).toUpperCase() + hciPassword.substring(1);
	        } else if (fName.length() == 2) {
	            fName = fName.substring(0, 2);
	            String hciPassword = fName + "@@@" +
	                    String.format("%02d%02d", dateOfBirth.getDayOfMonth(), dateOfBirth.getMonthValue());
	            return hciPassword.substring(0, 1).toUpperCase() + hciPassword.substring(1);
	        } else {
	            fName = fName.substring(0, 1);
	            String hciPassword = fName + "@@@@" +
	                    String.format("%02d%02d", dateOfBirth.getDayOfMonth(), dateOfBirth.getMonthValue());
	            return hciPassword.substring(0, 1).toUpperCase() + hciPassword.substring(1);
	        }
	    }
	 
	 
	 public boolean checkExerciseSubmitted(String username, long exerciseId)
	 {
		 ExerciseSubmission exerciseSubmission= exerciseSubmission_Repo.findByUsernameAndExerciseId(username, exerciseId);
		 if(exerciseSubmission != null)
			 return true;
		 return false;
	 }
	 
	public String generateVmName(String clonedvmName,String username)
	{
		int atIndex = username.indexOf('@');
	    String userNameBeforeAtTheRate = username.substring(0, atIndex);
	    User user = userRepo.findByUsername(username);
	    return clonedvmName+userNameBeforeAtTheRate+String.valueOf(user.getId());
	}
	
	public void save(String username,String vmName,String vmUUID,String vmIp,Long exerciseId)
	{
		AdvanceLabUserVmDetails advanceLabUserVmDetails = new AdvanceLabUserVmDetails();
		advanceLabUserVmDetails.setUsername(username);
		advanceLabUserVmDetails.setVmName(vmName);
		advanceLabUserVmDetails.setUUID(vmUUID);
		advanceLabUserVmDetails.setIp(vmIp);
		advanceLabUserVmDetails.setExerciseId(exerciseId);
		advanceLabUserVmDetailsRepository.save(advanceLabUserVmDetails);
	}
	
	public boolean checkAtLeastOneVmNotCreated(long exerciseId,String username)
	{
		Set<String> vmAllreadyCreated = advanceLabUserVmDetailsRepository.findByUsername(username, exerciseId);
		List<Vm_Master> AllVmOfTheExercise = vm_MasterRepo.findAllByExerciseId(Long.valueOf(exerciseId));
		for(Vm_Master vm:AllVmOfTheExercise)
		{
			String vmName = generateVmName(vm.getVm_name(),username);
			if(!vmAllreadyCreated.contains(vmName))
				return true;
		}
		return false;
	}
	
	public List<AdvanceLabUserVmDetails> checkVmAllReadyCreated(long exerciseId, String username)
	{
		List<AdvanceLabUserVmDetails> vmAllreadyCreated = advanceLabUserVmDetailsRepository.findAllByUsername(username,exerciseId);
		return vmAllreadyCreated;
	}
	
	public String saveAdvanceLabUserExercise(String username,int userId,Long exerciseId,MultipartFile submissionPdf) throws IOException
	{
		String pdfMessage = "valid";
		try (PDDocument pdfDocument = PDDocument.load(submissionPdf.getInputStream()))
		{
		    PDFTextStripper textStripper = new PDFTextStripper();
		    String pdfContent = textStripper.getText(pdfDocument);

		    if (pdfContent.contains("<script>")) {
		        pdfMessage ="MALICIOUS";
		        return pdfMessage;
		    } 
		}  	
		 catch (Exception e) {
		    e.printStackTrace();
		}
		
		if (exerciseSubmission_Repo.checkSubmitted((long)(userId), exerciseId) == null) {
			ExerciseSubmission exerciseSubmission = new ExerciseSubmission();
			exerciseSubmission.setUsername(username);
			exerciseSubmission.setUserid((long)(userId));
			exerciseSubmission.setExer_id(exerciseId);
			exerciseSubmission.setSubmission_pdf(submissionPdf.getBytes());
			exerciseSubmission.setPdfname(submissionPdf.getOriginalFilename());
			exerciseSubmission_Repo.save(exerciseSubmission);
		}
		return pdfMessage;
	}
	
	
	public void deleteAdvanceLabVM(String username,Long exerciseId) throws InterruptedException
	{
		List<AdvanceLabUserVmDetails> AllUserVmInThisExercise = advanceLabUserVmDetailsRepository.findAllByUsername(username, exerciseId);
		for(AdvanceLabUserVmDetails AD:AllUserVmInThisExercise)
		{
			advanceLabUserVmDetailsRepository.delete(AD);
			deleteVm(AD.getUUID());
			TimeUnit.SECONDS.sleep(5);
		}
	}
	
	public String checkInputFeildValidation(long exerciseId,String hppCode)
	{
		String tId=String.valueOf(exerciseId);
		String[] params = new String[] {tId};		
		if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
            logger.info("HTTP Parameter pollution");
            return "error";
        }
		return "notError";
	}
	
	public String checkInputFeildValidationForDeleteVm(long exerciseId,String fileNmae,String hppCode)
	{
		String tId=String.valueOf(exerciseId);
		String[] params = new String[] {tId,fileNmae};		
		if (!RequestParameterValidationUtility.validateRequestForHPP(params, hppCode)) {
            logger.info("HTTP Parameter pollution");
            return "error";
        }
		return "notError";
	}
}
