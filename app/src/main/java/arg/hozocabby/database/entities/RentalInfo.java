package arg.hozocabby.database.entities;

import arg.hozocabby.database.entities.person.*;
import arg.hozocabby.database.entities.vehicle.*;

public class RentalInfo {
    private int id;

    private Customer requester;
    private Vehicle requestedVehicle;
//    Driver driver;
    private Place pickup, destination;
    private double distance;

    private double fare;


}
