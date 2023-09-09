package checkers.core.gui;

import checkers.core.board.FieldManager;
import checkers.hosting.interfaces.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Objects;

/**
 * This symbolises a game piece that is used for the board in {@link FieldManager}
 * and for rendering in the GUI section {@link SceneManager}.
 */
public class Piece extends Group {

    public enum PieceColor {
        BLACK, RED
    }

    private int row;
    private int col;
    private PieceColor color;
    private boolean isKing;
    private Circle circle;
    private BaseDataService baseDataService;

    public Piece(int row, int col, PieceColor color, BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
        this.row = row;
        this.col = col;
        this.color = color;
        this.isKing = false;
        setCircle(new Circle(30, color == PieceColor.RED ? Color.RED : Color.BLACK));
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public PieceColor getColor() {
        return color;
    }

    public void setColor(PieceColor color) {
        this.color = color;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
        // The circle is placed in the centre of the square area,
        // provided the size of the square is twice the size of the circle.
        double squareWidth = this.baseDataService.getSquareSize();
        double circleRadius = circle.getRadius() * 2;
        double offset = (squareWidth - circleRadius) / 2;
        circle.setCenterX(offset + circle.getRadius());
        circle.setCenterY(offset + circle.getRadius());
    }

    public boolean isKing() {
        return isKing;
    }

    public void makeKing() {
        isKing = true;
        circle.setFill(color == PieceColor.RED ? Color.LIGHTCORAL : Color.DARKGRAY);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Piece otherPiece = (Piece) obj;
        return row == otherPiece.row && col == otherPiece.col && color == otherPiece.color && isKing == otherPiece.isKing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, color, isKing);
    }

    @Override
    public String toString() {
        return "Piece{" + "row: " + row + ", col: " + col + ", color: " + color + ", isKing: " + isKing + '}';
    }
}