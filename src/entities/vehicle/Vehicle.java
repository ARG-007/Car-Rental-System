package entities.vehicle;

import entities.person.Owner;

public class Vehicle {

    private int id, seats;
    private double chargePerKm, mileage;
    private Owner owner;
    private FuelType  fuelType;
    VehicleType type;


    public Vehicle(int id, VehicleType type, Owner owner, int seats, double cpk, double mileage,FuelType fuelType ){
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

    public Owner getOwner() {
        return owner;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public VehicleType getType() {
        return type;
    }
}
