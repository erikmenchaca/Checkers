package checkers.model;

/**
 * Custom exception for the checkers game.
 * Thrown when invalid moves or game operations are attempted.
 */
public class CheckersException extends Exception {
    
    /**
     * Creates a new CheckersException with the specified message.
     * 
     * @param message the error message
     */
    public CheckersException(String message) {
        super(message);
    }
    
    /**
     * Creates a new CheckersException with the specified message and cause.
     * 
     * @param message the error message
     * @param cause the underlying cause of this exception
     */
    public CheckersException(String message, Throwable cause) {
        super(message, cause);
    }
}