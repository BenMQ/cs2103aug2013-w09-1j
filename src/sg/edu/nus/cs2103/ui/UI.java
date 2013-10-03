package sg.edu.nus.cs2103.ui;

import java.util.Scanner;

import sg.edu.nus.cs2103.sudo.logic.InputParser;

public class UI {
	
	public static void main(String[] args) {
		
		Scanner user = new Scanner( System.in );
		InputParser parser = new InputParser();
		while (true) {
			String userInput = InputParser.readCommand(user); //listen to scanner
			parser.executeCommand(userInput);
		}

	}

}
