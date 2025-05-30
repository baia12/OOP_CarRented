package gui.User;
import MAin.CarRentalSystemApp;
import gui.Booking.ReturnCarPage;
import gui.Booking.SelectCarpage;
import gui.Booking.ViewUserBookingsPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import tools.Toolset;

public class UserMenuPage {

    private CarRentalSystemApp rentalSystem;
    private Stage primaryStage;
    private User loggedInUser;

    private Toolset tools = new Toolset();

    public UserMenuPage(CarRentalSystemApp rentalSystem, Stage primaryStage, User user) {
        this.rentalSystem = rentalSystem;
        this.primaryStage = primaryStage;
        this.loggedInUser = user;
        
    }

    public Scene getScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("Welcome, " + loggedInUser.getFullName() + " 👋");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button bookCarBtn = tools.createStyledButton("Book a Car", "#3498db");
        Button returnCarBtn = tools.createStyledButton("Return a Car", "#e67e22");
        Button myBookingsBtn = tools.createStyledButton("View My Bookings", "#9b59b6");
        Button logoutBtn = tools.createStyledButton("Logout", "#e74c3c");

        bookCarBtn.setOnAction(e -> {
            SelectCarpage menu = new SelectCarpage(rentalSystem,primaryStage, loggedInUser,rentalSystem.getCars());
            primaryStage.setScene(menu.getScene());
        });

        myBookingsBtn.setOnAction(e -> {
            ViewUserBookingsPage menu = new ViewUserBookingsPage(rentalSystem,primaryStage, loggedInUser,rentalSystem.getBookings());
            primaryStage.setScene(menu.getScene());
        });

        returnCarBtn.setOnAction(e -> {
            ReturnCarPage menu = new ReturnCarPage(rentalSystem,primaryStage, loggedInUser,rentalSystem.getBookings());
            primaryStage.setScene(menu.getScene());
        });

        logoutBtn.setOnAction(e -> {
            loggedInUser = null;
            rentalSystem.showMainMenu();
        });

        root.getChildren().addAll(titleLabel, bookCarBtn, returnCarBtn, myBookingsBtn, logoutBtn);
        return new Scene(root, 400, 300);
    }
}
