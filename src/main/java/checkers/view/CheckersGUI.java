package checkers.view;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import checkers.model.Game;
import checkers.model.Move;
import checkers.model.Piece;
import checkers.model.PieceColor;
import checkers.model.Player;
import checkers.model.Position;

/**
 * An elegant JavaFX GUI for the Checkers game with animations and improved styling.
 */
public class CheckersGUI extends Application {

    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private Game game;
    private GridPane boardGrid;
    private Label statusLabel;
    private StackPane pieceLayer; // Layer for animating pieces

    private Piece selectedPiece = null;
    private Node selectedPieceNode = null;
    private List<Move> possibleMoves = new ArrayList<>();
    private List<Node> highlightNodes = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Checkers");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #333;");
        root.setPadding(new Insets(20));

        statusLabel = new Label("Welcome to Checkers!");
        statusLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        statusLabel.setTextFill(Color.WHITE);
        BorderPane.setAlignment(statusLabel, Pos.CENTER);
        statusLabel.setPadding(new Insets(10, 0, 20, 0));

        // Create a StackPane to layer the board and the animating pieces
        StackPane gamePane = new StackPane();
        boardGrid = createBoardGrid();
        pieceLayer = new StackPane();
        pieceLayer.setMouseTransparent(true); // Clicks go through to the board
        gamePane.getChildren().addAll(boardGrid, pieceLayer);

        root.setTop(statusLabel);
        root.setCenter(gamePane);

        Player redPlayer = new Player("Player 1 (Red)", PieceColor.RED);
        Player blackPlayer = new Player("Player 2 (Black)", PieceColor.BLACK);
        game = new Game(redPlayer, blackPlayer);
        game.startGame();

        drawPieces();
        updateStatus();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Creates the grid of squares for the board.
     */
    private GridPane createBoardGrid() {
        GridPane grid = new GridPane();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle square = new Rectangle(TILE_SIZE, TILE_SIZE);
                square.setFill((row + col) % 2 == 0 ? Color.web("#EADCC7") : Color.web("#A98A6F"));
                
                StackPane squarePane = new StackPane(square);
                grid.add(squarePane, col, row);

                final int finalRow = row;
                final int finalCol = col;
                squarePane.setOnMouseClicked(event -> onSquareClicked(new Position(finalRow, finalCol)));
            }
        }
        return grid;
    }

    /**
     * Draws all pieces on their initial squares.
     */
    private void drawPieces() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = game.getBoard().getPiece(new Position(row, col));
                if (piece != null) {
                    StackPane squarePane = getSquarePane(new Position(row, col));
                    Node pieceNode = createPieceNode(piece);
                    squarePane.getChildren().add(pieceNode);
                }
            }
        }
    }

    /**
     * Creates a visual node for a piece.
     * @param piece The piece to create a node for.
     * @return A Node representing the piece.
     */
    private Node createPieceNode(Piece piece) {
        Circle pieceCircle = new Circle(TILE_SIZE * 0.38);
        
        Color baseColor = (piece.getColor() == PieceColor.RED) ? Color.web("#C40003") : Color.web("#1E1E1E");
        Color highlightColor = (piece.getColor() == PieceColor.RED) ? Color.web("#FF4C4C") : Color.web("#555555");

        RadialGradient gradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, highlightColor), new Stop(1.0, baseColor));
        
        pieceCircle.setFill(gradient);
        pieceCircle.setStroke(Color.BLACK);
        pieceCircle.setStrokeWidth(1.5);

        StackPane piecePane = new StackPane(pieceCircle);

        if (piece.isKing()) {
            Circle kingIndicator = new Circle(TILE_SIZE * 0.12);
            kingIndicator.setFill(Color.GOLD);
            kingIndicator.setStroke(Color.BLACK);
            kingIndicator.setStrokeWidth(1);
            piecePane.getChildren().add(kingIndicator);
        }
        return piecePane;
    }

    /**
     * Handles the logic when a square on the board is clicked.
     * @param pos The position of the clicked square.
     */
    private void onSquareClicked(Position pos) {
        if (game.isGameOver()) return;

        // Check if this click is to execute a move
        for (Move move : possibleMoves) {
            if (move.getTo().equals(pos)) {
                executeMove(move);
                return;
            }
        }
        
        // Otherwise, it's a piece selection click
        clearHighlights();
        Piece clickedPiece = game.getBoard().getPiece(pos);
        if (clickedPiece != null && clickedPiece.getColor() == game.getCurrentPlayer().getColor()) {
            selectedPiece = clickedPiece;
            selectedPieceNode = getSquarePane(pos).getChildren().get(1); // Assuming piece is the second child
            
            // Apply selection effect
            selectedPieceNode.setEffect(new DropShadow(20, Color.GOLD));
            
            possibleMoves = game.getPossibleMoves();
            possibleMoves.removeIf(move -> !move.getFrom().equals(selectedPiece.getPosition()));
            
            highlightPossibleMoves();
        } else {
            selectedPiece = null;
            selectedPieceNode = null;
            possibleMoves.clear();
        }
    }

    /**
     * Executes a move, including animation.
     * @param move The move to perform.
     */
    private void executeMove(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();

        StackPane fromPane = getSquarePane(from);
        Node pieceNode = fromPane.getChildren().get(fromPane.getChildren().size() - 1);

        // Animation setup
        TranslateTransition tt = new TranslateTransition(Duration.millis(400), pieceNode);
        tt.setToX((to.getCol() - from.getCol()) * TILE_SIZE);
        tt.setToY((to.getRow() - from.getRow()) * TILE_SIZE);

        tt.setOnFinished(event -> {
            // Update model
            game.makeMove(move);
            
            // Redraw the entire board state without animations
            redrawBoardState();
            updateStatus();
        });

        clearHighlights();
        tt.play();
    }

    /**
     * Redraws the board based on the current game model state.
     */
    private void redrawBoardState() {
        // Clear all visual pieces
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                getSquarePane(new Position(r, c)).getChildren().removeIf(node -> !(node instanceof Rectangle));
            }
        }
        // Redraw pieces in their new positions
        drawPieces();
    }
    
    /**
     * Highlights the squares that are valid destinations for the selected piece.
     */
    private void highlightPossibleMoves() {
        for (Move move : possibleMoves) {
            StackPane pane = getSquarePane(move.getTo());
            Circle highlight = new Circle(TILE_SIZE * 0.2, Color.rgb(255, 255, 0, 0.5));
            highlight.setMouseTransparent(true);
            pane.getChildren().add(highlight);
            highlightNodes.add(highlight);
        }
    }

    /**
     * Removes all highlights from the board and selection effects.
     */
    private void clearHighlights() {
        if (selectedPieceNode != null) {
            selectedPieceNode.setEffect(null);
            selectedPieceNode = null;
        }
        for (Node highlight : highlightNodes) {
            ((StackPane) highlight.getParent()).getChildren().remove(highlight);
        }
        highlightNodes.clear();
    }

    /**
     * Updates the status label with the current game state.
     */
    private void updateStatus() {
        if (game.isGameOver()) {
            Player winner = game.getWinner();
            statusLabel.setText("Game Over! Winner: " + (winner != null ? winner.getName() : "Draw"));
        } else {
            statusLabel.setText(game.getCurrentPlayer().getName() + "'s turn.");
        }
    }

    /**
     * Helper to get the StackPane for a given board position.
     */
    private StackPane getSquarePane(Position pos) {
        return (StackPane) boardGrid.getChildren().get(pos.getRow() * BOARD_SIZE + pos.getCol());
    }
}
