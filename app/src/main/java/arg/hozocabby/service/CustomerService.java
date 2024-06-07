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

    public List<Vehicle> queryAvailableVehicleOfType(Vehicle.VehicleType type) throws DataSourceException {
        try {
            return db.getVehicleDataAccess().getVehicleOfTypeWithStatus(type, Vehicle.VehicleStatus.AVAILABLE);
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

    public Rental bookRent(RentalInfo info) throws DataSourceException{
        Rental rent = new Rental(info);


        try {
//            List<Rental> previousRentals = db.getRentalDataAccess().getRentalOfCustomer(info.getRequester().getId());

            rent = db.getRentalDataAccess().addRental(rent);
            db.getVehicleDataAccess().updateVehicleStatus(info.getAssignedVehicle().getId(), Vehicle.VehicleStatus.BOOKED);

        } catch (DataAccessException dae) {
            System.err.println(dae.getMessage());
        }
        return rent;
    }

    public boolean cancelRent(int rentId) throws DataSourceException{

        try {
            cancelRent( db.getRentalDataAccess().getRentalOfId(rentId));

        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            System.err.println(dae.getStackTrace());
            return false;
        }

        return false;
    }

    public boolean cancelRent(Rental r) throws DataSourceException{
        try {

            if(db.getRentalDataAccess().updateRentalStatus(r.getId(), Rental.RentalStatus.CANCELLED)) {
                return db.getVehicleDataAccess().updateVehicleStatus(
                        r.getInfo().getAssignedVehicle().getId(),
                        Vehicle.VehicleStatus.AVAILABLE
                );
            }

        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            System.err.println(dae.getStackTrace());
            return false;
        }

        return false;
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
