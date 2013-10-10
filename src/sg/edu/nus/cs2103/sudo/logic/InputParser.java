package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;

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
		
	private static Scanner sc = new Scanner(System.in);
	private TaskManager manager; 
	
	public InputParser(TaskManager m) {
		manager = m;
	}
	
	/**
	 * Executes the user's input
	 * @param userInput 	string of the user's input
	 * @return executes the appropriate high level command
	 */
	public void executeCommand(String userInput){
		COMMAND_TYPE userCommand = parseCommand(userInput); 		
		String taskDescription = parseDescription(userInput);
		int targetId = parseId(userInput);
		ArrayList<DateTime> dateTimes = parseDateTime(userInput);
		
		switch(userCommand){
		case INVALID:
			System.out.print(Constants.MESSAGE_INVALID_COMMAND);
			return;		
		case INCOMPLETE:
			System.out.print(Constants.MESSAGE_INCOMPLETE_COMMAND);
			return;
		case DISPLAY:
			System.out.println(Constants.MESSAGE_DISPLAY);
			this.manager.displayAllTasks();
			return;
		case ADD:
			int num_dates = dateTimes.size();
			if(num_dates == 0){ //need to refactor this later
					System.out.println(Constants.MESSAGE_ADD_FLOATING + taskDescription);
					this.manager.addFloatingTask(new FloatingTask(taskDescription));
			} else if(num_dates == 1){
					System.out.println(Constants.MESSAGE_ADD_DEADLINE + taskDescription);
					this.manager.addNormalTask(new DeadlineTask(taskDescription, dateTimes));
			} else if(num_dates == 2){
					System.out.println(Constants.MESSAGE_ADD_TIMED + taskDescription);
					this.manager.addNormalTask(new TimedTask(taskDescription, dateTimes));
			} else {
					System.out.println(Constants.MESSAGE_INVALID_NUMBER_OF_DATES);
			}
			return;
		case SEARCH:
			System.out.println("Searching:" + taskDescription);
			this.manager.searchAndDisplay(taskDescription);
			return;
		case DELETE:
			System.out.println("Deleting:" + taskDescription);
			int numResults = this.manager.delete(taskDescription); //need to refactor this
			if (numResults > 1 ) {
				System.out.println(Constants.MESSAGE_ENTER_TASK_ID);
				int id = sc.nextInt(); // change this to take in any input and throw error if invalid integer
				System.out.println("Deleting task id " + id);
				this.manager.delete(id);
			}
			return;
		case EDIT:
		    System.out.println("Editing " + targetId);
		    this.manager.editFloatingTask(targetId, new FloatingTask(taskDescription));
		    return;
		default:
			//some error message
			return;
		}
	}

	/**
	 * Parses dates from the user's input string
	 * @param userInput 			string of the user's input
	 * @return ArrayList<DateTime>	A list of DateTime objects
	 */	
	public static ArrayList<DateTime> parseDateTime(String userInput){
		Parser dtparser = new Parser();
		List<DateGroup> dateGroups = dtparser.parse(userInput); //Each DateGroup contains a list of Date
		ArrayList<List<Date>> dateLists = getDateLists(dateGroups);	
		ArrayList<DateTime> dateTimes = convertToDateTimes(dateLists);
		
		return dateTimes;
	}

	/**
	 * Reads the user input for command
	 * @return String 	the user's input command 
	 */
	public String readCommand(Scanner userCommand){
		System.out.print("command:");
		return userCommand.nextLine();
	}	
	
	/**
	 * Validates the user input for a specific COMMAND_TYPE
	 * @param String	the user's input
	 * @return COMMAND_TYPE 
	 */	
	public static COMMAND_TYPE parseCommand(String userInput){
		String commandWord = getCommandWord(userInput);
		COMMAND_TYPE commandType = getCommandType(commandWord);
		int numOfWords = countWords(userInput);
		if (numOfWords < getNumOfWordsNeeded(commandType)) {
			return COMMAND_TYPE.INCOMPLETE;
		} else {
			return commandType;
		}
	}

	/**
	 * Parses the user input for task descriptions
	 * @param String 	the user's input
	 * @return String	task description 
	 */	
	public static String parseDescription(String userInput){
		Pattern p = Pattern.compile("(?:^|)'([^']*?)'(?:$|)", Pattern.MULTILINE);
        Matcher m = p.matcher(userInput);
        if (m.find()) {
//                System.out.print(m.group());
//                while (m.find()) System.out.print(", "+m.group());
            return m.group().substring(1,m.group().length()-1); //refactor regex to do this pruning
        } else {
            return null;
        }
         
	}
	
	/**
	 * Attempts to get the content between first and second space, 
	 * and parse as integer. -1 for unsuccessful parsing. 
	 */	
	public static int parseId(String userInput) {
	    final int NOT_FOUND = -1;
	    String[] spaceDelimitedInput = userInput.split("\\s+");
	    if (spaceDelimitedInput.length < 2) {
	        return NOT_FOUND;
	    }
	    String firstArgument = spaceDelimitedInput[1];
	    try {
	        int id = Integer.parseInt(firstArgument, 10);
	        return id;
	    } catch (NumberFormatException e) {
	        return NOT_FOUND;
	    }
	}

	
//	Helper methods
	
	/**
	 * Retrieves date List objects from joda-datetime DateGroup objects
	 * @param ArrayList of DateGroup objects
	 * @return ArrayList of Date Lists
	 */
	private static ArrayList<List<Date>> getDateLists(List<DateGroup> dateGroups) {
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
	private static ArrayList<DateTime> convertToDateTimes(ArrayList<List<Date>> dateLists){
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
	private static String getCommandWord(String userInput) {
		String[] words = userInput.trim().split(" ");
		return words[0];
	}		
	
	/**
	 * Validates that the first word (command word) is a valid COMMAND_TYPE
	 * In other words, this method checks if the command is in COMMAND_TYPE
	 */		
	private static COMMAND_TYPE getCommandType(String userCommand){
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
	private static int countWords(String inputString) {
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
	private static int getNumOfWordsNeeded(COMMAND_TYPE commandType) {
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

