package checkers.model;
/**
 * Represents a single square on the board.
 */
class Square {
    private final Position position;
    private Piece piece;

    public Square(Position pos) {
        this.position = pos;
        this.piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece() {
        this.piece = null;
    }


    public boolean isEmpty() {
        return piece == null;
    }
}