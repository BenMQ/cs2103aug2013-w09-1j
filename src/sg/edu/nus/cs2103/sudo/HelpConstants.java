package sg.edu.nus.cs2103.sudo;

import java.util.HashMap;

public class HelpConstants {

	/**
	 * Constants relating to the help command.
	 */

	public static final String MESSAGE_WELCOME_HELP_PAGE = "Welcome to the "
			+ "world of sudo!\n\nType help '<command>' for help on that command. "
			+ "\nHere are the list of commands:\nadd\nedit\ndisplay\ndelete\n"
			+ "finish\nsearch\nundo\nredo\nfree\nschedule\n";

	public static final String HELP_NOT_FOUND = "We couldn't find anything "
			+ "on '%s'.\nFor a list of help topics, type: help\n";

	public static final String HELP_HELP = "Type help '<command>' for help "
			+ "on that command. \n";
	public static final String HELP_DISPLAY = "To display incomplete "
			+ "tasks:\ndisplay\nTo display both completed and incomplete tasks:"
			+ "\nall\n\nYou could also type in the following instead of display:\n"
			+ "'show'\n\n";

	public static final String HELP_ADD = "To add a task,\nspecify your "
			+ "command in any of the following formats:\n\nadd '<description>' "
			+ "from <start date/time> to <end date/time>\nadd '<description>' "
			+ "by <end date/time>\nadd '<description>'\n\nYou could also type in "
			+ "the following instead of add:\n'do'\n'sudo'\n\n";

	public static final String HELP_EDIT = "To edit the details of a task,\n"
			+ "specify your command in any of the following formats:\n\n"
			+ "To update the description of a task only:\nedit <task index> "
			+ "'<new_task_description>'\nTo update the start and end date/time "
			+ "for a timed task:\nedit <task index> from <start date/time> to "
			+ "<end date/time>\nTo update the end date/time for a deadline "
			+ "task:\nedit <task index> by <end date/time>\n\nYou could also "
			+ "type in the following instead of edit:\n'change'\n'modify'\n\n";

	public static final String HELP_DELETE = "To delete any task:\n"
			+ "delete <task index>\n\nAlternatively, to delete a task by searching"
			+ " for a search term in its description:\ndelete '<search term>'\n"
			+ "If there is only one search result, it will be deleted.\n\n"
			+ "You could also type in the following instead of delete:\n'del'\n"
			+ "'remove'\n\n";

	public static final String HELP_FINISH = "To mark a task as completed:\n"
			+ "finish <task index>\nTo mark a task as incomplete:\nunfinish "
			+ "<task index>\n\nYou could also type in the following instead of "
			+ "finish:\n'complete'\n'done'\n\n";

	public static final String HELP_UNDO = "To undo the last action, simply "
			+ "type: undo\nTo redo the last action, simply type: redo\n\n";

	public static final String HELP_SEARCH = "To search for any task:\nsearch"
			+ " '<search term>'\n\n"
			+ "You could also type in the following instead of search:\n'find'\n\n";

	public static final String HELP_FREE = "To find free time slots on a given"
			+ " date:\nfree <day/date>\ne.g. free fri\n\nBy default, date is today.\n\n";

	public static final String HELP_SCHEDULE = "To schedule a task into a free "
			+ "period, type:\nschedule <task index> <duration> <day/date>\n"
			+ "e.g. schedule 3 1h30m tomorrow\n\nBy default, date is today.\n\n";

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
