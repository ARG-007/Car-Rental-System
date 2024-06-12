package arg.hozocabby.service;

import arg.hozocabby.database.DatabaseManager;
import arg.hozocabby.entities.*;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;

public class CustomerService {
    private final DatabaseManager dbMan;

    public CustomerService(DatabaseManager dbMan){
        this.dbMan = dbMan;
    }

    public List<Vehicle> queryAvailableVehicleOfType(Vehicle.VehicleType type) throws DataSourceException {
        try {
            return dbMan.getVehicleDataAccess().getVehicleOfTypeWithStatus(type, Vehicle.VehicleStatus.AVAILABLE);

        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            dae.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }

    public List<Place> queryReachablePlaces() throws DataSourceException{
        try {
            return dbMan.getPlaceDataAccess().getPlaces();
        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            dae.printStackTrace(System.err);
            return new ArrayList<>();
        }
    }

    public Rental bookRent(RentalInfo info, boolean driverAssignment) throws DataSourceException, NoSuchElementException {
        Rental rent = new Rental(info);
        EnumSet<Rental.RentalStatus> availablity = EnumSet.of(Rental.RentalStatus.COMPLETED, Rental.RentalStatus.CANCELLED);

        try {
            if(driverAssignment){
                List<Account> drivers = dbMan.getAccountDataAccess().getAllAccountsOfType(Account.UserType.DRIVER);

                List<Account> availableDrivers = new ArrayList<>();

                for(Account d : drivers){
                    boolean available = true;
                    for(Rental r : dbMan.getRentalDataAccess().getRentalsWithDriver(d.getId())) {
                        if(!availablity.contains(r.getStatus())){
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


            rent = dbMan.getRentalDataAccess().addRental(rent);
            dbMan.getVehicleDataAccess().updateVehicleStatus(info.getAssignedVehicle().getId(), Vehicle.VehicleStatus.BOOKED);

        } catch (DataAccessException dae) {
            System.err.println(dae.getMessage());
        }
        return rent;


    }

    public boolean cancelRent(int rentId) throws DataSourceException{

        try {
            cancelRent( dbMan.getRentalDataAccess().getRentalOfId(rentId));

        } catch (DataAccessException dae) {
            System.err.print(dae.getMessage());
            dae.printStackTrace(System.err);
            return false;
        }

        return false;
    }

    public boolean cancelRent(Rental r) throws DataSourceException{
        try {

            if(dbMan.getRentalDataAccess().updateRentalStatus(r.getId(), Rental.RentalStatus.CANCELLED)) {
                return dbMan.getVehicleDataAccess().updateVehicleStatus(
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
            return dbMan.getRentalDataAccess().getRentalOfCustomer(accountId);
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
