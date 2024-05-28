package arg.hozocabby.database;

import java.util.*;
import arg.hozocabby.database.entities.Place;
import arg.hozocabby.database.entities.RentalInfo;
import arg.hozocabby.database.entities.vehicle.*;
import arg.hozocabby.database.entities.person.*;

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
