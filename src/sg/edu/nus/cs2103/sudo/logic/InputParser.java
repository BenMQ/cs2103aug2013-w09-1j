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
 * 
 * @author Yos Riady 
 */
public class InputParser {
	
	/** Flag used when a Task of specified is not found. */
    private static final int NOT_FOUND = -1;

	/**
	 * Parses dates from the user's input string.
	 * 
	 * @param userInput 			string of the user's input
	 * @return ArrayList<DateTime>	A list of DateTime objects
	 */	
	public static ArrayList<DateTime> parseDateTime(String userInput,
	        COMMAND_TYPE command) {
		Parser dtparser = new Parser();
		String dateTimeString = getDateTimeString(userInput, command);
		List<DateGroup> dateGroups = dtparser.parse(dateTimeString);
		ArrayList<List<Date>> dateLists = ParserUtils.getDateLists(dateGroups);	
		ArrayList<DateTime> dateTimes = ParserUtils.
				convertToDateTimes(dateLists);
		
		return dateTimes;
	}
	
	/**
	 * Strips the command and other irrelevant arguments in the user input.
	 * @param userInput user input string
	 * @param command the type of the command
	 * @return the input string with irrelevant keywords stripped away
	 */
	public static String getDateTimeString(String userInput,
	        COMMAND_TYPE command) {

        String desc = parseDescription(userInput);
        if (desc != null) {
            userInput = userInput.replace(desc, "");
        }
        
	    String[] inputWords = userInput.trim().split("\\s+");	    
	    String[] relevantWord = 
	            Arrays.copyOfRange(inputWords,
	                               ParserUtils.getNumWordsToTrim(command),
	                               inputWords.length);
	    return ParserUtils.joinString(relevantWord, " ");
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
		String commandWord = ParserUtils.getCommandWord(userInput);
		COMMAND_TYPE commandType = ParserUtils.getCommandType(commandWord);
		assert commandType != null;
		int numOfWords = ParserUtils.countWords(userInput);
		if (numOfWords < ParserUtils.getNumOfWordsNeeded(commandType)) { 
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
        	if(description.length() <= 0){
        		return null;
        	}
            return description;
        } else {
            return null;
        }
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
        long millis = ParserUtils.parseDurationToMillis(secondArgument);
        if (millis < 0) {
            return NOT_FOUND;
        } else {
            return millis;
        }
    }
}

