package checkers.hosting.interfaces;

/**
 * Enables access to configuration data of the application.
 */
public interface BaseDataService extends Service {

    int getSquareNumber();

    int getSquareSize();

    String getName();
}
