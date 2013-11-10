package sg.edu.nus.cs2103.sudo.exceptions;

import sg.edu.nus.cs2103.sudo.Constants;

//@author A0099317U
public class IncompleteCommandException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String DEFAULT_MESSAGE = Constants.MESSAGE_INCOMPLETE_COMMAND;
	private String MESSAGE;

	public IncompleteCommandException() {
		MESSAGE = DEFAULT_MESSAGE;
	}

	public IncompleteCommandException(String message) {
		MESSAGE = message;
	}

	public String getMessage() {
		return MESSAGE;
	}

}