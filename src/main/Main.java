package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Main.primaryStage = primaryStage;
        Scene  mainScene = SceneBuilder.createScene();

        mainController controller = new mainController();
        controller.initialize();

        primaryStage.setTitle("Edge Detection");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
    * Get the primary Stage.
    * Used for creating dialog Stages(being their owner)
    */
    public static Stage getPrimaryStage()
    {
        return Main.primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
