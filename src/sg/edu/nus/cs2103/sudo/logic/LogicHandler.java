package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;

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

	public static LogicHandler getLogicHandler(TaskManager m, Scanner s) {
		if (logicHandler == null) {
			logicHandler = new LogicHandler(m, s);
		}
		return logicHandler;
	}

	/**
	 * Parses and executes the appropriate manager method based on the user's
	 * input. Via this method, LogicHandler becomes a facade class between UI
	 * and Logic.
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
			switch (userCommand) { // we can refactor this using the Command
									// pattern
			case INVALID:
				System.out.print(Constants.MESSAGE_INVALID_COMMAND);
				return;
			case INCOMPLETE:
				System.out.print(Constants.MESSAGE_INCOMPLETE_COMMAND);
				return;
			case DISPLAY:
				System.out.print(Constants.MESSAGE_DISPLAY);
				this.manager.displayAllTasks();
				return;
			case ALL:
				System.out.print(Constants.MESSAGE_DISPLAY_ALL);
				this.manager.displayAllTasks(true);
				return;
			case FINISH:
				this.manager.markAsComplete(targetId);
				return;
			case UNFINISH:
				this.manager.markAsIncomplete(targetId);
				return;
			case ADD:
				int num_dates = dateTimes.size();
				if (num_dates == 0) { // need to refactor this later
					if (taskDescription == null) {
						System.out.print(Constants.MESSAGE_MISSING_DESCRIPTION);
						return;
					}
					this.manager.addTask(new FloatingTask(taskDescription));
					System.out.printf(Constants.MESSAGE_ADD_FLOATING,
							taskDescription);
				} else if (num_dates == 1) {
					this.manager.addTask(new DeadlineTask(taskDescription,
							dateTimes));
					String endTime = dateTimes.get(0).toString(
							"EEE dd MMMM hh:mm a");
					System.out.printf(Constants.MESSAGE_ADD_DEADLINE,
							taskDescription, endTime);
				} else if (num_dates == 2) {
					this.manager.addTask(new TimedTask(taskDescription,
							dateTimes));
					String startTime = dateTimes.get(0).toString(
							"EEE dd MMMM hh:mm a");
					String endTime = dateTimes.get(1).toString(
							"EEE dd MMMM hh:mm a");
					System.out.printf(Constants.MESSAGE_ADD_TIMED,
							taskDescription, startTime, endTime);
				} else {
					System.out.print(Constants.MESSAGE_INVALID_NUMBER_OF_DATES);
				}
				return;
			case SEARCH:
				System.out.printf(Constants.MESSAGE_SEARCH, taskDescription);
				this.manager.searchAndDisplay(taskDescription);
				return;
			case FREE:
				this.manager.searchForFreeIntervals();
				return;
			case DELETE:
				int numResults = this.manager.delete(taskDescription); // need
																		// to
																		// refactor
																		// this
				if (numResults > 1) {
					System.out.println(Constants.MESSAGE_ENTER_TASK_ID);
					int id = scanner.nextInt();
					this.manager.delete(id);
				}
				return;
			case PASS:
				return;
			case EDIT:
				System.out.printf(Constants.MESSAGE_EDIT, targetId);
				this.manager.editTask(targetId, taskDescription, dateTimes);
				return;
			case HELP:
				System.out.println(Constants.MESSAGE_WELCOME_HELP_PAGE);
				this.help();
				return;
			case UNDO:
				this.manager.undo();
				this.manager.displayAllTasks(true);
				return;
			case REDO:
				this.manager.redo();
				this.manager.displayAllTasks(true);
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
		} catch (Exception e) {
			System.out.printf(e.getMessage());
			// System.out.printf("Fatal error occured!");
		}
	}

	/**
	 * Helps the user get started with using sudo
	 */
	private void help() {
		helpDisplay();
		helpAdd();
		helpEdit();
		helpDelete();
		helpFinish();
		helpUndoRedo();
		helpSearch();
		helpFree();
		helpSchedule();
	}

	private void helpSchedule() {
		System.out.print(Constants.HELP_SCHEDULE);
		System.out.println();
	}

	private void helpFree() {
		System.out.print(Constants.HELP_FREE);
		System.out.println();
	}

	private void helpSearch() {
		System.out.print(Constants.HELP_SEARCH);
		System.out.print(Constants.HELP_SEARCH_ALIASES);
		System.out.println();
	}

	private void helpUndoRedo() {
		System.out.print(Constants.HELP_UNDO);
		System.out.print(Constants.HELP_REDO);
		System.out.println();
	}

	/**
	 * Helps the user get started with using the finish command
	 */
	private void helpFinish() {
		System.out.print(Constants.HELP_FINISH);
		System.out.print(Constants.HELP_FINISH_ALIASES);
		System.out.println();
	}

	/**
	 * Helps the user get started with using the delete command
	 */
	private void helpDelete() {
		System.out.print(Constants.HELP_DELETE);
		System.out.print(Constants.HELP_DELETE_ALIASES);
		System.out.println();
	}

	/**
	 * Helps the user get started with using the edit command
	 */
	private void helpEdit() {
		System.out.print(Constants.HELP_EDIT);
		System.out.print(Constants.HELP_EDIT_ALIASES);
		System.out.println();
	}

	/**
	 * Helps the user get started with using the display command
	 */
	private void helpDisplay() {
		System.out.print(Constants.HELP_DISPLAY);
		System.out.print(Constants.HELP_DISPLAY_ALIASES);
		System.out.println();
	}

	/**
	 * Helps the user get started with using the add command
	 */
	private void helpAdd() {
		System.out.print(Constants.HELP_ADD);
		System.out.print(Constants.HELP_ADD_ALIASES);
		System.out.println();
	}

}
