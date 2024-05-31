package arg.hozocabby.database.entities;

import java.util.Date;

public class RentalInfo {
    private int id;

    private Account requester;
    private Vehicle requestedVehicle;
    private Account driver;
    private Place pickup, destination;
    private Date pickupTime, reachedTime;
    private double distance;

    private double fare;


}
