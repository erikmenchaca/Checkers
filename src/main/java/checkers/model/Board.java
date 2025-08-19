
package checkers.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final Square[][] squares = new Square[8][8];

    public Board() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new Square(new Position(row, col));
            }
        }
    }

    /**
     * Sets up the board with pieces in their starting positions.
     */
    public void initializeBoard() {
        // Place black pieces
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) { // Place on dark squares
                    setPiece(new Position(row, col), new Piece(PieceColor.BLACK, new Position(row, col)));
                }
            }
        }
        // Place red pieces
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 != 0) { // Place on dark squares
                    setPiece(new Position(row, col), new Piece(PieceColor.RED, new Position(row, col)));
                }
            }
        }
    }

    public Piece getPiece(Position pos) {
        if (!isValidPosition(pos))
            return null;
        return squares[pos.getRow()][pos.getCol()].getPiece();
    }

    public void setPiece(Position pos, Piece piece) {
        if (!isValidPosition(pos))
            return;
        squares[pos.getRow()][pos.getCol()].setPiece(piece);
    }

    public void removePiece(Position pos) {
        if (!isValidPosition(pos))
            return;
        squares[pos.getRow()][pos.getCol()].removePiece();
    }

    public void executeMove(Move move) {
        Piece piece = getPiece(move.getFrom());
        removePiece(move.getFrom());
        setPiece(move.getTo(), piece);
        piece.setPosition(move.getTo());

        if (move.isJump()) {
            int jumpedRow = (move.getFrom().getRow() + move.getTo().getRow()) / 2;
            int jumpedCol = (move.getFrom().getCol() + move.getTo().getCol()) / 2;
            removePiece(new Position(jumpedRow, jumpedCol));
        }
    }

    public boolean isValidPosition(Position pos) {
        return pos.getRow() >= 0 && pos.getRow() < 8 && pos.getCol() >= 0 && pos.getCol() < 8;
    }

    public boolean isEmpty(Position pos) {
        return isValidPosition(pos) && getPiece(pos) == null;
    }

    public List<Piece> getAllPieces(PieceColor color) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(new Position(row, col));
                if (piece != null && piece.getColor() == color) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }
}