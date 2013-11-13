//@author A0105656E
package sg.edu.nus.cs2103.sudo.exceptions;

public class WrongTaskDescriptionStringException extends Exception {
	private static final long serialVersionUID = 1L;

	public WrongTaskDescriptionStringException(String message) {
        super(message);
    }

    public WrongTaskDescriptionStringException(String message, Throwable throwable) {
        super(message, throwable);
    }

}