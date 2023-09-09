package checkers.core.logic;

import checkers.core.gui.Piece;
import checkers.core.gui.Piece.PieceColor;
import checkers.hosting.interfaces.*;

import java.util.Optional;

/**
 * Checks the moves of both types of players {@link HumanPlayer} and {@link ComputerPlayer}.
 */
public class MoveValidation {

    private static boolean hasEaten = false;

    // No one should be able to create an instance of this class, because this is only meant to validate moves.
    private MoveValidation() {
    }

    public static boolean isValidMove(Piece piece, int endRow, int endCol, FieldManagerService fieldManagerService) {
        int startRow = piece.getRow();
        int startCol = piece.getCol();

        if (!isMoveDiagonal(endRow, endCol, startRow, startCol)) {
            return false;
        }

        if (!isDestinationSquareEmpty(endRow, endCol, fieldManagerService)) {
            return false;
        }

        if (!isMoveInCorrectDirection(piece, endRow, startRow)) {
            return false;
        }

        if (isMoveTooBig(piece, endRow, endCol, startRow, startCol, fieldManagerService)) {
            return false;
        }
        // If a player has already eaten a piece, the next step has to be another eating step. Any other kind of step will return false.
        if (isHasEaten() && !isCaptureValid(piece, endRow, endCol, startRow, startCol, fieldManagerService)) {
            return false;
        }

        return true;
    }

    public static boolean isMoveDiagonal(int endRow, int endCol, int startRow, int startCol) {
        return Math.abs(endRow - startRow) == Math.abs(endCol - startCol);
    }

    public static boolean isDestinationSquareEmpty(int row, int col, FieldManagerService fieldManagerService) {
        return !fieldManagerService.getBoardPiece(row, col).isPresent();
    }

    public static boolean isMoveInCorrectDirection(Piece piece, int endRow, int startRow) {
        if (!piece.isKing()) {
            boolean checkForComputer = piece.getColor() == PieceColor.RED && endRow > startRow;
            boolean checkForHuman = piece.getColor() == PieceColor.BLACK && endRow < startRow;
            return checkForComputer || checkForHuman;
        }
        return true;
    }

    public static boolean isMoveTooBig(Piece piece, int endRow, int endCol, int startRow, int startCol, FieldManagerService fieldManagerService) {
        int rowDifference = Math.abs(endRow - startRow);
        int colDifference = Math.abs(endCol - startCol);
        if (Math.abs(rowDifference) > 1 || Math.abs(colDifference) > 1) {
            return !isCaptureValid(piece, endRow, endCol, startRow, startCol, fieldManagerService);
        }
        return false;
    }

    public static boolean isCaptureValid(Piece piece, int endRow, int endCol, int startRow, int startCol, FieldManagerService fieldManagerService) {
        int captureRow = (endRow + startRow) / 2;
        int captureCol = (endCol + startCol) / 2;
        Optional<Piece> capturePiece = fieldManagerService.getBoardPiece(captureRow, captureCol);
        if (!capturePiece.isPresent() || capturePiece.get().getColor() == piece.getColor()) {
            return false;
        }
        setHasEaten(true);
        return true;
    }

    public static boolean isHasEaten() {
        return hasEaten;
    }

    public static void setHasEaten(boolean hasEaten) {
        MoveValidation.hasEaten = hasEaten;
    }
}
