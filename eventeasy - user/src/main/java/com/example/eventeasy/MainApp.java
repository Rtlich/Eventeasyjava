package com.example.eventeasy;

import com.example.eventeasy.entities.User;
import com.example.eventeasy.entities.UserRole;
import com.example.eventeasy.utils.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    public static Stage mainStage;
    private static MainApp instance;
    private static User session;

    public static void main(String[] args) {
        launch(args);
    }

    public static MainApp getInstance() {
        if (instance == null) {
            instance = new MainApp();
        }
        return instance;
    }

    public static User getSession() {
        return session;
    }

    public static void setSession(User session) {
        MainApp.session = session;
    }

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        loadLogin();
    }

    public void loadLogin() {
        loadScene(
                Constants.FXML_LOGIN,
                "Connexion"
        );
    }

    public void loadForgotFirst() {
        loadScene(
                Constants.FXML_FORGOT_FIRST,
                "Connexion"
        );
    }

    public void loadForgotSecond() {
        loadScene(
                Constants.FXML_FORGOT_SECOND,
                "Connexion"
        );
    }

    public void loadForgotThird() {
        loadScene(
                Constants.FXML_FORGOT_THIRD,
                "Connexion"
        );
    }

    public void loadSignup() {
        loadScene(
                Constants.FXML_SIGNUP,
                "Inscription"
        );
    }

    public void loadFront() {
        loadScene(
                Constants.FXML_FRONT_MAIN_WINDOW,
                ""
        );
    }

    public void loadBack() {
        loadScene(
                Constants.FXML_BACK_MAIN_WINDOW,
                ""
        );
    }

    public void login(User user) {
        MainApp.setSession(user);

        if (user.getRole().equals(UserRole.Admin)) {
            loadBack();
        } else {
            loadFront();
        }
    }

    public void logout() {
        session = null;

        System.out.println("Deconnexion ..");
        loadLogin();
    }

    private void loadScene(String fxmlLink, String title) {
        try {
            Stage primaryStage = mainStage;
            primaryStage.close();
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlLink))));
            scene.setFill(Color.TRANSPARENT);

            //primaryStage.getIcons().add(new Image("app/images/app-icon.png"));
            primaryStage.setTitle(title);
            primaryStage.setWidth(1100);
            primaryStage.setHeight(700);
            primaryStage.setMinWidth(1100);
            primaryStage.setMinHeight(700);
            primaryStage.setScene(scene);
            primaryStage.setX((Screen.getPrimary().getBounds().getWidth() / 2) - (1100 / 2.0));
            primaryStage.setY((Screen.getPrimary().getBounds().getHeight() / 2) - (700 / 2.0));

            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
