package checkers.model;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    private final PieceColor color;
    private boolean isKing;
    private Position position;

    public Piece(PieceColor color, Position pos) {
        this.color = color;
        this.position = pos;
        this.isKing = false;
    }

    /**
     * Gets possible non-jump moves for this piece.
     */
    public List<Move> getPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int currentRow = position.getRow();
        int currentCol = position.getCol();
        int[] rowDirections = isKing ? new int[]{-1, 1} : (color == PieceColor.RED ? new int[]{-1} : new int[]{1});

        for (int rowDir : rowDirections) {
            for (int colDir : new int[]{-1, 1}) {
                Position to = new Position(currentRow + rowDir, currentCol + colDir);
                if (board.isValidPosition(to) && board.isEmpty(to)) {
                    moves.add(new Move(position, to, false));
                }
            }
        }
        return moves;
    }

    /**
     * Gets possible jump moves for this piece.
     */
    public List<Move> getPossibleJumps(Board board) {
        List<Move> jumps = new ArrayList<>();
        int currentRow = position.getRow();
        int currentCol = position.getCol();
        int[] rowDirections = isKing ? new int[]{-1, 1} : (color == PieceColor.RED ? new int[]{-1} : new int[]{1});

        for (int rowDir : rowDirections) {
            for (int colDir : new int[]{-1, 1}) {
                Position opponentPos = new Position(currentRow + rowDir, currentCol + colDir);
                Position to = new Position(currentRow + 2 * rowDir, currentCol + 2 * colDir);

                if (board.isValidPosition(to) && board.isEmpty(to)) {
                    Piece opponentPiece = board.getPiece(opponentPos);
                    if (opponentPiece != null && opponentPiece.getColor() != this.color) {
                        jumps.add(new Move(position, to, true));
                    }
                }
            }
        }
        return jumps;
    }

    public PieceColor getColor() {
        return color;
    }

    public boolean isKing() {
        return isKing;
    }

    public void promote() {
        this.isKing = true;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position pos) {
        this.position = pos;
    }
}