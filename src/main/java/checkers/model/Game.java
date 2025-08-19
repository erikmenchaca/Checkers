package checkers.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
      private Board board;
    private Player redPlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private GameState gameState;

     /**
     * Constructor to initialize the game with two players.
     * @param red The player for the red pieces.
     * @param black The player for the black pieces.
     */
    public Game(Player red, Player black) {
        if (red.getColor() != PieceColor.RED || black.getColor() != PieceColor.BLACK) {
            throw new IllegalArgumentException("Player colors must be RED and BLACK.");
        }
        this.board = new Board();
        this.redPlayer = red;
        this.blackPlayer = black;
        this.gameState = GameState.NOT_STARTED;
    }

    /**
     * Starts the game, initializes the board, and sets the current player.
     */
    public void startGame() {
        board.initializeBoard();
        currentPlayer = redPlayer; // Red player typically starts
        gameState = GameState.IN_PROGRESS;
    }

    /**
     * Attempts to make a move on the board.
     * @param move The move to be executed.
     * @return true if the move was successful, false otherwise.
     */
    public boolean makeMove(Move move) {
        if (isGameOver() || !isValidMove(move)) {
            return false;
        }

        board.executeMove(move);

        // Promote piece if it reaches the opposite end
        if (shouldPromote(move.getTo())) {
            board.getPiece(move.getTo()).promote();
        }

        // Check for game over condition after the move
        if (isGameOver()) {
            updateGameStateOnWin();
        } else {
            // If the move was a jump and more jumps are possible, the turn doesn't switch
            List<Move> possibleJumps = board.getPiece(move.getTo()).getPossibleJumps(board);
            if (!move.isJump() || possibleJumps.isEmpty()) {
                switchPlayer();
            }
        }
        return true;
    }

    /**
     * Gets all possible moves for the current player.
     * Prioritizes jumps over simple moves.
     * @return A list of valid moves.
     */
    public List<Move> getPossibleMoves() {
        return getPossibleMoves(currentPlayer);
    }
    
    /**
     * Gets all possible moves for a specific player.
     * @param player The player whose moves to find.
     * @return A list of valid moves for the given player.
     */
    public List<Move> getPossibleMoves(Player player) {
        List<Piece> pieces = board.getAllPieces(player.getColor());
        List<Move> possibleJumps = new ArrayList<>();
        List<Move> possibleSimpleMoves = new ArrayList<>();

        for (Piece piece : pieces) {
            possibleJumps.addAll(piece.getPossibleJumps(board));
            possibleSimpleMoves.addAll(piece.getPossibleMoves(board));
        }

        // If jumps are available, the player must take one
        return possibleJumps.isEmpty() ? possibleSimpleMoves : possibleJumps;
    }

    /**
     * Checks if the game has concluded.
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return getPossibleMoves(redPlayer).isEmpty() || getPossibleMoves(blackPlayer).isEmpty();
    }

    /**
     * Determines the winner of the game.
     * @return The winning player, or null if there is a draw or the game is not over.
     */
    public Player getWinner() {
        if (!isGameOver()) {
            return null;
        }
        if (getPossibleMoves(redPlayer).isEmpty()) {
            return blackPlayer;
        }
        if (getPossibleMoves(blackPlayer).isEmpty()) {
            return redPlayer;
        }
        return null; // Should not happen in standard checkers
    }

    // --- Helper and Getter Methods ---

    private boolean isValidMove(Move move) {
        List<Move> validMoves = getPossibleMoves(currentPlayer);
        return validMoves.contains(move);
    }
    
    private void updateGameStateOnWin() {
        Player winner = getWinner();
        if (winner == redPlayer) {
            gameState = GameState.RED_WINS;
        } else if (winner == blackPlayer) {
            gameState = GameState.BLACK_WINS;
        } else {
            gameState = GameState.DRAW;
        }
    }

    private boolean shouldPromote(Position pos) {
        Piece piece = board.getPiece(pos);
        if (piece == null) return false;
        if (piece.getColor() == PieceColor.RED && pos.getRow() == 7) {
            return true;
        }
        if (piece.getColor() == PieceColor.BLACK && pos.getRow() == 0) {
            return true;
        }
        return false;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == redPlayer) ? blackPlayer : redPlayer;
    }



    public Board getBoard() {
        return board;
    }
    
    public GameState getGameState() {
        return gameState;
    }
}