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

public class ViewAllBookingsPage {
   
    private List<Booking> bookings ;
     private CarRentalSystemApp rentalSystem;

    private Stage primaryStage;
    private Toolset tools = new Toolset();
    public ViewAllBookingsPage(CarRentalSystemApp rentalSystem,Stage stage,List<Booking> bookings) {
        this.primaryStage = stage;
        this.rentalSystem = rentalSystem;
        this.bookings=bookings;

    }
    public Scene getScene(){
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titleLabel = new Label("All Bookings");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        TextArea bookingsArea = new TextArea();
        bookingsArea.setEditable(false);
        bookingsArea.setPrefRowCount(15);
        
        if (bookings.isEmpty()) {
            bookingsArea.setText("No bookings found.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Booking b : bookings) {
                sb.append(b.toString()).append("\n\n");
            }
            bookingsArea.setText(sb.toString());
        }
        
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");
       backBtn.setOnAction(e ->
       {AdminMenuPage menu = new AdminMenuPage(rentalSystem,primaryStage,rentalSystem.getUsers(),bookings);
                primaryStage.setScene(menu.getScene());}
                );
        
        root.getChildren().addAll(titleLabel, bookingsArea, backBtn);
        
        
        
        
        
        
        return new Scene(root, 400, 300);}
    
}
