package sg.edu.nus.cs2103.ui;

import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.LogicHandler;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;

public class UI {
	
	public static void main(String[] args) throws Exception {	
		Scanner user = new Scanner( System.in );
		TaskManager manager = TaskManager.getTaskManager();
		LogicHandler logicHandler = LogicHandler.getLogicHandler(manager, user);
		while (true) {
			String userInput = InputParser.readCommand(user);
			logicHandler.executeCommand(userInput);
		}
	}
	
	public static void forcePrint(String message){
		System.out.println(message);
	}

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
	 * @param DateTime
	 */	
	public static void printDaySeparator(DateTime previousDate) {
		DateTimeFormatter datemonthformat = DateTimeFormat.forPattern("EEE d MMM");
		String prefix = UI.addPrefix(previousDate.getDayOfYear());
		System.out.println("\n["+ prefix + previousDate.toString(datemonthformat) + "]====================");
	}	
	
	public static String formatDate(DateTime datetime) {
		return datetime.toString("dd MMMM hh:mm a");
	}	
	
	
}
