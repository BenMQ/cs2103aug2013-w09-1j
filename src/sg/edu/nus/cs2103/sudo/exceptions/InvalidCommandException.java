package sg.edu.nus.cs2103.sudo.exceptions;

import sg.edu.nus.cs2103.sudo.Constants;

public class InvalidCommandException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String DEFAULT_MESSAGE = Constants.MESSAGE_INVALID_COMMAND;
	private String MESSAGE;

	public InvalidCommandException() {
		MESSAGE = DEFAULT_MESSAGE;
	}

	public InvalidCommandException(String message) {
		MESSAGE = message;
	}

	public String getMessage() {
		return MESSAGE;
	}

}