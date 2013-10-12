package sg.edu.nus.cs2103.sudo;

import java.util.logging.Logger;

import sg.edu.nus.cs2103.sudo.logic.InputParser;

public class Constants {

	public static final String MESSAGE_INVALID_COMMAND = "Invalid command\n";
	public static final String MESSAGE_INVALID_TASK_INDEX = "Invalid task index\n";
	public static final String MESSAGE_INCOMPLETE_COMMAND = "Incomplete command\n";	
	public static final String MESSAGE_INVALID_SEARCH = "Search is invalid.\n";
	public static final String MESSAGE_INVALID_DELETE = "Search term for delete is invalid.\n";
	
	public static final String MESSAGE_DISPLAY = "Displaying all tasks";
	public static final String MESSAGE_INVALID_NUMBER_OF_DATES = "Invalid number of dates";
	public static final String MESSAGE_ADD_TIMED = "Add timed task: ";
	public static final String MESSAGE_ADD_DEADLINE = "Add deadline task: ";
	public static final String MESSAGE_ADD_FLOATING = "Add floating task: ";
	public static final String MESSAGE_ENTER_TASK_ID = "Please enter task id:";
	public static final String MESSAGE_MISSING_DESCRIPTION = "Task description should be between single quotes.\n";
	
	public static final String MESSAGE_EMPTY_LIST = "Task List is empty!\n";
	public static final String MESSAGE_ALREADY_COMPLETE = "The task specified is complete.\n";
	public static final String MESSAGE_ALREADY_INCOMPLETE = "The task specified is incomplete.\n";
	public static final String MESSAGE_NO_SEARCH_RESULTS = "No search results found.\n";
	public static final String MESSAGE_NO_FLOATING_TASKS = "No floating tasks.\n";
	public static final String MESSAGE_FINISH = "Finishing task.";
	
	public static Logger parserLogger = Logger.getLogger(InputParser.class.getName());
}
