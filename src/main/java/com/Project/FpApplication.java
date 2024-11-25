package com.Project;

import com.Project.Utils.DatabaseUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FpApplication extends Application {

    @Override
    public void start(Stage login) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FpApplication.class.getResource("/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        login.setTitle("Seller App!");
        login.setScene(scene);
        login.show();
    }

    @Override
    public void stop() {
        DatabaseUtils.closeConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}