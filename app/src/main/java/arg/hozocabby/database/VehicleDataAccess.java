package arg.hozocabby.database;

import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Vehicle;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class VehicleDataAccess {
    private final Database db;

    VehicleDataAccess(Database db){
        this.db = db;
    }

    private final HashMap<Integer, Vehicle> vehicleReferenceMap = new HashMap<>();

    private static final String VEHICLE_QUERY = "SELECT * FROM Vehicle";
    private static final String VEHICLE_BY_OWNER = VEHICLE_QUERY + " WHERE owner = ?";
    private static final String VEHICLE_BY_TYPE = VEHICLE_QUERY + " WHERE type = ?";
    private static final String VEHICLE_BY_ID = VEHICLE_QUERY + " WHERE id = ?";


    private void saveToInternalReferenceMap(Vehicle v){
        vehicleReferenceMap.put(v.getId(), v);
    }

    private Vehicle createVehicleFromResultSet(ResultSet rs)  throws DataSourceException, DataAccessException{
        try {
            return new Vehicle(
                    rs.getInt("id"),
                    Vehicle.VehicleType.valueOf(rs.getInt("vehicleType")),
                    db.getAccountDataAccess().getAccountByID(rs.getInt("owner")).get(),
                    rs.getInt("seats"),
                    rs.getDouble("chargePerKm"),
                    rs.getDouble("mileage"),
                    Vehicle.FuelType.valueOf(rs.getInt("fuelType"))
            );
        } catch (SQLException sqlEx) {
            throw new DataAccessException("TABLE_FIELD_MISMATCH", sqlEx);
        }
    }

    private List<Vehicle> getListOfVehicleBy(String query, int value) throws DataSourceException, DataAccessException {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        Vehicle queriedVehicle = null;

        try(PreparedStatement ps = db.getPreparedStatement(query)){
            ps.setInt(1, value);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                queriedVehicle = createVehicleFromResultSet(rs);
                saveToInternalReferenceMap(queriedVehicle);
                vehicles.add(queriedVehicle);
            }
        } catch (SQLException sqlEx) {
            throw new DataAccessException(sqlEx);
        }

        return vehicles;
    }

    public List<Vehicle> getVehiclesOfOwner(int ownerId) throws DataSourceException, DataAccessException {
        return getListOfVehicleBy(VEHICLE_BY_OWNER, ownerId);
    }

    public List<Vehicle> getVehiclesOfOwner(Account acc) throws DataSourceException, DataAccessException{
        return getVehiclesOfOwner(acc.getId());
    }

    public List<Vehicle> getVehiclesByType(Vehicle.VehicleType type) throws DataSourceException, DataAccessException{
        return getListOfVehicleBy(VEHICLE_BY_TYPE, type.getOrdinal());
    }

    public Optional<Vehicle> getVehicleById(int id) throws DataSourceException, DataAccessException{
        if(vehicleReferenceMap.containsKey(id))
            return Optional.of(vehicleReferenceMap.get(id));
        try(PreparedStatement ps = db.getPreparedStatement(VEHICLE_BY_ID)){
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Vehicle v = createVehicleFromResultSet(rs);
                saveToInternalReferenceMap(v);
                return Optional.of(v);
            }

        } catch(SQLException sqlEx) {
            throw new DataAccessException(sqlEx);
        }
        return Optional.empty();
    }
}
