package com.wicpar.sinking_simulator.engine.crashes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by fnieto on 1/6/17.
 */
public class CrashApp extends Application {
    private static String error;
    private static boolean loaded = false;
    private static TextArea errorPanel;

    public static void catchMessageWithGUI(final Exception e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        error = sw.toString();
        if (!loaded) {
            loaded = true;
            launch();
        } else {
            errorPanel.setText(error);
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("error");

        errorPanel = new TextArea();
        errorPanel.setText(error);
        errorPanel.setEditable(false);

        final StackPane root = new StackPane();
        root.getChildren().add(errorPanel);
        primaryStage.setScene(new Scene(root, 750, 250));
        primaryStage.show();
    }
}
