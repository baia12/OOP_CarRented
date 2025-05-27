import java.util.*;
import java.text.SimpleDateFormat;

class User {
    private String fullName;
    private String icNumber;
    private String university;
    private String matricNumber;
    private String username;
    private String password;
    private boolean verified;
    private boolean blacklisted;

    public User(String fullName, String icNumber, String university, String matricNumber,
                String username, String password) {
        this.fullName = fullName;
        this.icNumber = icNumber;
        this.university = university;
        this.matricNumber = matricNumber;
        this.username = username;
        this.password = password;
        this.verified = false;
        this.blacklisted = false;
    }

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean val) { this.verified = val; }
    public boolean isBlacklisted() { return blacklisted; }
    public void setBlacklisted(boolean val) { this.blacklisted = val; }
    public boolean checkPassword(String pass) { return password.equals(pass); }
    public String toString() { 
        return fullName + (verified ? " [Verified]" : " [Not Verified]") + (blacklisted ? " [Blacklisted]" : "");
    }
}

class Car {
    private String model;
    private boolean available;
    private double fuelLevel;

    public Car(String model, double fuelLevel) {
        this.model = model;
        this.fuelLevel = fuelLevel;
        this.available = true;
    }

    public String getModel() { return model; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean val) { available = val; }
    public double getFuelLevel() { return fuelLevel; }
    public void setFuelLevel(double fuelLevel) { this.fuelLevel = fuelLevel; }

    public String toString() {
        return model + (available ? " [Available]" : " [Rented]");
    }
}

class Booking {
    private static int nextId = 1;

    private int bookingId;
    private User user;
    private Car car;
    private Date rentDate;
    private Date returnDate;
    private boolean delivery;
    private String deliveryLocation;
    private boolean returned;
    private double totalCost;
    private boolean lateReturn;

    public Booking(User user, Car car, Date rentDate, Date returnDate, boolean delivery, String deliveryLocation) {
        this.bookingId = nextId++;
        this.user = user;
        this.car = car;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.delivery = delivery;
        this.deliveryLocation = deliveryLocation;
        this.returned = false;
        this.lateReturn = false;
        this.totalCost = 0.0;
    }

    public int getBookingId() { return bookingId; }
    public User getUser() { return user; }
    public Car getCar() { return car; }
    public Date getRentDate() { return rentDate; }
    public Date getReturnDate() { return returnDate; }
    public boolean isDelivered() { return delivery; }
    public String getDeliveryLocation() { return deliveryLocation; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean val) { this.returned = val; }
    public boolean isLateReturn() { return lateReturn; }
    public void setLateReturn(boolean val) { this.lateReturn = val; }
    public double getTotalCost() { return totalCost; }

    public double calculateBaseCost() {
        long diff = returnDate.getTime() - rentDate.getTime();
        double hours = diff / (1000.0 * 60 * 60);
        if (hours < 1) hours = 1; // minimum one hour charge
        return hours * 10.0; // $10 per hour
    }

    public void finalizeCost(double fuelAtReturn, boolean paymentMade, boolean isLate) {
        double cost = calculateBaseCost();

        if(delivery) {
            cost += 15; // extra delivery charge
        }
        if (isLate) {
            this.lateReturn = true;
            cost += 20; // late fee
        }
        if (fuelAtReturn < 15) {
            cost += 10; // fuel charge
        } else if (fuelAtReturn > 15) {
            cost -= 5; // discount
        }

        this.totalCost = cost;
        this.returned = true;

        if (!paymentMade) {
            user.setBlacklisted(true);
        }
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "BookingID: "+bookingId+
                ", User: "+user.getFullName()+
                ", Car: "+car.getModel()+
                ", Rent: "+sdf.format(rentDate)+
                ", Return: "+sdf.format(returnDate)+
                (delivery?(", Delivery at: "+deliveryLocation):"")+
                ", Returned: "+(returned?"Yes":"No")+
                (returned?(", Total Cost: $"+String.format("%.2f", totalCost)):"");
    }
}

public class CarRentalSystem {

    private Scanner scanner = new Scanner(System.in);
    private List<User> users = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    private User loggedInUser = null;

    // Admin credentials hardcoded
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        CarRentalSystem system = new CarRentalSystem();
        system.run();
    }

    public void run() {
        System.out.println("=== Welcome to University Car Rental System ===");
        boolean exit = false;
        while (!exit) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Register as User");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();

            switch(choice) {
                case "1":
                    adminLogin();
                    break;
                case "2":
                    userLogin();
                    break;
                case "3":
                    registerUser();
                    break;
                case "4":
                    exit = true;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Admin functionalities
    private void adminLogin() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine().trim();

        if(ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            System.out.println("Admin login successful.");
            adminMenu();
        } else {
            System.out.println("Incorrect admin credentials.");
        }
    }

    private void adminMenu() {
        boolean logout = false;
        while(!logout) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View unverified users");
            System.out.println("2. Verify user");
            System.out.println("3. Add car");
            System.out.println("4. View all bookings");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();

            switch(choice) {
                case "1":
                    viewUnverifiedUsers();
                    break;
                case "2":
                    verifyUser();
                    break;
                case "3":
                    addCar();
                    break;
                case "4":
                    viewAllBookings();
                    break;
                case "5":
                    logout = true;
                    System.out.println("Admin logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewUnverifiedUsers() {
        System.out.println("\nUnverified Users:");
        boolean found = false;
        for(User u : users) {
            if(!u.isVerified()) {
                System.out.println("- "+u.getUsername()+" : "+u.getFullName());
                found=true;
            }
        }
        if(!found) System.out.println("No unverified users.");
    }

    private void verifyUser() {
        System.out.print("Enter username to verify: ");
        String username = scanner.nextLine().trim();
        User u = findUserByUsername(username);
        if(u==null) {
            System.out.println("User not found.");
            return;
        }
        if(u.isVerified()) {
            System.out.println("User already verified.");
            return;
        }
        u.setVerified(true);
        System.out.println("User " + u.getUsername() + " verified.");
    }

    private void addCar() {
        System.out.print("Enter car model: ");
        String model = scanner.nextLine().trim();
        double fuel=0;
        while(true){
            System.out.print("Enter fuel level (0-100): ");
            String fuelStr = scanner.nextLine().trim();
            try {
                fuel = Double.parseDouble(fuelStr);
                if(fuel<0 || fuel>100){
                    System.out.println("Fuel must be between 0 and 100.");
                } else break;
            } catch(NumberFormatException e){
                System.out.println("Invalid number.");
            }
        }
        Car car = new Car(model, fuel);
        cars.add(car);
        System.out.println("Car added: " + model);
    }

    private void viewAllBookings() {
        System.out.println("\nAll Bookings:");
        if(bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        for(Booking b : bookings) {
            System.out.println(b);
        }
    }

    // User methods
    private void userLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User u = findUserByUsername(username);
        if(u==null) {
            System.out.println("User not found.");
            return;
        }
        if(!u.checkPassword(password)) {
            System.out.println("Incorrect password.");
            return;
        }
        if(u.isBlacklisted()) {
            System.out.println("You are blacklisted. Contact admin.");
            return;
        }
        loggedInUser = u;
        System.out.println("Welcome, "+u.getFullName());
        userMenu();
    }

    private void userMenu() {
        boolean logout = false;
        while(!logout){
            System.out.println("\nUser Menu:");
            System.out.println("1. Book a car");
            System.out.println("2. Return a car");
            System.out.println("3. View my bookings");
            System.out.println("4. Logout");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine().trim();

            switch(choice){
                case "1":
                    bookCar();
                    break;
                case "2":
                    returnCar();
                    break;
                case "3":
                    myBookings();
                    break;
                case "4":
                    logout = true;
                    loggedInUser = null;
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void bookCar() {
        if(!loggedInUser.isVerified()){
            System.out.println("You must be verified by admin to book a car.");
            return;
        }
        List<Car> availableCars = new ArrayList<>();
        for(Car c : cars){
            if(c.isAvailable()){
                availableCars.add(c);
            }
        }
        if(availableCars.isEmpty()){
            System.out.println("No cars available now.");
            return;
        }
        System.out.println("Available Cars:");
        for(int i=0; i<availableCars.size(); i++){
            System.out.println((i+1)+". "+availableCars.get(i));
        }
        int choice=-1;
        while(true){
            System.out.print("Select car by number: ");
            try{
                choice = Integer.parseInt(scanner.nextLine());
                if(choice<1 || choice>availableCars.size()){
                    System.out.println("Invalid choice.");
                } else break;
            }catch(Exception e){
                System.out.println("Invalid input.");
            }
        }
        Car selectedCar = availableCars.get(choice-1);

        Date rentDate = readDate("Enter rental Date & Time (yyyy-MM-dd HH:mm): ");
        if(rentDate == null) {
            System.out.println("Invalid date.");
            return;
        }
        Date returnDate = readDate("Enter return Date & Time (yyyy-MM-dd HH:mm): ");
        if(returnDate == null || !returnDate.after(rentDate)) {
            System.out.println("Invalid return date.");
            return;
        }

        System.out.print("Delivery service? (Y/N): ");
        String delIn = scanner.nextLine().trim().toUpperCase();
        boolean delivery = delIn.equals("Y");
        String deliveryLoc = "";
        if(delivery){
            System.out.print("Enter delivery location: ");
            deliveryLoc = scanner.nextLine().trim();
            if(deliveryLoc.isEmpty()){
                System.out.println("Delivery location required.");
                return;
            }
        }

        Booking booking = new Booking(loggedInUser, selectedCar, rentDate, returnDate, delivery, deliveryLoc);
        bookings.add(booking);
        selectedCar.setAvailable(false);

        System.out.println("Booking created! Booking ID: "+booking.getBookingId());
    }

    private void returnCar() {
        // Show user's active bookings not returned
        List<Booking> userActiveBookings = new ArrayList<>();
        for(Booking b : bookings) {
            if(b.getUser().equals(loggedInUser) && !b.isReturned()) {
                userActiveBookings.add(b);
            }
        }
        if(userActiveBookings.isEmpty()) {
            System.out.println("No active bookings to return.");
            return;
        }
        System.out.println("Your Active Bookings:");
        for(int i=0; i<userActiveBookings.size(); i++){
            System.out.println((i+1)+". "+userActiveBookings.get(i));
        }
        int choice = -1;
        while(true){
            System.out.print("Select booking to return by number: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if(choice < 1 || choice > userActiveBookings.size()){
                    System.out.println("Invalid choice.");
                } else break;
            } catch(Exception e){
                System.out.println("Invalid input.");
            }
        }
        Booking booking = userActiveBookings.get(choice-1);

        double fuel = -1;
        while(true){
            System.out.print("Enter fuel level on return (%): ");
            try {
                fuel = Double.parseDouble(scanner.nextLine());
                if(fuel < 0 || fuel > 100){
                    System.out.println("Fuel must be between 0 and 100.");
                } else break;
            } catch(Exception e){
                System.out.println("Invalid input.");
            }
        }

        // Check if late
        Date now = new Date();
        boolean late = now.after(booking.getReturnDate());

        System.out.print("Payment completed? (Y/N): ");
        String payIn = scanner.nextLine().trim().toUpperCase();
        boolean paymentMade = payIn.equals("Y");

        booking.finalizeCost(fuel, paymentMade, late);
        booking.getCar().setFuelLevel(fuel);
        booking.getCar().setAvailable(true);

        System.out.println("Car returned.");
        System.out.printf("Total cost: $%.2f\n", booking.getTotalCost());
        if (!paymentMade) {
            System.out.println("You are blacklisted due to non-payment. Contact admin.");
        }
    }

    private void myBookings(){
        System.out.println("Your Bookings:");
        boolean found = false;
        for(Booking b : bookings){
            if(b.getUser().equals(loggedInUser)){
                System.out.println(b);
                found = true;
            }
        }
        if(!found) System.out.println("No bookings found.");
    }

    private void registerUser() {
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("IC Number: ");
        String ic = scanner.nextLine().trim();

        System.out.print("University: ");
        String university = scanner.nextLine().trim();

        System.out.print("Matric Number: ");
        String matric = scanner.nextLine().trim();

        String username;
        while(true) {
            System.out.print("Choose username: ");
            username = scanner.nextLine().trim();
            if(findUserByUsername(username)!=null) {
                System.out.println("Username already taken.");
            } else break;
        }

        System.out.print("Choose password: ");
        String password = scanner.nextLine().trim();

        User user = new User(fullName, ic, university, matric, username, password);
        users.add(user);
        System.out.println("Registered successfully. Please wait for admin verification.");
    }

    private User findUserByUsername(String username) {
        for(User u : users) {
            if(u.getUsername().equalsIgnoreCase(username)) return u;
        }
        return null;
    }

    private Date readDate(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(input);
        } catch(Exception e) {
            return null;
        }
    }
}
