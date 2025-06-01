package gui.User;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import model.User;
import java.util.ArrayList;
import java.util.List;
import MAin.CarRentalSystemApp;
import javafx.geometry.*;
import tools.*;

/**
 * UserLoginPage.java
 * Login interface for users to access the system.
 * 
 * Created by: FITRI RAZMAN
 */
public class UserLoginPage {

    private CarRentalSystemApp rentalSystem; // Main app reference
    private Stage primaryStage;              // Main window
    private User loggedInUser;               // Stores the user after login
    private Toolset tools = new Toolset();   // Helper functions (buttons, alerts)
    private List<User> users = new ArrayList<>(); // Registered users list

    // Constructor to initialize required data
    public UserLoginPage(CarRentalSystemApp rentalSystem, Stage primaryStage, List<User> users) {
        this.rentalSystem = rentalSystem;
        this.primaryStage = primaryStage;
        this.users = users;
    }

    // Method to build and return the login scene
    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("User Login");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Form layout for username and password
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        // Buttons
        Button loginBtn = tools.createStyledButton("Login", "#2ecc71");
        Button registerBtn = tools.createStyledButton("Register", "#3b81eb");
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");

        // Login logic
        loginBtn.setOnAction(e -> {
            User u = findUserByUsername(usernameField.getText());
            if (u == null) {
                tools.showAlert("Error", "User not found.");
            } else if (!u.checkPassword(passwordField.getText())) {
                tools.showAlert("Error", "Incorrect password.");
            } else if (u.isBlacklisted()) {
                tools.showAlert("Error", "You are blacklisted. Contact admin.");
            } else {
                loggedInUser = u;
                UserMenuPage menu = new UserMenuPage(rentalSystem, primaryStage, loggedInUser);
                primaryStage.setScene(menu.getScene());
            }
        });

        // Register button logic
        registerBtn.setOnAction(e -> {
            UserRegisterPage menu = new UserRegisterPage(rentalSystem, primaryStage, users);
            primaryStage.setScene(menu.getScene());
        });

        // Back to main menu
        backBtn.setOnAction(e -> rentalSystem.showMainMenu());

        // Arrange buttons nicely
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, registerBtn);

        VBox vbuttonBox = new VBox(10);
        vbuttonBox.setAlignment(Pos.BOTTOM_CENTER);
        vbuttonBox.getChildren().addAll(backBtn);

        // Add all to root layout
        root.getChildren().addAll(titleLabel, grid, buttonBox, vbuttonBox);

        return new Scene(root, 400, 300);
    }

    // Helper method to find a user by username
    private User findUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) return u;
        }
        return null;
    }
}
