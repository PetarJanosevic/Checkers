package checkers.hosting.interfaces;

import checkers.core.gui.Piece;

import java.util.Optional;

/**
 * Allows access to the human player of the checkers game.
 */
public interface HumanPlayerService extends Service {

    int handleMoveClick(int currRow, int currCol, Optional<Piece> selectedPiece);

    String getName();
}
