package sg.edu.nus.cs2103.sudo.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.ui.UI;

public class LogicHandler {

	private static Scanner scanner;
	private static LogicHandler logicHandler;
	private TaskManager manager;

	private LogicHandler(TaskManager m, Scanner s) {
		if (m == null) {
			throw new NullPointerException("TaskManager cannot be null!");
		}
		manager = m;
		scanner = s;
	}

	/**
	 * Returns a singleton instance of a logicHandler object.
	 * Uses dependency injection to take in a TaskManager and Scanner object.
	 * 
	 * @param TaskManager, Scanner
	 * @return logicHandler
	 */
	public static LogicHandler getLogicHandler(TaskManager m, Scanner s) {
		if (logicHandler == null) {
			logicHandler = new LogicHandler(m, s);
		}
		return logicHandler;
	}

	/**
	 * Parses and executes the appropriate manager method based on the user's
	 * input. LogicHandler becomes a facade class between UI and Logic components.
	 * 
	 * @param userInput
	 *            string of the user's input
	 * @return executes the appropriate high level command
	 */
	public void executeCommand(String userInput) {
		COMMAND_TYPE userCommand = InputParser.parseCommandType(userInput);
		assert (userCommand != null);

		String taskDescription = InputParser.parseDescription(userInput);
		int targetId = InputParser.parseId(userInput);
		ArrayList<DateTime> dateTimes = InputParser.parseDateTime(userInput);

		try {
			switch (userCommand) {
			case INVALID:
				System.out.print(Constants.MESSAGE_INVALID_COMMAND);
				return;
			case INCOMPLETE:
				System.out.print(Constants.MESSAGE_INCOMPLETE_COMMAND);
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
				this.manager.displayAllTasks();
				return;
			case DELETE:
				delegateDelete(taskDescription);
				this.manager.displayAllTasks();
				return;
			case EDIT:
				this.manager.editTask(targetId, taskDescription, dateTimes);
				this.manager.displayAllTasks();
				return;
			case SEARCH:
				this.manager.searchAndDisplay(taskDescription);
				return;
			case FREE:
	            this.manager.searchForFreeIntervals(dateTimes);   
			    return;
			case SCHEDULE:
                this.manager.scheduleTask(taskDescription, dateTimes);   
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
				assert false; // Unreachable code. Invalid commands must be
								// caught.
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			System.out.printf(e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delegateDelete(String taskDescription) throws IOException {
		int numResults = this.manager.delete(taskDescription); 
		if (numResults > 1) {
			System.out.println(Constants.MESSAGE_ENTER_TASK_ID);
			System.out.println(scanner); //scanner is null? why?
			
			int id = scanner.nextInt();
			this.manager.delete(id);
		}
	}

	public void delegateAddTasks(String taskDescription,
			ArrayList<DateTime> dateTimes) throws Exception {
		int numOfDates = dateTimes.size();
		if(numOfDates == 0){
				if(taskDescription == null){
					System.out.print(Constants.MESSAGE_MISSING_DESCRIPTION);
					return;
				}
				this.manager.addTask(new FloatingTask(taskDescription));
				System.out.printf(Constants.MESSAGE_ADD_FLOATING, taskDescription);
		} else if(numOfDates == 1){
				this.manager.addTask(new DeadlineTask(taskDescription, dateTimes));
				System.out.printf(Constants.MESSAGE_ADD_DEADLINE, taskDescription, UI.formatDate(dateTimes.get(0)));
		} else if(numOfDates == 2){
				this.manager.addTask(new TimedTask(taskDescription, dateTimes));
				System.out.printf(Constants.MESSAGE_ADD_TIMED, taskDescription,
						UI.formatDate(dateTimes.get(0)), UI.formatDate(dateTimes.get(1)));
		} else {
			System.out.print(Constants.MESSAGE_INVALID_NUMBER_OF_DATES);
		}
	}
	
}