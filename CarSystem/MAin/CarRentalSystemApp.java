package MAin;

import gui.User.UserLoginPage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Booking;
import model.Car;
import model.User;
import tools.Toolset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import gui.Admin.AdminLoginPage;

/**
 * Main class for launching the Car Rental System application.
 make BY DINIE
 
 */
public class CarRentalSystemApp extends Application {

    // Global lists to store users, cars, and bookings
    private List<User> users = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    // Utility class for UI styling and alerts
    private Toolset tools = new Toolset();

    // Main stage (window)
    private Stage primaryStage;

    /**
     * JavaFX entry point.
     */
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("University Car Rental System");

        initDummyData();     // Load some test data (users, cars, booking)
        showMainMenu();      // Display main menu
    }

    /**
     * Displays the main menu with buttons for User/Admin login.
     */
    public void showMainMenu() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Load and display logo 
        try {
            String imageUrl = "https://media2.giphy.com/media/v1.Y2lkPTc5MGI3NjExNHp4M2Q5eWM2am01eXprNWU0c2VkMGs4MHgxMHQ5ZHhjamJvMGhmOCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/3ov9jWu7BuHufyLs7m/giphy.gif";
            Image logoImage = new Image(imageUrl);
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitWidth(200);
            logoView.setPreserveRatio(true);
            root.getChildren().add(logoView);
        } catch (Exception e) {
            System.out.println("Failed to load logo image: " + e.getMessage());
        }

        // Title
        Label titleLabel = new Label("University Car Rental System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Login buttons
        Button adminLoginBtn = tools.createStyledButton("Admin Login", "#3498db");
        Button userLoginBtn = tools.createStyledButton("User Login", "#2ecc71");

        // Button actions
        adminLoginBtn.setOnAction(e -> showAdminLogin());
        userLoginBtn.setOnAction(e -> showUserLogin());

        root.getChildren().addAll(titleLabel, userLoginBtn, adminLoginBtn);

        // Set scene and show
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // === Getters ===
    public List<Booking> getBookings() {
        return bookings;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Car> getCars() {
        return cars;
    }

    /**
     * Shows the admin login page.
     */
    private void showAdminLogin() {
        AdminLoginPage loginPage = new AdminLoginPage(this, primaryStage, bookings, users);
        primaryStage.setScene(loginPage.getScene());
    }

    /**
     * Shows the user login page.
     */
    private void showUserLogin() {
        UserLoginPage loginPage = new UserLoginPage(this, primaryStage, users);
        primaryStage.setScene(loginPage.getScene());
    }

    /**
     * Initializes dummy users, cars, and bookings.
     */
    private void initDummyData() {
        // Dummy users
        User user1 = new User("Alice Johnson", "A111111", "UIA", "U12345", "alice", "pass1");
        user1.setVerified(true);
        User user2 = new User("Bob Lee", "B222222", "UIA", "U67890", "bob", "pass2");

        users.add(user1);
        users.add(user2);

        // Dummy cars
        cars.add(new Car("Toyota Camry", 80, 4.3, "https://images.dealer.com/...jpg"));
        cars.add(new Car("Honda Civic", 70, 4.4, "https://upload.wikimedia.org/...jpg"));
        cars.add(new Car("Mazda 3", 75, 4.6, "https://img-ik.cars.co.za/...jpg"));
        cars.add(new Car("Hyundai Elantra", 65, 4.3, "https://hips.hearstapps.com/...jpg"));
        cars.add(new Car("Nissan Altima", 78, 4.4, "https://www.bedfordnissan.com/...png"));

        // Dummy booking
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date rentDate = sdf.parse("2024-06-04 10:00");
            Date returnDate = sdf.parse("2024-06-05 18:00");

            Booking b = new Booking(user1, cars.get(0), rentDate, returnDate, false, "");
            b.setReturned(false);
            cars.get(0).setAvailable(false);
            bookings.add(b);
        } catch (Exception e) {
            tools.showAlert("Error", "Error creating dummy bookings: " + e.getMessage());
        }
    }

    /**
     * Main method to launch the JavaFX app.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
