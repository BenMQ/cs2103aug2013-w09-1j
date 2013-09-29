package sg.edu.nus.cs2103.sudo.logic;

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
	
	public static void executeCommand(String userInput){
		String userCommand = parseCommand(userInput); 
		COMMAND_TYPE userCommandType = getCommandType(userCommand);
		
		// here need to parse description, and later parse datetime objects
		String taskDescription = parseDescription(userInput);
		
		//executes the method associated with userCommandType, methods not yet written
		
	}
	
	
	public static List<DateGroup> parseDateTime(String userInput){
		Parser parser = new Parser(); // Use natty to parse into joda datetime objects
		List<DateGroup> groups = parser.parse(userInput); //Each DateGroup contains a list of Date
		for(DateGroup group:groups) {
			  List<Date> dates = group.getDates();
		}
		
		return groups;
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
	
	
	public static void main(String[] args){
//		List<DateGroup> t = parseDateTime("the day before next thursday");
//		System.out.println(t.get(0));
		Parser parser = new Parser();
		
	}
	
	
}
