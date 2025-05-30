package gui.Booking;

import java.util.List;

import MAin.CarRentalSystemApp;
import gui.User.UserMenuPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import model.Car;
import model.User;
import tools.Toolset;

public class SelectCarpage {

    private CarRentalSystemApp rentalSystem;
    private Toolset tools = new Toolset();
    private List<Car> cars;
    private Stage primaryStage;
    private User loggedInUser;

    public SelectCarpage(CarRentalSystemApp rentalSystem,Stage stage, User user, List<Car> cars) {
        this.primaryStage = stage;
        this.loggedInUser = user;
        this.cars = cars;
        this.rentalSystem=rentalSystem;
    }

    public Scene getScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Select a Car to Book");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        FlowPane carPane = new FlowPane();
        carPane.setHgap(15);
        carPane.setVgap(15);
        carPane.setAlignment(Pos.CENTER);
        carPane.setPadding(new Insets(10));
        carPane.setPrefWrapLength(550);
        carPane.setStyle("-fx-background-color: transparent;");

        if (!loggedInUser.isVerified()) {
            tools.showAlert("Error", "You must be verified by admin to book a car.");
            return new Scene(new VBox(), 400, 300); // fallback empty scene
        }

        for (Car car : cars) {
            if (!car.isAvailable()) continue;

            VBox carBox = new VBox(10);
            carBox.setAlignment(Pos.CENTER);
            carBox.setStyle("-fx-background-color: white;" +
                    "-fx-border-color:rgb(209, 209, 209);" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 15;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

            Label star = new Label(" ★");
            star.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 16px;");

            Label ratingLabel = new Label("Rating: " + car.getRating());
            ratingLabel.setStyle("-fx-text-fill: #555;");
            HBox ratingBox = new HBox(2, ratingLabel, star);
            ratingBox.setAlignment(Pos.CENTER);

            ImageView carImage = new ImageView(new Image(car.getImageUrl()));
            carImage.setFitWidth(150);
            carImage.setFitHeight(100);

            Label nameLabel = new Label(car.getModel());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333;");

            Button selectBtn = tools.createStyledButton("SELECT", "#2ecc71");
            selectBtn.setOnAction(e -> {
               
                tools.showAlert("Selected", "You selected: " + car.getModel());
                 BookCarPage menu = new BookCarPage(rentalSystem,primaryStage, loggedInUser,car,cars);
                primaryStage.setScene(menu.getScene());
            });

            carBox.getChildren().addAll(carImage, nameLabel, ratingBox, selectBtn);
            carPane.getChildren().add(carBox);
        }

        ScrollPane scrollPane = new ScrollPane(carPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background: transparent;");

        Button backBtn = tools.createStyledButton("Back", "#95a5a6");
        backBtn.setOnAction(e -> {
           
            UserMenuPage menu = new UserMenuPage(rentalSystem, primaryStage, loggedInUser);
            primaryStage.setScene(menu.getScene());
        });

        root.getChildren().addAll(title, scrollPane, backBtn);
        return new Scene(root, 600, 500);
    }
}
