package sg.edu.nus.cs2103.sudo.exceptions;

public class MissingFileException extends Exception {


    public MissingFileException(String message) {
        super(message);
    }

    public MissingFileException(String message, Throwable throwable) {
        super(message, throwable);
    }

}