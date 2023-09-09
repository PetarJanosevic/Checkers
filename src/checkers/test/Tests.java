package checkers.test;

import checkers.core.gui.Piece;
import checkers.core.logic.ComputerPlayer;
import checkers.core.logic.MoveValidation;
import checkers.hosting.Container;
import checkers.hosting.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    private Container container;
    private BaseDataService baseDataService;
    private FieldManagerService fieldManagerService;
    private HumanPlayerService humanPlayerService;
    private ComputerPlayerService computerPlayerService;
    private SceneManagerService sceneManagerService;

    @BeforeEach
    void setUp() {
        container = new Container();
        container.initializeContainer();
        Optional<BaseDataService> optionalBaseDataService = container.getService(BaseDataService.class);
        baseDataService = optionalBaseDataService.orElseThrow(() -> new NullPointerException(Container.BASE_DATA_EXCEPTION));

        Optional<FieldManagerService> optionalFieldManagerService = container.getService(FieldManagerService.class);
        fieldManagerService = optionalFieldManagerService.orElseThrow(() -> new NullPointerException(Container.FIELD_MANAGER_EXCEPTION));

        Optional<HumanPlayerService> optionalHumanPlayerService = container.getService(HumanPlayerService.class);
        humanPlayerService = optionalHumanPlayerService.orElseThrow(() -> new NullPointerException(Container.HUMAN_PLAYER_EXCEPTION));

        Optional<ComputerPlayerService> optionalComputerPlayerService = container.getService(ComputerPlayerService.class);
        computerPlayerService = optionalComputerPlayerService.orElseThrow(() -> new NullPointerException(Container.COMPUTER_PLAYER_EXCEPTION));

        Optional<SceneManagerService> optionalSceneManagerService = container.getService(SceneManagerService.class);
        sceneManagerService = optionalSceneManagerService.orElseThrow(() -> new NullPointerException(Container.SCENE_MANAGER_EXCEPTION));
    }

    @Test
    void boardInstantiation_shouldReturnEqualBoards_whenGameIsInitialized() {
        // Arrange
        Piece[][] expectedBoard = {
                {null, new Piece(0, 1, Piece.PieceColor.RED, baseDataService), null, new Piece(0, 3, Piece.PieceColor.RED, baseDataService), null, new Piece(0, 5, Piece.PieceColor.RED, baseDataService), null, new Piece(0, 7, Piece.PieceColor.RED, baseDataService)},
                {new Piece(1, 0, Piece.PieceColor.RED, baseDataService), null, new Piece(1, 2, Piece.PieceColor.RED, baseDataService), null, new Piece(1, 4, Piece.PieceColor.RED, baseDataService), null, new Piece(1, 6, Piece.PieceColor.RED, baseDataService), null},
                {null, new Piece(2, 1, Piece.PieceColor.RED, baseDataService), null, new Piece(2, 3, Piece.PieceColor.RED, baseDataService), null, new Piece(2, 5, Piece.PieceColor.RED, baseDataService), null, new Piece(2, 7, Piece.PieceColor.RED, baseDataService)},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {new Piece(5, 0, Piece.PieceColor.BLACK, baseDataService), null, new Piece(5, 2, Piece.PieceColor.BLACK, baseDataService), null, new Piece(5, 4, Piece.PieceColor.BLACK, baseDataService), null, new Piece(5, 6, Piece.PieceColor.BLACK, baseDataService), null},
                {null, new Piece(6, 1, Piece.PieceColor.BLACK, baseDataService), null, new Piece(6, 3, Piece.PieceColor.BLACK, baseDataService), null, new Piece(6, 5, Piece.PieceColor.BLACK, baseDataService), null, new Piece(6, 7, Piece.PieceColor.BLACK, baseDataService)},
                {new Piece(7, 0, Piece.PieceColor.BLACK, baseDataService), null, new Piece(7, 2, Piece.PieceColor.BLACK, baseDataService), null, new Piece(7, 4, Piece.PieceColor.BLACK, baseDataService), null, new Piece(7, 6, Piece.PieceColor.BLACK, baseDataService), null}
        };

        // Act
        Piece[][] officialGameBoard = fieldManagerService.getBoard();

        // Assert
        assertArrayEquals(officialGameBoard, expectedBoard);
    }

    @Test
    void movePieceToNewPosition_returnTrue_whenPieceIsMovedAndStandsOnNewPosition() {
        // Arrange
        int toRow = 4;
        int toCol = 5;
        int fromRow = 5;
        int fromCol = 6;
        Optional<Piece> newPosition;
        Optional<Piece> oldPosition = fieldManagerService.getBoardPiece(fromRow, fromCol);

        // Act
        fieldManagerService.movePiece(toRow, toCol, fromRow, fromCol);
        newPosition = fieldManagerService.getBoardPiece(toRow, toCol);

        // Assert
        assertEquals(newPosition.get(), oldPosition.get());
    }

    @Test
    void movePieceToNewPositionAndCheckOldPosition_returnTrue_whenPieceIsMovedAndTheOldPositionBecomesEmpty() {
        // Arrange
        int toRow = 4;
        int toCol = 5;
        int fromRow = 5;
        int fromCol = 6;
        Optional<Piece> oldPosition;

        // Act
        fieldManagerService.movePiece(toRow, toCol, fromRow, fromCol);
        oldPosition = fieldManagerService.getBoardPiece(fromRow, fromCol);

        // Assert
        assertFalse(oldPosition.isPresent());
    }

    @Test
    void moveDiagonal_returnTrue() {
        // Arrange
        int endRow = 4;
        int endCol = 1;
        int startRow = 5;
        int startCol = 0;

        // Act
        boolean moveDiagonal = MoveValidation.isMoveDiagonal(endRow, endCol, startRow, startCol);

        // Assert
        assertTrue(moveDiagonal);
    }

    @Test
    void moveDiagonal_returnFalse() {
        // Arrange
        int endRow = 5;
        int endCol = 1;
        int startRow = 5;
        int startCol = 0;

        // Act
        boolean moveDiagonal = MoveValidation.isMoveDiagonal(endRow, endCol, startRow, startCol);

        // Assert
        assertFalse(moveDiagonal);
    }

    @Test
    void isDestinationSquareEmpty_returnTrue() {
        // Arrange
        int row = 4;
        int col = 7;

        // Act
        boolean destinationSquareEmpty = MoveValidation.isDestinationSquareEmpty(row, col, fieldManagerService);

        // Assert
        assertTrue(destinationSquareEmpty);
    }

    @Test
    void isDestinationSquareEmpty_returnFalse() {
        // Arrange
        int row = 2;
        int col = 1;

        // Act
        boolean destinationSquareEmpty = MoveValidation.isDestinationSquareEmpty(row, col, fieldManagerService);

        // Assert
        assertFalse(destinationSquareEmpty);
    }

    @Test
    void moveInCorrectDirection_returnTrue_whenHumanPlayerIsMoving() {
        // Arrange
        int startRow = 5;
        int startCol = 2;
        int endRow = 4;
        Optional<Piece> humanPiece = fieldManagerService.getBoardPiece(startRow, startCol);

        // Act
        boolean moveInCorrectDirection = MoveValidation.isMoveInCorrectDirection(humanPiece.get(), endRow, startRow);

        // Assert
        assertTrue(moveInCorrectDirection);
    }

    @Test
    void moveInCorrectDirection_returnFalse_whenHumanPlayerIsMoving() {
        // Arrange
        int startRow = 5;
        int startCol = 2;
        int endRow = 6;
        Optional<Piece> humanPiece = fieldManagerService.getBoardPiece(startRow, startCol);

        // Act
        boolean moveInCorrectDirection = MoveValidation.isMoveInCorrectDirection(humanPiece.get(), endRow, startRow);

        // Assert
        assertFalse(moveInCorrectDirection);
    }

    @Test
    void moveIsTooBig_returnTrue_whenHumanPlayerTriesJumpingTwoRows() {
        // Arrange
        int startRow = 5;
        int startCol = 4;
        int endRow = 3;
        int endCol = 6;
        Optional<Piece> humanPiece = fieldManagerService.getBoardPiece(startRow, startCol);

        // Act
        boolean isMoveTooBig = MoveValidation.isMoveTooBig(humanPiece.get(), endRow, endCol, startRow, startCol, fieldManagerService);

        // Assert
        assertTrue(isMoveTooBig);
    }

    @Test
    void moveIsTooBig_returnTrue_whenHumanPlayerTriesJumpingMoreThanTwoRows() {
        // Arrange
        int startRow = 5;
        int startCol = 4;
        int endRow = 3;
        int endCol = 6;
        Optional<Piece> humanPiece = fieldManagerService.getBoardPiece(startRow, startCol);

        // Act
        boolean isMoveTooBig = MoveValidation.isMoveTooBig(humanPiece.get(), endRow, endCol, startRow, startCol, fieldManagerService);

        // Assert
        assertTrue(isMoveTooBig);
    }

    @Test
    void moveIsTooBig_returnFalse_whenHumanPlayerTriesJumpingOneRow() {
        // Arrange
        int startRow = 5;
        int startCol = 4;
        int endRow = 4;
        int endCol = 5;
        Optional<Piece> humanPiece = fieldManagerService.getBoardPiece(startRow, startCol);

        // Act
        boolean isMoveTooBig = MoveValidation.isMoveTooBig(humanPiece.get(), endRow, endCol, startRow, startCol, fieldManagerService);

        // Assert
        assertFalse(isMoveTooBig);
    }

    @Test
    void captureIsValid_returnTrue_whenHumanPlayerTriesEating() {
        // Arrange
        int startRow = 5;
        int startCol = 4;
        int endRow = 3;
        int endCol = 6;
        Optional<Piece> humanPiece = fieldManagerService.getBoardPiece(startRow, startCol);

        // Act
        fieldManagerService.movePiece(4, 5, 2, 5);
        boolean isCaptureValid = MoveValidation.isCaptureValid(humanPiece.get(), endRow, endCol, startRow, startCol, fieldManagerService);

        // Assert
        assertTrue(isCaptureValid);
    }

    @Test
    void captureIsValid_returnFalse_whenHumanPlayerTriesJumpingTwoRows() {
        // Arrange
        int startRow = 5;
        int startCol = 4;
        int endRow = 3;
        int endCol = 6;
        Optional<Piece> humanPiece = fieldManagerService.getBoardPiece(startRow, startCol);

        // Act
        boolean isCaptureValid = MoveValidation.isCaptureValid(humanPiece.get(), endRow, endCol, startRow, startCol, fieldManagerService);

        // Assert
        assertFalse(isCaptureValid);
    }

    @Test
    void captureIsValid_returnFalse_whenHumanPlayerTriesJumpingOverOwnPiece() {
        // Arrange
        int startRow = 6;
        int startCol = 1;
        int endRow = 4;
        int endCol = 3;
        Optional<Piece> humanPiece = fieldManagerService.getBoardPiece(startRow, startCol);

        // Act
        boolean isCaptureValid = MoveValidation.isCaptureValid(humanPiece.get(), endRow, endCol, startRow, startCol, fieldManagerService);

        // Assert
        assertFalse(isCaptureValid);
    }

    @Test
    void computerPrioritizesEatingOverRegularMove_returnTrue_whenHumanPieceIsEaten() {
        // Arrange
        Optional<Piece> selectedPiece = fieldManagerService.getBoardPiece(5, 6);
        Optional<Piece> eatenBlackPiece;

        // Act
        humanPlayerService.handleMoveClick(4, 7, selectedPiece);
        humanPlayerService.handleMoveClick(3, 6, selectedPiece);
        computerPlayerService.computerMove();
        eatenBlackPiece = fieldManagerService.getBoardPiece(3, 6);

        // Assert
        assertFalse(eatenBlackPiece.isPresent());
    }

    @Test
    void captureThreePieces_returnTrue_whenComputerPlayerEatsAllThreeHumanPieces() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> redPieces = new ArrayList<>();
        Piece redPiece = new Piece(1, 0, Piece.PieceColor.RED, baseDataService);
        Piece blackPieceFirst = new Piece(2, 1, Piece.PieceColor.BLACK, baseDataService);
        Piece blackPieceSecond = new Piece(4, 3, Piece.PieceColor.BLACK, baseDataService);
        Piece blackPieceThird = new Piece(6, 5, Piece.PieceColor.BLACK, baseDataService);
        int blackPiecesSizeAfterEating;
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {redPiece, null, null, null, null, null, null, null},
                {null, blackPieceFirst, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, blackPieceSecond, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, blackPieceThird, null, null},
                {null, null, null, null, null, null, null, null}
        };
        redPieces.add(redPiece);
        blackPieces.add(blackPieceFirst);
        blackPieces.add(blackPieceSecond);
        blackPieces.add(blackPieceThird);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        computerPlayerService.computerMove();
        blackPiecesSizeAfterEating = fieldManagerService.getBlackPieces().size();

        // Assert
        assertEquals(blackPiecesSizeAfterEating, 0);
    }

    @Test
    void removeBlackPiece_returnTrue_whenComputerEatsHumanPiece() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> redPieces = new ArrayList<>();
        Piece redPiece = new Piece(2, 5, Piece.PieceColor.RED, baseDataService);
        Piece blackPiece = new Piece(3, 6, Piece.PieceColor.BLACK, baseDataService);
        int blackPiecesSizeAfterEating;
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, redPiece, null, null},
                {null, null, null, null, null, null, blackPiece, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
        };

        redPieces.add(redPiece);
        blackPieces.add(blackPiece);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        computerPlayerService.computerMove();
        blackPiecesSizeAfterEating = fieldManagerService.getBlackPieces().size();

        // Assert
        assertEquals(blackPiecesSizeAfterEating, 0);
    }

    @Test
    void becomeKingPiece_returnTrue_whenComputerPlayerReachesEndOfBoard() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> redPieces = new ArrayList<>();
        Piece redPiece = new Piece(1, 0, Piece.PieceColor.RED, baseDataService);
        Piece blackPieceFirst = new Piece(2, 1, Piece.PieceColor.BLACK, baseDataService);
        Piece blackPieceSecond = new Piece(4, 3, Piece.PieceColor.BLACK, baseDataService);
        Piece blackPieceThird = new Piece(6, 5, Piece.PieceColor.BLACK, baseDataService);
        boolean isKing;
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {redPiece, null, null, null, null, null, null, null},
                {null, blackPieceFirst, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, blackPieceSecond, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, blackPieceThird, null, null},
                {null, null, null, null, null, null, null, null}
        };
        redPieces.add(redPiece);
        blackPieces.add(blackPieceFirst);
        blackPieces.add(blackPieceSecond);
        blackPieces.add(blackPieceThird);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        computerPlayerService.computerMove();
        isKing = redPiece.isKing();

        // Assert
        assertTrue(isKing);
    }

    @Test
    void becomeKingPieceAndEatAgain_returnTrue_whenComputerPlayerReachesEndOfBoardAndCanEatAgain() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> redPieces = new ArrayList<>();
        Piece redPiece = new Piece(5, 2, Piece.PieceColor.RED, baseDataService);
        Piece blackPieceFirst = new Piece(6, 3, Piece.PieceColor.BLACK, baseDataService);
        Piece blackPieceSecond = new Piece(6, 5, Piece.PieceColor.BLACK, baseDataService);
        int blackPiecesSizeAfterEating;
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, redPiece, null, null, null, null, null},
                {null, null, null, blackPieceFirst, null, blackPieceSecond, null, null},
                {null, null, null, null, null, null, null, null}
        };
        redPieces.add(redPiece);
        blackPieces.add(blackPieceFirst);
        blackPieces.add(blackPieceSecond);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        computerPlayerService.computerMove();
        blackPiecesSizeAfterEating = fieldManagerService.getBlackPieces().size();

        // Assert
        assertEquals(blackPiecesSizeAfterEating, 0);
    }

    @Test
    void becomeKingPieceAndEatAgainAfterTwoEatings_returnTrue_whenComputerPlayerReachesEndOfBoardAndCanEatAgain() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> redPieces = new ArrayList<>();
        Piece redPiece = new Piece(3, 0, Piece.PieceColor.RED, baseDataService);
        Piece blackPieceFirst = new Piece(4, 1, Piece.PieceColor.BLACK, baseDataService);
        Piece blackPieceSecond = new Piece(6, 3, Piece.PieceColor.BLACK, baseDataService);
        Piece blackPieceThird = new Piece(6, 5, Piece.PieceColor.BLACK, baseDataService);
        int blackPiecesSizeAfterEating;
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {redPiece, null, null, null, null, null, null, null},
                {null, blackPieceFirst, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, blackPieceSecond, null, blackPieceThird, null, null},
                {null, null, null, null, null, null, null, null}
        };
        redPieces.add(redPiece);
        blackPieces.add(blackPieceFirst);
        blackPieces.add(blackPieceSecond);
        blackPieces.add(blackPieceThird);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        computerPlayerService.computerMove();
        blackPiecesSizeAfterEating = fieldManagerService.getBlackPieces().size();

        // Assert
        assertEquals(blackPiecesSizeAfterEating, 0);
    }

    @Test
    void becomeKingPieceAndEatAgainAfterTwoEatings_returnTrue_whenHumanPlayerReachesEndOfBoardAndCanEatAgain() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> redPieces = new ArrayList<>();
        Piece redPieceFirst = new Piece(1, 2, Piece.PieceColor.RED, baseDataService);
        Piece redPieceSecond = new Piece(3, 6, Piece.PieceColor.RED, baseDataService);
        Piece redPieceThird = new Piece(1, 4, Piece.PieceColor.RED, baseDataService);
        Piece blackPiece = new Piece(4, 7, Piece.PieceColor.BLACK, baseDataService);
        int redPiecesSizeAfterEating;
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, redPieceFirst, null, redPieceThird, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, redPieceSecond, null},
                {null, null, null, null, null, null, null, blackPiece},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
        };
        redPieces.add(redPieceFirst);
        redPieces.add(redPieceSecond);
        redPieces.add(redPieceThird);
        blackPieces.add(blackPiece);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        humanPlayerService.handleMoveClick(2, 5, Optional.of(blackPiece));
        humanPlayerService.handleMoveClick(0, 3, Optional.of(blackPiece));
        humanPlayerService.handleMoveClick(2, 1, Optional.of(blackPiece));
        redPiecesSizeAfterEating = fieldManagerService.getRedPieces().size();

        // Assert
        assertEquals(redPiecesSizeAfterEating, 0);
    }

    @Test
    void gameOver_returnTrue_whenHumanPlayerEatsLastComputerPiece() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> redPieces = new ArrayList<>();
        Piece redPieceFirst = new Piece(1, 2, Piece.PieceColor.RED, baseDataService);
        Piece redPieceSecond = new Piece(3, 6, Piece.PieceColor.RED, baseDataService);
        Piece redPieceThird = new Piece(1, 4, Piece.PieceColor.RED, baseDataService);
        Piece blackPiece = new Piece(4, 7, Piece.PieceColor.BLACK, baseDataService);
        boolean isGameOver;
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, redPieceFirst, null, redPieceThird, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, redPieceSecond, null},
                {null, null, null, null, null, null, null, blackPiece},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
        };
        redPieces.add(redPieceFirst);
        redPieces.add(redPieceSecond);
        redPieces.add(redPieceThird);
        blackPieces.add(blackPiece);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        humanPlayerService.handleMoveClick(2, 5, Optional.of(blackPiece));
        humanPlayerService.handleMoveClick(0, 3, Optional.of(blackPiece));
        humanPlayerService.handleMoveClick(2, 1, Optional.of(blackPiece));
        MoveValidation.setHasEaten(false);
        isGameOver = sceneManagerService.checkIfHumanWon(redPieces);

        // Assert
        assertTrue(isGameOver);
    }

    @Test
    void gameOver_returnTrue_whenComputerPlayerEatsLastHumanPiece() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> redPieces = new ArrayList<>();
        Piece redPiece = new Piece(3, 0, Piece.PieceColor.RED, baseDataService);
        Piece blackPieceFirst = new Piece(4, 1, Piece.PieceColor.BLACK, baseDataService);
        Piece blackPieceSecond = new Piece(6, 3, Piece.PieceColor.BLACK, baseDataService);
        Piece blackPieceThird = new Piece(6, 5, Piece.PieceColor.BLACK, baseDataService);
        boolean isGameOver;
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {redPiece, null, null, null, null, null, null, null},
                {null, blackPieceFirst, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, blackPieceSecond, null, blackPieceThird, null, null},
                {null, null, null, null, null, null, null, null}
        };
        redPieces.add(redPiece);
        blackPieces.add(blackPieceFirst);
        blackPieces.add(blackPieceSecond);
        blackPieces.add(blackPieceThird);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        computerPlayerService.computerMove();
        MoveValidation.setHasEaten(false);
        isGameOver = sceneManagerService.checkIfComputerWon(blackPieces);

        // Assert
        assertTrue(isGameOver);
    }

    @Test
    void cantDoRegularMoveAfterEating_returnTrue_whenHumanEatsOpponentPieceAndTriesToWalkARegularWalkAfterClickingOnSeveralFieldsWhichAreNotMoveableTo() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> redPieces = new ArrayList<>();
        Piece redPieceFirst = new Piece(3, 6, Piece.PieceColor.RED, baseDataService);
        int column = 7;
        int row = 4;
        int columnTest, rowTest;
        boolean isColEqual, isRowEqual;
        Piece blackPiece = new Piece(row, column, Piece.PieceColor.BLACK, baseDataService);
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, redPieceFirst, null},
                {null, null, null, null, null, null, null, blackPiece},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
        };
        redPieces.add(redPieceFirst);
        blackPieces.add(blackPiece);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        humanPlayerService.handleMoveClick(2, 5, Optional.of(blackPiece));
        column = blackPiece.getCol();
        row = blackPiece.getRow();
        humanPlayerService.handleMoveClick(0, 3, Optional.of(blackPiece));
        humanPlayerService.handleMoveClick(1, 4, Optional.of(blackPiece));
        columnTest = blackPiece.getCol();
        rowTest = blackPiece.getRow();
        isColEqual = column == columnTest;
        isRowEqual = row == rowTest;

        // Assert
        assertTrue(isRowEqual);
        assertTrue(isColEqual);
    }

    @Test
    void regularHumanMove_returnTrue() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        int column = 2;
        int row = 5;
        boolean isColEqual, isRowEqual;
        Piece blackPiece = new Piece(row, column, Piece.PieceColor.BLACK, baseDataService);
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, blackPiece, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
        };
        blackPieces.add(blackPiece);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        humanPlayerService.handleMoveClick(4, 1, Optional.of(blackPiece));
        isColEqual = (column - 1) == blackPiece.getCol();
        isRowEqual = (row - 1) == blackPiece.getRow();

        // Assert
        assertTrue(isColEqual);
        assertTrue(isRowEqual);
    }

    @Test
    void regularComputerMove_returnTrue() {
        // Arrange
        ArrayList<Piece> redPieces = new ArrayList<>();
        int column = 0;
        int row = 1;
        boolean isColEqual, isRowEqual;
        Piece redPiece = new Piece(row, column, Piece.PieceColor.RED, baseDataService);
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {redPiece, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
        };
        redPieces.add(redPiece);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        computerPlayerService.computerMove();
        isColEqual = (column + 1) == redPiece.getCol();
        isRowEqual = (row + 1) == redPiece.getRow();

        // Assert
        assertTrue(isColEqual);
        assertTrue(isRowEqual);
    }

    @Test
    void regularHumanKingMove_returnTrue() {
        // Arrange
        ArrayList<Piece> blackPieces = new ArrayList<>();
        int column = 6;
        int row = 4;
        boolean isColEqual, isRowEqual;
        Piece blackPiece = new Piece(row, column, Piece.PieceColor.BLACK, baseDataService);
        blackPiece.makeKing();
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, blackPiece, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
        };
        blackPieces.add(blackPiece);
        fieldManagerService.setBlackPieces(blackPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        humanPlayerService.handleMoveClick(5, 7, Optional.of(blackPiece));
        isColEqual = (column + 1) == blackPiece.getCol();
        isRowEqual = (row + 1) == blackPiece.getRow();

        // Assert
        assertTrue(isColEqual);
        assertTrue(isRowEqual);
    }

    @Test
    void regularComputerKingMove_returnTrue() {
        // Arrange
        ArrayList<Piece> redPieces = new ArrayList<>();
        int column = 0;
        int row = 7;
        boolean isColEqual, isRowEqual;
        Piece redPiece = new Piece(row, column, Piece.PieceColor.RED, baseDataService);
        redPiece.makeKing();
        Piece[][] expectedBoard = {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {redPiece, null, null, null, null, null, null, null}
        };
        redPieces.add(redPiece);
        fieldManagerService.setRedPieces(redPieces);
        fieldManagerService.setBoard(expectedBoard);

        // Act
        computerPlayerService.computerMove();
        isColEqual = (column + 1) == redPiece.getCol();
        isRowEqual = (row - 1) == redPiece.getRow();

        // Assert
        assertTrue(isColEqual);
        assertTrue(isRowEqual);
    }
}
