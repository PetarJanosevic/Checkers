package checkers.hosting.interfaces;

import java.util.List;
import java.util.Optional;

import checkers.core.gui.Piece;

/**
 * Allows access to the checkers game board.
 */
public interface FieldManagerService extends Service {

    Optional<Piece> getBoardPiece(int row, int col);

    void movePiece(int toRow, int toCol, int fromRow, int fromCol);

    void removePiece(int rowOfCapturedPiece, int colOfCapturedPiece);

    void setBlackPieces(List<Piece> blackPieces);

    List<Piece> getBlackPieces();

    void setRedPieces(List<Piece> redPieces);

    void updateRedKingPieces();

    List<Piece> getRedKingPieces();

    List<Piece> getRedPieces();

    Piece[][] getBoard();

    void setBoard(Piece[][] expectedBoard);

    String getName();
}
