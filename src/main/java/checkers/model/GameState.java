package checkers.model;

/**
 * Enumeration representing the possible states of a checkers game.
 * Used to track game progress and determine when the game has ended.
 */
public enum GameState {
    /** Game has been created but not yet started */
    NOT_STARTED("Game not started"),
    
    /** Game is currently in progress */
    IN_PROGRESS("Game in progress"),
    
    /** Game has ended with red player winning */
    RED_WINS("Red wins"),
    
    /** Game has ended with black player winning */
    BLACK_WINS("Black wins"),
    
    /** Game has ended in a draw (no winner) */
    DRAW("Draw");
    
    private final String description;
    
    /**
     * Creates a GameState with the specified description.
     * 
     * @param description human-readable description of this state
     */
    GameState(String description) {
        this.description = description;
    }
    
    /**
     * Gets the human-readable description of this game state.
     * 
     * @return the description string
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if the game has ended (either in victory or draw).
     * 
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return this != NOT_STARTED && this != IN_PROGRESS;
    }
    
    /**
     * Checks if the game is currently active (started and not finished).
     * 
     * @return true if the game is in progress, false otherwise
     */
    public boolean isActive() {
        return this == IN_PROGRESS;
    }
    
    /**
     * Checks if this state represents a victory for the specified color.
     * 
     * @param color the color to check for victory
     * @return true if the specified color has won
     */
    public boolean isVictoryFor(PieceColor color) {
        return (color == PieceColor.RED && this == RED_WINS) ||
               (color == PieceColor.BLACK && this == BLACK_WINS);
    }
    
    /**
     * Creates a victory state for the specified color.
     * 
     * @param winner the color that won the game
     * @return RED_WINS if winner is RED, BLACK_WINS if winner is BLACK
     * @throws IllegalArgumentException if winner is null
     */
    public static GameState victoryFor(PieceColor winner) {
        if (winner == null) {
            throw new IllegalArgumentException("Winner cannot be null");
        }
        return winner == PieceColor.RED ? RED_WINS : BLACK_WINS;
    }
    
    /**
     * Gets the winning color if this state represents a victory.
     * 
     * @return the winning PieceColor, or null if this is not a victory state
     */
    public PieceColor getWinner() {
        switch (this) {
            case RED_WINS:
                return PieceColor.RED;
            case BLACK_WINS:
                return PieceColor.BLACK;
            default:
                return null;
        }
    }
    
    /**
     * Returns the description when converting to string.
     * 
     * @return the description of this game state
     */
    @Override
    public String toString() {
        return description;
    }
}