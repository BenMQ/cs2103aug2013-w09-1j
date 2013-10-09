package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import org.joda.time.DateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;;

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
		String userCommand = parseCommand(userInput); 		
		String taskDescription = parseDescription(userInput);
		int targetId = parseId(userInput);
		ArrayList<DateTime> dateTimes = parseDateTime(userInput);
		
		COMMAND_TYPE userCommandType = getCommandType(userCommand);
		
		switch(userCommandType){
		case DISPLAY:
			System.out.println("Displaying all tasks");
			this.manager.displayAllTasks();
			return;
		case ADD:
			int num_dates = dateTimes.size();
			if(num_dates == 0){ //need to refactor this later
					System.out.println("Adding floating task:" + taskDescription);
					this.manager.addFloatingTask(new FloatingTask(taskDescription));
			} else if(num_dates == 1){
					System.out.println("Adding deadline task:" + taskDescription);
					this.manager.addNormalTask(new DeadlineTask(taskDescription, dateTimes));
			} else if(num_dates == 2){
					System.out.println("Adding timed task:" + taskDescription);
					this.manager.addNormalTask(new TimedTask(taskDescription, dateTimes));
			} else {
					System.out.println("Invalid number of dates");
			}
			return;
		case SEARCH:
			System.out.println("Searching:" + taskDescription);
			this.manager.searchAndDisplay(taskDescription);
			return;
		case DELETE:
			System.out.println("Deleting:" + taskDescription);
			int numResults = this.manager.delete(taskDescription);
			if (numResults > 1 ) {
				System.out.println("Please enter task id:");
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
	 * @param userCommand 	the user's input command 
	 */
	public String readCommand(Scanner userCommand){
		System.out.print("command:");
		return userCommand.nextLine();
	}	
	
	public static String parseCommand(String userInput){
		if(userInput.indexOf(" ") == -1){
			return userInput;
		}
		return userInput.substring(0, userInput.indexOf(" "));
	}	

	public static String parseDescription(String userInput){
		Pattern p = Pattern.compile("(?:^|)'([^']*?)'(?:$|)", Pattern.MULTILINE);
        Matcher m = p.matcher(userInput);
        if (m.find()) {
//                System.out.print(m.group());
//                while (m.find()) System.out.print(", "+m.group());
            return m.group().substring(1,m.group().length()-1); //refactor regex to do this pruning
        } else {
//            System.out.println("NO DESCRIPTION");
            return null;
        }
         
	}
	
	// attempts to get the content between first and second space, and parse as integer. -1 for unsuccessful parsing.
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
	
}

