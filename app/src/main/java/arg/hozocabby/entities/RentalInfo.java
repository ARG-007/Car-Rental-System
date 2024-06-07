package arg.hozocabby.entities;

import java.sql.Timestamp;

public class RentalInfo {
    private Integer id;
    private Account requester;
    private Vehicle assignedVehicle;
    private Vehicle.VehicleType requestedVehicleType;
    private Place pickup, destination;
    private Timestamp pickupTime;

    public RentalInfo() { }

    public RentalInfo(Integer id, Account requester, Vehicle assignedVehicle, Vehicle.VehicleType requestedVehicleType, Place pickup, Place destination, Timestamp pickupTime) {
        this.id = id;
        this.requester = requester;
        this.assignedVehicle = assignedVehicle;
        this.requestedVehicleType = requestedVehicleType;
        this.pickup = pickup;
        this.destination = destination;
        this.pickupTime = pickupTime;
    }

    public Integer getId() {
        return id;
    }

    public Account getRequester() {
        return requester;
    }

    public void setRequester(Account requester) {
        this.requester = requester;
    }

    public Vehicle getAssignedVehicle() {
        return assignedVehicle;
    }

    public void setAssignedVehicle(Vehicle assignedVehicle) {
        this.assignedVehicle = assignedVehicle;
    }

    public Vehicle.VehicleType getRequestedVehicleType() {
        return requestedVehicleType;
    }

    public void setRequestedVehicleType(Vehicle.VehicleType requestedVehicleType) {
        this.requestedVehicleType = requestedVehicleType;
    }

    public Place getPickup() {
        return pickup;
    }

    public void setPickup(Place pickup) {
        this.pickup = pickup;
    }

    public Place getDestination() {
        return destination;
    }

    public void setDestination(Place destination) {
        this.destination = destination;
    }

    public Timestamp getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Timestamp pickupTime) {
        this.pickupTime = pickupTime;
    }

    public double getFare() {
        return assignedVehicle.getChargePerKm()*(pickup.distanceBetween(destination));
    }
}
