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


public class BookCarPage {
    private CarRentalSystemApp rentalSystem ;
    private User loggedInUser;
    private Car selectedCar;

    private List<Car> cars;
    private Stage primaryStage;

    public BookCarPage(CarRentalSystemApp rentalSystem,Stage stage, User user, Car car, List<Car> cars) {
        this.primaryStage = stage;
        this.loggedInUser = user;
        this.selectedCar = car;
        this.cars=cars;
        this.rentalSystem=rentalSystem;
    
    }

    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("Booking Details for: " + selectedCar.getModel());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        DatePicker rentDatePicker = new DatePicker();
        ComboBox<String> rentTimeBox = createTimeComboBox();
        DatePicker returnDatePicker = new DatePicker();
        ComboBox<String> returnTimeBox = createTimeComboBox();

        CheckBox deliveryCheckBox = new CheckBox("Delivery Service");
        TextField deliveryLocationField = new TextField();
        deliveryLocationField.setPromptText("Delivery location");
        deliveryLocationField.setDisable(true);

        deliveryCheckBox.setOnAction(e -> deliveryLocationField.setDisable(!deliveryCheckBox.isSelected()));

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

        Button bookBtn = createStyledButton("Confirm Booking", "#2ecc71");
        Button backBtn = createStyledButton("Back", "#95a5a6");

        bookBtn.setOnAction(e -> {
            Date rentDate = parseDateTime(rentDatePicker.getValue(), rentTimeBox.getValue());
            Date returnDate = parseDateTime(returnDatePicker.getValue(), returnTimeBox.getValue());

            if (rentDate == null || returnDate == null || !returnDate.after(rentDate)) {
                showAlert("Error", "Invalid date/time selection.");
                return;
            }

            boolean delivery = deliveryCheckBox.isSelected();
            String deliveryLoc = deliveryLocationField.getText();

            if (delivery && deliveryLoc.trim().isEmpty()) {
                showAlert("Error", "Delivery location required.");
                return;
            }

            Booking booking = new Booking(loggedInUser, selectedCar, rentDate, returnDate, delivery, deliveryLoc);
           rentalSystem.getBookings().add(booking);//send to carrental
            selectedCar.setAvailable(false);


            showAlert("Success", "Booking created! Booking ID: " + booking.getBookingId());
            UserMenuPage menu = new UserMenuPage(rentalSystem,primaryStage, loggedInUser);
            primaryStage.setScene(menu.getScene());
        });

        backBtn.setOnAction(e -> {
            SelectCarpage menu = new SelectCarpage(rentalSystem,primaryStage, loggedInUser,cars);
            primaryStage.setScene(menu.getScene());
        });

        HBox buttonBox = new HBox(10, bookBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleLabel, grid, buttonBox);
        return new Scene(root, 600, 450);
    }

    private ComboBox<String> createTimeComboBox() {
        ComboBox<String> timeBox = new ComboBox<>();
        timeBox.getItems().addAll("08:00", "10:00", "12:00", "14:00", "16:00", "18:00");
        timeBox.setValue("08:00");
        return timeBox;
    }

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

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;");
        return btn;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
