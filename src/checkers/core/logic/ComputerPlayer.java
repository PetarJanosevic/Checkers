package checkers.core.logic;

import java.util.List;
import java.util.Optional;

import checkers.core.gui.Piece;
import checkers.hosting.interfaces.*;

/**
 * Controls the moves of the opponent ,that is the computer player.
 */
public class ComputerPlayer implements ComputerPlayerService {

    private FieldManagerService fieldManagerService;
    private BaseDataService baseDataService;
    private static final int[][] COMPUTER_REGULAR_DIRECTIONS = {{1, -1}, {1, 1}};
    private static final int[][] COMPUTER_REGULAR_EATING_DIRECTIONS = {{2, -2}, {2, 2}};
    private static final int[][] COMPUTER_KING_DIRECTIONS = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
    private static final int[][] COMPUTER_KING_EATING_DIRECTIONS = {{2, -2}, {2, 2}, {-2, -2}, {-2, 2}};

    public ComputerPlayer(FieldManagerService fieldManagerService, BaseDataService baseDataService) {
        this.fieldManagerService = fieldManagerService;
        this.baseDataService = baseDataService;
    }

    @Override
    public void computerMove() {
        fieldManagerService.updateRedKingPieces();
        List<Piece> redPieces = fieldManagerService.getRedPieces();
        List<Piece> redKingPieces = fieldManagerService.getRedKingPieces();
        int redPiecesSize = redPieces.size();
        int redKingPiecesSize = redKingPieces.size();
        if (eatingMove(redKingPieces, redKingPiecesSize, COMPUTER_KING_EATING_DIRECTIONS)) {
            return;
        }
        if (eatingMove(redPieces, redPiecesSize, COMPUTER_REGULAR_EATING_DIRECTIONS)) {
            return;
        }
        if (regularMove(redPieces, redPiecesSize, COMPUTER_REGULAR_DIRECTIONS)) {
            return;
        }
        regularMove(redKingPieces, redKingPiecesSize, COMPUTER_KING_DIRECTIONS);
    }

    private boolean eatingMove(List<Piece> pieces, int piecesSize, int[][] computerDirection) {
        for (int i = 0; i < piecesSize; i++) {
            Piece redPiece = pieces.get(i);
            int startRow = redPiece.getRow();
            int startCol = redPiece.getCol();
            for (int[] direction : computerDirection) {
                int row = startRow + direction[0];
                int col = startCol + direction[1];
                boolean isOutOfBounds = isOutOfBounds(row, col);
                if (!isOutOfBounds && MoveValidation.isValidMove(redPiece, row, col, this.fieldManagerService) && MoveValidation.isHasEaten()) {
                    eatingProcess(redPiece, startRow, startCol, row, col, computerDirection);
                    return true;
                }
            }
        }
        return false;
    }

    private void consecutiveEatingMove(Piece redPiece, int row, int col, int[][] computerDirection) {
        if (MoveValidation.isHasEaten()) {
            MoveValidation.setHasEaten(false);
            for (int[] nextDirection : computerDirection) {
                int nextRow = row + nextDirection[0];
                int nextCol = col + nextDirection[1];
                boolean isOutOfBounds = isOutOfBounds(nextRow, nextCol);
                if (!isOutOfBounds && MoveValidation.isValidMove(redPiece, nextRow, nextCol, this.fieldManagerService) && MoveValidation.isHasEaten()) {
                    eatingProcess(redPiece, row, col, nextRow, nextCol, computerDirection);
                }
            }
        }
    }

    private void eatingProcess(Piece redPiece, int startRow, int startCol, int row, int col, int[][] computerDirection) {
        int nextRowOfCapturedPiece = (redPiece.getRow() + row) / 2;
        int nextColOfCapturedPiece = (redPiece.getCol() + col) / 2;
        Optional<Piece> nextCapturedPiece = fieldManagerService.getBoardPiece(nextRowOfCapturedPiece, nextColOfCapturedPiece);
        nextCapturedPiece.ifPresent(piece -> fieldManagerService.getBlackPieces().remove(piece));
        fieldManagerService.removePiece(nextRowOfCapturedPiece, nextColOfCapturedPiece);
        fieldManagerService.movePiece(row, col, startRow, startCol);
        kingTransition(this.baseDataService.getSquareNumber(), redPiece, row);
        eatingMove(fieldManagerService.getRedKingPieces(), fieldManagerService.getRedKingPieces().size(), ComputerPlayer.COMPUTER_KING_EATING_DIRECTIONS);
        consecutiveEatingMove(redPiece, row, col, computerDirection);
    }

    private boolean regularMove(List<Piece> pieces, int piecesSize, int[][] computerDirection) {
        for (int i = 0; i < piecesSize; i++) {
            Piece redPiece = pieces.get(i);
            int startRow = redPiece.getRow();
            int startCol = redPiece.getCol();
            for (int[] direction : computerDirection) {
                int row = startRow + direction[0];
                int col = startCol + direction[1];
                boolean isOutOfBounds = isOutOfBounds(row, col);
                if (!isOutOfBounds && MoveValidation.isValidMove(redPiece, row, col, this.fieldManagerService)) {
                    fieldManagerService.movePiece(row, col, startRow, startCol);
                    kingTransition(this.baseDataService.getSquareNumber(), redPiece, row);
                    MoveValidation.setHasEaten(false);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOutOfBounds(int row, int col) {
        return col > 7 || col < 0 || row > 7 || row < 0;
    }

    private void kingTransition(int boardSize, Piece redPiece, int row) {
        if (row == boardSize - 1) {
            redPiece.makeKing();
            fieldManagerService.updateRedKingPieces();
        }
    }

    @Override
    public String getName() {
        return ComputerPlayerService.class.getSimpleName();
    }
}
