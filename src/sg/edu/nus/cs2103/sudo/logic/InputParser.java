package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import org.joda.time.DateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class InputParser {

	/**
	 * This InputParser class is responsible for extracting commands
	 * and description/time parameters from user string inputs,
	 * delegating to subclasses whenever necessary. 
	 * It then calls the appropriate CRUD methods.
	 * 
	 * @author Yos Riady 
	 */
	
	
	private static final String TO = " to ";
	private static final String FROM = " from ";	
	private static final String DOUBLE_QUOTE = "\"";

	/**
	 * Executes the user's input
	 * @param userInput 	string of the user's input
	 * @return executes the appropriate high level command
	 */
	public static void executeCommand(String userInput){
		String userCommand = parseCommand(userInput); 
		COMMAND_TYPE userCommandType = getCommandType(userCommand);
		
		// here need to parse description, and later parse datetime objects
		String taskDescription = parseDescription(userInput);
		
		//executes the method associated with userCommandType, methods not yet written
		
	}
	
	/**
	 * Parses dates from the user's input string
	 * @param userInput 			string of the user's input
	 * @return ArrayList<DateTime>	A list of DateTime objects
	 */	
	public static ArrayList<DateTime> parseDateTime(String userInput){
		Parser parser = new Parser();
		List<DateGroup> dateGroups = parser.parse(userInput); //Each DateGroup contains a list of Date
		ArrayList<List<Date>> dateLists = getDateLists(dateGroups);	
		ArrayList<DateTime> dateTimes = convertToDateTimes(dateLists);
		
		return dateTimes;
	}

	public static String parseCommand(String userInput){
		return userInput.substring(0, userInput.indexOf(" "));
	}	

	public static String parseDescription(String userInput){
		return userInput.substring(userInput.indexOf(" ")+1);
	}	
	
	public static COMMAND_TYPE getCommandType(String userCommand){
		//here need to catch exceptions and tolerate some user typos
		return COMMAND_TYPE.valueOf(userCommand.toUpperCase());
	}
	
	
	
	
	
	// Helper method
	private static ArrayList<List<Date>> getDateLists(List<DateGroup> dateGroups) {
		ArrayList<List<Date>> dateLists = new ArrayList<List<Date>>();
		for(DateGroup dateGroup:dateGroups) {
			  List<Date> dateList = dateGroup.getDates();
			  dateLists.add(dateList);
		}
		
		return dateLists;
	}
	
	// Helper method
	public static ArrayList<DateTime> convertToDateTimes(ArrayList<List<Date>> dateLists){
		List<Date> dates = dateLists.get(0);
		ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		for(Date date:dates){ //cast Date to joda-DateTime
			DateTime dt = new DateTime(date);
			dateTimes.add(dt);
		}
		
		return dateTimes;
	}	
}
