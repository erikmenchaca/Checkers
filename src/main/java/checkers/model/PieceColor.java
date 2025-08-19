package checkers.model;

/**
 * Enumeration representing the two possible colors for checkers pieces.
 * Also provides utility methods for game logic.
 */
public enum PieceColor {
    RED,
    BLACK;
    
    /**
     * Returns the opposite color.
     * 
     * @return BLACK if this is RED, RED if this is BLACK
     */
    public PieceColor opposite() {
        return this == RED ? BLACK : RED;
    }
    
    /**
     * Returns the direction this color moves on the board.
     * In standard checkers, RED moves "up" (decreasing row numbers)
     * and BLACK moves "down" (increasing row numbers).
     * 
     * @return -1 for RED (moving up), +1 for BLACK (moving down)
     */
    public int getForwardDirection() {
        return this == RED ? -1 : 1;
    }
    
    /**
     * Returns the starting row for regular pieces of this color.
     * 
     * @return the row number where pieces of this color start
     */
    public int getStartingRow() {
        return this == RED ? 7 : 0;
    }
    
    /**
     * Returns the promotion row for this color (where pieces become kings).
     * 
     * @return the row number where pieces of this color get promoted
     */
    public int getPromotionRow() {
        return this == RED ? 0 : 7;
    }
    
    /**
     * Returns a user-friendly display name for this color.
     * 
     * @return the color name in proper case
     */
    public String getDisplayName() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}