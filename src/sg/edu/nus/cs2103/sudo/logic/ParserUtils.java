package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;

import com.joestelmach.natty.DateGroup;

/**
 * This class contains some static methods to 
 * parse task information such as ID, description,
 * and dates.
 * 
 * @author Yos Riady
 * 
 */
public class ParserUtils {
	/**
	 * Retrieves date List objects from joda-datetime DateGroup objects.
	 * @param dateGroups	A list of joda-datetime DateGroup objects
	 * @return ArrayList of Date Lists
	 */
	public static ArrayList<List<Date>> getDateLists(
			List<DateGroup> dateGroups) {
		ArrayList<List<Date>> dateLists = new ArrayList<List<Date>>();
		for (DateGroup dateGroup:dateGroups) {
			  List<Date> dateList = dateGroup.getDates();
			  dateLists.add(dateList);
		}
		
		return dateLists;
	}
	
	/**
	 * Convert DateLists to joda-DateTime objects.
	 * @param dateLists		ArrayList of Date Lists
	 * @return ArrayList of DateTime objects
	 */
	public static ArrayList<DateTime> convertToDateTimes(
			ArrayList<List<Date>> dateLists){
		ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		if (dateLists.isEmpty()) {
			return dateTimes;
		}
		
		List<Date> dates = dateLists.get(0);
		for (Date date:dates) {
			DateTime dt = new DateTime(date);
			dateTimes.add(dt);
		}
		
		return dateTimes;
	}	
	
	/**
	 * Get the first word (the command word) from the user input.
	 * @param userInput		the user's input
	 * @return String first word
	 */
	public static String getCommandWord(String userInput) {
		String[] words = userInput.trim().split(" ");
		return words[0];
	}		
	
	/**
	 * Validates that the first word (command word) is a valid COMMAND_TYPE.
	 * In other words, this method checks if the command is in COMMAND_TYPE.
	 * @param userCommand	The user's command word
	 * @return COMMAND_TYPE
	 */		
	public static COMMAND_TYPE getCommandType(String userCommand) {
		COMMAND_TYPE commandType = Constants.aliases.get(
				userCommand.toUpperCase());
		if (commandType == null) {
			return COMMAND_TYPE.INVALID;
		}
		return commandType;	
	}

	/**
	 * Count the number of words in a string.
	 * @param inputString	A string input
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
	 * Returns the minimum number of words required
	 * for a valid command.
	 * @param commandType
	 * @return number of words
	 */
	public static int getNumOfWordsNeeded(COMMAND_TYPE commandType) {
		switch (commandType) {
		case ADD:
		case DELETE:
		case FINISH:
		case EDIT:
		case SEARCH:
			return 2;

		default:
			return 1;
		}
	}	
}
