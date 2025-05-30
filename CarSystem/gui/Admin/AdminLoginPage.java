package gui.Admin;

import tools.Toolset;

import java.util.List;

import MAin.CarRentalSystemApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Booking;

import model.User;


public class AdminLoginPage {
    private Toolset tools =new Toolset(); //call tool
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";
    private Stage primaryStage;
    private List<Booking> bookings ;
    private List<User> users ;
    private CarRentalSystemApp rentalSystem;
    public AdminLoginPage(CarRentalSystemApp rentalSystem, Stage primaryStage,List<Booking> bookings,List<User> users) {
        this.rentalSystem = rentalSystem;
        this.primaryStage = primaryStage;
        this.bookings=bookings;
        this.users=users;
    }

 public Scene getScene(){ VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titleLabel = new Label("Admin Login");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        Button loginBtn = tools.createStyledButton("Login", "#3498db");
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");
        
        loginBtn.setOnAction(e -> { 
            if (ADMIN_USERNAME.equals(usernameField.getText()) && 
                ADMIN_PASSWORD.equals(passwordField.getText())) {
               AdminMenuPage menu = new AdminMenuPage(rentalSystem,primaryStage,users,bookings);
                primaryStage.setScene(menu.getScene());
            } else {
                tools.showAlert("Error", "Incorrect admin credentials.");
            }
        });
        
        backBtn.setOnAction(e -> rentalSystem.showMainMenu());
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, backBtn);
        
        root.getChildren().addAll(titleLabel, grid, buttonBox);

    
    

    return new Scene(root, 400, 300);
}
}






