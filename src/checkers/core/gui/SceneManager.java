package checkers.core.gui;

import checkers.core.logic.ComputerPlayer;
import checkers.core.logic.MoveValidation;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import checkers.core.gui.Piece.*;
import checkers.hosting.interfaces.*;

import java.util.List;
import java.util.Optional;

/**
 *  Controls the user interface and the scenes of the game with his moves of the two players.
 */
public class SceneManager implements SceneManagerService {

    private GridPane gridPane;
    private Scene scene;
    private Piece selectedPiece;
    private Button button;
    private Text textMessageField;
    private FieldManagerService fieldManagerService;
    private BaseDataService baseDataService;
    private HumanPlayerService humanPlayerService;
    private ComputerPlayerService computerPlayerService;

    public SceneManager(HumanPlayerService humanPlayerService, ComputerPlayerService computerPlayerService,
                        FieldManagerService fieldManagerService, BaseDataService baseDataService) {
        this.humanPlayerService = humanPlayerService;
        this.computerPlayerService = computerPlayerService;
        this.fieldManagerService = fieldManagerService;
        this.baseDataService = baseDataService;
    }

    @Override
    public void initializeScene(Stage stage) {
        scene = createScene();
        stage.setScene(scene);
        stage.show();
        updateBoardUI();
    }

    private Scene createScene() {
        createGridPane();
        VBox vBox = addVBox();
        addButton(vBox);
        addTextMessageFeld(vBox);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(vBox);
        scene = new Scene(borderPane);
        return scene;
    }

    // Creates the different field colours on the board <b>without</b> the game pieces.
    private void createGridPane() {
        gridPane = new GridPane();
        int boardSize = baseDataService.getSquareNumber();
        int squareSize = baseDataService.getSquareSize();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Pane square = new Pane();
                square.setPrefSize(squareSize, squareSize);
                square.setStyle((row + col) % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: green;");
                gridPane.add(square, col, row);
            }
        }
    }

    private VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 0, 0, 0));
        vbox.setSpacing(10);
        return vbox;
    }

    private void addButton(VBox vBox) {
        button = new Button("Spielzug beenden");
        button.setVisible(false);
        button.setOnAction(event -> {
            selectedPiece.getCircle().setStroke(null);
            selectedPiece = null;
            MoveValidation.setHasEaten(false);
            button.setVisible(false);
            textMessageField.setVisible(false);
            updateBoardUI();
            computerPlayerService.computerMove();
            updateBoardUI();
        });
        vBox.getChildren().add(button);
    }

    private void addTextMessageFeld(VBox vBox) {
        textMessageField = new Text("Wenn du keinen Spielzug machen kannst, kannst du mit dem Button hier den Zug abgeben.");
        textMessageField.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textMessageField.setFill(Color.RED);
        textMessageField.setVisible(false);
        vBox.getChildren().add(textMessageField);
    }

    @Override
    public void updateBoardUI() {
        if (checkGameOver()) {
            displayGameOverInformation();
            for (int row = 0; row < baseDataService.getSquareNumber(); row++) {
                for (int col = 0; col < baseDataService.getSquareNumber(); col++) {
                    updateBoardSquareGameOver(col, row);
                }
            }
        } else {
            for (int row = 0; row < baseDataService.getSquareNumber(); row++) {
                for (int col = 0; col < baseDataService.getSquareNumber(); col++) {
                    updateBoardSquare(col, row);
                }
            }
        }
    }

    /**
     * This method checks if there is a {@link Piece} at the given coordinates and draws a circle on
     * the field, displaying the game piece on the board.
     *
     * Additionally, all empty fields are assigned an event listener that triggers the {@link #handleMoveClick} method,
     * allowing the human player to make a move.
     *
     * All game pieces are assigned an event listener that changes the marking of the piece to yellow when clicked,
     * indicating that it has been selected and can be used for a move.
     */
    private void updateBoardSquare(int col, int row) {
        Optional<Piece> piece = fieldManagerService.getBoardPiece(row, col);
        // Find the Pane in the GridPane for the current square.
        Pane square = (Pane) gridPane.getChildren().get(row * baseDataService.getSquareNumber() + col);
        square.requestLayout();
        ObservableList<Node> squareChildren = square.getChildren();
        if (piece.isPresent()) {
            Circle circle = piece.get().getCircle();
            circle.setOnMouseClicked(event ->
                    handlePieceClick(piece.get())
            );
            // Add or update the Circle in the Pane.
            if (squareChildren.isEmpty()) {
                squareChildren.add(circle);
            } else {
                squareChildren.set(0, circle);
            }
        } else {
            // If there is no piece on the square, remove any Circle from the Pane.
            squareChildren.clear();
            final int currRow = row;
            final int currCol = col;
            square.setOnMouseClicked(event ->
                    handleMoveClick(currRow, currCol)
            );
        }
    }

    /**
     * Like {@link #updateBoardSquare}, but without the event listeners,
     * because when the game is over, no piece should be selectable or movable.
     */
    private void updateBoardSquareGameOver(int col, int row) {
        Optional<Piece> piece = fieldManagerService.getBoardPiece(row, col);
        Pane square = (Pane) gridPane.getChildren().get(row * baseDataService.getSquareNumber() + col);
        square.requestLayout();
        ObservableList<Node> squareChildren = square.getChildren();
        if (piece.isPresent()) {
            Circle circle = piece.get().getCircle();
            circle.setOnMouseClicked(null);
            if (squareChildren.isEmpty()) {
                squareChildren.add(circle);
            } else {
                squareChildren.set(0, circle);
            }
        } else {
            squareChildren.clear();
        }
    }

    /**
     * Case 1: On the first jump, the user has eaten. This leads to the user being able to make another move.
     * Case 2: Nothing eaten on the first jump. A regular move has been made and it's the {@link ComputerPlayer}'s turn now.
     * Case default/0: No move has been made. Method aborts and player should click on another field. This case is
     * not noticeable to the user. Nothing will happen on the GUI side and the user can confidently select another field.
     */
    private void handleMoveClick(int currRow, int currCol) {
        int move = humanPlayerService.handleMoveClick(currRow, currCol, Optional.ofNullable(selectedPiece));
        switch (move) {
            case 1:
                button.setVisible(true);
                textMessageField.setVisible(true);
                updateBoardUI();
                break;
            case 2:
                // SelectedPiece can never be null here, because if it is null, we would get 0 from the
                // handleMoveClick method which would lead to default case in here and not case 2.
                selectedPiece.getCircle().setStroke(null);
                selectedPiece = null;
                updateBoardUI();
                button.setVisible(false);
                textMessageField.setVisible(false);
                computerPlayerService.computerMove();
                updateBoardUI();
                break;
            default:
        }
    }

    private void handlePieceClick(Piece piece) {
        // Check if the clicked piece belongs to the human player.
        if (piece != null && piece.getColor() == PieceColor.BLACK && !MoveValidation.isHasEaten()) {
            // Unselect any previously selected piece.
            if (selectedPiece != null) {
                selectedPiece.getCircle().setStroke(null);
            }
            // Select the new piece.
            selectedPiece = piece;
            selectedPiece.getCircle().setStroke(Color.YELLOW);
        }
    }

    public boolean checkGameOver() {
        boolean humanWon = checkIfHumanWon(fieldManagerService.getRedPieces());
        boolean computerWon = checkIfComputerWon(fieldManagerService.getBlackPieces());
        return humanWon || computerWon;
    }

    @Override
    public boolean checkIfHumanWon(List<Piece> redPieces) {
        return redPieces.isEmpty();
    }

    @Override
    public boolean checkIfComputerWon(List<Piece> blackPieces) {
        return blackPieces.isEmpty();
    }

    private void displayGameOverInformation() {
        boolean humanWon = checkIfHumanWon(fieldManagerService.getRedPieces());
        boolean computerWon = checkIfComputerWon(fieldManagerService.getBlackPieces());
        if (humanWon) {
            this.textMessageField.setText("GAME OVER....YOU WON!!");
            this.textMessageField.setVisible(true);
            this.button.setVisible(false);
        } else if (computerWon) {
            this.textMessageField.setText("GAME OVER....COMPUTER WON!!");
            this.textMessageField.setVisible(true);
        }
    }

    @Override
    public String getName() {
        return SceneManagerService.class.getSimpleName();
    }
}
