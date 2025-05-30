package gui.Admin;


import java.util.List;

import MAin.CarRentalSystemApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import tools.Toolset;

public class VerifyUserPage {
     private List<User> users;
      private CarRentalSystemApp rentalSystem;
    private Stage primaryStage;
    private Toolset tools = new Toolset();
    public VerifyUserPage(CarRentalSystemApp rentalSystem,Stage stage,List<User> users) {
        this.primaryStage = stage;
        this.rentalSystem= rentalSystem;
        this.users=users;

    }
    public Scene getScene(){VBox root = new VBox(15);
    root.setPadding(new Insets(30));
    root.setAlignment(Pos.CENTER);
    root.setStyle("-fx-background-color: #ecf0f1;");

    Label titleLabel = new Label("Verify User");
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

    ComboBox<User> userComboBox = new ComboBox<>();
    for (User u : users) {
        if (!u.isVerified()) {
            userComboBox.getItems().add(u);
        }
    }
    userComboBox.setPromptText("Select user to verify");

    userComboBox.setCellFactory(param -> new ListCell<User>() {
        @Override
        protected void updateItem(User item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.getUsername() + " - " + item.getFullName());
        }
    });
    userComboBox.setButtonCell(new ListCell<User>() {
        @Override
        protected void updateItem(User item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.getUsername() + " - " + item.getFullName());
        }
    });

    Button verifyBtn = tools.createStyledButton("Verify", "#2ecc71");
    Button backBtn = tools.createStyledButton("Back", "#95a5a6");

    verifyBtn.setOnAction(e -> {
        User selectedUser = userComboBox.getValue();
        if (selectedUser == null) {
            tools.showAlert("Error", "Please select a user.");
        } else {
            selectedUser.setVerified(true);
            tools.showAlert("Success", "User " + selectedUser.getUsername() + " verified.");
            userComboBox.getItems().remove(selectedUser);
        }
    });

     backBtn.setOnAction(e -> {
        AdminMenuPage menu = new AdminMenuPage(rentalSystem,primaryStage,users,rentalSystem.getBookings());
                primaryStage.setScene(menu.getScene());
     });

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(verifyBtn, backBtn);

    root.getChildren().addAll(titleLabel, userComboBox, buttonBox);


         return new Scene(root, 400, 300);
    }

}

