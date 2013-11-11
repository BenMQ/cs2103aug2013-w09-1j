package sg.edu.nus.cs2103.sudo.logic;

import java.util.Arrays;

//@author A0099317U

/**
 * This class contains static methods used to create and modify strings. 
 */
public class StringUtils {
	
	/**
	 * Get the first word (the command word) from the user input.
	 * 
	 * @param userInput
	 *            the user's input
	 * @return String first word
	 */
	public static String getFirstWord(String userInput) {
		String[] words = userInput.trim().split(" ");
		return words[0];
	}	
	
	/**
	 * Count the number of words in a string.
	 * 
	 * @param inputString
	 *            A string input
	 * @return number of words
	 */
	public static int countWords(String inputString) {
		if (inputString.trim().isEmpty()) {
			return 0;
		} else {
			return inputString.trim().split("\\s+").length;
		}
	}	
	
    /**
     * Joins an array of strings with the delimiter
     * @param strings array of strings to join
     * @return joined strings
     */
    public static String joinString(String[] strings, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        
        for (int i = 0; i < strings.length; i++) {
            buffer.append(strings[i]);
            if (i < strings.length - 1) {
                buffer.append(delimiter);
            }
        }
        
        return buffer.toString();
    }
    
	/**
	 * Helper method to generate a string of characters
	 * of specified length.
	 * @param int
	 * @param char
	 * @return String
	 */	
	public static String fillString(final int length, 
			final char charToFill) {
		  if (length > 0) {
		    char[] array = new char[length];
		    Arrays.fill(array, charToFill);
		    return new String(array);
		  }
		  return "";
	}    
    
	public static String trimDescription(String userInput, String desc) {
		if (desc != null) {
            userInput = userInput.replace("'" + desc + "'", "");
        }
		return userInput;
	}	    
}
