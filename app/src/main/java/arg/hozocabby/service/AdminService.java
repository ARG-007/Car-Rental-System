package arg.hozocabby.service;

import arg.hozocabby.database.DatabaseManager;
import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Place;
import arg.hozocabby.entities.Rental;
import arg.hozocabby.entities.Vehicle;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private final DatabaseManager dbMan;

    public AdminService(DatabaseManager dbMan) {
        this.dbMan = dbMan;
    }

    public List<Account> getAccounts() throws DataSourceException {
        try {
            return dbMan.getAccountDataAccess().getAllAccounts();
        } catch (DataAccessException dae) {
            System.err.println(dae.getMessage());
            dae.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }

    public List<Vehicle> getVehicles() throws DataSourceException {
        try {
            return dbMan.getVehicleDataAccess().getAllVehicle();
        } catch (DataAccessException dae) {
            System.err.println(dae.getMessage());
            dae.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }

    public List<Rental> getRentals() throws DataSourceException {
        try {
            return dbMan.getRentalDataAccess().getAllRentals();
        } catch (DataAccessException dae) {
            System.err.println(dae.getMessage());
            dae.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }
    public List<Place> getPlaces() throws DataSourceException {
        try {
            return dbMan.getPlaceDataAccess().getPlaces();
        } catch (DataAccessException dae) {
            System.err.println(dae.getMessage());
            dae.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }
}
