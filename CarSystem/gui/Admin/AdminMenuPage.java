package gui.Admin;

import tools.Toolset;
import java.util.List;
import MAin.CarRentalSystemApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Booking;
import model.User;

/**
 * AdminMenuPage.java
 * 
 * This class serves as the main control panel for the admin.
 * Admin can view unverified users, verify users, see all bookings, or log out.
 * 
 * Made by: DINIE HUMAIDI 
 */

public class AdminMenuPage {

    private CarRentalSystemApp rentalSystem;
    private Toolset tools = new Toolset();  // Utility for UI components
    private Stage primaryStage;
    private List<Booking> bookings;
    private List<User> users;

    // Constructor with dependencies
    public AdminMenuPage(CarRentalSystemApp rentalSystem, Stage stage, List<User> users, List<Booking> bookings) {
        this.primaryStage = stage;
        this.rentalSystem = rentalSystem;
        this.users = users;
        this.bookings = bookings;
    }

    // Generates the admin menu interface
    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("Admin Panel");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button viewUnverifiedBtn = tools.createStyledButton("View Unverified Users", "#9b59b6");
        Button verifyUserBtn = tools.createStyledButton("Verify User", "#2ecc71");
        Button viewBookingsBtn = tools.createStyledButton("View All Bookings", "#34495e");
        Button logoutBtn = tools.createStyledButton("Logout", "#e74c3c");

        // Navigation: View Unverified Users
        viewUnverifiedBtn.setOnAction(e -> {
            UnverifiedUserPage menu = new UnverifiedUserPage(rentalSystem, primaryStage, users);
            primaryStage.setScene(menu.getScene());
        });

        // Navigation: Verify User
        verifyUserBtn.setOnAction(e -> {
            VerifyUserPage menu = new VerifyUserPage(rentalSystem, primaryStage, users);
            primaryStage.setScene(menu.getScene());
        });

        // Navigation: View All Bookings
        viewBookingsBtn.setOnAction(e -> {
            ViewAllBookingsPage menu = new ViewAllBookingsPage(rentalSystem, primaryStage, bookings);
            primaryStage.setScene(menu.getScene());
        });

        // Logout button
        logoutBtn.setOnAction(e -> rentalSystem.showMainMenu());

        // Add all buttons to layout
        root.getChildren().addAll(titleLabel, viewUnverifiedBtn, verifyUserBtn, viewBookingsBtn, logoutBtn);
        return new Scene(root, 400, 300);
    }
}
