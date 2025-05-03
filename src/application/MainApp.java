package application;

import controller.AdminView;
import controller.LoginView;
import controller.RegisterView;
import controller.UserView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.UserSession;
import util.DataStore;

public class MainApp extends Application {
    private static Stage primaryStage;
    private static UserSession session = new UserSession();

    @Override
    public void start(Stage stage) {
        DataStore.loadAll();
        primaryStage = stage;
        showLogin();
        primaryStage.setOnCloseRequest(e -> DataStore.saveAll());
        primaryStage.show();
    }

    public static void showLogin() {
        LoginView view = new LoginView(
                user -> { session.setUser(user); if (user.isAdmin()) showAdmin(); else showUser(); },
                () -> showRegister()
        );
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(view.getRoot(), 400, 300));
    }

    public static void showRegister() {
        RegisterView view = new RegisterView(
                () -> showLogin(),
                newUser -> { session.setUser(newUser); showUser(); }
        );
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(new Scene(view.getRoot(), 400, 350));
    }

    public static void showUser() {
        UserView view = new UserView(session.getUser());
        primaryStage.setTitle("User Panel - " + session.getUser().getUsername());
        primaryStage.setScene(new Scene(view.getRoot(), 600, 400));
    }

    public static void showAdmin() {
        AdminView view = new AdminView();
        primaryStage.setTitle("Admin Panel - " + session.getUser().getUsername());
        primaryStage.setScene(new Scene(view.getRoot(), 800, 500));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
