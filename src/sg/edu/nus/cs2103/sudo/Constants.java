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
	public static final String MESSAGE_INVALID_NUMBER_OF_DATES = "Invalid number of dates.\n";
	public static final String MESSAGE_ADD_TIMED = "Add timed task: %s\n";
	public static final String MESSAGE_ADD_DEADLINE = "Add deadline task: %s\n";
	public static final String MESSAGE_ADD_FLOATING = "Add floating task: %s\n";
	public static final String MESSAGE_SEARCH = "Searching: %s\n";
	public static final String MESSAGE_EDIT = "Editing: %s\n";
	public static final String MESSAGE_DELETE = "Deleted: ";
	public static final String MESSAGE_ENTER_TASK_ID = "Please enter task id:";
	public static final String MESSAGE_MISSING_DESCRIPTION = "Task description should be between single quotes.\n";
	
	public static final String MESSAGE_INVALID_START_TIME = "Start date is before today!";
	public static final String MESSAGE_INVALID_END_TIME = "End date is before today!";
	public static final String MESSAGE_SAME_START_END_TIME = "Start date and end date for the task are the same!";
	public static final String MESSAGE_END_BEFORE_START_TIME = "End date of the task occurs before start date!";
			
	public static final String MESSAGE_EMPTY_LIST = "Task List is empty!\n";
	public static final String MESSAGE_ALREADY_COMPLETE = "The task specified is complete.\n";
	public static final String MESSAGE_ALREADY_INCOMPLETE = "The task specified is incomplete.\n";
	public static final String MESSAGE_NO_SEARCH_RESULTS = "No search results found.\n";
	public static final String MESSAGE_NO_FLOATING_TASKS = "No floating tasks.\n";
	public static final String MESSAGE_FINISH = "Finishing task.";
	
	public static final String FILE_NAME = "sudo.sav";
	public static final String HISTORY_NAME = "sudo.his";
	
	public static final String HISTORY_NAME_TEST = "test.his";
	
	private static final String MESSAGE_HISTORY_LOAD_ERROR = "Loading history file error: file can not be found.";
	private static final String MESSAGE_LAST_HISTORY = "No more histories. Can not undo.";
	
}
