package database;

import java.util.*;
import database.entities.Place;
import database.entities.RentalInfo;
import database.entities.vehicle.*;
import database.entities.person.*;

public class Database {
    private static Database db;



    private Database(){
        loadDatabase();

    }

    public static Database getDatabase(){
        if(db == null){
            db = new Database();
        }
        return db;
    }

    List<Place> places = new ArrayList<>();
    List<Vehicle> vehicles = new ArrayList<>();
    List<Owner> owners = new ArrayList<>();
    List<Customer> customers = new ArrayList<>();
    List<Driver> drivers = new ArrayList<>();
    HashMap<Integer, RentalInfo> rentals = new HashMap<>();


    private void loadDatabase(){

    }
}
