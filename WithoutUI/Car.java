public class Car {
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