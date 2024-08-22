/*
 * package com.Advanceelab.cdacelabAdvance.service;
 * 
 * import org.clamav4j.ClamAV; import org.clamav4j.ClamAVException; import
 * org.springframework.stereotype.Service; import
 * org.springframework.web.multipart.MultipartFile;
 * 
 * import java.io.IOException;
 * 
 * @Service public class VirusScanService {
 * 
 * private final ClamAV clamAV;
 * 
 * public VirusScanService() throws IOException { // Initialize ClamAV
 * this.clamAV = new ClamAV("localhost", 3310); // Change the host and port as
 * needed }
 * 
 * public boolean isFileInfected(MultipartFile file) throws IOException,
 * ClamAVException { byte[] fileBytes = file.getBytes();
 * 
 * // Scan the file for viruses int result = clamAV.scan(fileBytes);
 * 
 * // Check the scan result if (ClamAV.isClean(result)) { // The file is clean
 * (not infected) return false; } else { // The file is infected return true; }
 * } }
 */