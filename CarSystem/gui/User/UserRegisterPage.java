package gui.User;

import java.util.ArrayList;
import java.util.List;

import MAin.CarRentalSystemApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import tools.Toolset;

public class UserRegisterPage {
   
    private Stage primaryStage;
     private CarRentalSystemApp rentalSystem;
   
    private Toolset tools =new Toolset(); //call tool
     private List<User> users = new ArrayList<>();
     
    public UserRegisterPage(CarRentalSystemApp rentalSystem,Stage primaryStage,List<User> users) {
        this.rentalSystem = rentalSystem;
         this.primaryStage = primaryStage;
         this.users = users;
        
    }
    public Scene getScene() {
        VBox root = new VBox(15);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(20));
    root.setStyle("-fx-background-color: #ecf0f1;");
    
    Label titleLabel = new Label("User Registration");
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
    
   
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setAlignment(Pos.CENTER);
    TextField fullNameField = new TextField();
    TextField icField = new TextField();
    TextField universityField = new TextField();
    TextField matricField = new TextField();
    TextField usernameField = new TextField();
    PasswordField passwordField = new PasswordField();
    
    grid.add(new Label("Full Name:"), 0, 0);
    grid.add(fullNameField, 1, 0);
    grid.add(new Label("IC Number:"), 0, 1);
    grid.add(icField, 1, 1);
    grid.add(new Label("University:"), 0, 2);
    grid.add(universityField, 1, 2);
    grid.add(new Label("Matric Number:"), 0, 3);
    grid.add(matricField, 1, 3);
    grid.add(new Label("Username:"), 0, 4);
    grid.add(usernameField, 1, 4);
    grid.add(new Label("Password:"), 0, 5);
    grid.add(passwordField, 1, 5);
    
    Button registerBtn = tools.createStyledButton("Register", "#2ecc71");
    Button backBtn = tools.createStyledButton("Back", "#95a5a6");
    
    registerBtn.setOnAction(e -> {
        if (findUserByUsername(usernameField.getText()) != null) {
            tools.showAlert("Error", "Username already taken.");
        } else {
            if (fullNameField.getText().trim().isEmpty() ||
             icField.getText().trim().isEmpty() ||
         universityField.getText().trim().isEmpty() ||
        matricField.getText().trim().isEmpty() ||
        usernameField.getText().trim().isEmpty() ||
             passwordField.getText().trim().isEmpty()) {
    
         tools.showAlert("Error", "All fields are required.");
         return;
}
            User user = new User(fullNameField.getText(), icField.getText(), 
            universityField.getText(), matricField.getText(),
            usernameField.getText(), passwordField.getText());
            users.add(user);
            tools.showAlert("Success", "Registered successfully. Please wait for admin verification.");
            UserLoginPage menu = new UserLoginPage(rentalSystem,primaryStage,users);
                  primaryStage.setScene(menu.getScene());
        }
    });
    
    backBtn.setOnAction(e -> {
         UserLoginPage menu = new UserLoginPage(rentalSystem,primaryStage,users);
                primaryStage.setScene(menu.getScene());
    });
    
    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backBtn,registerBtn);
    
    root.getChildren().addAll(titleLabel, grid, buttonBox);
    
        
        return new Scene(root, 400, 300);
    }

private User findUserByUsername(String username) {
    for (User u : users) {
        if (u.getUsername().equalsIgnoreCase(username)) return u;
    }
    return null;
}
}


