package sg.edu.nus.cs2103.ui;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.logic.Task;

//@author A0099317U
public class DisplayUtils {
	
	public static final DateTimeFormatter DATE_MONTH_FORMAT = 
			DateTimeFormat.forPattern("EEE d MMM");
	public static final DateTimeFormatter HOUR_FORMAT = 
			DateTimeFormat.forPattern("ha");
	public static final DateTimeFormatter HOUR_MINUTE_FORMAT = 
			DateTimeFormat.forPattern("h:mma");
	private static final int SEPARATOR_LENGTH = 40;
	private static final char SEPARATOR_CHAR = '=';

	/**
	 * Prints a pretty string representation of a Task.
	 * @param Task
	 */
	public static String prettyPrint (Task task) {
		String toReturn = "";
		DateTimeFormatter onlytimeformat = HOUR_FORMAT;
		if (hasZeroMinutes(task.getEndTime())) {
			onlytimeformat = HOUR_MINUTE_FORMAT;
		} 
		
		if (task.getStartTime() == null && task.getEndTime() == null) {
			GUI.print_add(task.getId() + ". ", 4);
			toReturn+=(task.getId() + ". ");
			GUI.print_add(task.getDescription(), 0);
			toReturn+=(task.getDescription());
			return toReturn;
		} else if (task.getStartTime() == null){
			GUI.print_add(formatDeadlineTask(task, onlytimeformat), 4);
			toReturn+=(formatDeadlineTask(task, onlytimeformat));
			GUI.print_add(task.getDescription(), 0);
			toReturn+=task.getDescription();
			return toReturn;
		} else {
			GUI.print_add(formatTimedTask(task, onlytimeformat), 4);
			toReturn+=(formatTimedTask(task, onlytimeformat));
			GUI.print_add(task.getDescription(), 0);
			toReturn+=task.getDescription();
			return toReturn;
		}
	}	
	
	/**
	 * Adds contextual prefixes to day separators such as Today, Overdue, 
	 * and so on based on current day
	 * @param int
	 */	
	public static String addPrefix(int previousDay) {
		String prefix = "";
		if (previousDay < DateTime.now().getDayOfYear()) {
			prefix = "Overdue: ";
		} else if (previousDay == DateTime.now().getDayOfYear()) {
			prefix = "Today: ";
		} else if (previousDay == (DateTime.now().getDayOfYear() + 1)) {
			prefix = "Tomorrow: ";
		}
		return prefix;
	}	
	
	/**
	 * Determines when to add date-level separators in display.
	 * @param DateTime
	 * @param Task
	 * @return DateTime
	 */		
	public static DateTime insertDateSeparators(DateTime previousDate, 
			Task task) {
		
		DateTime time = task.getEndTime();
		if(task.isTimedTask()){
			time = task.getStartTime();
		}
		
		if (previousDate == null || time.getDayOfYear() != 
				previousDate.getDayOfYear()) {
			previousDate = time;
			DisplayUtils.printDateSeparator(previousDate);
		}
		return previousDate;
	}
	
	/**
	 * Adds constant-length date-level separators between groups of tasks.
	 * @param DateTime
	 */	
	public static void printDateSeparator(DateTime previousDate) {
		int index = 1;
		String prefix = addPrefix(previousDate.getDayOfYear());
		String label = prefix + previousDate.toString(DATE_MONTH_FORMAT);
		int separatorLength = SEPARATOR_LENGTH - label.length();
		if (label.contains("verdue")) {
			index += 2;
		}
		GUI.print_add("\n["+ label + "]" + fillString(
				separatorLength, SEPARATOR_CHAR), index);
	}	
	
	/**
	 * Adds separators to indicate finished tasks.
	 * @param boolean
	 * @param Task
	 * @return boolean
	 */		
	public static boolean insertFinishedSeparator(boolean finishedStarted, 
			Task task) {
		if (!finishedStarted && task.isComplete()) {
			finishedStarted = true;
			GUI.print_add(Constants.FINISHED_TASK_SEPARATOR, 1);
		}
		return finishedStarted;
	}

	/**
	 * Adds separators to indicate floating tasks.
	 * @param boolean
	 * @param Task
	 * @return boolean
	 */			
	public static boolean insertFloatingSeparator(boolean floatingStarted, 
			Task task) {
		if (!floatingStarted && task.isFloatingTask()) {
			floatingStarted = true;
			GUI.print_add(Constants.FLOATING_TASK_SEPARATOR, 1);
		}
		return floatingStarted;
	}
	
	/**
	 * Determines if a DateTime has zero minute values.
	 * @param DateTime
	 * @return boolean
	 */		
	public static boolean hasZeroMinutes(final DateTime datetime) {
		return datetime != null 
				&& datetime.getMinuteOfHour() > 0;
	}	
	
	/**
	 * Helper method to generate a string of characters
	 * of specified length.
	 * @param int
	 * @param char
	 * @return String
	 */	
	protected static String fillString(final int length, 
			final char charToFill) {
		  if (length > 0) {
		    char[] array = new char[length];
		    Arrays.fill(array, charToFill);
		    return new String(array);
		  }
		  return "";
	}
	
	/**
	 * Helper method to return a pretty string for DateTime objects.
	 * @param DateTime
	 * @return String
	 */		
	public static String formatDate(final DateTime datetime) {
		return datetime.toString("dd MMMM hh:mm a");
	}	

	/**
	 * Helper method to return a formatted string for Timed tasks.
	 * @param Task
	 * @param DateTimeFormatter
	 * @return String
	 */	
	public static String formatTimedTask(Task task,
			DateTimeFormatter onlytimeformat) {
		if(task.isOnSameDay()){
			return new StringBuilder(task.getId() + ". [" + task.getStartTime().
					toString(onlytimeformat) + " - "  
					+ task.getEndTime().toString(onlytimeformat) + 
					"] ").toString();
		}
		
		return new StringBuilder(task.getId() + ". [" + task.getStartTime().
				toString(onlytimeformat) + " - " 
				+ task.getEndTime().toString(DATE_MONTH_FORMAT) + " " 
				+ task.getEndTime().toString(onlytimeformat) + "] ").toString();
	}

	/**
	 * Helper method to return a formatted string for Deadline tasks.
	 * @param Task
	 * @param DateTimeFormatter
	 * @return String
	 */		
	public static String formatDeadlineTask(Task task,
			DateTimeFormatter onlytimeformat) {
		return new StringBuilder(task.getId() + ". [by " + task.getEndTime().
				toString(onlytimeformat) + "] ").toString();
	}	
	
}
