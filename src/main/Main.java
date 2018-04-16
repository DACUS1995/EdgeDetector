package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage = null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Main.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Edge Detection");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    /**
    * Get the primary Stage.
    * Used for creating diaalog Stages(being their owner)
    */
    public static Stage getPrimaryStage()
    {
        return Main.primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
