package arg.hozocabby.database;

import java.util.*;
import arg.hozocabby.database.entities.Place;
import arg.hozocabby.database.entities.RentalInfo;
import arg.hozocabby.database.entities.vehicle.*;
import arg.hozocabby.database.entities.user.*;

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

    public Map<Integer, Place> places = new HashMap<>();
    public List<Vehicle> vehicles = new ArrayList<>();
    public Map<String, User> users = new HashMap<>();
    public List<Driver> drivers = new ArrayList<>();
    public Map<Integer, RentalInfo> rentals = new HashMap<>();


    private void loadDatabase(){

    }


}
