package sg.edu.nus.cs2103.ui;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.logic.DateTimeUtils;
import sg.edu.nus.cs2103.sudo.logic.StringUtils;
import sg.edu.nus.cs2103.sudo.logic.Task;

//@author A0099317U
/**
 * This class is library of static methods to output strings
 * for display by UI and GUI components.
 */
public class DisplayUtils {
	
	/**
	 * Prints a pretty string representation of a Task.
	 * @param Task
	 */
	public static String prettyPrint (Task task) {
		String taskString = task.getDateString();
		if (task.isFloatingTask()) {
			taskString = task.getId() + ". ";
		} 
		
		return GUI.print_add(taskString, 
				GUIConstants.COLOR_CODE_WHITE) + 
				GUI.print_add(task.getDescription(), 
						GUIConstants.COLOR_CODE_GREEN);
	}	
	
	/**
	 * Returns contextual prefixes to day separators such as Today, Overdue, 
	 * and so on based on current day
	 * @param int
	 * @return String
	 */	
	public static String getPrefix(DateTime previousDate) {
		String prefix = "";
		DateTime today = DateTime.now();
		DateTime tomorrow = DateTime.now().plusDays(1);
		boolean is_overdue = previousDate.compareTo(DateTime.now()) < 0;
		boolean is_today = DateTimeUtils.isSameDate(previousDate, today);
		boolean is_tomorrow = DateTimeUtils.isSameDate(previousDate, tomorrow);
		
		if (is_overdue) {
			prefix = GUIConstants.OVERDUE_PREFIX;
		} else if (is_today) {
			prefix = GUIConstants.TODAY_PREFIX;
		} else if (is_tomorrow) {
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
		DateTime currentDate = task.getEndTime();
		boolean dateIsNull = (previousDate == null);
		
		if (dateIsNull || !DateTimeUtils.isSameDate(currentDate, previousDate)) {
			previousDate = currentDate;
			DisplayUtils.printDateSeparator(previousDate);
		}
		return previousDate;
	}
	
	/**
	 * Adds separators between groups of tasks on a different date.
	 * For example, tasks for today are separated from tasks 
	 * tomorrow and next Monday.
	 * 
	 * @param DateTime
	 */	
	public static void printDateSeparator(DateTime previousDate) {
		DateTimeFormatter dateFormat = getDateFormat(previousDate);
		String prefix = getPrefix(previousDate);
		String label = createLabel(previousDate, dateFormat, prefix);
		int color = getPrefixColor(prefix);
		
		GUI.print_add("\n\n["+ label + "]" + generateSeparator(label), color);
	}

	/**
	 * Returns a string label containing any contextual prefixes 
	 * (Today, Overdue, etc) and date information.
	 * 
	 * @param DateTime
	 * @param DateTimeFormatter
	 * @param String
	 */		
	public static String createLabel(DateTime previousDate,
			DateTimeFormatter dateFormat, String prefix) {
		return prefix + previousDate.toString(dateFormat);
	}
	
	
	/**
	 * Returns date-month-year date format 
	 * for tasks not on the current year, else 
	 * returns a date-month format for task display. 
	 * 
	 * @param DateTime
	 * @return DateTimeFormatter
	 */
	public static DateTimeFormatter getDateFormat(DateTime previousDate) {
		DateTimeFormatter dateFormat = Constants.DATE_MONTH_FORMAT;
		boolean isNotTheSameYear = previousDate.getYear() 
				!= DateTime.now().getYear();
		if(isNotTheSameYear){
			dateFormat = Constants.DATE_MONTH_YEAR_FORMAT;
		}
		return dateFormat;
	}

	/**
	 * Gets the right color (red, yellow, etc.)
	 * for a given String prefix (Overdue, etc.). 
	 * 
	 * @param String
	 * @return int
	 */
	public static int getPrefixColor(String prefix) {
		int index = 1;
		if (prefix.equals(GUIConstants.OVERDUE_PREFIX)) {
			index += 2;
		}
		return index;
	}	
	
	/**
	 * Adds separator to indicate finished tasks.
	 * @param boolean
	 * @param Task
	 * @return boolean
	 */	
	public static boolean insertFinishedSeparator(boolean finishedStarted, 
			Task task) {
		if (!finishedStarted && task.isComplete()) {
			finishedStarted = true;
			GUI.print_add(Constants.FINISHED_TASK_SEPARATOR, 
					GUIConstants.COLOR_CODE_YELLOW);
		}
		return finishedStarted;
	}

	/**
	 * Adds separator to indicate floating tasks.
	 * @param boolean
	 * @param Task
	 * @return boolean
	 */			
	public static boolean insertFloatingSeparator(boolean floatingStarted, 
			Task task) {
		if (!floatingStarted && task.isFloatingTask()) {
			floatingStarted = true;
			GUI.print_add(Constants.FLOATING_TASK_SEPARATOR, 
					GUIConstants.COLOR_CODE_YELLOW);
		}
		return floatingStarted;
	}
	
	
	/**
	 * Generates a separator of constant length 
	 * Constants.SEPARATOR_LENGTH
	 * 
	 * @param label
	 * @return String
	 */				
	protected static String generateSeparator(String label){
		int separatorLength = Constants.SEPARATOR_LENGTH - label.length();
		return StringUtils.fillString(separatorLength, Constants.SEPARATOR_CHAR);
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
