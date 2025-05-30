package gui.Admin;

import tools.Toolset;


import java.util.List;

import MAin.CarRentalSystemApp;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import model.User;


public class UnverifiedUserPage {
    
      
 private CarRentalSystemApp rentalSystem;
    private Stage primaryStage;

    private List<User> users ;
    private Toolset tools = new Toolset();
    public UnverifiedUserPage(CarRentalSystemApp rentalSystem,Stage stage,List<User> users) {
        this.rentalSystem=rentalSystem;
        this.primaryStage = stage;
        this.users=users;
        
        
        

    }
     public Scene getScene() {
         VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titleLabel = new Label("Unverified Users");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        ListView<String> listView = new ListView<>();
        boolean found = false;
        for (User u : users) {
            if (!u.isVerified()) {
                listView.getItems().add(u.getUsername() + " : " + u.getFullName());
                found = true;
            }
        }
        
        if (!found) {
            listView.getItems().add("No unverified users.");
        }
        
        Button backBtn = tools.createStyledButton("Back", "#95a5a6");
         backBtn.setOnAction(e -> {AdminMenuPage menu = new AdminMenuPage(rentalSystem,primaryStage,users,rentalSystem.getBookings());
                primaryStage.setScene(menu.getScene());});
        
        root.getChildren().addAll(titleLabel, listView, backBtn);

        return new Scene(root, 400, 300);
     }
}

