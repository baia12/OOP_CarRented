package model;
public class Car {
    private String model;
    private boolean available;
    private double fuelLevel;
    private double rating;
    private String imageUrl;
    

    public Car(String model, double fuelLevel,double rating,String imageUrl) {
        this.model = model;
        this.fuelLevel = fuelLevel;
        this.available = true;
        this.rating=rating;
        this.imageUrl = imageUrl;
    }

    public String getModel() { return model; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean val) { available = val; }
    public double getRating(){return rating;}
    public double getFuelLevel() { return fuelLevel; }
    public void setFuelLevel(double fuelLevel) { this.fuelLevel = fuelLevel; }
    public String getImageUrl() {return imageUrl;}


    public String toString() {
         return model + " (" + fuelLevel + "% fuel, " + rating + "★)" + (available ? " [Available]" : " [Rented]");
    }
}