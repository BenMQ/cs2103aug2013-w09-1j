package sg.edu.nus.cs2103.ui;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import sg.edu.nus.cs2103.sudo.logic.Task;

//@author A0099317U
public class DisplayUtils {
	
	private static final int SEPARATOR_LENGTH = 40;
	private static final char SEPARATOR_CHAR = '=';

	/**
	 * Returns a pretty string representation of a task.
	 * @param Task
	 */
	public static String prettyPrint(Task task){
		//Reference: http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html
		DateTimeFormatter onlytimeformat = DateTimeFormat.forPattern("ha");
		if(task.getEndTime() !=null && task.getEndTime().getMinuteOfHour() > 0){
			onlytimeformat = DateTimeFormat.forPattern("h:mma");
		} 
		
		if(task.getStartTime() == null && task.getEndTime() == null){
			return task.getId() + ". " + task.getDescription();
		} else if (task.getStartTime() == null){
			return task.getId() + ". [by "+task.getEndTime().toString(onlytimeformat)+"] "+ task.getDescription();
		} else {
			if(task.getStartTime().getDayOfYear() == task.getEndTime().getDayOfYear()){
				return task.getId() + ". ["+task.getStartTime().toString(onlytimeformat)+" - "+task.getEndTime().toString(onlytimeformat)+"] "+ task.getDescription();
			}
			return task.getId() + ". ["+task.getStartTime().toString(onlytimeformat)+" - "+task.getEndTime().toString(onlytimeformat)+"] "+ task.getDescription();
		}
	}	
	
	/**
	 * Adds contextual prefixes to day separators such as Today, Overdue, 
	 * and so on based on current day
	 * @param int
	 */	
	public static String addPrefix(int previousDay) {
		String prefix = "";
		if (previousDay < DateTime.now().getDayOfYear()){
			prefix = "Overdue: ";
		} else if (previousDay == DateTime.now().getDayOfYear()){
			prefix = "Today: ";
		} else if (previousDay == (DateTime.now().getDayOfYear()+1)){
			prefix = "Tomorrow: ";
		}
		return prefix;
	}	
	
	/**
	 * Adds day-level separators between groups of tasks.
	 * TODO: need to have of length
	 * @param DateTime
	 */	
	public static void printDaySeparator(DateTime previousDate) {
		DateTimeFormatter datemonthformat = DateTimeFormat.forPattern(
				"EEE d MMM");
		String prefix = addPrefix(previousDate.getDayOfYear());
		String label = prefix + previousDate.toString(datemonthformat);
		int separatorLength = SEPARATOR_LENGTH - label.length();
		MainFrame.print_add("\n["+ label + "]" + fillString(
				separatorLength, SEPARATOR_CHAR), 3);
	}	
	
	/**
	 * Helper method to generate a string of characters
	 * of specified length.
	 * @param length
	 * @param charToFill
	 */	
	protected static String fillString(int length, char charToFill) {
		  if (length > 0) {
		    char[] array = new char[length];
		    Arrays.fill(array, charToFill);
		    return new String(array);
		  }
		  return "";
	}
	
	public static String formatDate(DateTime datetime) {
		return datetime.toString("dd MMMM hh:mm a");
	}	
	
	
}
