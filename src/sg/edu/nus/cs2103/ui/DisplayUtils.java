package sg.edu.nus.cs2103.ui;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TaskManagerUtils;

//@author A0099317U
public class DisplayUtils {
	
	/**
	 * Prints a pretty string representation of a Task.
	 * @param Task
	 */
	public static String prettyPrint (Task task) {
		String toReturn = "";
		
		if (task.getStartTime() == null && task.getEndTime() == null) {
			return GUI.print_add(task.getId() + ". ", GUIConstants.COLOR_CODE_WHITE)+
			GUI.print_add(task.getDisplayString(), GUIConstants.COLOR_CODE_GREEN);
		} else if (task.getStartTime() == null){
			return GUI.print_add(task.getDisplayString(), GUIConstants.COLOR_CODE_WHITE)+
			GUI.print_add(task.getDescription(), GUIConstants.COLOR_CODE_GREEN);
		} else {
			return GUI.print_add(task.getDisplayString(), GUIConstants.COLOR_CODE_WHITE)+
			GUI.print_add(task.getDescription(), GUIConstants.COLOR_CODE_GREEN);
		}
	}	
	
	/**
	 * Adds contextual prefixes to day separators such as Today, Overdue, 
	 * and so on based on current day
	 * @param int
	 */	
	public static String addPrefix(DateTime previousDate) {
		String prefix = "";
		if (previousDate.compareTo(DateTime.now()) < 0) {
			prefix = GUIConstants.OVERDUE_PREFIX;
		} else if (isSameDate(previousDate, DateTime.now())) {
			prefix = GUIConstants.TODAY_PREFIX;
		} else if (isSameDate(previousDate, DateTime.now().plusDays(1))) {
			prefix = GUIConstants.TOMORROW_PREFIX;
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
		
		if (previousDate == null || !isSameDate(time, previousDate)) {
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
		String prefix = addPrefix(previousDate);
		
		DateTimeFormatter dateFormat = Constants.DATE_MONTH_FORMAT;
		if(previousDate.getYear() != DateTime.now().getYear()){
			dateFormat = Constants.DATE_MONTH_YEAR_FORMAT;
		}
		
		String label = prefix + previousDate.toString(dateFormat);
		
		
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
	
	/**
	 * This operation check if 2 DateTime objects have exactly same date
	 * 
	 * @param dt1
	 *            is first date time
	 * @param dt2
	 *            is second date time
	 * @return true if 2 objects have same date or false if otherwise
	 * 
	 */
	public static boolean isSameDate(DateTime dt1, DateTime dt2) {
		if (dt1 == null && dt2 == null)
			return true;
		if (dt1 == null && dt2 != null)
			return false;
		if (dt1 != null && dt2 == null)
			return false;

		return ((dt1.getYear() == dt2.getYear())
				&& (dt1.getMonthOfYear() == dt2.getMonthOfYear()) 
				&& (dt1.getDayOfMonth() == dt2.getDayOfMonth()));
	}	
}
