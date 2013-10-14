package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;

import com.joestelmach.natty.DateGroup;

public class ParserUtils {
	/**
	 * Retrieves date List objects from joda-datetime DateGroup objects
	 * @param ArrayList of DateGroup objects
	 * @return ArrayList of Date Lists
	 */
	public static ArrayList<List<Date>> getDateLists(List<DateGroup> dateGroups) {
		ArrayList<List<Date>> dateLists = new ArrayList<List<Date>>();
		for(DateGroup dateGroup:dateGroups) {
			  List<Date> dateList = dateGroup.getDates();
			  dateLists.add(dateList);
		}
		
		return dateLists;
	}
	
	/**
	 * Convert DateLists to joda-DateTime objects
	 * @param ArrayList of Date Lists
	 * @return ArrayList of DateTime objects
	 */
	public static ArrayList<DateTime> convertToDateTimes(ArrayList<List<Date>> dateLists){
		ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		if(dateLists.isEmpty()){
			return dateTimes;
		}
		
		List<Date> dates = dateLists.get(0);
		for(Date date:dates){ //cast Date to joda-DateTime
			DateTime dt = new DateTime(date);
			dateTimes.add(dt);
		}
		
		return dateTimes;
	}	
	
	/**
	 * Get the first word (the command word) from the user input
	 * @param String userInput
	 * @return String first word
	 */
	public static String getCommandWord(String userInput) {
		String[] words = userInput.trim().split(" ");
		return words[0];
	}		
	
	/**
	 * Validates that the first word (command word) is a valid COMMAND_TYPE
	 * In other words, this method checks if the command is in COMMAND_TYPE
	 */		
	public static COMMAND_TYPE getCommandType(String userCommand){
		try{
			COMMAND_TYPE commandType = COMMAND_TYPE.valueOf(userCommand.toUpperCase());
		    for (COMMAND_TYPE c : COMMAND_TYPE.values()) {
		        if (c.name().equals(commandType.name())) {
		        	return commandType;
		        }
		    }
		} catch(IllegalArgumentException e){
			return COMMAND_TYPE.INVALID;
		}
	    return COMMAND_TYPE.INVALID;
	}

	/**
	 * Count the number of words in a string
	 * @param String inputString
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
	 * for a valid command
	 * @param COMMAND_TYPE commandType
	 * @return number of words
	 */
	public static int getNumOfWordsNeeded(COMMAND_TYPE commandType) {
		switch (commandType) {
		case ADD:
		case DELETE:
		case EDIT:
		case SEARCH:
			return 2;

		default:
			return 1;
		}
	}	
}
