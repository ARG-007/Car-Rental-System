package arg.hozocabby.service;

import arg.hozocabby.database.DatabaseManager;
import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Vehicle;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OwnerService {
    private final DatabaseManager dbMan;

    public OwnerService(DatabaseManager dbMan) {
        this.dbMan = dbMan;
    }

    public Optional<Vehicle> addVehicle(Vehicle v) throws DataSourceException {
        try {
            return Optional.of(dbMan.getVehicleDataAccess().addVehicle(v));
        } catch(DataAccessException dae) {
            dae.printStackTrace(System.err);
        }
        return Optional.empty();
    }

    public List<Vehicle> getVehiclesOf(Account owner) throws DataSourceException{
        try {
            return dbMan.getVehicleDataAccess().getVehiclesOfOwner(owner.getId());
        } catch(DataAccessException dae) {
            dae.printStackTrace(System.err);
        }

        return new ArrayList<>();
    }

    public boolean retireVehicle(Vehicle v) throws DataSourceException{
        try {
            return dbMan.getVehicleDataAccess().updateVehicleStatus(v.getId(), Vehicle.VehicleStatus.RETIRED);
        } catch(DataAccessException dae) {
            dae.printStackTrace(System.err);
        }
        return false;
    }
}
