package com.Advanceelab.cdacelabAdvance.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncodingUtility {

	/**
	 * Get input code (iterations of encoding) for a parameter value
	 * 
	 * @param input Parameter Value
	 * @return Number of Iterations
	 */
	public static String getInputCode(String input) {
	    if (input == null) {
	        return null;
	    }
	    StringBuilder output = new StringBuilder();

	    for (int i = 0; i < input.length(); i++) {
	        char charCode = input.charAt(i);
	 
	        output.append(charCode);
	    }
	   
	    return output.toString();
	}

	/**
	 * Get input code (iterations of encoding) for a parameter value with multiple
	 * lines
	 * 
	 * @param input Parameter Value
	 * @return Number of Iterations
	 */
	public static String getInputCodeWithLines(String input) {
		 if (input == null) {
		        return null;
		    }
		    StringBuilder output = new StringBuilder();

		    for (int i = 0; i < input.length(); i++) {
		        char charCode = input.charAt(i);
		 
		        output.append(charCode);
		    }
		    return output.toString();
	}

	/**
	 * Generate code for checking HTTP Parameter Pollution (HPP)
	 * 
	 * @param hppCode   Initial HPP Code
	 * @param inputCode Number of Iterations
	 * @return HPP Code after running iterations of encoding
	 */
	public static String getHPPCode(String salt, StringBuilder inputCode) {
	
		 String saltedInput = salt + inputCode;
		
		String saltedSHA256Hash = generateSaltedSHA256Hash(saltedInput);

       
        System.out.println("Salted SHA-256 Hash: " + saltedSHA256Hash);
        return saltedSHA256Hash;
	}
	
	
	
	
	
	private static String generateSaltedSHA256Hash(String input) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

            // Update salted input string in message digest
            sha256.update(input.getBytes());

            // Get the hash's bytes
            byte[] bytes = sha256.digest();

            // Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            // Return the complete salted hashed string
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }
    }
	
	
	
	
	
	
	

	/**
	 * Encode value by shifting each character
	 * 
	 * @param input Value
	 * @param key   Shift Value
	 * @return Encoded Value
	 */
	public static String encode(String input, int key) {

		StringBuffer output = new StringBuffer();

		for (int i = 0; i < input.length(); i++) {
			char charCode = input.charAt(i);
			int asciiCode = (int) charCode;

			if (asciiCode >= 65 && asciiCode <= 90) {
				output.append((char) ((charCode - 65 + key) % 26 + 65)); // Uppercase

			} else if (asciiCode >= 97 && asciiCode <= 122) {
				output.append((char) ((charCode - 97 + key) % 26 + 97)); // Lowercase

			} else {
				output.append(charCode);
			}
		}
		return output.toString();
	}

	/**
	 * Decode value by shifting each character
	 * 
	 * @param input Value
	 * @param key   Shift Value
	 * @return Decoded Value
	 */
	public static String decode(String input, int key) {

		StringBuffer output = new StringBuffer();

		for (int i = 0; i < input.length(); i++) {
			char charCode = input.charAt(i);
			int asciiCode = (int) charCode;

			if (asciiCode >= 65 && asciiCode <= 90) {
				output.append((char) ((charCode - 65 - key + 26) % 26 + 65)); // Uppercase

			} else if (asciiCode >= 97 && asciiCode <= 122) {
				output.append((char) ((charCode - 97 - key + 26) % 26 + 97)); // Lowercase

			} else {
				output.append(charCode);
			}
		}
		return output.toString();
	}

	/**
	 * Get input code (iterations of encoding) for a parameter value
	 * 
	 * @param input Parameter Value
	 * @return Number of Iterations
	 */
	public static String getLongInputCode(String input) {
		
		 if (input == null) {
		        return null;
		    }
		    StringBuilder output = new StringBuilder();

		    for (int i = 0; i < input.length(); i++) {
		        char charCode = input.charAt(i);
		 
		        output.append(charCode);
		    }
		    return output.toString();
		
	}

	/**
	 * Encode password by shifting each character
	 * 
	 * @param input Value
	 * @param key   Shift Value
	 * @return Encoded Password
	 */
	public static String encodePassword(String input, int key) {

		StringBuffer output = new StringBuffer();

		for (int i = 0; i < input.length(); i++) {
			char charCode = input.charAt(i);
			int asciiCode = (int) charCode;

			if (asciiCode >= 65 && asciiCode <= 90) {
				output.append((char) ((charCode - 65 + key) % 26 + 65)); // Uppercase

			} else if (asciiCode >= 97 && asciiCode <= 122) {
				output.append((char) ((charCode - 97 + key) % 26 + 97)); // Lowercase

			} else if (asciiCode >= 49 && asciiCode <= 57) {
				int digit = charCode - 48; // Number

				if (digit % 2 == 0) {
					output.append(digit * 2 + 2); // Even Number
				} else {
					output.append(digit * 3 + 3); // Odd Number
				}
			} else {
				output.append(charCode);
			}
		}
		return output.toString();
	}
	
	public static String decodeWithSeedNEW(String input, int key) {

		// 3 + 6 + Output + 6
		int length = input.length() - 6;
		input = input.substring(9, length);
		
		StringBuffer output = new StringBuffer();

		for (int i = 0; i < input.length(); i++) {
			char charCode = input.charAt(i);
			int asciiCode = (int) charCode;

			if (asciiCode >= 65 && asciiCode <= 90) {
				output.append((char) ((charCode - 65 - key + 26) % 26 + 65)); // Uppercase

			} else if (asciiCode >= 97 && asciiCode <= 122) {
				output.append((char) ((charCode - 97 - key + 26) % 26 + 97)); // Lowercase

			} else if (asciiCode >= 48 && asciiCode <= 57) {
				char charCode1 = input.charAt(++i);
				int asciiCode1 = Integer.parseInt(charCode + "" + charCode1) - key;
				output.append((char) asciiCode1); // Digits

			} else {
				output.append(charCode);
			}
		}
		return output.toString();
	}
}
