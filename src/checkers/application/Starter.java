package checkers.application;

import checkers.hosting.*;
import checkers.hosting.interfaces.*;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Is responsible for starting the application.
 */
public class Starter extends Application {

    private static final Logger LOGGER = Logger.getLogger(Starter.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            ShowInformation(throwable);
            LOGGER.log(Level.SEVERE, "An error occurred during application execution", throwable);
            System.exit(0);
        });

        Container container = new Container();
        container.initializeContainer();
        SceneManagerService sceneManager = container.getService(SceneManagerService.class)
                .orElseThrow(() -> new NullPointerException(Container.SCENE_MANAGER_EXCEPTION));
        sceneManager.initializeScene(stage);
    }

    private void ShowInformation(Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Checkers Information");
        alert.setHeaderText("Es ist ein Fehler aufgetreten...");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}