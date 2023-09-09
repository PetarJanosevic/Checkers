package checkers.hosting.interfaces;

/**
 * Enables access to the opponent of the checkers game.
 */
public interface ComputerPlayerService extends Service {

    void computerMove();

    String getName();
}
