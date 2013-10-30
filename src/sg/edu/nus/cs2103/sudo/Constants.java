package sg.edu.nus.cs2103.sudo;

import java.util.HashMap;


public class Constants {

	public static final String MESSAGE_WELCOME_TO_SUDO_FIRST = "Welcome to the world of Sudo!\nTo get help, simply type: help\nTo learn more about a command: help [command]\n";
	public static final String MESSAGE_WELCOME_TO_SUDO_RELOAD = "Welcome back, dear user.\nYour past records have been loaded successfully.\nTo learn more, simply type: help\nTo learn more about a command: help [command]\n";

	public static final String MESSAGE_WELCOME_HELP_PAGE = "Type help '<command>' for help on that command. \nHere are the list of commands:\nadd\nedit\ndisplay\ndelete\nfinish\nsearch\nundo\nredo\nfree\nschedule\n";
	public static final String HELP_NOT_FOUND = "We couldn't find anything on '%s'. Try help 'list' for a list of help topics.\n";
	public static final String HELP_LIST = "Listing all help topics. WIP\n";
	
	public static final String HELP_HELP = "Type help '<command>' for help on that command. \n";
	public static final String HELP_DISPLAY = "To display incomplete tasks:\ndisplay\nTo display both completed and incomplete tasks:\nall\nYou could also type in the following instead of 'display': 'show'.\n\n";
	public static final String HELP_ADD = "To add a task, specify your command in any of the following formats:\nadd '[description]' from [start date/time] to [end date/time]\nadd '[description]' by [end date/time]\nadd '[description]'\nYou could also type in the following instead of 'add': 'do', 'sudo'.\n\n";
	public static final String HELP_EDIT = "To edit the details of a task, specify your command in any of the following formats:\nTo update the description of a task only:\nedit [task index] '[new_task_description]'\nTo update the start and end date/time for a timed task:\nedit [task index] from [start date/time] to [end date/time]\nTo update the end date/time for a deadline task:\nedit [task index] by [end date/time]\nYou could also type in the following instead of 'edit': 'change', 'modify'.\n\n";
	public static final String HELP_DELETE = "To delete any task:\ndelete [task index]\nAlternatively, to delete a task by searching for a search term in its description:\ndelete '[search term]'\nIf there is only one search result, it will be deleted. If there are multiple search results, you will then be prompted to indicate the task index of the task you would like to delete.\nYou could also type in the following instead of 'delete': 'del', 'remove'.\n\n";
	public static final String HELP_FINISH = "To mark a task as completed:\nfinish [task index]\nTo mark a task as incomplete:\nunfinish [task index]\nYou could also type in the following instead of 'finish': 'complete', 'done'.\n\n";
	public static final String HELP_UNDO = "To undo the last action, simply type: undo\nTo redo the last action, simply type: redo\n\n";
	public static final String HELP_SEARCH = "To search for any incomplete task:\nsearch '[search term]'\nTo search for any incomplete or completed task:\nsearch -all '[search term]'\nTo search for tasks on a particular day or time:\nsearch [date/time]\nsearch [date/time] to [date/time]\nYou could also type in the following instead of 'search': 'find'.\n\n";
	public static final String HELP_FREE = "To find free time slots for today:\nfree today\n\n";
	public static final String HELP_SCHEDULE = "To add a task and automatically schedule it into a free period today, use:\nschedule '[description]' [duration]\ne.g. schedule 'do yoga' 1h\n\n";
	
	public static final String MESSAGE_INVALID_COMMAND = "Invalid command\n";
	public static final String MESSAGE_INVALID_TASK_INDEX = "Invalid task index\n";
	public static final String MESSAGE_INCOMPLETE_COMMAND = "Incomplete command\n";
	public static final String MESSAGE_INVALID_SEARCH = "Search is invalid.\n";
	public static final String MESSAGE_INVALID_DELETE = "Search term for delete is invalid.\n";

	public static final String MESSAGE_DISPLAY = "Displaying incomplete tasks\n";
	public static final String MESSAGE_DISPLAY_ALL = "Displaying all tasks\n";
	public static final String MESSAGE_INVALID_NUMBER_OF_DATES = "Invalid number of dates.\n";
	public static final String MESSAGE_ADD_TIMED = "Add: %s from %s to %s\n";
	public static final String MESSAGE_ADD_DEADLINE = "Add: %s by %s\n";
	public static final String MESSAGE_ADD_FLOATING = "Add: %s\n";
	public static final String MESSAGE_SEARCH = "Searching: %s\n";
	public static final String MESSAGE_EDIT = "Editing: %s\n";
	public static final String MESSAGE_DELETE = "Deleted: %s\n";
	public static final String MESSAGE_ENTER_TASK_ID = "Please enter task id:";
	public static final String MESSAGE_MISSING_DESCRIPTION = "Task description should be between single quotes.\n";

	public static final String MESSAGE_SAME_START_END_TIME = "Start date and end date for the task are the same!\n";
	public static final String MESSAGE_END_BEFORE_START_TIME = "End date of the task occurs before start date!\n";

	public static final String MESSAGE_EMPTY_LIST = "Task List is empty!\n";
	public static final String MESSAGE_ALREADY_COMPLETE = "The task specified is complete.\n";
	public static final String MESSAGE_ALREADY_INCOMPLETE = "The task specified is incomplete.\n";
	public static final String MESSAGE_NO_SEARCH_RESULTS = "No search results found.\n";
	public static final String MESSAGE_NO_FLOATING_TASKS = "No floating tasks.\n";
	public static final String MESSAGE_FINISH = "Finishing task.\n";
	public static final String MESSAGE_UNFINISH = "Un-Finishing task.\n";

	public static final String MESSAGE_NO_FREE_SLOTS = "No free slots found. :(";
	public static final String MESSAGE_FREE_SLOTS_PREFIX = "Here are your free time slots for ";

	public static final int FREE_SLOT_MINIMUM_DURATION_IN_MINUTES = 10;
	public static final int FREE_SLOT_MINIMUM_DURATION = FREE_SLOT_MINIMUM_DURATION_IN_MINUTES * 60 * 1000; // milliseconds

	public static final String FILE_NAME = "sudo.sav";
	public static final String HISTORY_NAME = "sudo.his";

	public static final String HISTORY_NAME_TEST = "test.his";

	public static final String MESSAGE_HISTORY_LOAD_ERROR = "Loading history file error: file can not be found.";
	public static final String MESSAGE_SAVE_ERROR = "Saving file error: file can not be found.";
	public static final String MESSAGE_LAST_HISTORY = "No more history records found.";

	public static final String MESSAGE_UNDO = "Undo...";

	public static final String MESSAGE_REDO = "Redo...";

	public static final int KEYBOARD_SPACE = 32;
	public static final int KEYBOARD_TAB = 9;

	public static final HashMap<String, COMMAND_TYPE> aliases = new HashMap<String, COMMAND_TYPE>() {
		/**
		 *  This key-value table maps aliases to their corresponding COMMAND_TYPE.
		 */
		private static final long serialVersionUID = 1L;
		{
			put("ADD", COMMAND_TYPE.ADD);
			put("DO", COMMAND_TYPE.ADD);
			put("SUDO", COMMAND_TYPE.ADD);

			put("DELETE", COMMAND_TYPE.DELETE);
			put("DEL", COMMAND_TYPE.DELETE);
			put("REMOVE", COMMAND_TYPE.DELETE);

			put("EDIT", COMMAND_TYPE.EDIT);
			put("CHANGE", COMMAND_TYPE.EDIT);
			put("MODIFY", COMMAND_TYPE.EDIT);

			put("FINISH", COMMAND_TYPE.FINISH);
			put("COMPLETE", COMMAND_TYPE.FINISH);
			put("DONE", COMMAND_TYPE.FINISH);

			put("SEARCH", COMMAND_TYPE.SEARCH);
			put("FIND", COMMAND_TYPE.SEARCH);

			put("DISPLAY", COMMAND_TYPE.DISPLAY);
			put("SHOW", COMMAND_TYPE.DISPLAY);

			put("UNDO", COMMAND_TYPE.UNDO);
			put("REDO", COMMAND_TYPE.REDO);
			put("FREE", COMMAND_TYPE.FREE);
			put("SCHEDULE", COMMAND_TYPE.SCHEDULE);
			put("SORT", COMMAND_TYPE.SORT);
			put("HELP", COMMAND_TYPE.HELP);
			put("ALL", COMMAND_TYPE.ALL);
			put("UNFINISH", COMMAND_TYPE.UNFINISH);

			put("EXIT", COMMAND_TYPE.EXIT);
			put("QUIT", COMMAND_TYPE.EXIT);
		}
	};

	public static final HashMap<String, String> helpTopics = new HashMap<String, String>() {
		/**
		 * 	This key-value table maps keywords to help messages.
		 */
		private static final long serialVersionUID = 1L;

		{
			put("HELP", HELP_HELP);
			put("LIST", HELP_LIST);
			
			put("ADD", HELP_ADD);
			put("SUDO", HELP_ADD);
			put("DO", HELP_ADD);
			
			put("DISPLAY", HELP_DISPLAY);
			put("SHOW", HELP_DISPLAY);
			
			put("EDIT", HELP_EDIT);
			put("CHANGE", HELP_EDIT);
			put("MODIFY", HELP_EDIT);
			
			put("DELETE", HELP_DELETE);
			put("DEL", HELP_DELETE);
			put("REMOVE", HELP_DELETE);
			
			put("FINISH", HELP_FINISH);
			put("COMPLETE", HELP_FINISH);
			put("DONE", HELP_FINISH);
			
			put("UNDO", HELP_UNDO);
			put("REDO", HELP_UNDO);
			
			put("SEARCH", HELP_SEARCH);
			put("FIND", HELP_SEARCH);
			
			put("FREE", HELP_FREE);
			put("SCHEDULE", HELP_SCHEDULE);
		}
	};

	
	
}
