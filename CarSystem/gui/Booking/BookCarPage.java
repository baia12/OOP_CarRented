package gui.Booking;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Booking;
import model.Car;
import model.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import MAin.CarRentalSystemApp;
import gui.User.UserMenuPage;
import tools.Toolset;

/**
 * BookCarPage.java
 * This class handles the car booking form for users.
 * 
 * Created by: FAIZUL ISYRAF
 */
public class BookCarPage {
    private CarRentalSystemApp rentalSystem;
    private User loggedInUser;
    private Car selectedCar;
    private List<Car> cars;
    private Stage primaryStage;
    private Toolset tools = new Toolset();  // Utility class for styling and alerts

    // Constructor to initialize the booking page
    public BookCarPage(CarRentalSystemApp rentalSystem, Stage stage, User user, Car car, List<Car> cars) {
        this.primaryStage = stage;
        this.loggedInUser = user;
        this.selectedCar = car;
        this.cars = cars;
        this.rentalSystem = rentalSystem;
    }

    // Main UI scene for booking page
    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        // Title
        Label titleLabel = new Label("Booking Details for: " + selectedCar.getModel());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // Date and time selection
        DatePicker rentDatePicker = new DatePicker();
        ComboBox<String> rentTimeBox = createTimeComboBox();
        DatePicker returnDatePicker = new DatePicker();
        ComboBox<String> returnTimeBox = createTimeComboBox();

        // Delivery options
        CheckBox deliveryCheckBox = new CheckBox("Delivery Service");
        TextField deliveryLocationField = new TextField();
        deliveryLocationField.setPromptText("Delivery location");
        deliveryLocationField.setDisable(true);

        // Toggle delivery field based on checkbox
        deliveryCheckBox.setOnAction(e -> deliveryLocationField.setDisable(!deliveryCheckBox.isSelected()));

        // Add form components to grid
        grid.add(new Label("Rental Date:"), 0, 0);
        grid.add(rentDatePicker, 1, 0);
        grid.add(new Label("Rental Time:"), 0, 1);
        grid.add(rentTimeBox, 1, 1);
        grid.add(new Label("Return Date:"), 0, 2);
        grid.add(returnDatePicker, 1, 2);
        grid.add(new Label("Return Time:"), 0, 3);
        grid.add(returnTimeBox, 1, 3);
        grid.add(deliveryCheckBox, 0, 4);
        grid.add(deliveryLocationField, 1, 4);

        // Buttons
        Button bookBtn = tools.createStyledButton("Confirm Booking", "#2ecc71");
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");

        // Booking logic
        bookBtn.setOnAction(e -> {
            Date rentDate = parseDateTime(rentDatePicker.getValue(), rentTimeBox.getValue());
            Date returnDate = parseDateTime(returnDatePicker.getValue(), returnTimeBox.getValue());

            // Basic validation
            if (rentDate == null || returnDate == null || !returnDate.after(rentDate)) {
                tools.showAlert("Error", "Invalid date/time selection.");
                return;
            }

            boolean delivery = deliveryCheckBox.isSelected();
            String deliveryLoc = deliveryLocationField.getText();

            if (delivery && deliveryLoc.trim().isEmpty()) {
                tools.showAlert("Error", "Delivery location required.");
                return;
            }

            // Create booking
            Booking booking = new Booking(loggedInUser, selectedCar, rentDate, returnDate, delivery, deliveryLoc);
            rentalSystem.getBookings().add(booking);  // Store booking in system
            selectedCar.setAvailable(false);  // Mark car as unavailable

            // Confirmation
            tools.showAlert("Success", "Booking created! Booking ID: " + booking.getBookingId());

            // Redirect to user menu
            primaryStage.setScene(new UserMenuPage(rentalSystem, primaryStage, loggedInUser).getScene());
        });

        // Go back to car selection
        backBtn.setOnAction(e -> {
            SelectCarpage menu = new SelectCarpage(rentalSystem, primaryStage, loggedInUser, cars);
            primaryStage.setScene(menu.getScene());
        });

        HBox buttonBox = new HBox(10, bookBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleLabel, grid, buttonBox);
        return new Scene(root, 600, 450);
    }

    // Helper to create a time selection dropdown
    private ComboBox<String> createTimeComboBox() {
        ComboBox<String> timeBox = new ComboBox<>();
        timeBox.getItems().addAll("08:00", "10:00", "12:00", "14:00", "16:00", "18:00");
        timeBox.setValue("08:00");
        return timeBox;
    }

    // Combine date and time strings into java.util.Date object
    private Date parseDateTime(java.time.LocalDate date, String time) {
        try {
            if (date == null || time == null) return null;
            String dateTimeStr = date + "T" + time + ":00";
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr);
            return java.sql.Timestamp.valueOf(localDateTime);
        } catch (Exception e) {
            return null;
        }
    }
}
