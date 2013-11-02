package sg.edu.nus.cs2103.sudo.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.ui.UI;

/**
 * LogicHandler is a facade class responsible for providing
 * a simple API for handling most of the operations of sudo. 
 * 
 * @author Yos Riady 
 */
public final class LogicHandler {

	private static LogicHandler logicHandler;
	
	private Scanner scanner;
	private TaskManager manager;

	/** logicHandler Constructor to initialize the logicHandler Object.
	 * @param manager
	 *            : a TaskManager object to delegate operations to
	 * @param scanner
	 *            : a Scanner object that listens for user input
	 */
	private LogicHandler(TaskManager manager, Scanner scanner) {
		if (manager == null) {
			throw new NullPointerException(
					"TaskManager cannot be null!");
		}
		this.manager = manager;
		this.scanner = scanner;
	}
	
	/**
	 * Returns a singleton instance of a logicHandler object.
	 * Uses dependency injection to take in a TaskManager 
	 * and Scanner object.
	 * @param manager
	 *            : a TaskManager object to delegate operations to
	 * @param scanner
	 *            : a Scanner object that listens for user input
	 * @return logicHandler
	 * 			  : a logicHandler object
	 */
	public static LogicHandler getLogicHandler(TaskManager manager, Scanner scanner) {
		if (logicHandler == null) {
			logicHandler = new LogicHandler(manager, scanner);
		}
		return logicHandler;
	}
	
	public TaskManager getManager(){
		return this.manager;
	}
	
	public Scanner getScanner(){
		return this.scanner;
	}	

	/**
	 * Parses and executes the appropriate manager method based 
	 * on the user's input. LogicHandler becomes a 
	 * facade class between UI and Logic components.
	 * @param userInput
	 *           : string of the user's input
	 */
	public void executeCommand(final String userInput) {
		COMMAND_TYPE userCommand = InputParser.
				parseCommandType(userInput);
		assert (userCommand != null);

		String taskDescription = InputParser.
				parseDescription(userInput);
		int targetId = InputParser.parseId(userInput);
		ArrayList<DateTime> dateTimes = InputParser.
				parseDateTime(userInput);

		try {
			switch (userCommand) {
			case INVALID:
				System.out.print(
						Constants.MESSAGE_INVALID_COMMAND);
				return;
			case INCOMPLETE:
				System.out.print(
						Constants.MESSAGE_INCOMPLETE_COMMAND);
				return;
			case DISPLAY:
				this.manager.displayAllTasks();
				return;
			case ALL:
				this.manager.displayAllTasks(true);
				return;
			case FINISH:
				this.manager.markAsComplete(targetId);
				this.manager.displayAllTasks();
				return;
			case UNFINISH:
				this.manager.markAsIncomplete(targetId);
				this.manager.displayAllTasks();
				return;
			case ADD:
				delegateAddTasks(taskDescription, dateTimes);
				return;
			case DELETE:
				delegateDelete(taskDescription);
				this.manager.displayAllTasks();
				return;
			case EDIT:
				this.manager.editTask(targetId, 
						taskDescription, dateTimes);
				this.manager.displayAllTasks();
				return;
			case SEARCH:
				this.manager.searchAndDisplay(taskDescription);
				return;
			case FREE:
	            this.manager.searchForFreeIntervals(
	            		dateTimes);   
			    return;
			case SCHEDULE:
                this.manager.scheduleTask(
                		taskDescription, dateTimes);   
                return;				
			case UNDO:
				this.manager.undo();
				this.manager.displayAllTasks();
				return;
			case REDO:
				this.manager.redo();
				this.manager.displayAllTasks();
				return;
			case HELP:
				this.manager.help(taskDescription);
				return;
			case DESTROY:
				this.manager.relaunch();
				return;
			case PASS:
				return;
			case EXIT:
				this.manager.saveTasks();
				System.exit(0);
				return;
			default:
				assert false; //Invalid commands are caught.
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			System.out.printf(e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method delegates delete commands to the right
	 * TaskManager delete APIs based on the number of
	 * search results.
	 * @param taskDescription
	 * @throws IOException
	 */
	public void delegateDelete(String taskDescription) throws IOException {
		int numResults = this.manager.delete(taskDescription);
		if (numResults > 1) {
			System.out.println(Constants.MESSAGE_ENTER_TASK_ID);
			int id = scanner.nextInt();
			this.manager.delete(id);
		}
	}

	/**
	 * This method delegates add commands to the right
	 * TaskManager add API based on the number of date arguments.
	 * @param taskDescription	The task description
	 * @param dateTimes			A list of DateTimes
	 * @throws Exception
	 */
	public void delegateAddTasks(String taskDescription,
		ArrayList<DateTime> dateTimes) throws Exception {
		
		if (taskDescription == null) {
			System.out.print(Constants.MESSAGE_MISSING_DESCRIPTION);
			return;
		}		
		
		assert taskDescription != null;
		int numOfDates = dateTimes.size();
		if (numOfDates == 0) {
				this.manager.addTask(new FloatingTask(taskDescription));
				System.out.printf(Constants.MESSAGE_ADD_FLOATING, 
						taskDescription);
		} else if (numOfDates == 1) {
				this.manager.addTask(
						new DeadlineTask(taskDescription, dateTimes));
				System.out.printf(
						Constants.MESSAGE_ADD_DEADLINE, taskDescription, 
								  UI.formatDate(dateTimes.get(0)));
		} else if (numOfDates == 2) {
				this.manager.addTask(new TimedTask(taskDescription, dateTimes));
				System.out.printf(Constants.MESSAGE_ADD_TIMED, taskDescription,
						UI.formatDate(dateTimes.get(0)), 
						UI.formatDate(dateTimes.get(1)));
		} else {
			System.out.print(Constants.MESSAGE_INVALID_NUMBER_OF_DATES);
		}
		this.manager.displayAllTasks();
	}
	
}
