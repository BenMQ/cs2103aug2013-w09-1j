package sg.edu.nus.cs2103.sudo;

import java.util.HashMap;

//@author A0099317U
public class HelpConstants {

	/**
	 * Constants relating to the help command.
	 */

	public static final String MESSAGE_WELCOME_HELP_PAGE = "Welcome to the world of sudo!\nType help '<command>' for help on that command. \nHere are the list of commands:\nadd\nedit\ndisplay\ndelete\nfinish\nsearch\nundo\nredo\nfree\nschedule\n";
	public static final String HELP_NOT_FOUND = "We couldn't find anything on '%s'. Try help 'list' for a list of help topics.\n";
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

	public static final HashMap<String, String> helpTopics = new HashMap<String, String>() {
		/**
		 * This key-value table maps keywords to help messages.
		 */
		private static final long serialVersionUID = 1L;

		{
			put("HELP", HELP_HELP);

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
