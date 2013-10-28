package sg.edu.nus.cs2103.sudo.exceptions;

public class NoHistoryException extends Exception {

    public NoHistoryException(String message) {
        super(message);
    }

    public NoHistoryException(String message, Throwable throwable) {
        super(message, throwable);
    }

}