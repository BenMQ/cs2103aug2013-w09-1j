package sg.edu.nus.cs2103.sudo;

import java.util.Scanner;

import sg.edu.nus.cs2103.sudo.logic.InputParser;

public class Executor {

	/**
	 * The main Executor class
	 */
	
	public static void main(String[] args) {
		
		Scanner user = new Scanner( System.in );
		InputParser parser = new InputParser();
		while(true){
			String userInput = InputParser.readCommand(user);
			parser.executeCommand(userInput);
		}

		
	}

}
