package gui.User;

import MAin.CarRentalSystemApp;
import gui.Booking.ReturnCarPage;
import gui.Booking.SelectCarpage;
import gui.Booking.ViewUserBookingsPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import tools.Toolset;

/**
 * UserMenuPage.java
 * This page is shown after a user logs in.
 * It lets the user book a car, return a car, view their bookings, or log out.
 * 
 * Created by: Dinie Huamaidi
 */
public class UserMenuPage {

    private CarRentalSystemApp rentalSystem;  // Main app reference
    private Stage primaryStage;               // JavaFX main window
    private User loggedInUser;                // The user who is logged in

    private Toolset tools = new Toolset();    // For styled buttons and alerts

    // Constructor
    public UserMenuPage(CarRentalSystemApp rentalSystem, Stage primaryStage, User user) {
        this.rentalSystem = rentalSystem;
        this.primaryStage = primaryStage;
        this.loggedInUser = user;
    }

    // This builds and returns the scene for the user's main menu
    public Scene getScene() {
        VBox root = new VBox(15); // Vertical layout with spacing
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        // Welcome message with user full name
        Label titleLabel = new Label("Welcome, " + loggedInUser.getFullName() + " 👋");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Create buttons using the toolset helper
        Button bookCarBtn = tools.createStyledButton("Book a Car", "#3498db");
        Button returnCarBtn = tools.createStyledButton("Return a Car", "#e67e22");
        Button myBookingsBtn = tools.createStyledButton("View My Bookings", "#9b59b6");
        Button logoutBtn = tools.createStyledButton("Logout", "#e74c3c");

        // When "Book a Car" is clicked
        bookCarBtn.setOnAction(e -> {
            SelectCarpage menu = new SelectCarpage(rentalSystem, primaryStage, loggedInUser, rentalSystem.getCars());
            primaryStage.setScene(menu.getScene());
        });

        // When "View My Bookings" is clicked
        myBookingsBtn.setOnAction(e -> {
            ViewUserBookingsPage menu = new ViewUserBookingsPage(rentalSystem, primaryStage, loggedInUser, rentalSystem.getBookings());
            primaryStage.setScene(menu.getScene());
        });

        // When "Return a Car" is clicked
        returnCarBtn.setOnAction(e -> {
            ReturnCarPage menu = new ReturnCarPage(rentalSystem, primaryStage, loggedInUser, rentalSystem.getBookings());
            primaryStage.setScene(menu.getScene());
        });

        // When "Logout" is clicked
        logoutBtn.setOnAction(e -> {
            loggedInUser = null; // Clear session
            rentalSystem.showMainMenu(); // Go back to home page
        });

        // Add everything to the screen
        root.getChildren().addAll(titleLabel, bookCarBtn, returnCarBtn, myBookingsBtn, logoutBtn);
        return new Scene(root, 400, 300); // Return a new scene
    }
}
