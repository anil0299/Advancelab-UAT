package com.Advanceelab.cdacelabAdvance.Captcha;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.springframework.stereotype.Service;
@Service
public class GenerateCaptcha {

	
	
	public String generateCaptchaChallenge() {
		 int length = 6; // Specify the length of the CAPTCHA challenge
		    
		    StringBuilder captchaChallenge = new StringBuilder();
		    
		    // Define the characters to use in the CAPTCHA challenge
		    String characters = "A!$%&lm!$%&nopq!$%&rstuvwxB!$%&CDE!$%&FGH!$%&IJK!$%&LMNOP!$%&QRST!$%&UV!$%&WXY!$%&Zabcdefghijkyz!$%&012345!$%&6789";
		    
		    Random random = new Random();
		    
		    for (int i = 0; i < length; i++) {
		        int index = random.nextInt(characters.length());
		        char character = characters.charAt(index);
		        captchaChallenge.append(character);
		    }
		    System.out.println(captchaChallenge.toString());
		    return captchaChallenge.toString();
	}
	
	public static BufferedImage generateImage(String challenge) {
	    int width = 200;
	    int height = 50;

	    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics = image.createGraphics();

	    // Set the background color
	    graphics.setColor(Color.WHITE);
	    graphics.fillRect(0, 0, width, height);

	    // Set the text color
	    graphics.setColor(Color.BLACK);

	    // Set the font for the challenge text
	    Font font = new Font("Arial", Font.BOLD, 30 + (int)(Math.random() * 10)); // Vary font size
	    graphics.setFont(font);

	    Random random = new Random();
	    
	    // Draw each character of the challenge text with a random rotation
	    int x = 10;
	    for (char c : challenge.toCharArray()) {
	        // Randomly rotate the character slightly
	        double rotationAngle = Math.toRadians(-20 + random.nextDouble() * 10); // Rotate between -5 and 5 degrees
	        AffineTransform tx = new AffineTransform();
	        tx.rotate(rotationAngle, x, height / 2);
	        graphics.setTransform(tx);

	        // Draw the character
	        graphics.drawChars(new char[]{c}, 0, 1, x, 35);

	        x += graphics.getFontMetrics().charWidth(c);
	    }

	    // Add some minimal noise to the image
	    for (int i = 0; i < 30; i++) { // Further reduced the number of noise elements
	        // Add random lines
	        graphics.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
	        int x1 = random.nextInt(width);
	        int y1 = random.nextInt(height);
	        int x2 = random.nextInt(width);
	        int y2 = random.nextInt(height);
	        graphics.drawLine(x1, y1, x2, y2);

	        // Add random dots
	        graphics.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
	        graphics.fillOval(random.nextInt(width), random.nextInt(height), 2, 2);
	    }

	    graphics.dispose();

	    return image;
	}

	public static boolean validateResponse(String challenge, String response) {
        // Perform the validation by comparing the response with the challenge
		//System.out.println(challenge.equalsIgnoreCase(response));
        return challenge.equalsIgnoreCase(response);
    }
	
}
