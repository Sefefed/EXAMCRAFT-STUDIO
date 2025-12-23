package com.quizmasterfx.utils;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class StyleManager {
    private static boolean dark = false;
    private static final List<Scene> scenes = new ArrayList<>();

    public static void registerScene(Scene scene) {
        if (!scenes.contains(scene)) scenes.add(scene);
    }

    public static boolean isDark() { return dark; }

    public static void toggleTheme() {
        dark = !dark;
        for (Scene scene : scenes) {
            scene.getStylesheets().removeIf(s -> s.contains("dark.css"));
            if (dark) {
                scene.getStylesheets().add(StyleManager.class.getResource("/styles/dark.css").toExternalForm());
            }
        }
    }
}
