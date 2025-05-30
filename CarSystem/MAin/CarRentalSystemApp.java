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



public class CarRentalSystemApp extends Application{
      private List<User> users = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private Toolset tools =new Toolset();
    private Stage primaryStage;
     @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("University Car Rental System");
        initDummyData();
        showMainMenu();
    }///first start okay
    public void showMainMenu() {
       VBox root = new VBox(20);
    root.setPadding(new Insets(30));
    root.setAlignment(Pos.CENTER);
    root.setStyle("-fx-background-color: #f0f0f0;");

    // Load image from URL
    try {
        String imageUrl = "https://media2.giphy.com/media/v1.Y2lkPTc5MGI3NjExNHp4M2Q5eWM2am01eXprNWU0c2VkMGs4MHgxMHQ5ZHhjamJvMGhmOCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/3ov9jWu7BuHufyLs7m/giphy.gif"; // example IIUM logo
        Image logoImage = new Image(imageUrl);
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(200);
    
        logoView.setPreserveRatio(true);
        root.getChildren().add(logoView);
    } catch (Exception e) {
        System.out.println("Failed to load logo image: " + e.getMessage());
    }

    Label titleLabel = new Label("University Car Rental System");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

    Button adminLoginBtn = tools.createStyledButton("Admin Login", "#3498db");
    Button userLoginBtn = tools.createStyledButton("User Login", "#2ecc71");
   


    adminLoginBtn.setOnAction(e -> showAdminLogin());
    userLoginBtn.setOnAction(e -> showUserLogin());
    root.getChildren().addAll(titleLabel, userLoginBtn,adminLoginBtn);    
    Scene scene = new Scene(root, 500, 500);
    primaryStage.setScene(scene);
        primaryStage.show();
    }
   
   public List<Booking> getBookings() {
    return bookings; // or however you're storing bookings
}
 public List<User> getUsers() {
    return users; // just getteer
}
public List<Car> getCars() {
    return cars; // just getteer
}

    private void showAdminLogin() {
        AdminLoginPage loginPage = new AdminLoginPage(this,primaryStage,bookings,users);
        primaryStage.setScene(loginPage.getScene());
        
    }
    private void showUserLogin() {
    UserLoginPage loginPage = new UserLoginPage(this,primaryStage,users);
    primaryStage.setScene(loginPage.getScene());
}
private void initDummyData() {
        // Adding users
        User user1 = new User("Alice Johnson", "A111111", "UIA", "U12345", "alice", "pass1");
        user1.setVerified(true);
        User user2 = new User("Bob Lee", "B222222", "UIA", "U67890", "bob", "pass2");
        users.add(user1);
        users.add(user2);
        
        // Adding cars
       cars.add(new Car("Toyota Camry", 80, 4.3, "https://images.dealer.com/ddc/vehicles/2025/Toyota/Camry/Sedan/trim_LE_4b85cc/color/Underground-1L7-83%2C81%2C83-640-en_US.jpg?impolicy=downsize_bkpt&imdensity=1&w=520"));
       cars.add(new Car("Honda Civic", 70, 4.4, "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Honda_Civic_Type_R_%28FK%3B_France%29_front_view.jpg/500px-Honda_Civic_Type_R_%28FK%3B_France%29_front_view.jpg"));
       cars.add(new Car("Mazda 3", 75, 4.6, "https://img-ik.cars.co.za/news-site-za/images/2016/08/mazda3-4.jpg?tr=h-347,w-617,q-80"));
       cars.add(new Car("Hyundai Elantra", 65, 4.3, "https://hips.hearstapps.com/hmg-prod/images/2024-hyundai-elantra-n-lightning-lap-2025-178-67b0a408c7cd0.jpg?crop=0.590xw:0.498xh;0.232xw,0.310xh&resize=1200:*"));
       cars.add(new Car("Nissan Altima", 78, 4.4, "https://www.bedfordnissan.com/blogs/1502/wp-content/uploads/2020/07/2020_Nissan_Altima_S.png"));

        
        // Adding bookings
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date rentDate = sdf.parse("2024-06-01 10:00");
            Date returnDate = sdf.parse("2024-06-01 18:00");
            Booking b = new Booking(user1, cars.get(0), rentDate, returnDate, false, "");
            b.setReturned(false);
            cars.get(0).setAvailable(false);
            bookings.add(b);
        } catch (Exception e) {
            tools.showAlert("Error", "Error creating dummy bookings: "+ e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}