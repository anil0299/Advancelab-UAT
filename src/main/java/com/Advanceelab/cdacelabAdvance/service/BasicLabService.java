package com.Advanceelab.cdacelabAdvance.service;

import com.Advanceelab.cdacelabAdvance.entity.BasicLabSubmission;
import com.Advanceelab.cdacelabAdvance.entity.StudentDtls;
import com.Advanceelab.cdacelabAdvance.repository.BasicLabAssignRepository;
import com.Advanceelab.cdacelabAdvance.repository.StudentRepository;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class BasicLabService {

    @Autowired
    private VmFunction vmFunction;

    @Autowired
    private BasicLabAssignRepository basiclabRepo;

    @Autowired
    private StudentRepository studentRepo;

    public String generateUserPass(String username) {
        int atIndex = username.indexOf('@');
        String usernameAt = username.substring(0, atIndex);
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

    public boolean checkAndPrepareVms(String username, String userpass, ModelAndView mav) throws InterruptedException, IOException, ExecutionException {
        String usernameAt = getUsernameAt(username);
        String usernameWin = usernameAt.concat("-window");
        String usernameLinux = usernameAt.concat("-linux");

        boolean vmExist = vmFunction.checkVmBasic(usernameWin, usernameLinux, userpass).get();

        if (vmExist) {
            addVmDetailsToModel(usernameWin, userpass, mav, "win");
            addVmDetailsToModel(usernameLinux, userpass, mav, "linux");
        }
        return vmExist;
    }

    public void createAndStartVms(String username, String userpass, ModelAndView mav) throws InterruptedException, IOException, ExecutionException {
        String usernameAt = getUsernameAt(username);
        String usernameWin = usernameAt.concat("-window");
        String usernameLinux = usernameAt.concat("-linux");
        
        vmFunction.cloneVmWindow(usernameWin, userpass);
        vmFunction.cloneVmLinux(usernameLinux, userpass);

        TimeUnit.SECONDS.sleep(15);

        addVmDetailsToModel(usernameWin, userpass, mav, "win");
        addVmDetailsToModel(usernameLinux, userpass, mav, "linux");
    }

    private void addVmDetailsToModel(String username, String userpass, ModelAndView mav, String osType) throws InterruptedException, IOException, ExecutionException {
        String vmUuid = vmFunction.searchUuid(username, userpass).get();
        String vmIp = vmFunction.searchIp(username, userpass).get();
        String vmName = vmFunction.searchVmName(username, userpass).get();
        vmFunction.powerOn(vmUuid, userpass);
        
        mav.addObject("ipaddress" + osType, vmIp);
        mav.addObject("vmnameshow" + osType, vmName);
    }

    private String getUsernameAt(String username) {
        int atIndex = username.indexOf('@');
        return username.substring(0, atIndex);
    }

    public void deleteVms(String username, String userpass) throws InterruptedException, IOException, ExecutionException {
        String usernameAt = getUsernameAt(username);
        String usernameWin = usernameAt.concat("-window");
        String usernameLinux = usernameAt.concat("-linux");

        String vmUuidWin = vmFunction.searchUuid(usernameWin, userpass).get();
        String vmUuidLinux = vmFunction.searchUuid(usernameLinux, userpass).get();
        vmFunction.deleteVm(vmUuidWin);
        vmFunction.deleteVm(vmUuidLinux);
    }

    public void saveSubmission(String username, byte[] pdffile1, String originalFilename) {
        BasicLabSubmission basicSubmission = new BasicLabSubmission();
        basicSubmission.setUsername(username);
        basicSubmission.setSubmission_pdf(pdffile1);
        basicSubmission.setPdfname(originalFilename);
        basiclabRepo.save(basicSubmission);
    }

    public boolean isPdfContentSafe(MultipartFile submissionPdf) throws IOException {
        try (PDDocument pdfDocument = PDDocument.load(submissionPdf.getInputStream())) {
            PDFTextStripper textStripper = new PDFTextStripper();
            String pdfContent = textStripper.getText(pdfDocument);
            return !pdfContent.contains("<script>");
        }
    }
}
