package gui.Admin;

import tools.Toolset;
import java.util.List;
import MAin.CarRentalSystemApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Booking;
import model.User;

/**
 * AdminLoginPage.java
 * 
 * This class provides the login interface for administrators.
 * Admin credentials are hardcoded for simplicity.
 * 
 * Made by: WAN ZUL IR'FAN and DINIE HUMAIDI
 */

public class AdminLoginPage {

    // Tool for styled buttons and alerts
    private Toolset tools = new Toolset();

    // Hardcoded admin credentials
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";

    // Dependencies
    private Stage primaryStage;
    private List<Booking> bookings;
    private List<User> users;
    private CarRentalSystemApp rentalSystem;

    // Constructor
    public AdminLoginPage(CarRentalSystemApp rentalSystem, Stage primaryStage, List<Booking> bookings, List<User> users) {
        this.rentalSystem = rentalSystem;
        this.primaryStage = primaryStage;
        this.bookings = bookings;
        this.users = users;
    }

    // Create and return the login scene
    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        // Title
        Label titleLabel = new Label("Admin Login");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Input form
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
        Button loginBtn = tools.createStyledButton("Login", "#3498db");
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");

        // Login logic
        loginBtn.setOnAction(e -> {
            if (ADMIN_USERNAME.equals(usernameField.getText()) &&
                ADMIN_PASSWORD.equals(passwordField.getText())) {
                
                AdminMenuPage menu = new AdminMenuPage(rentalSystem, primaryStage, users, bookings);
                primaryStage.setScene(menu.getScene());
            } else {
                tools.showAlert("Error", "Incorrect admin credentials.");
            }
        });

        // Back to main menu
        backBtn.setOnAction(e -> rentalSystem.showMainMenu());

        // Button layout
        HBox buttonBox = new HBox(10, loginBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Final layout
        root.getChildren().addAll(titleLabel, grid, buttonBox);
        return new Scene(root, 400, 300);
    }
}
