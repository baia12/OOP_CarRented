package model;

/**
 * Car.java
 * 
 * Represents a car object in the car rental system.
 * 
 * Made by: WAN SYAFIQ
 */

public class Car {
    // Car attributes
    private String model;           // Car model name (e.g. Toyota Camry)
    private boolean available;      // Availability status (true = can be rented)
    private double fuelLevel;       // Fuel level percentage (0–100)
    private double rating;          // Rating out of 5 stars
    private String imageUrl;        // URL to the car image

    /**
     * Constructor to initialize a Car object.
     * 
     * @param model      The model of the car.
     * @param fuelLevel  The initial fuel level of the car.
     * @param rating     User rating of the car.
     * @param imageUrl   Link to the image of the car.
     */
    public Car(String model, double fuelLevel, double rating, String imageUrl) {
        this.model = model;
        this.fuelLevel = fuelLevel;
        this.available = true; // By default, a new car is available
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // === Getter and Setter Methods ===

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean val) {
        this.available = val;
    }

    public double getRating() {
        return rating;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Returns a readable string of car details, shown in dropdowns or lists.
     */
    @Override
    public String toString() {
        return model + " (" + fuelLevel + "% fuel, " + rating + "★)" + (available ? " [Available]" : " [Rented]");
    }
}
