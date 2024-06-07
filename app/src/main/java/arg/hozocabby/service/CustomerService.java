package arg.hozocabby.service;

import arg.hozocabby.database.Database;
import arg.hozocabby.entities.*;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private final Database db;

    public CustomerService(Database db){
        this.db = db;
    }

    public List<Vehicle> queryVehicleOfType(Vehicle.VehicleType type) throws DataSourceException {
        try {
            return db.getVehicleDataAccess().getVehiclesByType(type);
        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            System.err.println(dae.getStackTrace());
            return new ArrayList<>();
        }
    }

    public List<Place> queryReachablePlaces() throws DataSourceException{
        try {
            return db.getPlaceDataAccess().getPlaces();
        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            System.err.println(dae.getStackTrace());
            return new ArrayList<>();
        }
    }

//    public List<Account> getAvailableDrivers() {
//
//    }

    public void bookRent(RentalInfo info) throws DataSourceException{
        Rental rent = new Rental(info);


        try {
//            List<Rental> previousRentals = db.getRentalDataAccess().getRentalOfCustomer(info.getRequester().getId());

            db.getRentalDataAccess().addRental(rent);
            db.getVehicleDataAccess().updateVehicleStatus(info.getAssignedVehicle().getId(), Vehicle.VehicleStatus.BOOKED);


        } catch (DataAccessException dae) {
            System.err.println(dae.getMessage());
        }
    }

    public boolean cancelRent(int rentId) throws DataSourceException{
        try {
            return db.getRentalDataAccess().updateRentalStatus(rentId, Rental.RentalStatus.CANCELLED);
        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            System.err.println(dae.getStackTrace());
            return false;
        }
    }

    public void cancelRent(Rental r) throws DataSourceException{
        cancelRent(r.getId());
    }

    public List<Rental> queryRentalHistory(int accountId) throws DataSourceException{
        try {
            return db.getRentalDataAccess().getRentalOfCustomer(accountId);
        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            System.err.println(dae.getStackTrace());
            return new ArrayList<>();
        }
    }

    public void queryRentalHistory(Account acc) throws DataSourceException{
        queryRentalHistory(acc.getId());
    }
}
