package tools;

import java.net.URI;
import java.awt.Desktop;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * Toolset class provides reusable UI helper methods such as styled buttons,
 * alerts, and fun extras like Rickroll feature.
 * MAKE BY Dinie Humaidi
 */
public class Toolset {

    /**
     * Creates a JavaFX Button with custom color styling and hover effect.
     * 
     * @param text  The button label.
     * @param color The background color in hex (e.g., "#2ecc71").
     * @return A styled Button instance.
     */
    public Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(200);  // Set standard width
        button.setPrefHeight(40);  // Set standard height

        // Basic styling with dynamic color
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                        "-fx-font-size: 14px; -fx-background-radius: 5px;");

        // Hover effect: fade the button slightly on mouse hover
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.8;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.8;", "")));

        return button;
    }

    /**
     * Displays a basic JavaFX information alert with a title and message.
     * 
     * @param title   The title of the alert window.
     * @param message The main message text inside the alert.
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);          // No header
        alert.setContentText(message);      // Main message
        alert.showAndWait();                // Blocks until user closes it
    }

    /**
     * Opens a Rickroll video in the user's default web browser.
     * Used for fun or trolling within the app.
     */
    public void showRickrollScene() {
        try {
            // Opens a web browser with the Rickroll link
            Desktop.getDesktop().browse(new URI("https://youtu.be/xvFZjo5PgG0?si=_Ip-QQvxWJeYhzQ7"));
        } catch (Exception ex) {
            showAlert("Error", "Failed to open browser.");
        }
    }
}
