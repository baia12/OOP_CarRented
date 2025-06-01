package model;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Represents a car rental booking made by a user.
 * Make by FAIZUL NAZRI and FITRI RAZMAN
 */
public class Booking {
    private static int nextId = 1; // Static ID generator for unique booking IDs

    private int bookingId;
    private User user;
    private Car car;
    private Date rentDate;
    private Date returnDate;
    private boolean delivery;
    private String deliveryLocation;
    private boolean returned;
    private double totalCost;
    private double basicCost;
    private boolean lateReturn;
    private double Fuel;

    // Constructor
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

    // Getters and Setters
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

    /**
     * Calculates the base cost of the booking.
     * Uses duration in hours and multiplies by a fixed rate (RM10/hour).
     */
    public double calculateBaseCost() {
        long diff = returnDate.getTime() - rentDate.getTime();
        double hours = diff / (1000.0 * 60 * 60);
        if (hours < 1) hours = 1; // Ensure minimum charge
        return hours * 10.0;
    }

    /**
     * Finalizes the cost after considering delivery, fuel, and late return.
     * Updates booking state and blacklists user if payment not made.
     */
    public void finalizeCost(double fuelAtReturn, boolean paymentMade, boolean isLate) {
        double cost = calculateBaseCost();
        double basic = cost;

        if (delivery) cost += 15;
        if (isLate) {
            this.lateReturn = true;
            cost += 20;
        }
        if (fuelAtReturn < 15) {
            cost += 10;
        } else if (fuelAtReturn > 15) {
            cost -= 5;
        }

        this.totalCost = cost;
        this.basicCost = basic;
        this.Fuel = fuelAtReturn;
        this.returned = true;

        if (!paymentMade) {
            user.setBlacklisted(true);
        }
    }

    /**
     * Returns a string summary of the booking.
     */
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "BookingID: " + bookingId +
                ", User: " + user.getFullName() +
                ", Car: " + car.getModel() +
                ", Rent: " + sdf.format(rentDate) +
                ", Return: " + sdf.format(returnDate) +
                (delivery ? (", Delivery at: " + deliveryLocation) : "") +
                ", Returned: " + (returned ? "Yes" : "No") +
                (returned ? (", Total Cost: RM" + String.format("%.2f", totalCost)) : "");
    }

    /**
     * Generates a plain text receipt for the booking and writes it to a file.
     */
    public void generateReceipt() {
        String filename = "receipt_" + bookingId + ".txt";
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("===== Car Rental Receipt =====\n");
            writer.write("Booking ID    : " + bookingId + "\n");
            writer.write("Name          : " + user.getFullName() + "\n");
            writer.write("IC            : " + user.getICNumber() + "\n");
            writer.write("Car Model     : " + car.getModel() + "\n");
            writer.write("Rent Date     : " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(rentDate) + "\n");
            writer.write("Return Date   : " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(returnDate) + "\n");
            writer.write(String.format("Cost         : RM %.2f\n", basicCost));

            if (delivery) {
                writer.write("Delivery      : Yes Cost + 15 \n");
                writer.write("Location      : " + deliveryLocation + "\n");
            } else {
                writer.write("Delivery      : No\n");
            }

            writer.write("Returned      : " + (returned ? "Yes" : "No") + "\n");
            writer.write("Discount      : " + (Fuel > 15 ? "Yes Cost - 5" : "No") + "\n");
            writer.write("Late Return   : " + (lateReturn ? "Yes Cost +20" : "No") + "\n");
            writer.write(String.format("Total Cost    : RM %.2f\n", totalCost));
            writer.write("=============================\n");

            System.out.println("Receipt saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error writing receipt: " + e.getMessage());
        }
    }
}
