package gui.Booking;

import java.util.ArrayList;
import java.util.List;

import MAin.CarRentalSystemApp;
import gui.User.UserMenuPage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Booking;
import model.User;
import tools.Toolset;

/**
 * ViewUserBookingsPage.java
 * Shows a logged-in user their personal booking history.
 * 
 * Made by: FAIZUL and DINIE
 */
public class ViewUserBookingsPage {

    private List<Booking> bookings = new ArrayList<>();
    private CarRentalSystemApp rentalSystem;
    private User loggedInUser;
    private Stage primaryStage;
    private Toolset tools = new Toolset();

    // Constructor to pass main app, stage, user info and bookings
    public ViewUserBookingsPage(CarRentalSystemApp rentalSystem, Stage stage, User user, List<Booking> bookings) {
        this.primaryStage = stage;
        this.loggedInUser = user;
        this.bookings = bookings;
        this.rentalSystem = rentalSystem;
    }

    // Builds and returns the scene for viewing user-specific bookings
    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("My Bookings");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Text area to show booking details
        TextArea bookingsArea = new TextArea();
        bookingsArea.setEditable(false);
        bookingsArea.setPrefRowCount(12);

        // Populate only this user's bookings
        boolean found = false;
        StringBuilder sb = new StringBuilder();
        for (Booking b : bookings) {
            if (b.getUser().equals(loggedInUser)) {
                sb.append(b.toString()).append("\n\n");
                found = true;
            }
        }

        if (!found) {
            bookingsArea.setText("No bookings found.");
        } else {
            bookingsArea.setText(sb.toString());
        }

        // Back button to return to user menu
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");
        backBtn.setOnAction(e -> {
            UserMenuPage menu = new UserMenuPage(rentalSystem, primaryStage, loggedInUser);
            primaryStage.setScene(menu.getScene());
        });

        // Add all UI elements to root layout
        root.getChildren().addAll(titleLabel, bookingsArea, backBtn);

        return new Scene(root, 400, 300);
    }
}
