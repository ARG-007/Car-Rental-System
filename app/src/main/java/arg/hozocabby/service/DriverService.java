package arg.hozocabby.service;

import arg.hozocabby.database.Database;
import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Rental;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.util.ArrayList;
import java.util.List;

public class DriverService {
    private Database db;

    DriverService(Database db) {
        this.db = db;
    }

    public List<Rental> getAssignedRentals(Account driver) throws DataSourceException {
        try {
            return db.getRentalDataAccess().getRentalsWithDriver(driver.getId());
        }catch (DataAccessException dae){
            dae.printStackTrace(System.err);
        }
        return new ArrayList<>();
    }

    public boolean endRental(Account driver, Rental rentToBeEnded) throws DataSourceException{
        try {
            if(rentToBeEnded!=null && driver!=null && rentToBeEnded.getDriver().getId().equals(driver.getId())){
                db.getRentalDataAccess().updateRentalStatus(rentToBeEnded.getId(), Rental.RentalStatus.COMPLETED);
                return true;
            }
        } catch(DataAccessException dae) {
            dae.printStackTrace(System.err);
        }
        return false;
    }
}
