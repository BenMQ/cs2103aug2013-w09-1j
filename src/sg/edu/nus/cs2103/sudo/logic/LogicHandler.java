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
		if (m == null){
			throw new NullPointerException("TaskManager cannot be null!");
		}
		manager = m;
		scanner = s;
	}
	
	public static LogicHandler getLogicHandler(TaskManager m, Scanner s){
		if(logicHandler == null){
			logicHandler = new LogicHandler(m,s );
		}
		return logicHandler;
	}
	
	/**
	 * Parses and executes the appropriate manager method based on the user's input.
	 * Via this method, InputParser becomes a facade class between
	 * UI and Logic.
	 * 
	 * @param userInput 	string of the user's input
	 * @return executes the appropriate high level command
	 */
	public void executeCommand(String userInput){
		COMMAND_TYPE userCommand = InputParser.parseCommandType(userInput);
		assert(userCommand != null);
		
		String taskDescription = InputParser.parseDescription(userInput);
		int targetId = InputParser.parseId(userInput);
		ArrayList<DateTime> dateTimes = InputParser.parseDateTime(userInput);
		
		try {
			switch(userCommand){ //we can refactor this using the Command pattern
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
				if(num_dates == 0){ //need to refactor this later
						if(taskDescription == null){
							System.out.print(Constants.MESSAGE_MISSING_DESCRIPTION);
							return;
						}
						this.manager.addTask(new FloatingTask(taskDescription));
						System.out.printf(Constants.MESSAGE_ADD_FLOATING, taskDescription);
				} else if(num_dates == 1){
						this.manager.addTask(new DeadlineTask(taskDescription, dateTimes));
						System.out.printf(Constants.MESSAGE_ADD_DEADLINE, taskDescription);
				} else if(num_dates == 2){
						this.manager.addTask(new TimedTask(taskDescription, dateTimes));
						System.out.printf(Constants.MESSAGE_ADD_TIMED, taskDescription);						
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
				int numResults = this.manager.delete(taskDescription); //need to refactor this
				if (numResults > 1 ) {
					System.out.println(Constants.MESSAGE_ENTER_TASK_ID);
					int id = scanner.nextInt();
					this.manager.delete(id);
				}
				return;
			case EDIT:
			    System.out.printf(Constants.MESSAGE_EDIT, targetId);
			    this.manager.editTask(targetId, taskDescription, dateTimes);
			    return;
			case HELP:
				 this.manager.undo();
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
				assert false; //Unreachable code. Invalid commands must be caught.
				return;
			}
		} catch (Exception e) {
			System.out.printf(e.getMessage());
		}
	}	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
