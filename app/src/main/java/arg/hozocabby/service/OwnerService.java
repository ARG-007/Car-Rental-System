package arg.hozocabby.service;

import arg.hozocabby.database.Database;
import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Rental;
import arg.hozocabby.entities.Vehicle;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OwnerService {
    private final Database db;

    public OwnerService(Database db) {
        this.db = db;
    }

    public Optional<Vehicle> addVehicle(Vehicle v) throws DataSourceException {
        try {
            return Optional.of(db.getVehicleDataAccess().addVehicle(v));
        } catch(DataAccessException dae) {
            dae.printStackTrace(System.err);
        }
        return Optional.empty();
    }

    public List<Vehicle> getVehiclesOf(Account owner) throws DataSourceException{
        try {
            return db.getVehicleDataAccess().getVehiclesOfOwner(owner.getId());
        } catch(DataAccessException dae) {
            dae.printStackTrace(System.err);
        }

        return new ArrayList<>();
    }

    public boolean retireVehicle(Vehicle v) throws DataSourceException{
        try {
            return db.getVehicleDataAccess().updateVehicleStatus(v.getId(), Vehicle.VehicleStatus.RETIRED);
        } catch(DataAccessException dae) {
            dae.printStackTrace(System.err);
        }
        return false;
    }
}
