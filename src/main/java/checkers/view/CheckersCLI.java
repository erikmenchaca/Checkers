package checkers.view;

import java.util.List;
import java.util.Scanner;

import checkers.model.Game;
import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PieceColor;
import checkers.model.Position;
import checkers.model.Player;
import checkers.model.Board;
import checkers.model.Piece;

public class CheckersCLI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Player redPlayer = new Player("Player 1 (Red)", PieceColor.RED);
        Player blackPlayer = new Player("Player 2 (Black)", PieceColor.BLACK);
        Game game = new Game(redPlayer, blackPlayer);

        game.startGame(); // Removed because Game does not have startGame()

        while (game.getGameState() == GameState.IN_PROGRESS) {
            printBoard(game.getBoard());
            Player currentPlayer = game.getCurrentPlayer();
            System.out.println("\n" + currentPlayer.getName() + "'s turn.");

            List<Move> possibleMoves = game.getPossibleMoves();
            if (possibleMoves.isEmpty()) {
                System.out.println("No moves available. Game over.");
                break;
            }

            System.out.println("Available moves:");
            for (int i = 0; i < possibleMoves.size(); i++) {
                Move move = possibleMoves.get(i);
                System.out.printf("%d: Move from %s to %s%s%n",
                        i + 1,
                        move.getFrom(),
                        move.getTo(),
                        move.isJump() ? " (Jump!)" : "");
            }

            Move selectedMove = null;
            while (selectedMove == null) {
                System.out.print("Enter the number of the move you want to make: ");
                try {
                    String input = scanner.nextLine();
                    int moveIndex = Integer.parseInt(input) - 1;
                    if (moveIndex >= 0 && moveIndex < possibleMoves.size()) {
                        selectedMove = possibleMoves.get(moveIndex);
                    } else {
                        System.out.println("Invalid number. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            game.makeMove(selectedMove);
            System.out.println("----------------------------------------");
        }

        // Game over
        printBoard(game.getBoard());
        System.out.println("\nGAME OVER!");
        Player winner = game.getWinner();
        if (winner != null) {
            System.out.println("The winner is: " + winner.getName());
        } else {
             System.out.println("The game is a draw.");
        }
        
        scanner.close();
    }

    /**
     * Prints a text-based representation of the checkers board to the console.
     * @param board The board to print.
     */
    public static void printBoard(Board board) {
        System.out.println("\n  0 1 2 3 4 5 6 7 (col)");
        System.out.println(" +-----------------+");
        for (int row = 0; row < 8; row++) {
            System.out.print(row + "| ");
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(new Position(row, col));
                char pieceChar = '.';
                if (piece != null) {
                    if (piece.getColor() == PieceColor.RED) {
                        pieceChar = piece.isKing() ? 'R' : 'r';
                    } else {
                        pieceChar = piece.isKing() ? 'B' : 'b';
                    }
                }
                System.out.print(pieceChar + " ");
            }
            System.out.println("|");
        }
        System.out.println(" +-----------------+");
    }
}

