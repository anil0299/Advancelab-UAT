package com.Advanceelab.cdacelabAdvance.security;

public class EncodingUtility2 {

	
	public static int getInputCode(String input) {
		if (input == null) {
			return 0;
		}
		int output = 0;

		for (int i = 0; i < input.length(); i++) {
			char charCode = input.charAt(i);
			if ((int) charCode < 32) {
				continue;
			}
			int asciiCode = (int) charCode + i;
			output += asciiCode;
		}
		return output;
	}

	/**
	 * Get input code (iterations of encoding) for a parameter value with multiple
	 * lines
	 * 
	 * @param input Parameter Value
	 * @return Number of Iterations
	 */
	public static int getInputCodeWithLines(String input) {
		if (input == null) {
			return 0;
		}
		int output = 0;

		for (int i = 0; i < input.length(); i++) {
			char charCode = input.charAt(i);
			if ((int) charCode < 32) {
				continue;
			}
			int asciiCode = (int) charCode;
			output += asciiCode;
		}
		return output;
	}

	/**
	 * Generate code for checking HTTP Parameter Pollution (HPP)
	 * 
	 * @param hppCode   Initial HPP Code
	 * @param inputCode Number of Iterations
	 * @return HPP Code after running iterations of encoding
	 */
	public static String getHPPCode(String hppCode, int inputCode) {

		int n = inputCode;

		for (int i = 0; i < n; i++) {
			hppCode = encode(hppCode, 3);
		}
		return hppCode;
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
	public static long getLongInputCode(String input) {

		long output = 0;

		for (int i = 0; i < input.length(); i++) {
			char charCode = input.charAt(i);
			int asciiCode = (int) charCode + i;
			output += asciiCode;
		}
		return output;
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
	
//	public static String decodeWithSeedNEW(String input, int key) {
//
//		// 3 + 6 + Output + 6
//		int length = input.length() - 6;
//		input = input.substring(9, length);
//		
//		StringBuffer output = new StringBuffer();
//
//		for (int i = 0; i < input.length(); i++) {
//			char charCode = input.charAt(i);
//			int asciiCode = (int) charCode;
//
//			if (asciiCode >= 65 && asciiCode <= 90) {
//				output.append((char) ((charCode - 65 - key + 26) % 26 + 65)); // Uppercase
//
//			} else if (asciiCode >= 97 && asciiCode <= 122) {
//				output.append((char) ((charCode - 97 - key + 26) % 26 + 97)); // Lowercase
//
//			} else if (asciiCode >= 48 && asciiCode <= 57) {
//				char charCode1 = input.charAt(++i);
//				int asciiCode1 = Integer.parseInt(charCode + "" + charCode1) - key;
//				output.append((char) asciiCode1); // Digits
//
//			} else {
//				output.append(charCode);
//			}
//		}
//		return output.toString();
//	}
//	
	
	
	public static String decodeWithSeedNEW(String input, int key) {
	    // Extract the actual encoded password part
	    int startIndex = 9;
	    int endIndex = input.length() - 6;
	    String encodedPart = input.substring(startIndex, endIndex);
	    
	    StringBuffer output = new StringBuffer();

	    for (int i = 0; i < encodedPart.length(); i++) {
	        char charCode = encodedPart.charAt(i);
	        int asciiCode = (int) charCode;

	        if (asciiCode >= 65 && asciiCode <= 90) {
	            output.append((char) ((charCode - 65 - key + 26) % 26 + 65)); // Uppercase

	        } else if (asciiCode >= 97 && asciiCode <= 122) {
	            output.append((char) ((charCode - 97 - key + 26) % 26 + 97)); // Lowercase

	        } else if (asciiCode >= 48 && asciiCode <= 57) {
	            String digitStr = encodedPart.substring(i, i + 2);
	            int digit = Integer.parseInt(digitStr) - key;
	            output.append((char) digit);
	            i++; // Move to next character as we processed two characters
	        } else {
	            output.append(charCode);
	        }
	    }
	    return output.toString();
	}

	
}
