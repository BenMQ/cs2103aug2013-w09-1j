package sg.edu.nus.cs2103.sudo.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.exceptions.IncompleteCommandException;
import sg.edu.nus.cs2103.sudo.exceptions.InvalidCommandException;
import sg.edu.nus.cs2103.ui.DisplayUtils;
import sg.edu.nus.cs2103.ui.GUI;
import sg.edu.nus.cs2103.ui.GUIConstants;


//@author A0099317U
/**
 * LogicHandler is a facade class responsible for providing a simple API to
 * handle most of the Task operations of sudo.
 * 
 * @author Yos Riady
 */
public final class LogicHandler {

	private static LogicHandler logicHandler;

	private Scanner scanner;
	private TaskManager manager;

	/**
	 * Constructor to initialize the logicHandler Object.
	 * 
	 * @param manager
	 *            : a TaskManager object to delegate operations to
	 * @param scanner
	 *            : a Scanner object that listens for user input
	 */
	private LogicHandler(TaskManager manager, Scanner scanner) {
		if (manager == null) {
			throw new NullPointerException("TaskManager cannot be null!");
		}
		
		//Uncomment this once Dake finishes the fix at GUI
		//if (scanner == null) {
			//throw new NullPointerException("Scanner cannot be null!");
	//	}		
		
		this.manager = manager;
		this.scanner = scanner;
	}

	/**
	 * Returns a singleton instance of logicHandler. Uses dependency injection
	 * to take in a TaskManager and Scanner object.
	 * 
	 * @param manager
	 *            : a TaskManager object to delegate operations to
	 * @param scanner
	 *            : a Scanner object that listens for user input
	 * @return logicHandler : a logicHandler object
	 */
	public static LogicHandler getLogicHandler(TaskManager manager,
			Scanner scanner) {
		if (logicHandler == null) {
			logicHandler = new LogicHandler(manager, scanner);
		}
		return logicHandler;
	}

	public TaskManager getManager() {
		return this.manager;
	}

	public Scanner getScanner() {
		return this.scanner;
	}

	/**
	 * Executes a TaskManager method based on the user's input.
	 * 
	 * @param userInput
	 *            : string of the user's input
	 */
	public void executeCommand(final String userInput) {
		COMMAND_TYPE userCommand = InputParser.
				parseCommandType(userInput);
		assert (userCommand != null);

		String taskDescription = InputParser.
				parseDescription(userInput);
		int targetId = InputParser.parseId(userInput);
		long duration = InputParser.parseDuration(userInput);
		
		ArrayList<DateTime> dateTimes = InputParser.
				parseDateTime(userInput, userCommand);

		try {
			switch (userCommand) {
			case INVALID:
				throw new InvalidCommandException();
			case INCOMPLETE:
				throw new IncompleteCommandException();
			case DISPLAY:
				this.manager.displayAllTasks();
				return;
			case ALL:
				this.manager.displayAllTasks(true);
				return;
			case FINISH:
				if(targetId == -1){
					this.manager.displayFinishedTasks();
				} else {
					this.manager.markAsComplete(targetId);
					this.manager.displayAllTasks();
				}
				return;
			case UNFINISH:
				this.manager.markAsIncomplete(targetId);
				this.manager.displayAllTasks();
				return;
			case ADD:
				this.manager.add(taskDescription, dateTimes);
				this.manager.displayAllTasks();
				return;
			case DELETE:;
				delegateDelete(taskDescription, targetId);
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
                		targetId, duration, dateTimes); 
                this.manager.displayAllTasks();
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
		} catch (Exception e) {
			GUI.print_add(e.getMessage(), GUIConstants.COLOR_CODE_RED);
		}
	}

	/**
	 * Delegates delete commands to the TaskManager delete API based on the
	 * number of search results.
	 * 
	 * @param taskDescription
	 * @throws IOException
	 */
	public void delegateDelete(String taskDescription, int targetId)
			throws IOException {
		if (taskDescription == null) {
			this.manager.delete(targetId);
		} else {
			this.manager.delete(taskDescription);
		}
	}

}
