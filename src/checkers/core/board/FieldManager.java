package checkers.core.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.scene.Group;
import checkers.core.gui.Piece;
import checkers.core.gui.Piece.PieceColor;
import checkers.hosting.interfaces.*;

/**
 * Allows access to the checkers game board.
 */
public class FieldManager extends Group implements FieldManagerService {

    private BaseDataService baseDataService;
    private Piece[][] board;
    private List<Piece> blackPieces;
    private List<Piece> redPieces;
    private List<Piece> redKingPieces;

    public FieldManager(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
        board = new Piece[baseDataService.getSquareNumber()][baseDataService.getSquareNumber()];
        blackPieces = new ArrayList<>();
        redPieces = new ArrayList<>();
        initializePieces();
    }

    private void initializePieces() {
        int squareNumber = baseDataService.getSquareNumber();
        int loopEndCondition = (squareNumber / 2) - 1;
        for (int row = 0; row < loopEndCondition; row++) {
            for (int col = 0; col < squareNumber; col++) {
                if ((row + col) % 2 == 1) {
                    Piece piece = new Piece(row, col, PieceColor.RED, this.baseDataService);
                    redPieces.add(piece);
                    board[row][col] = piece;
                }
            }
        }
        for (int row = squareNumber - loopEndCondition; row < squareNumber; row++) {
            for (int col = 0; col < squareNumber; col++) {
                if ((row + col) % 2 == 1) {
                    Piece piece = new Piece(row, col, PieceColor.BLACK, this.baseDataService);
                    blackPieces.add(piece);
                    board[row][col] = piece;
                }
            }
        }
    }

    @Override
    public Optional<Piece> getBoardPiece(int row, int col) {
        return Optional.ofNullable(board[row][col]);
    }

    @Override
    public void movePiece(int toRow, int toCol, int fromRow, int fromCol) {
        Piece piece = board[fromRow][fromCol];
        piece.setRow(toRow);
        piece.setCol(toCol);
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = null;
    }

    @Override
    public void removePiece(int rowOfCapturedPiece, int colOfCapturedPiece) {
        board[rowOfCapturedPiece][colOfCapturedPiece] = null;
    }

    @Override
    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    @Override
    public void setBlackPieces(List<Piece> blackPieces) {
        this.blackPieces = blackPieces;
    }

    @Override
    public List<Piece> getRedPieces() {
        return redPieces;
    }

    @Override
    public void setRedPieces(List<Piece> redPieces) {
        this.redPieces = redPieces;
    }

    @Override
    public void updateRedKingPieces() {
        redPieces = getRedPieces();
        redKingPieces = redPieces.stream().filter(p -> p.isKing()).collect(Collectors.toList());
    }

    @Override
    public List<Piece> getRedKingPieces() {
        return redKingPieces;
    }

    @Override
    public Piece[][] getBoard() {
        return board;
    }

    @Override
    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    @Override
    public String getName() {
        return FieldManagerService.class.getSimpleName();
    }
}