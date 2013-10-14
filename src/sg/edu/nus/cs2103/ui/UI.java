package sg.edu.nus.cs2103.ui;

import java.util.Scanner;

import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;

public class UI {
	
	public static void main(String[] args) {
		
		Scanner user = new Scanner( System.in );
		TaskManager manager = TaskManager.getTaskManager();
		InputParser parser = InputParser.getInputParser(manager);
		while (true) {
			String userInput = parser.readCommand(user); //listen to scanner
			parser.executeCommand(userInput);
		}

	}

}
