package checkers.model;

import java.util.Objects;

/**
 * Represents a move in a checkers game.
 * A move consists of moving a piece from one position to another,
 * and can be either a regular move or a jump (capture).
 */
/**
 * Represents a move from one position to another.
 */
public class Move {
    private final Position from;
    private final Position to;
    private final boolean isJump;

    public Move(Position from, Position to, boolean isJump) {
        this.from = from;
        this.to = to;
        this.isJump = isJump;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public boolean isJump() {
        return isJump;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return isJump == move.isJump &&
               Objects.equals(from, move.from) &&
               Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, isJump);
    }

    @Override
    public String toString() {
        return "Move{" + "from=" + from + ", to=" + to + ", isJump=" + isJump + '}';
    }
}