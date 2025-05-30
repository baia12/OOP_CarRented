import javafx.application.Application;
import java.awt.Desktop;
import java.net.URI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class CarRentalSystemApp extends Application {
    
    private Stage primaryStage;
    private List<User> users = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private User loggedInUser = null;
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initDummyData();
        
        primaryStage.setTitle("University Car Rental System");
        primaryStage.setResizable(false);
        showMainMenu();
        primaryStage.show();
    }
    //*DUMMY DATA */
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
            showAlert("Error", "Error creating dummy bookings: "+ e.getMessage());
        }
    }
    
    /*MAIN MENU */
    private void showMainMenu() {
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

    Button adminLoginBtn = createStyledButton("Admin Login", "#3498db");
    Button userLoginBtn = createStyledButton("User Login", "#2ecc71");
   


    adminLoginBtn.setOnAction(e -> showAdminLogin());
    userLoginBtn.setOnAction(e -> showUserLogin());
    root.getChildren().addAll(titleLabel, userLoginBtn,adminLoginBtn);    
    Scene scene = new Scene(root, 500, 500);
    primaryStage.setScene(scene);
}

    // ADMIN PAGE start
    private void showAdminLogin() {
        VBox root = new VBox(15);
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

        Button loginBtn = createStyledButton("Login", "#3498db");
        Button backBtn = createStyledButton("Back", "#95a5a6");
        
        loginBtn.setOnAction(e -> {
            if (ADMIN_USERNAME.equals(usernameField.getText()) && 
                ADMIN_PASSWORD.equals(passwordField.getText())) {
                showAdminMenu();
            } else {
                showAlert("Error", "Incorrect admin credentials.");
            }
        });
        
        backBtn.setOnAction(e -> showMainMenu());
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, backBtn);
        
        root.getChildren().addAll(titleLabel, grid, buttonBox);
        
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
    }

    private void showAdminMenu() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titleLabel = new Label("Admin Panel");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Button viewUnverifiedBtn = createStyledButton("View Unverified Users", "#9b59b6");
        Button verifyUserBtn = createStyledButton("Verify User", "#2ecc71");
        // Button addCarBtn = createStyledButton("Add Car", "#f39c12");
        Button viewBookingsBtn = createStyledButton("View All Bookings", "#34495e");
        Button logoutBtn = createStyledButton("Logout", "#e74c3c");
        
        viewUnverifiedBtn.setOnAction(e -> showUnverifiedUsers());
        verifyUserBtn.setOnAction(e -> showVerifyUser());
        //addCarBtn.setOnAction(e -> showAddCar());
        viewBookingsBtn.setOnAction(e -> showAllBookings());
        logoutBtn.setOnAction(e -> showMainMenu());
        
        root.getChildren().addAll(titleLabel, viewUnverifiedBtn, verifyUserBtn,viewBookingsBtn, logoutBtn);
        
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
    }
    
    private void showUnverifiedUsers() {
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
        
        Button backBtn = createStyledButton("Back", "#95a5a6");
        backBtn.setOnAction(e -> showAdminMenu());
        
        root.getChildren().addAll(titleLabel, listView, backBtn);
        
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
    }
    
   
    private void showVerifyUser() {
    VBox root = new VBox(15);
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

    Button verifyBtn = createStyledButton("Verify", "#2ecc71");
    Button backBtn = createStyledButton("Back", "#95a5a6");

    verifyBtn.setOnAction(e -> {
        User selectedUser = userComboBox.getValue();
        if (selectedUser == null) {
            showAlert("Error", "Please select a user.");
        } else {
            selectedUser.setVerified(true);
            showAlert("Success", "User " + selectedUser.getUsername() + " verified.");
            userComboBox.getItems().remove(selectedUser);
        }
    });

    backBtn.setOnAction(e -> showAdminMenu());

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(verifyBtn, backBtn);

    root.getChildren().addAll(titleLabel, userComboBox, buttonBox);

    Scene scene = new Scene(root, 400, 250);
    primaryStage.setScene(scene);
}

/* 
private void showAddCar() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titleLabel = new Label("Add New Car");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField modelField = new TextField();
        TextField fuelField = new TextField();
        
        grid.add(new Label("Car Model:"), 0, 0);
        grid.add(modelField, 1, 0);
        grid.add(new Label("Fuel Level (0-100):"), 0, 1);
        grid.add(fuelField, 1, 1);
        
        Button addBtn = createStyledButton("Add Car", "#2ecc71");
        Button backBtn = createStyledButton("Back", "#95a5a6");
        
        addBtn.setOnAction(e -> {
            try {
                double fuel = Double.parseDouble(fuelField.getText());
                if (fuel < 0 || fuel > 100) {
                    showAlert("Error", "Fuel must be between 0 and 100.");
                } else {
                    Car car = new Car(modelField.getText(), fuel,4,"gdss");
                    cars.add(car);
                    showAlert("Success", "Car added: " + modelField.getText());
                    modelField.clear();
                    fuelField.clear();
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid fuel level.");
            }
        });
        
        backBtn.setOnAction(e -> showAdminMenu());
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addBtn, backBtn);
        
        root.getChildren().addAll(titleLabel, grid, buttonBox);
        
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
    }
    */
    
    private void showAllBookings() {
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
        
        Button backBtn = createStyledButton("Back", "#95a5a6");
        backBtn.setOnAction(e -> showAdminMenu());
        
        root.getChildren().addAll(titleLabel, bookingsArea, backBtn);
        
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
    } //END OF ADMIN PAGE
    
    // USER PAGE START
    private void showUserLogin() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titleLabel = new Label("User Login");
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
        
        Button loginBtn = createStyledButton("Login", "#2ecc71");
        Button RegisterBtn = createStyledButton("Register", "#3b81eb");
        Button backBtn = createStyledButton("Back", "#95a5a6");
        
        loginBtn.setOnAction(e -> {
            User u = findUserByUsername(usernameField.getText());
            if (u == null) {
                showAlert("Error", "User not found.");
            } else if (!u.checkPassword(passwordField.getText())) {
                showAlert("Error", "Incorrect password.");
            } else if (u.isBlacklisted()) {
                showAlert("Error", "You are blacklisted. Contact admin.");
            } else {
                loggedInUser = u;
                showUserMenu();
            }
        });
        RegisterBtn.setOnAction(e->showUserRegistration());
        backBtn.setOnAction(e -> showMainMenu());
        
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn,RegisterBtn);
        VBox vbuttonBox= new VBox(10);
        vbuttonBox.setAlignment(Pos.BOTTOM_CENTER);
        vbuttonBox.getChildren().addAll(backBtn);


        
        root.getChildren().addAll(titleLabel, grid, buttonBox,vbuttonBox);
        
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
    }
    
    private void showUserMenu() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titleLabel = new Label("Welcome, " + loggedInUser.getFullName()+" 👋");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Button bookCarBtn = createStyledButton("Book a Car", "#3498db");
        Button returnCarBtn = createStyledButton("Return a Car", "#e67e22");
        Button myBookingsBtn = createStyledButton("View My Bookings", "#9b59b6");
        Button logoutBtn = createStyledButton("Logout", "#e74c3c");
        
        
        bookCarBtn.setOnAction(e -> showCarSelectionScene());
        
        myBookingsBtn.setOnAction(e -> showMyBookings());
        logoutBtn.setOnAction(e -> {
            loggedInUser = null;
            showMainMenu();
        });
        returnCarBtn.setOnAction(e->showReturnCarStep1());
        
        
        root.getChildren().addAll(titleLabel, bookCarBtn, returnCarBtn, myBookingsBtn, logoutBtn);
        
        Scene scene = new Scene(root, 400, 350);
        primaryStage.setScene(scene);
    }
    
  
/// calendar POPUP
   private ComboBox<String> createTimeComboBox() { 
    ComboBox<String> timeBox = new ComboBox<>();
    for (int hour = 0; hour < 24; hour++) {
        for (int min = 0; min < 60; min += 30) {
            String time = String.format("%02d:%02d", hour, min);
            timeBox.getItems().add(time);
        }
    }
    timeBox.setPromptText("Select Time");
    return timeBox;
}
private Date parseDateTime(LocalDate date, String time) {
    if (date == null || time == null) return null;
    try {
        String dateTimeStr = date.toString() + " " + time;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateTimeStr);
    } catch (Exception e) {
        return null;
    }
}

    
    private void showReturnCarStep1() {
    List<Booking> userActiveBookings = new ArrayList<>();
    for (Booking b : bookings) {
        if (b.getUser().equals(loggedInUser) && !b.isReturned()) {
            userActiveBookings.add(b);
        }
    }

    if (userActiveBookings.isEmpty()) {
        showAlert("Info", "Bro you do not have any booking.");
        return;
    }

    VBox root = new VBox(15);
    root.setPadding(new Insets(20));
    root.setStyle("-fx-background-color: #ecf0f1;");

    Label titleLabel = new Label("Return a Car (Step 1)");
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

    ComboBox<Booking> bookingComboBox = new ComboBox<>();
    bookingComboBox.getItems().addAll(userActiveBookings);
    bookingComboBox.setPromptText("Select booking to return");
    Button nextBtn = createStyledButton("Next", "#2ecc71");
    Button backBtn = createStyledButton("Back", "#95a5a6");

    nextBtn.setOnAction(e -> {
        Booking selectedBooking = bookingComboBox.getValue();
        if (selectedBooking == null) {
            showAlert("Error", "Please select a booking.");
            return;
        }
        try {
            double fuel ; //to random generate
            fuel=Math.random()*100;
            if (fuel < 0 || fuel > 100) {
                showAlert("Error", "Fuel must be between 0 and 100.");
                
                return;
            }
           
            showReturnCarStep2(selectedBooking, fuel);
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid fuel level.");
        }
    });
    

    backBtn.setOnAction(e -> showUserMenu());

    HBox buttonBox = new HBox(10, nextBtn, backBtn);
    buttonBox.setAlignment(Pos.CENTER);

    root.getChildren().addAll(titleLabel, bookingComboBox,buttonBox);

    Scene scene = new Scene(root, 400, 300);
    primaryStage.setScene(scene);
}


//payment platform 
private void showReturnCarStep2(Booking booking, double fuel) {
    VBox root = new VBox(15);
    root.setPadding(new Insets(20));
    root.setAlignment(Pos.CENTER);
    root.setStyle("-fx-background-color: #ecf0f1;");

    Label titleLabel = new Label("Payment 💸");
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

    // Estimate cost first (temporary calculation without finalizing)
    Date now = new Date();
    boolean late = now.after(booking.getReturnDate());
    double estimatedCost = booking.calculateBaseCost();
    if (booking.isDelivered()) estimatedCost += 15;
    if (late) estimatedCost += 20;
    if (fuel < 15) estimatedCost += 10;
    else if (fuel > 15) estimatedCost -= 5;

    Label costLabel = new Label("Total Cost:RM" + String.format("%.2f", estimatedCost));
    costLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495e;");

    Image qrImage = new Image("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://youtu.be/L8XbI9aJOXk?si=QjHpyAV40os3gHCo"); // Change to real link if needed
    ImageView qrView = new ImageView(qrImage);

    CheckBox paymentCheckBox = new CheckBox("Payment Completed");

    Button confirmBtn = createStyledButton("Confirm Return", "#e67e22");
    Button backBtn = createStyledButton("Back", "#95a5a6");

    confirmBtn.setOnAction(e -> {
        boolean paymentMade = paymentCheckBox.isSelected();
        if(paymentMade){
        booking.finalizeCost(fuel, paymentMade, late);
        booking.generateReceipt();
        booking.getCar().setFuelLevel(fuel);
        booking.getCar().setAvailable(true);
        
        String msg = "Car returned.\nTotal cost: RM" + String.format("%.2f", booking.getTotalCost());
        
      
        showAlert("Success", msg);
        showUserMenu(); }
        else{
        showAlert("Blocked", "Just pay bro.");
        showRickrollScene();}
        
    });

    backBtn.setOnAction(e -> showReturnCarStep1());

    root.getChildren().addAll(titleLabel, costLabel, qrView, paymentCheckBox, new HBox(10, confirmBtn, backBtn));

    Scene scene = new Scene(root, 400, 450);
    primaryStage.setScene(scene);
}

private void showMyBookings() {
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
    
    Button backBtn = createStyledButton("Back", "#95a5a6");
    backBtn.setOnAction(e -> showUserMenu());
    
    root.getChildren().addAll(titleLabel, bookingsArea, backBtn);
    
    Scene scene = new Scene(root, 600, 600);
    primaryStage.setScene(scene);
}

private void showUserRegistration() {
    VBox root = new VBox(15);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(20));
    root.setStyle("-fx-background-color: #ecf0f1;");
    
    Label titleLabel = new Label("User Registration");
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
    
   
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setAlignment(Pos.CENTER);
    TextField fullNameField = new TextField();
    TextField icField = new TextField();
    TextField universityField = new TextField();
    TextField matricField = new TextField();
    TextField usernameField = new TextField();
    PasswordField passwordField = new PasswordField();
    
    grid.add(new Label("Full Name:"), 0, 0);
    grid.add(fullNameField, 1, 0);
    grid.add(new Label("IC Number:"), 0, 1);
    grid.add(icField, 1, 1);
    grid.add(new Label("University:"), 0, 2);
    grid.add(universityField, 1, 2);
    grid.add(new Label("Matric Number:"), 0, 3);
    grid.add(matricField, 1, 3);
    grid.add(new Label("Username:"), 0, 4);
    grid.add(usernameField, 1, 4);
    grid.add(new Label("Password:"), 0, 5);
    grid.add(passwordField, 1, 5);
    
    Button registerBtn = createStyledButton("Register", "#2ecc71");
    Button backBtn = createStyledButton("Back", "#95a5a6");
    
    registerBtn.setOnAction(e -> {
        if (findUserByUsername(usernameField.getText()) != null) {
            showAlert("Error", "Username already taken.");
        } else {
            User user = new User(fullNameField.getText(), icField.getText(), 
            universityField.getText(), matricField.getText(),
            usernameField.getText(), passwordField.getText());
            users.add(user);
            showAlert("Success", "Registered successfully. Please wait for admin verification.");
            showMainMenu();
        }
    });
    
    backBtn.setOnAction(e -> showMainMenu());
    
    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(backBtn,registerBtn);
    
    root.getChildren().addAll(titleLabel, grid, buttonBox);
    
    Scene scene = new Scene(root, 450, 400);
    primaryStage.setScene(scene);
}

private User findUserByUsername(String username) {
    for (User u : users) {
        if (u.getUsername().equalsIgnoreCase(username)) return u;
    }
    return null;
}





// Step 1: Car Selection Scene (with images)
private void showCarSelectionScene() {
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
    carPane.setPrefWrapLength(550); // width limit before wrapping
    carPane.setStyle("-fx-background-color: transparent;");

    if (!loggedInUser.isVerified()) {
        showAlert("Error", "You must be verified by admin to book a car.");
        return;
    }

    for (Car car : cars) {
         if (!car.isAvailable()) continue;

        VBox carBox = new VBox(10);
        carBox.setAlignment(Pos.CENTER);
        carBox.setStyle("-fx-background-color: white;" +"-fx-border-color:rgb(209, 209, 209);" +"-fx-border-width: 1;" +"-fx-border-radius: 8;" +"-fx-background-radius: 8;" +"-fx-padding: 15;" + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
    
        Label star = new Label(" ★");
        star.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 16px;");     
        Label RatingLabel = new Label("Rating: " + car.getRating());
        RatingLabel.setStyle("-fx-text-fill: #555;");
       HBox ratingBox = new HBox(2, RatingLabel, star);
       ratingBox.setAlignment(Pos.CENTER);

        ImageView carImage = new ImageView(new Image(car.getImageUrl()));
        carImage.setFitWidth(150);
        carImage.setFitHeight(100);

        Label nameLabel = new Label(car.getModel());
       nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #333;");


        Button selectBtn =createStyledButton("SELECT", "#2ecc71");
        selectBtn.setOnAction(e -> showBookingDetailsScene(car));

        carBox.getChildren().addAll(carImage,nameLabel,ratingBox, selectBtn);
        carPane.getChildren().add(carBox);
    }

    Button backBtn=createStyledButton("Back", "#95a5a6");
    backBtn.setOnAction(e -> showUserMenu());
    ScrollPane scrollPane = new ScrollPane(carPane);//scrollable
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(400); // adjust as needed
    scrollPane.setStyle("-fx-background:transparent;");
    root.getChildren().addAll(title, scrollPane, backBtn);
    Scene scene = new Scene(root, 600, 500);
    primaryStage.setScene(scene);
}

// Step 2: Booking Detail Scene (based on selected car)
private void showBookingDetailsScene(Car selectedCar) {
    VBox root = new VBox(15);
    root.setPadding(new Insets(20));
    root.setStyle("-fx-background-color: #ecf0f1;");

    Label titleLabel = new Label("Booking Details for: " + selectedCar.getModel());
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    DatePicker rentDatePicker = new DatePicker();
    ComboBox<String> rentTimeBox = createTimeComboBox();
    DatePicker returnDatePicker = new DatePicker();
    ComboBox<String> returnTimeBox = createTimeComboBox();

    CheckBox deliveryCheckBox = new CheckBox("Delivery Service");
    TextField deliveryLocationField = new TextField();
    deliveryLocationField.setPromptText("Delivery location");
    deliveryLocationField.setDisable(true);

    deliveryCheckBox.setOnAction(e -> deliveryLocationField.setDisable(!deliveryCheckBox.isSelected()));

    grid.add(new Label("Rental Date:"), 0, 0);
    grid.add(rentDatePicker, 1, 0);
    grid.add(new Label("Rental Time:"), 0, 1);
    grid.add(rentTimeBox, 1, 1);
    grid.add(new Label("Return Date:"), 0, 2);
    grid.add(returnDatePicker, 1, 2);
    grid.add(new Label("Return Time:"), 0, 3);
    grid.add(returnTimeBox, 1, 3);
    grid.add(deliveryCheckBox, 0, 4);
    grid.add(deliveryLocationField, 1, 4);

    Button bookBtn = createStyledButton("Confirm Booking", "#2ecc71");
    Button backBtn = createStyledButton("Back", "#95a5a6");

    bookBtn.setOnAction(e -> {
        Date rentDate = parseDateTime(rentDatePicker.getValue(), rentTimeBox.getValue());
        Date returnDate = parseDateTime(returnDatePicker.getValue(), returnTimeBox.getValue());

        if (rentDate == null || returnDate == null || !returnDate.after(rentDate)) {
            showAlert("Error", "Invalid date/time selection.");
            return;
        }

        boolean delivery = deliveryCheckBox.isSelected();
        String deliveryLoc = deliveryLocationField.getText();

        if (delivery && deliveryLoc.trim().isEmpty()) {
            showAlert("Error", "Delivery location required.");
            return;
        }

        Booking booking = new Booking(loggedInUser, selectedCar, rentDate, returnDate, delivery, deliveryLoc);
        bookings.add(booking);
        selectedCar.setAvailable(false);

        showAlert("Success", "Booking created! Booking ID: " + booking.getBookingId());
        showUserMenu();
    });

    backBtn.setOnAction(e -> showCarSelectionScene());

    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(bookBtn, backBtn);

    root.getChildren().addAll(titleLabel, grid, buttonBox);

    Scene scene = new Scene(root, 500, 450);
    primaryStage.setScene(scene);
}
//tools
//rickroll
private void showRickrollScene() {
    try {
        Desktop.getDesktop().browse(new URI("https://youtu.be/xvFZjo5PgG0?si=_Ip-QQvxWJeYhzQ7"));
    } catch (Exception ex) {
        showAlert("Error", "Failed to open browser.");
    }
    return;

}
private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

    // to create BUTTON interactive
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                      "-fx-font-size: 14px; -fx-background-radius: 5px;");
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.8;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("-fx-opacity: 0.8;", "")));
        return button;
    }

// MAIN
public static void main(String[] args) {
    launch(args);
}


}