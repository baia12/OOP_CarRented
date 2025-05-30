package tools;
import java.net.URI;
import java.awt.Desktop;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;


public class Toolset {

    public Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                      "-fx-font-size: 14px; -fx-background-radius: 5px;");
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.8;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.8;", "")));
        return button;
    }
    public void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
    public void showRickrollScene() {
    try {
        Desktop.getDesktop().browse(new URI("https://youtu.be/xvFZjo5PgG0?si=_Ip-QQvxWJeYhzQ7"));
    } catch (Exception ex) {
        showAlert("Error", "Failed to open browser.");
    }
    return;

}


}
