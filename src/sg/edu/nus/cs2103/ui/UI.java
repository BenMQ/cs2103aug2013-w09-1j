package sg.edu.nus.cs2103.ui;

import java.util.Scanner;

import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.LogicHandler;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;

//@author A0099317U
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
	
}
