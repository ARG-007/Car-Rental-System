package arg.hozocabby.database;

import java.util.*;
import arg.hozocabby.database.entities.Place;
import arg.hozocabby.database.entities.RentalInfo;
import arg.hozocabby.database.entities.Account;
import arg.hozocabby.database.entities.Vehicle;

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
    public Map<Integer, Vehicle> vehicles = new HashMap<>();
    public Map<String, Account> users = new HashMap<>();
    public Map<Integer, RentalInfo> rentals = new HashMap<>();


    private void loadDatabase(){

    }


}
