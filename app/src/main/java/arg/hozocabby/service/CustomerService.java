package arg.hozocabby.service;

import arg.hozocabby.database.Database;
import arg.hozocabby.entities.*;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
            dae.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }

    public List<Place> queryReachablePlaces() throws DataSourceException{
        try {
            return db.getPlaceDataAccess().getPlaces();
        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            dae.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }

    public Rental bookRent(RentalInfo info, boolean driverAssignment) throws DataSourceException, NoSuchElementException {
        Rental rent = new Rental(info);


        try {
            if(driverAssignment){
                List<Account> drivers = db.getAccountDataAccess().getAllAccountsOfType(Account.UserType.DRIVER);

                List<Account> availableDrivers = new ArrayList<>();

                for(Account d : drivers){
                    boolean available = true;
                    for(Rental r : db.getRentalDataAccess().getRentalsWithDriver(d.getId())) {
                        if(r.getStatus() != Rental.RentalStatus.COMPLETED){
                            available = false;
                            break;
                        }
                    }
                    if(available)
                        availableDrivers.add(d);
                }


                if(availableDrivers.isEmpty())
                    throw new NoSuchElementException("NO_DRIVER_AVAILABLE");

                rent.setDriver(availableDrivers.get(0));
            }


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
            dae.printStackTrace(System.err);
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
            dae.printStackTrace(System.err);
            return false;
        }

        return false;
    }

    public List<Rental> queryRentalHistory(int accountId) throws DataSourceException{
        try {
            return db.getRentalDataAccess().getRentalOfCustomer(accountId);
        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            dae.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }

    public void queryRentalHistory(Account acc) throws DataSourceException{
        queryRentalHistory(acc.getId());
    }

}
