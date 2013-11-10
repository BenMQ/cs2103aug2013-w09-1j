package sg.edu.nus.cs2103.sudo;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Constants {

	public static final String MESSAGE_WELCOME_TO_SUDO_FIRST_A = "Welcome to "
			+ "the world of sudo!\nFor first time users, to learn about the "
			+ "full list of commands, type:\n";
	public static final String MESSAGE_WELCOME_TO_SUDO_FIRST_B = "help\n\nTo " +
			"learn more about a command,"
			+ " type: help <command>\n";
	public static final String MESSAGE_WELCOME_TO_SUDO_RELOAD_A = "Welcome " +
			"back, dear user.\nYour past records have been loaded " +
			"successfully.\n";
	public static final String MESSAGE_WELCOME_TO_SUDO_RELOAD_B =	"To get " +
			"a full list of commands, type: help\nTo learn more about a"
			+ " command: help <command>\n";
	public static final String MESSAGE_INVALID_COMMAND = "Invalid command.\n";
	public static final String MESSAGE_INVALID_TASK_INDEX = "Invalid task "
			+ "index.\n";
	public static final String MESSAGE_INCOMPLETE_COMMAND = "Incomplete "
			+ "command.\n";
	public static final String MESSAGE_INVALID_SEARCH = "Search is invalid.\n";
	public static final String MESSAGE_INVALID_DELETE = "Search term for delete"
			+ " is invalid.\n";
	public static final String MESSAGE_SEARCH_RESULTS = "Search Results";
	public static final String MESSAGE_NO_HISTORY = "History file was removed "
			+ "or deleted.";
	public static final String MESSAGE_DISPLAY = "Remaining tasks:\n";
	public static final String MESSAGE_DISPLAY_ALL = "Displaying all tasks\n";
	public static final String MESSAGE_DISPLAY_FINISHED = "Displaying finished "
			+ "tasks\n";
	public static final String MESSAGE_INVALID_NUMBER_OF_DATES = "Invalid "
			+ "number of dates.\n";
	public static final String MESSAGE_ADD_TIMED = "Add: %s from %s to %s\n";
	public static final String MESSAGE_ADD_DEADLINE = "Add: %s by %s\n";
	public static final String MESSAGE_ADD_FLOATING = "Add: %s\n";
	public static final String MESSAGE_SEARCH = "Searching: %s\n";
	public static final String MESSAGE_EDIT = "Edited: %s\n";
	public static final String MESSAGE_DELETE = "Deleted: %s\n";
	public static final String MESSAGE_MULTIPLE_DELETE = "You have multiple "
			+ "items that match your delete search term.\nTo delete " +
			"a particular task, type: delete <task index>\n\n";
	public static final String MESSAGE_MISSING_DESCRIPTION = "Task description"
			+ " should be between single quotes.\n";
	public static final String MESSAGE_SAME_START_END_TIME = "Start date and"
			+ " end date for the task are the same!\n";
	public static final String MESSAGE_END_BEFORE_START_TIME = "End date of "
			+ "the task occurs before start date!\n";
	public static final String MESSAGE_EMPTY_LIST = "Task List is empty!\n";
	public static final String MESSAGE_ALREADY_COMPLETE = "The task is "
			+ "already complete.\n";
	public static final String MESSAGE_ALREADY_INCOMPLETE = "The task is "
			+ "already incomplete.\n";
	public static final String MESSAGE_NO_SEARCH_RESULTS = "No search "
			+ "results found.\n";
	public static final String MESSAGE_NO_FLOATING_TASKS = "No floating "
			+ "tasks exist.\n";
	public static final String MESSAGE_FINISH = "Finished task: %s\n";
	public static final String MESSAGE_UNFINISH = "Un-Finished task: %s\n";
	public static final String MESSAGE_UNDO = "Undo...";
	public static final String MESSAGE_REDO = "Redo...";
	public static final String NOTHING_TO_DISPLAY = "Nothing to display.\n";
	public static final String MESSAGE_NO_FREE_SLOTS = "No free slots " +
			"found. :(\n";
	public static final String MESSAGE_FREE_SLOTS_PREFIX = "Here are your "
			+ "free time slots for %s:\n";
	public static final String MESSAGE_HISTORY_LOAD_ERROR = "Loading "
			+ "history file error: file can not be found.";
	public static final String MESSAGE_SAVE_ERROR = "Saving file error: "
			+ "file can not be found.";
	public static final String MESSAGE_LAST_HISTORY = "No more history "
			+ "records found.";
	
	public static final int FREE_SLOT_MINIMUM_DURATION_IN_MINUTES = 10;
	public static final int FREE_SLOT_MINIMUM_DURATION = 
			FREE_SLOT_MINIMUM_DURATION_IN_MINUTES * 60 * 1000; // milliseconds

	public static final String FILE_NAME = "sudo.sav";
	public static final String HISTORY_NAME = "sudo.his";
	public static final String HISTORY_NAME_TEST = "test.his";

	public static final DateTimeFormatter DATE_MONTH_FORMAT = 
			DateTimeFormat.forPattern("EEE d MMM");
	public static final DateTimeFormatter DATE_MONTH_YEAR_FORMAT = 
			DateTimeFormat.forPattern("EEE d MMM y");
	public static final DateTimeFormatter HOUR_FORMAT = 
			DateTimeFormat.forPattern("ha");
	public static final DateTimeFormatter HOUR_MINUTE_FORMAT = 
			DateTimeFormat.forPattern("h:mma");
	public static final String TASK_COMPLETED_FLAG = "Done!";
	public static final String FINISHED_TASK_SEPARATOR = "\n\n"
			+ "[Finished tasks]====================================";
	public static final String FLOATING_TASK_SEPARATOR = "\n\n"
			+ "[Floating tasks]====================================";	
	public static final int SEPARATOR_LENGTH = 50;
	public static final char SEPARATOR_CHAR = '=';

}
