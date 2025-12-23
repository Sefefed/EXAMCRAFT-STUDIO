package com.quizmasterfx;

/*
javac -d out --module-path "C:/java/javafx-sdk-17.0.17/lib" --add-modules javafx.controls,javafx.fxml @out/sources.txt && java --module-path "C:/java/javafx-sdk-17.0.17/lib" --add-modules javafx.controls,javafx.fxml -Djava.library.path="C:/java/javafx-sdk-17.0.17/bin" -cp out com.quizmasterfx.Main
 */

import com.quizmasterfx.utils.StyleManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/view/AdminDashboard.fxml"));
        Scene scene = new Scene(root, 1100, 720);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        StyleManager.registerScene(scene);

        stage.setTitle("QuizMaster FX - Interactive Quiz Platform");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
