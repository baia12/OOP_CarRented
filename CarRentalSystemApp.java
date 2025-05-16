import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CarRentalSystemApp extends Application {

    public static class User {
        private String fullName;
        private String icNumber;
        private String university;
        private String matricNumber;
        private String username;
        private String password;
        private boolean isVerified;

        public User(String fullName, String icNumber, String university, String matricNumber,
                    String username, String password) {
            this.fullName = fullName;
            this.icNumber = icNumber;
            this.university = university;
            this.matricNumber = matricNumber;
            this.username = username;
            this.password = password;
            this.isVerified = false;
        }

        public void verify() {
            this.isVerified = true;
        }

        public boolean isVerified() {
            return isVerified;
        }

        public String getFullName() {
            return fullName;
        }

        public String getUsername() {
            return username;
        }

        public boolean checkPassword(String password) {
            return this.password.equals(password);
        }

        @Override
        public String toString() {
            return fullName + (isVerified ? " (Verified)" : " (Not Verified)");
        }
    }

    public static class Car {
        private String model;
        private boolean isAvailable;
        private double fuelLevel;

        public Car(String model, double fuelLevel) {
            this.model = model;
            this.fuelLevel = fuelLevel;
            this.isAvailable = true;
        }

        public String getModel() {
            return model;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public void rent() {
            this.isAvailable = false;
        }

        public void returnCar(double fuelLevel) {
            this.isAvailable = true;
            this.fuelLevel = fuelLevel;
        }

        public double getFuelLevel() {
            return fuelLevel;
        }

        @Override
        public String toString() {
            return model + (isAvailable ? " (Available)" : " (Rented)");
        }
    }

    public static class Booking {
        private User user;
        private Car car;
        private Date startDate;
        private Date endDate;
        private boolean isLate;
        private boolean deliverySelected;
        private String deliveryLocation;
        private double totalCost;
        private boolean isReturned;

        public Booking(User user, Car car, Date startDate, Date endDate, boolean deliverySelected, String deliveryLocation) {
            this.user = user;
            this.car = car;
            this.startDate = startDate;
            this.endDate = endDate;
            this.deliverySelected = deliverySelected;
            this.deliveryLocation = deliveryLocation;
            this.isLate = false;
            this.isReturned = false;
            this.totalCost = calculateCost();
        }

        private double calculateCost() {
            long duration = endDate.getTime() - startDate.getTime();
            double hours = (double) duration / (1000 * 60 * 60);
            double base = hours * 10; // $10/hr base rate
            double deliveryCharge = deliverySelected ? 15 : 0;
            double lateFee = isLate ? 20 : 0;
            return base + deliveryCharge + lateFee;
        }

        public void markLate() {
            this.isLate = true;
            this.totalCost += 20;
        }

        public boolean isDeliverySelected() {
            return deliverySelected;
        }

        public String getDeliveryLocation() {
            return deliveryLocation;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public Car getCar() {
            return car;
        }

        public User getUser() {
            return user;
        }

        public Date getEndDate() {
            return endDate;
        }

        public boolean isReturned() {
            return isReturned;
        }

        public void setReturned(boolean returned) {
            isReturned = returned;
        }

        @Override
        public String toString() {
            return String.format("Booking: %s - %s from %s to %s%s. Cost: $%.2f%s",
                    user.getFullName(),
                    car.getModel(),
                    startDate.toString(),
                    endDate.toString(),
                    deliverySelected ? " (Delivery: " + deliveryLocation + ")" : " (Self Pickup)",
                    totalCost,
                    (isReturned ? " [Returned]" : ""));
        }
    }

    public static class RentalSystem {
        private List<User> users = new ArrayList<>();
        private List<Car> cars = new ArrayList<>();
        private List<Booking> bookings = new ArrayList<>();

        private List<User> blacklistedUsers = new ArrayList<>();

        public void addUser(User user) {
            users.add(user);
        }

        public User findUserByUsername(String username) {
            return users.stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
        }

        public void verifyUser(User user) {
            user.verify();
        }

        public void addCar(Car car) {
            cars.add(car);
        }

        public List<Car> getAvailableCars() {
            List<Car> available = new ArrayList<>();
            for (Car car : cars) {
                if (car.isAvailable()) {
                    available.add(car);
                }
            }
            return available;
        }

        public Booking createBooking(User user, Car car, Date start, Date end, boolean delivery, String location) {
            if (blacklistedUsers.contains(user)) {
                throw new IllegalStateException("User is blacklisted and cannot make bookings.");
            }
            if (!user.isVerified()) {
                throw new IllegalStateException("User is not verified.");
            }
            if (!car.isAvailable()) {
                throw new IllegalStateException("Car not available.");
            }
            car.rent();
            Booking booking = new Booking(user, car, start, end, delivery, location);
            bookings.add(booking);
            return booking;
        }

        public void returnCar(Booking booking, double fuelLevel, boolean paymentMade) {
            Car car = booking.getCar();
            car.returnCar(fuelLevel);

            if (fuelLevel < 15) {
                booking.totalCost += 10; // fuel charge
            } else if (fuelLevel > 15) {
                booking.totalCost -= 5; // discount
            }

            if (!paymentMade) {
                blacklistedUsers.add(booking.getUser());
            }

            booking.setReturned(true);
        }

        public List<User> getUsers() {
            return users;
        }

        public List<User> getUnverifiedUsers() {
            List<User> list = new ArrayList<>();
            for (User u : users) {
                if (!u.isVerified())
                    list.add(u);
            }
            return list;
        }

        public List<Car> getCars() {
            return cars;
        }

        public List<Booking> getBookings() {
            return bookings;
        }

        public List<User> getBlacklistedUsers() {
            return blacklistedUsers;
        }

        public List<Booking> getActiveBookingsForUser(User user) {
            List<Booking> list = new ArrayList<>();
            for (Booking b : bookings) {
                if (b.getUser().equals(user) && !b.isReturned()) {
                    list.add(b);
                }
            }
            return list;
        }
    }

    private RentalSystem rentalSystem = new RentalSystem();

    private Stage primaryStage;

    private User loggedInUser = null;

    private ObservableList<User> registeredUsers = FXCollections.observableArrayList();
    private ObservableList<Car> carsObservable = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("University Car Rental System");

        showLoginScene();
    }

    private void showLoginScene() {
        Label title = new Label("Login");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Button loginBtn = new Button("Login");
        Button toRegisterBtn = new Button("Register (User)");

        Label messageLabel = new Label();

        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();

            if (username.equals("ADMIN") && password.equals("ADMIN")) {
                messageLabel.setText("");
                showAdminScene();
                return;
            }

            User user = rentalSystem.findUserByUsername(username);
            if (user == null) {
                messageLabel.setText("User not found.");
            } else if (!user.checkPassword(password)) {
                messageLabel.setText("Incorrect password.");
            } else {
                loggedInUser = user;
                messageLabel.setText("");
                // if (!user.isVerified()) {
                //     showError("User not verified by admin yet.");
                //     return;
                // }
                showUserDashboard();
            }
        });

        toRegisterBtn.setOnAction(e -> showRegisterScene());

        VBox vbox = new VBox(10, title, userLabel, userField, passLabel, passField, loginBtn, toRegisterBtn, messageLabel);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showRegisterScene() {
        Label title = new Label("User Registration");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField icField = new TextField();
        icField.setPromptText("IC Number");
        TextField uniField = new TextField();
        uniField.setPromptText("University");
        TextField matricField = new TextField();
        matricField.setPromptText("Matric Number");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerBtn = new Button("Register");
        Button backBtn = new Button("Back to Login");

        Label messageLabel = new Label();

        registerBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String ic = icField.getText().trim();
            String uni = uniField.getText().trim();
            String matric = matricField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (name.isEmpty() || ic.isEmpty() || uni.isEmpty() || matric.isEmpty()
                    || username.isEmpty() || password.isEmpty()) {
                showError("Please fill all fields.");
                return;
            }

            if (rentalSystem.findUserByUsername(username) != null) {
                showError("Username already taken.");
                return;
            }

            User newUser = new User(name, ic, uni, matric, username, password);
            rentalSystem.addUser(newUser);
            registeredUsers.add(newUser);
            messageLabel.setText("Registered! Wait for admin verification.");
            clearFields(nameField, icField, uniField, matricField, usernameField, passwordField);
        });

        backBtn.setOnAction(e -> showLoginScene());

        VBox vbox = new VBox(10, title, nameField, icField, uniField, matricField, usernameField, passwordField, registerBtn, backBtn, messageLabel);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 400, 450);
        primaryStage.setScene(scene);
    }

    private void showAdminScene() {
        Label title = new Label("Admin Dashboard");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TabPane tabPane = new TabPane();

        Tab verifyTab = new Tab("Verify Users", createAdminVerifyPane());
        verifyTab.setClosable(false);

        Tab carTab = new Tab("Car Management", createAdminCarManagementPane());
        carTab.setClosable(false);

        Tab bookingTab = new Tab("All Bookings", createAdminBookingOverviewPane());
        bookingTab.setClosable(false);

        tabPane.getTabs().addAll(verifyTab, carTab, bookingTab);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            loggedInUser = null;
            showLoginScene();
        });

        VBox vbox = new VBox(10, title, tabPane, logoutBtn);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
    }

    private VBox createAdminVerifyPane() {
        ListView<User> unverifiedUsersList = new ListView<>();
        refreshUnverifiedUsers(unverifiedUsersList);

        Button verifyBtn = new Button("Verify Selected User");
        verifyBtn.setDisable(true);

        unverifiedUsersList.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            verifyBtn.setDisable(newSel == null);
        });

        verifyBtn.setOnAction(e -> {
            User selectedUser = unverifiedUsersList.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                rentalSystem.verifyUser(selectedUser);
                refreshUnverifiedUsers(unverifiedUsersList);
                showInfo("User " + selectedUser.getFullName() + " verified.");
            }
        });

        VBox vbox = new VBox(10, new Label("Unverified Users:"), unverifiedUsersList, verifyBtn);
        vbox.setPadding(new Insets(10));
        return vbox;
    }

    private void refreshUnverifiedUsers(ListView<User> listView) {
        listView.getItems().setAll(rentalSystem.getUnverifiedUsers());
    }

    private VBox createAdminCarManagementPane() {
        ListView<Car> carsListView = new ListView<>();
        carsObservable.setAll(rentalSystem.getCars());
        carsListView.setItems(carsObservable);

        TextField modelField = new TextField();
        modelField.setPromptText("Car Model");

        TextField fuelField = new TextField();
        fuelField.setPromptText("Fuel Level (%)");

        Button addCarBtn = new Button("Add Car");
        addCarBtn.setOnAction(e -> {
            String model = modelField.getText().trim();
            String fuelStr = fuelField.getText().trim();
            double fuel;
            try {
                fuel = Double.parseDouble(fuelStr);
                if (fuel < 0 || fuel > 100) throw new NumberFormatException();
            } catch (Exception ex) {
                showError("Fuel level must be number between 0 and 100");
                return;
            }
            if (model.isEmpty()) {
                showError("Car model cannot be empty");
                return;
            }
            Car car = new Car(model, fuel);
            rentalSystem.addCar(car);
            carsObservable.add(car);

            showInfo("Car added: " + model);
            modelField.clear();
            fuelField.clear();
        });

        VBox vbox = new VBox(10,
                new Label("Cars:"),
                carsListView,
                modelField,
                fuelField,
                addCarBtn);
        vbox.setPadding(new Insets(10));
        return vbox;
    }

    private VBox createAdminBookingOverviewPane() {
        TableView<Booking> bookingTable = new TableView<>();
        ObservableList<Booking> bookingObservable = FXCollections.observableArrayList(rentalSystem.getBookings());

        TableColumn<Booking, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUser().getFullName()));
        userCol.setPrefWidth(150);

        TableColumn<Booking, String> carCol = new TableColumn<>("Car");
        carCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCar().getModel()));
        carCol.setPrefWidth(150);

        TableColumn<Booking, String> datesCol = new TableColumn<>("Rental Dates");
        datesCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().startDate.toString() + " to " + cellData.getValue().endDate.toString()));
        datesCol.setPrefWidth(200);

        TableColumn<Booking, String> deliveryCol = new TableColumn<>("Delivery");
        deliveryCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().isDeliverySelected() ? cellData.getValue().getDeliveryLocation() : "Self Pickup"));
        deliveryCol.setPrefWidth(150);

        TableColumn<Booking, String> costCol = new TableColumn<>("Total Cost");
        costCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                String.format("$%.2f", cellData.getValue().getTotalCost())));
        costCol.setPrefWidth(100);

        TableColumn<Booking, String> returnedCol = new TableColumn<>("Returned");
        returnedCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().isReturned() ? "Yes" : "No"));
        returnedCol.setPrefWidth(80);

        bookingTable.getColumns().addAll(userCol, carCol, datesCol, deliveryCol, costCol, returnedCol);
        bookingTable.setItems(bookingObservable);

        VBox vbox = new VBox(10, new Label("All Bookings Overview"), bookingTable);
        vbox.setPadding(new Insets(10));
        return vbox;
    }

    private void showUserDashboard() {
        Label welcomeLabel = new Label("Welcome, " + loggedInUser.getFullName());
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button bookBtn = new Button("Book a Car");
        Button returnBtn = new Button("Return a Car");
        Button logoutBtn = new Button("Logout");

        bookBtn.setOnAction(e -> {
            if (!loggedInUser.isVerified()) {
                    showError("User not verified by admin yet.");
                    return;}
            showBookingScene();
            });
         bookBtn.setOnAction(e -> {
            if (!loggedInUser.isVerified()) {
                    showError("User not verified by admin yet.");
                    return;}
            showReturnCarScene();
            });

        logoutBtn.setOnAction(e -> {
            loggedInUser = null;
            showLoginScene();
        });

        VBox vbox = new VBox(20, welcomeLabel, bookBtn, returnBtn, logoutBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
    }

    private void showBookingScene() {
        Label title = new Label("Car Booking");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ComboBox<Car> carComboBox = new ComboBox<>();
        carComboBox.getItems().setAll(rentalSystem.getAvailableCars());
        carComboBox.setPromptText("Select Car");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Rental Start Date");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Rental End Date");

        CheckBox deliveryCheckBox = new CheckBox("Delivery Service");

        TextField deliveryLocationField = new TextField();
        deliveryLocationField.setPromptText("Delivery Location");
        deliveryLocationField.setDisable(true);

        deliveryCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            deliveryLocationField.setDisable(!isNowSelected);
        });

        Button bookBtn = new Button("Book Car");
        Label bookingResultLabel = new Label();

        bookBtn.setOnAction(e -> {
            Car car = carComboBox.getValue();
            LocalDate startL = startDatePicker.getValue();
            LocalDate endL = endDatePicker.getValue();
            boolean delivery = deliveryCheckBox.isSelected();
            String deliveryLoc = deliveryLocationField.getText().trim();

            if (car == null) {
                showError("Please select a car");
                return;
            }
            if (startL == null || endL == null) {
                showError("Please select rental start and end dates");
                return;
            }
            if (endL.isBefore(startL)) {
                showError("End date must be after start date");
                return;
            }
            if (delivery && deliveryLoc.isEmpty()) {
                showError("Enter delivery location");
                return;
            }

            Date startDate = Date.from(startL.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endL.atStartOfDay(ZoneId.systemDefault()).toInstant());

            try {
                Booking booking = rentalSystem.createBooking(loggedInUser, car, startDate, endDate, delivery, deliveryLoc);
                bookingResultLabel.setText("Booking successful! Total cost: $" + String.format("%.2f", booking.getTotalCost()));
                carComboBox.getItems().setAll(rentalSystem.getAvailableCars());
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> showUserDashboard());

        VBox vbox = new VBox(10,
                title,
                new Label("Select Car:"), carComboBox,
                new Label("Rental Start Date:"), startDatePicker,
                new Label("Rental End Date:"), endDatePicker,
                deliveryCheckBox, deliveryLocationField,
                bookBtn, bookingResultLabel,
                backBtn);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_LEFT);

        Scene scene = new Scene(vbox, 450, 450);
        primaryStage.setScene(scene);
    }

    private void showReturnCarScene() {
        Label title = new Label("Return Car");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        List<Booking> activeBookings = rentalSystem.getActiveBookingsForUser(loggedInUser);
        ObservableList<Booking> activeBookingsObservable = FXCollections.observableArrayList(activeBookings);

        ListView<Booking> bookingListView = new ListView<>(activeBookingsObservable);
        bookingListView.setPrefHeight(150);

        TextField fuelLevelField = new TextField();
        fuelLevelField.setPromptText("Fuel Level on Return (%)");

        CheckBox paymentCheckBox = new CheckBox("Payment Made");

        Button returnBtn = new Button("Return Selected Car");
        returnBtn.setDisable(true);

        Label actionStatusLabel = new Label();

        bookingListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            returnBtn.setDisable(newSel == null);
            actionStatusLabel.setText("");
        });

        returnBtn.setOnAction(e -> {
            Booking booking = bookingListView.getSelectionModel().getSelectedItem();
            if (booking == null) {
                showError("Select a booking to return car");
                return;
            }
            String fuelStr = fuelLevelField.getText().trim();
            double fuel;
            try {
                fuel = Double.parseDouble(fuelStr);
                if (fuel < 0 || fuel > 100) throw new NumberFormatException();
            } catch (Exception ex) {
                showError("Fuel level must be number between 0 and 100");
                return;
            }
            boolean paymentMade = paymentCheckBox.isSelected();

            rentalSystem.returnCar(booking, fuel, paymentMade);

            actionStatusLabel.setText("Car returned. Total cost: $" + String.format("%.2f", booking.getTotalCost()));
            if (!paymentMade) {
                actionStatusLabel.setText(actionStatusLabel.getText() + " User blacklisted for non-payment.");
            }

            activeBookingsObservable.remove(booking);
            carsObservable.setAll(rentalSystem.getCars());
            fuelLevelField.clear();
            paymentCheckBox.setSelected(false);
        });

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> showUserDashboard());

        VBox vbox = new VBox(10,
                title,
                new Label("Active Bookings:"),
                bookingListView,
                fuelLevelField,
                paymentCheckBox,
                returnBtn,
                actionStatusLabel,
                backBtn);

        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_LEFT);

        Scene scene = new Scene(vbox, 500, 450);
        primaryStage.setScene(scene);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
}
