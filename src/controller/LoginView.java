package controller;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.User;
import util.DataStore;
import java.util.function.Consumer;


public class LoginView {
    private VBox root;
    public LoginView(Consumer<User> onSuccess, Runnable onRegister) {
        root=new VBox(10); root.setPadding(new Insets(20));
        TextField user=new TextField(); user.setPromptText("Username");
        PasswordField pass=new PasswordField(); pass.setPromptText("Password");
        Label error=new Label(); Button login=new Button("Login"); Button signup=new Button("Sign Up");
        login.setOnAction(e->{ DataStore.authenticate(user.getText(),pass.getText()).ifPresentOrElse(onSuccess,()->error.setText("Invalid credentials"));});
        signup.setOnAction(e->onRegister.run());
        root.getChildren().addAll(user,pass,login,signup,error);
    }
    public Pane getRoot(){return root;}
}