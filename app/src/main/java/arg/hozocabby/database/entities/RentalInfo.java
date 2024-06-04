package arg.hozocabby.database.entities;

import java.util.Date;

public class RentalInfo {
    private int id;

    private Account requester;
    private Vehicle assignedVehicle;
    private Vehicle.VehicleType requestedVehicleType;
    private Place pickup, destination;
    private Date pickupTime;

    private double fare;

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

    public Date getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    public double getFare() {
        return assignedVehicle.getChargePerKm()*(pickup.distanceBetween(destination));
    }
}
