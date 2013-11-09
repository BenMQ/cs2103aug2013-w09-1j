package sg.edu.nus.cs2103.ui;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.logic.Task;

//@author A0099317U
public class DisplayUtils {

	/**
	 * Prints a pretty string representation of a Task.
	 * @param Task
	 */
	public static String prettyPrint (Task task) {
		String toReturn = "";
		
		if (task.getStartTime() == null && task.getEndTime() == null) {
			GUI.print_add(task.getId() + ". ", 4);
			toReturn+=(task.getId() + ". ");
			GUI.print_add(task.getDisplayString(), 0);
			toReturn+=(task.getDisplayString());
			return toReturn;
		} else if (task.getStartTime() == null){
			GUI.print_add(task.getDisplayString(), 4);
			toReturn+=(task.getDisplayString());
			GUI.print_add(task.getDescription(), 0);
			toReturn+=task.getDescription();
			return toReturn;
		} else {
			GUI.print_add(task.getDisplayString(), 4);
			toReturn+=(task.getDisplayString());
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
			prefix = Constants.OVERDUE_PREFIX;
		} else if (previousDay == DateTime.now().getDayOfYear()) {
			prefix = Constants.TODAY_PREFIX;
		} else if (previousDay == (DateTime.now().getDayOfYear() + 1)) {
			prefix = Constants.TOMORROW_PREFIX;
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
		String label = prefix + previousDate.toString(Constants.DATE_MONTH_FORMAT);
		int separatorLength = Constants.SEPARATOR_LENGTH - label.length();
		if (label.contains("verdue")) {
			index += 2;
		}
		GUI.print_add("\n["+ label + "]" + fillString(
				separatorLength, Constants.SEPARATOR_CHAR), index);
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
	
}
