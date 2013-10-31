package sg.edu.nus.cs2103.sudo.exceptions;

public class MissingFileException extends Exception {
	private static final long serialVersionUID = 1L;

	public MissingFileException(String message) {
        super(message);
    }

    public MissingFileException(String message, Throwable throwable) {
        super(message, throwable);
    }

}