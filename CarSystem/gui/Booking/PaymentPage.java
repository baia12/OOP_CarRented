package gui.Booking;

import java.util.Date;
import MAin.CarRentalSystemApp;
import gui.User.UserMenuPage;
import tools.Toolset;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Booking;
import model.User;

/**
 * PaymentPage.java
 * This class handles the return process and payment confirmation for a booking.
 * 
 * Created by: FITRI RAZMAN
 */
public class PaymentPage {

    private Toolset tools = new Toolset(); // Utility class for styling and alerts
    private CarRentalSystemApp rentalSystem;
    private User loggedInUser;
    private Stage primaryStage;
    private Booking booking;
    private double fuel; // Fuel level returned by user

    // Constructor
    public PaymentPage(CarRentalSystemApp rentalSystem, Stage stage, Booking booking, double fuel, User loggedInUser) {
        this.primaryStage = stage;
        this.booking = booking;
        this.fuel = fuel;
        this.rentalSystem = rentalSystem;
        this.loggedInUser = loggedInUser;
    }

    // Generates the payment confirmation scene
    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        // Page title
        Label titleLabel = new Label("Payment 💸");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Calculate estimated cost
        Date now = new Date();
        boolean late = now.after(booking.getReturnDate()); // Check for late return
        double estimatedCost = booking.calculateBaseCost(); // Base cost
        if (booking.isDelivered()) estimatedCost += 15;     // Delivery fee
        if (late) estimatedCost += 20;                      // Late fee
        if (fuel < 15) estimatedCost += 10;                 // Low fuel penalty
        else if (fuel > 15) estimatedCost -= 5;             // Fuel bonus

        // Show estimated total cost
        Label costLabel = new Label("Total Cost: RM " + String.format("%.2f", estimatedCost));
        costLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495e;");

        // QR code image (for fun or simulation)
        Image qrImage = new Image("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://youtu.be/L8XbI9aJOXk?si=QjHpyAV40os3gHCo");
        ImageView qrView = new ImageView(qrImage);

        // Checkbox to simulate payment confirmation
        CheckBox paymentCheckBox = new CheckBox("Payment Completed");

        // Buttons
        Button confirmBtn = tools.createStyledButton("Confirm Return", "#e67e22");
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");

        // Confirm return logic
        confirmBtn.setOnAction(e -> {
            boolean paymentMade = paymentCheckBox.isSelected();
            if (paymentMade) {
                // Finalize booking with charges
                booking.finalizeCost(fuel, true, late);
                booking.generateReceipt();
                booking.getCar().setFuelLevel(fuel);
                booking.getCar().setAvailable(true);

                tools.showAlert("Success", "Car returned.\nTotal cost: RM " + String.format("%.2f", booking.getTotalCost()));
                
                // Redirect to user menu
                primaryStage.setScene(new UserMenuPage(rentalSystem, primaryStage, loggedInUser).getScene());
            } else {
                // Simulate blacklisting if user did not pay
                tools.showAlert("Blocked", "You are blacklisted from this rent car. Please pay.");
                tools.showRickrollScene(); // Rickroll for fun
                loggedInUser.setBlacklisted(true);
            }
        });

        // Back button logic
        backBtn.setOnAction(e -> {
            UserMenuPage menu = new UserMenuPage(rentalSystem, primaryStage, loggedInUser);
            primaryStage.setScene(menu.getScene());
        });

        // Layout buttons
        HBox buttons = new HBox(10, confirmBtn, backBtn);
        buttons.setAlignment(Pos.CENTER);

        // Add components to UI
        root.getChildren().addAll(titleLabel, costLabel, qrView, paymentCheckBox, buttons);

        return new Scene(root, 400, 450);
    }
}
