package checkers.hosting;

import checkers.hosting.interfaces.*;

/**
 * Represents the configuration data of the application.
 */
public class BaseData implements BaseDataService {

    private static final int SQUARE_NUMBER = 8;
    private static final int SQUARE_SIZE = 90;

    @Override
    public int getSquareNumber() {
        return SQUARE_NUMBER;
    }

    @Override
    public int getSquareSize() {
        return SQUARE_SIZE;
    }

    @Override
    public String getName() {
        return BaseDataService.class.getSimpleName();
    }
}
