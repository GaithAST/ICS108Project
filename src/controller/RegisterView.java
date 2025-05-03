package controller;
import javafx.geometry.Insets;import javafx.scene.control.*;import javafx.scene.layout.*;import model.User;import util.DataStore;import java.util.function.Consumer;
public class RegisterView {
    private VBox root;
    public RegisterView(Runnable onBack, Consumer<User> onSuccess) {
        root=new VBox(10); root.setPadding(new Insets(20));
        TextField user=new TextField(); user.setPromptText("Choose username");
        PasswordField pass=new PasswordField(); pass.setPromptText("Choose password");
        Label error=new Label(); Button register=new Button("Register"); Button back=new Button("Back");
        register.setOnAction(e->{ DataStore.register(user.getText(),pass.getText()).ifPresentOrElse(onSuccess,()->error.setText("Username exists"));});
        back.setOnAction(e->onBack.run());
        root.getChildren().addAll(user,pass,register,back,error);
    }
    public Pane getRoot(){return root;}
}
