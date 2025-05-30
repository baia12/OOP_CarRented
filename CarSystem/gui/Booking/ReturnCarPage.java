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

public class ReturnCarPage {

    private Stage primaryStage;
    private CarRentalSystemApp rentalSystem;
    private User loggedInUser;
   
    private List<Booking> bookings;
    private Toolset tools = new Toolset();

    public ReturnCarPage(CarRentalSystemApp rentalSystem,Stage stage, User user, List<Booking> bookings) {
        this.primaryStage = stage;
        this.loggedInUser = user;
        this.rentalSystem=rentalSystem;
        this.bookings = bookings;
    }

    public Scene getScene() {
        List<Booking> userActiveBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getUser().equals(loggedInUser) && !b.isReturned()) {
                userActiveBookings.add(b);
            }
        }

        if (userActiveBookings.isEmpty()) {
            tools.showAlert("Info", "Bro you do not have any booking.");
            // Optional: redirect to user menu
            return new Scene(new VBox(), 400, 200);
        }

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("Return a Car (Step 1)");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        ComboBox<Booking> bookingComboBox = new ComboBox<>();
        bookingComboBox.getItems().addAll(userActiveBookings);
        bookingComboBox.setPromptText("Select booking to return");

        Button nextBtn = tools.createStyledButton("Next", "#2ecc71");
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");

        nextBtn.setOnAction(e -> {
            Booking selectedBooking = bookingComboBox.getValue();
            if (selectedBooking == null) {
                tools.showAlert("Error", "Please select a booking.");
                return;
            }
            // simulate random fuel level between 0–100
            double fuel = Math.random() * 100;
            showReturnCarStep2(selectedBooking, fuel);
        });

        backBtn.setOnAction(e -> {
             UserMenuPage menu = new UserMenuPage(rentalSystem, primaryStage, loggedInUser);
    primaryStage.setScene(menu.getScene());
            
        });

        root.getChildren().addAll(titleLabel, bookingComboBox, nextBtn, backBtn);
        return new Scene(root, 400, 300);
    }

    private void showReturnCarStep2(Booking selectedBooking, double fuel) {
        // Go to payment/confirmation page
        PaymentPage paymentPage = new PaymentPage(rentalSystem,primaryStage, selectedBooking, fuel,loggedInUser);
        primaryStage.setScene(paymentPage.getScene());
    }
}
