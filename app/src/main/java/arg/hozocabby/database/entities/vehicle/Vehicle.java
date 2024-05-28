package arg.hozocabby.database.entities.vehicle;

import arg.hozocabby.database.entities.person.Owner;

public class Vehicle {

    private int id, seats;
    private double chargePerKm, mileage;
    private Owner owner;
    private FuelType  fuelType;
    VehicleType vehicleType;


    public Vehicle(int id, VehicleType vehicleType, Owner owner, int seats, double cpk, double mileage,FuelType fuelType){
        this.id = id;
        this.vehicleType = vehicleType;
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

}
