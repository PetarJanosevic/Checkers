package checkers.core.logic;

import checkers.core.gui.Piece;
import checkers.hosting.interfaces.*;

import java.util.Optional;

/**
 * Controls the moves of the human player
 */
public class HumanPlayer implements HumanPlayerService {

    private FieldManagerService fieldManagerService;

    public HumanPlayer(FieldManagerService fieldManagerService) {
        this.fieldManagerService = fieldManagerService;
    }

    @Override
    public int handleMoveClick(int row, int col, Optional<Piece> selectedPiece) {
        boolean isPiecePresent = selectedPiece.isPresent();

        if (MoveValidation.isHasEaten() && isPiecePresent) {
            return handleCaptureSubsequentSteps(row, col, selectedPiece.get());
        }

        if (!isPiecePresent || !MoveValidation.isValidMove(selectedPiece.get(), row, col, this.fieldManagerService)) {
            return 0;
        }

        if (MoveValidation.isHasEaten()) {
            humanEatingProcess(row, col, selectedPiece.get());
            return 1;
        }

        this.fieldManagerService.movePiece(row, col, selectedPiece.get().getRow(), selectedPiece.get().getCol());
        kingTransition(row, selectedPiece.get());
        MoveValidation.setHasEaten(false);
        return 2;
    }

    // Method handles the steps after a player captured an opponent's piece.
    public int handleCaptureSubsequentSteps(int row, int col, Piece selectedPiece) {
        boolean isValidMove = MoveValidation.isValidMove(selectedPiece, row, col, this.fieldManagerService);
        if (MoveValidation.isHasEaten() && isValidMove) {
            humanEatingProcess(row, col, selectedPiece);
            return 1;
        } else if (isValidMove) {
            this.fieldManagerService.movePiece(row, col, selectedPiece.getRow(), selectedPiece.getCol());
            kingTransition(row, selectedPiece);
            MoveValidation.setHasEaten(false);
            return 2;
        }
        return 0;
    }

    private void humanEatingProcess(int row, int col, Piece selectedPiece) {
        int rowOfCapturedPiece = (selectedPiece.getRow() + row) / 2;
        int colOfCapturedPiece = (selectedPiece.getCol() + col) / 2;
        Optional<Piece> capturedPiece = this.fieldManagerService.getBoardPiece(rowOfCapturedPiece, colOfCapturedPiece);
        capturedPiece.ifPresent(p -> {
            this.fieldManagerService.getRedPieces().remove(p);
            this.fieldManagerService.removePiece(rowOfCapturedPiece, colOfCapturedPiece);
        });
        this.fieldManagerService.movePiece(row, col, selectedPiece.getRow(), selectedPiece.getCol());
        kingTransition(row, selectedPiece);
    }

    private void kingTransition(int row, Piece selectedPiece) {
        if (row == 0) {
            selectedPiece.makeKing();
        }
    }

    @Override
    public String getName() {
        return HumanPlayerService.class.getSimpleName();
    }
}
