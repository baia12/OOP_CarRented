package gui.Admin;

import java.util.List;

import MAin.CarRentalSystemApp;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Booking;
import tools.Toolset;

/**
 * ViewAllBookingsPage.java
 * 
 * This page allows the admin to view a list of all user bookings.
 * It displays booking details in a non-editable TextArea.
 *
 * Made by: WAN ZUL IR'FAN and FAIZUL ISYRAF
 */

public class ViewAllBookingsPage {
    private List<Booking> bookings;
    private CarRentalSystemApp rentalSystem;
    private Stage primaryStage;
    private Toolset tools = new Toolset();

    // Constructor
    public ViewAllBookingsPage(CarRentalSystemApp rentalSystem, Stage stage, List<Booking> bookings) {
        this.primaryStage = stage;
        this.rentalSystem = rentalSystem;
        this.bookings = bookings;
    }

    // Returns the full scene layout
    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ecf0f1;");

        // Title label
        Label titleLabel = new Label("All Bookings");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // TextArea to show booking information
        TextArea bookingsArea = new TextArea();
        bookingsArea.setEditable(false);
        bookingsArea.setPrefRowCount(15);

        // Populate booking data
        if (bookings.isEmpty()) {
            bookingsArea.setText("No bookings found.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Booking b : bookings) {
                sb.append(b.toString()).append("\n\n");
            }
            bookingsArea.setText(sb.toString());
        }

        // Back button
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");
        backBtn.setOnAction(e -> {
            AdminMenuPage menu = new AdminMenuPage(rentalSystem, primaryStage, rentalSystem.getUsers(), bookings);
            primaryStage.setScene(menu.getScene());
        });

        // Add elements to the layout
        root.getChildren().addAll(titleLabel, bookingsArea, backBtn);
        return new Scene(root, 400, 300);
    }
}
