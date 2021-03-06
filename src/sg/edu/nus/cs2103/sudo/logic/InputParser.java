package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;

import org.joda.time.DateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

//@author A0099317U
/**
 * This InputParser class is responsible for extracting commands
 * and description/time parameters from user string inputs.
 */
public class InputParser {
	
	/** Flag used when a Task of specified is not found. */
    public static final int NOT_FOUND = Integer.MIN_VALUE;

	/**
	 * Parses dates from the user's input string.
	 * 
	 * @param userInput 			string of the user's input
	 * @return ArrayList<DateTime>	A list of DateTime objects
	 */	
	public static ArrayList<DateTime> parseDateTime(String userInput,
	        COMMAND_TYPE command) {
		Parser dtparser = new Parser();
		String dateTimeString = InputParserUtils.getDateTimeString(userInput, command);
		List<DateGroup> dateGroups = dtparser.parse(dateTimeString);
		ArrayList<List<Date>> dateLists = InputParserUtils.getDateLists(dateGroups);	
		ArrayList<DateTime> dateTimes = InputParserUtils.
				convertToDateTimes(dateLists);
		
		return dateTimes;
	}

	/**
	 * Reads the user input for command.
	 * 
	 * @param scanner		A scanner object
	 * @return String 	the user's input command 
	 */
	public static String readCommand(Scanner scanner) {
		System.out.print("command:");
		return scanner.nextLine();
	}	
	
	/**
	 * Validates the user input for a specific COMMAND_TYPE.
	 * 
	 * @param userInput		the user's input
	 * @return COMMAND_TYPE 
	 */	
	public static COMMAND_TYPE parseCommandType(String userInput) {
		String commandWord = StringUtils.getFirstWord(userInput);
		COMMAND_TYPE commandType = InputParserUtils.getCommandType(commandWord);
		assert commandType != null;
		
		int numOfWords = StringUtils.countWords(userInput);
		boolean notEnoughArguments = numOfWords < 
				InputParserUtils.getNumOfWordsNeeded(commandType);
		
		if (notEnoughArguments) { 
			return COMMAND_TYPE.INCOMPLETE;
		} else {
			return commandType;
		}
	}

	/**
	 * Parses the user input for task descriptions.
	 * 
	 * @param userInput		the user's input
	 * @return String 
	 */	
	public static String parseDescription(String userInput) {
		Pattern p = Pattern.compile("(?:^|)'([^']*?)'(?:$|)", 
				Pattern.MULTILINE);
        Matcher m = p.matcher(userInput);
        if (m.find()) {
        	String description = m.group().substring(1, m.group().length() - 1);
        	return validateEmptyDescription(description);
        } else {
            return null;
        }
	}

	/**
	 * Returns null for empty task descriptions.
	 * 
	 * @param String
	 * @return String 
	 */	
	public static String validateEmptyDescription(String description) {
		if(description.length() <= 0){
			return null;
		}
		return description;
	}
	
	//@author A0099314Y
	/**
	 * Attempts to get the content between first and second space, 
	 * and parse as integer. -1 for unsuccessful parsing.
	 * 
	 * @param userInput 
	 * @return int
	 */	
	public static int parseId(String userInput) {
	    String[] spaceDelimitedInput = userInput.split("\\s+");
	    if (spaceDelimitedInput.length < 2) {
	        return NOT_FOUND;
	    }
	    String firstArgument = spaceDelimitedInput[1];
	    try {
	        int id = Integer.parseInt(firstArgument);
	        return id;
	    } catch (NumberFormatException e) {
	        return NOT_FOUND;
	    }
	}
	
	/**
     * Attempts to get the content between second and third space, 
     * and parse as milliseconds. -1 for unsuccessful parsing.
     * 
     * @param userInput 
     * @return duration in milliseconds
     */ 
    public static long parseDuration(String userInput) {
        String[] spaceDelimitedInput = userInput.split("\\s+");
        if (spaceDelimitedInput.length < 3) {
            return NOT_FOUND;
        }
        String secondArgument = spaceDelimitedInput[2];
        long millis = InputParserUtils.parseDurationToMillis(secondArgument);
        if (millis < 0) {
            return NOT_FOUND;
        } else {
            return millis;
        }
    }
    
}

