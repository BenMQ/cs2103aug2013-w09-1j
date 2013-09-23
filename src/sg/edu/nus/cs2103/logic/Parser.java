package sg.edu.nus.cs2103.logic;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;

public class Parser {

	/**
	 * This Parser class is responsible for extracting commands
	 * and description/time parameters from user string inputs,
	 * delegating to subclasses whenever necessary. 
	 * It then calls the appropriate CRUD methods.
	 * 
	 * @author Yos Riady 
	 */
	
	
	public static final String DATE_TIME_SPLIT = "TO";
	public static final String FROM = "FROM";	
	public static final String DOUBLE_QUOTE = "\"";
	
	// Parser object constructor
	public Parser(){
		
	}
	
	public void executeCommand(String userInput){
		String userCommand = parseCommand(userInput); 
		COMMAND_TYPE userCommandType = getCommandType(userCommand);
		
		// here need to parse description, and later parse time objects
		String taskDescription = parseDescription(userInput);
		
		//executes the method associated with userCommandType
		
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
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
