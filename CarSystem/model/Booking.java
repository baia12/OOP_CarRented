package model;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Booking {
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
    private double basicCost;
    private boolean lateReturn;
    private double Fuel;

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
        double basic = calculateBaseCost();
        

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
        this.basicCost=basic;
        this.Fuel=fuelAtReturn;

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
                (delivery?(", Delivery at: "+deliveryLocation):"")+", Returned: "+(returned?"Yes":"No")+
                (returned?(", Total Cost: $"+String.format("%.2f", totalCost)):
                "");
    }
    
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
        writer.write("Discount      : " + (Fuel>15 ? "Yes Cost - 5" : "No") + "\n");
        writer.write("Late Return   : " + (lateReturn ? "Yes Cost +20" : "No") + "\n");
    
        writer.write(String.format("Total Cost    : RM %.2f\n", totalCost));
        writer.write("=============================\n");
        System.out.println("Receipt saved to " + filename);
    } catch (IOException e) {
        System.out.println("Error writing receipt: " + e.getMessage());
    }
}

}