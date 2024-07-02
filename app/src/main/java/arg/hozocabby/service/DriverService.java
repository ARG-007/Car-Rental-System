package arg.hozocabby.service;

import arg.hozocabby.database.DatabaseManager;
import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Rental;
import arg.hozocabby.entities.Vehicle;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.util.ArrayList;
import java.util.List;

public class DriverService {
    private final DatabaseManager dbMan;

    DriverService(DatabaseManager dbMan) {
        this.dbMan = dbMan;
    }

    public List<Rental> getAssignedRentals(Account driver) throws DataSourceException {
        try {
            return dbMan.getRentalDataAccess().getRentalsWithDriver(driver.getId());
        }catch (DataAccessException dae){
            dae.printStackTrace(System.err);
        }
        return new ArrayList<>();
    }

    public List<Rental> getRentalsByStatusFor(Account driver, Rental.RentalStatus status) throws DataSourceException {
        try {
            return dbMan.getRentalDataAccess().getRentalsWithDriverWithStatus(driver.getId(), status);
        } catch (DataAccessException dae) {
            dae.printStackTrace(System.err);
        }

        return new ArrayList<>();
    }

    public boolean endRental(Account driver, Rental rentToBeEnded) throws DataSourceException{
        try {
            if(rentToBeEnded!=null && driver!=null && rentToBeEnded.getDriver().getId().equals(driver.getId())){
                dbMan.getRentalDataAccess().updateRentalStatus(rentToBeEnded.getId(), Rental.RentalStatus.COMPLETED);
                dbMan.getVehicleDataAccess().updateVehicleStatus(rentToBeEnded.getInfo().getAssignedVehicle().getId(), Vehicle.VehicleStatus.AVAILABLE);
                return true;
            }
        } catch(DataAccessException dae) {
            dae.printStackTrace(System.err);
        }
        return false;
    }


}
