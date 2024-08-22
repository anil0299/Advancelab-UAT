package com.Advanceelab.cdacelabAdvance.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;



@Service
public class ExerciseImageService {
	
	
	public void saveImage(String uploadDirectory,@RequestParam("courseTopicId") int courseTopicId,
			 				@RequestParam("ArchitectureImage") MultipartFile architectureImage,
			 				 @RequestParam("GUIImage") MultipartFile guiImage) throws IOException
	{
		Path directory = Paths.get(uploadDirectory+"/"+courseTopicId);
		if(!Files.exists(directory)) {
			Files.createDirectories(directory);
		}
        
        if (!architectureImage.isEmpty()) {
        	 Path targetPath = Paths.get(directory+"/"+architectureImage.getOriginalFilename());
	         Files.copy(architectureImage.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        if (!guiImage.isEmpty()) {
       	 Path targetPath = Paths.get(directory+"/"+guiImage.getOriginalFilename());
	         Files.copy(guiImage.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
       }
	}
	
	
	

    @Value("${upload.directory}")
    private String uploadDirectory;

    public String getExerciseImage(int exerciseId, Model model) {
        String courseExerciseIdDirectory = Paths.get(uploadDirectory, String.valueOf(exerciseId)).toString();
        Path folder = Paths.get(courseExerciseIdDirectory);

        boolean[] exists = { false, false };

        if (Files.exists(folder) && Files.isDirectory(folder)) {
            try (Stream<Path> paths = Files.list(folder)) {
                paths.forEach(path -> {
                    if (Files.isRegularFile(path)) {
                        String fileName = path.getFileName().toString().toLowerCase();
                        if (fileName.contains("architecture")) {
                            exists[0] = true;
                        } else if (fileName.contains("gui")) {
                            exists[1] = true;
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately in your application
            }
        }

        model.addAttribute("exerciseId", exerciseId);
        model.addAttribute("architectureImageExists", exists[0]);
        model.addAttribute("guiImageExists", exists[1]);

        return "studentExercisePage";
    }
}
