package entities.vehicle;

public class Vehicle {

    private int id, seats;
    private double chargePerKm, mileage;
    private String owner, fuelType;
    VehicleType type;


    public Vehicle(int id, VehicleType type, String owner, int seats, double cpk, double mileage, String fuelType ){
        this.id = id;
        this.type = type;
        this.owner = owner;
        this.seats = seats;
        this.chargePerKm = cpk;
        this.mileage = mileage;
        this.fuelType = fuelType;
    }
    public int getId() {
        return id;
    }

    public int getSeats() {
        return seats;
    }

    public double getChargePerKm() {
        return chargePerKm;
    }

    public double getMileage() {
        return mileage;
    }

    public String getOwner() {
        return owner;
    }

    public String getFuelType() {
        return fuelType;
    }

    public VehicleType getType() {
        return type;
    }
}
