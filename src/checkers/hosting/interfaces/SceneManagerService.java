package checkers.hosting.interfaces;

import checkers.core.gui.Piece;
import javafx.stage.Stage;

import java.util.List;

/**
 * Enables access to the service that controls the user interface
 * and the scenes of the game with his moves of the two players.
 */
public interface SceneManagerService extends Service {

    void initializeScene(Stage stage);

    void updateBoardUI();

    boolean checkIfHumanWon(List<Piece> redPieces);

    boolean checkIfComputerWon(List<Piece> blackPieces);

    String getName();
}
