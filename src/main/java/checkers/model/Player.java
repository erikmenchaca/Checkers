package checkers.model;
/**
 * Represents a player in the game.
 */
public class Player {
    private final String name;
    private final PieceColor color;

    public Player(String name, PieceColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public PieceColor getColor() {
        return color;
    }
}