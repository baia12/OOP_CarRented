package gui.Booking;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Booking;
import model.User;
import tools.Toolset;

import java.util.ArrayList;
import java.util.List;

import MAin.CarRentalSystemApp;
import gui.User.UserMenuPage;

/**
 * ReturnCarPage.java
 * Handles the return process of a car by the user.
 * User selects an active booking to return, and the system proceeds to payment.
 * 
 * Made by: WAN SYAFIQ and DINIE
 */
public class ReturnCarPage {

    private Stage primaryStage;
    private CarRentalSystemApp rentalSystem;
    private User loggedInUser;
    private List<Booking> bookings;
    private Toolset tools = new Toolset(); // Helper tools (UI styling, alerts)

    // Constructor
    public ReturnCarPage(CarRentalSystemApp rentalSystem, Stage stage, User user, List<Booking> bookings) {
        this.primaryStage = stage;
        this.loggedInUser = user;
        this.rentalSystem = rentalSystem;
        this.bookings = bookings;
    }

    // Returns the main scene for selecting which booking to return
    public Scene getScene() {
        // Filter bookings: Only active ones for this user
        List<Booking> userActiveBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getUser().equals(loggedInUser) && !b.isReturned()) {
                userActiveBookings.add(b);
            }
        }

        // If user has no active bookings
        if (userActiveBookings.isEmpty()) {
            tools.showAlert("Info", "Bro you do not have any booking.");
            
            // Redirect to User Menu Page
           UserMenuPage menu = new UserMenuPage(rentalSystem, primaryStage, loggedInUser);
         return menu.getScene();
        }

        // UI Setup
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("Return a Car (Step 1)");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Dropdown for selecting booking
        ComboBox<Booking> bookingComboBox = new ComboBox<>();
        bookingComboBox.getItems().addAll(userActiveBookings);
        bookingComboBox.setPromptText("Select booking to return");

        // Buttons
        Button nextBtn = tools.createStyledButton("Next", "#2ecc71");
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");

        // Next button: go to payment
        nextBtn.setOnAction(e -> {
            Booking selectedBooking = bookingComboBox.getValue();
            if (selectedBooking == null) {
                tools.showAlert("Error", "Please select a booking.");
                return;
            }

            // Simulate fuel level (random for now)
            double fuel = Math.random() * 100;
            showReturnCarStep2(selectedBooking, fuel);
        });

        // Back to user menu
        backBtn.setOnAction(e -> {
            UserMenuPage menu = new UserMenuPage(rentalSystem, primaryStage, loggedInUser);
            primaryStage.setScene(menu.getScene());
        });

        root.getChildren().addAll(titleLabel, bookingComboBox, nextBtn, backBtn);
        return new Scene(root, 400, 300);
    }

    // Step 2: Payment confirmation and return
    private void showReturnCarStep2(Booking selectedBooking, double fuel) {
        PaymentPage paymentPage = new PaymentPage(rentalSystem, primaryStage, selectedBooking, fuel, loggedInUser);
        primaryStage.setScene(paymentPage.getScene());
    }
}
