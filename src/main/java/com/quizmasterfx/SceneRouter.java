package com.quizmasterfx;

import com.quizmasterfx.utils.AnimationManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneRouter {
    public static void switchTo(String fxmlPath) {
        Stage stage = Main.getPrimaryStage();
        if (stage == null) return;
        try {
            Parent next = FXMLLoader.load(SceneRouter.class.getResource(fxmlPath));
            Scene scene = stage.getScene();
            AnimationManager.fadeOutIn(scene.getRoot(), () -> {
                scene.setRoot(next);
                AnimationManager.fadeIn(next);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
