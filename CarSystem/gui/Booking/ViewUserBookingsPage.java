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

public class ViewUserBookingsPage {
    
    private List<Booking> bookings = new ArrayList<>();
    private CarRentalSystemApp rentalSystem ;
    private User loggedInUser;
    private Stage primaryStage;
    private Toolset tools = new Toolset();
    public ViewUserBookingsPage(CarRentalSystemApp rentalSystem,Stage stage,User user,List<Booking> bookings) {
        this.primaryStage = stage;
        this.loggedInUser=user;
        this.bookings = bookings; 
        this.rentalSystem =rentalSystem;

    }
     public Scene getScene(){ 
        VBox root = new VBox(15);
    root.setPadding(new Insets(20));
    root.setStyle("-fx-background-color: #ecf0f1;");
    
    Label titleLabel = new Label("My Bookings");
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
    
    TextArea bookingsArea = new TextArea();
    bookingsArea.setEditable(false);
    bookingsArea.setPrefRowCount(12);
    
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
    
    Button backBtn = tools.createStyledButton("Back", "#95a5a6");
    backBtn.setOnAction(e -> {
         UserMenuPage menu = new UserMenuPage(rentalSystem, primaryStage, loggedInUser);
    primaryStage.setScene(menu.getScene());
    });
    
    root.getChildren().addAll(titleLabel, bookingsArea, backBtn);
    
        
        
        return new Scene(root, 400, 300);}
}
