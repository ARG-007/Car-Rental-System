package arg.hozocabby.database;

import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Vehicle;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class VehicleDataAccess {
    private final Database db;

    private final HashMap<Integer, Vehicle> vehicleReferenceMap = new HashMap<>();

    private static final String VEHICLE_QUERY = "SELECT * FROM Vehicle";

    private static final String VEHICLE_BY_OWNER = VEHICLE_QUERY + " WHERE owner = ?";
    private static final String VEHICLE_BY_TYPE = VEHICLE_QUERY + " WHERE type = ?";
    private static final String VEHICLE_BY_ID = VEHICLE_QUERY + " WHERE id = ?";
    private static final String VEHICLE_BY_STATUS = VEHICLE_QUERY + " WHERE status = ?";
    private static final String VEHICLE_BY_TYPE_WITH_STATUS = VEHICLE_QUERY + " WHERE type = ? and status = ?";
    private static final String VEHICLE_CREATE = "INSERT INTO Vehicle(seats, chargePerKm, owner, mileage, fuelType, vehicleType) values (?, ?, ?, ?, ?, ?)";

    private static final String VEHICLE_UPDATE_STATUS = "UPDATE Vehicle SET status = ? WHERE id = ?";

    VehicleDataAccess(Database db){
        this.db = db;
    }

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

    private List<Vehicle> getListOfVehicleBy(String query, int... values) throws DataSourceException, DataAccessException {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        Vehicle queriedVehicle = null;

        try(PreparedStatement ps = db.getPreparedStatement(query)){
            int pi = 1;
            for(Integer value : values){
                ps.setInt(pi++, value);
            }

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

    public List<Vehicle> getVehiclesByType(Vehicle.VehicleType type) throws DataSourceException, DataAccessException{
        return getListOfVehicleBy(VEHICLE_BY_TYPE, type.getOrdinal());
    }

    public List<Vehicle> getVehicleByStatus(Vehicle.VehicleStatus status) throws DataSourceException, DataAccessException{
        return getListOfVehicleBy(VEHICLE_BY_STATUS, status.getOrdinal());
    }

    public List<Vehicle> getVehicleOfTypeWithStatus(Vehicle.VehicleType type, Vehicle.VehicleStatus status) throws DataSourceException, DataAccessException {
        return getListOfVehicleBy(VEHICLE_BY_TYPE_WITH_STATUS, type.getOrdinal(), status.getOrdinal());
    }

    public Vehicle addVehicle(Vehicle v) throws DataSourceException, DataAccessException{
        try(PreparedStatement ps = db.getPreparedStatement(VEHICLE_CREATE)) {
            ps.setInt(1, v.getSeats());
            ps.setDouble(2, v.getChargePerKm());
            ps.setInt(3, v.getOwner().getId());
            ps.setDouble(4, v.getMileage());
            ps.setInt(5, v.getFuelType().getOrdinal());
            ps.setInt(6, v.getVehicleType().getOrdinal());

            ps.executeUpdate();

            ResultSet rs = ps.executeQuery("SELECT last_inserted_row() as lir");
            rs.next();
            int id = rs.getInt("lir");

            Vehicle newV = new Vehicle(id, v.getVehicleType(), v.getOwner(), v.getSeats(), v.getChargePerKm(), v.getMileage(), v.getFuelType());
            saveToInternalReferenceMap(newV);

            return newV;
        } catch (SQLException sqe) {
            throw new DataAccessException(sqe);
        }
    }

    public boolean updateVehicleStatus(int id, Vehicle.VehicleStatus vs) throws DataAccessException, DataSourceException{
        boolean success = false;
        try(PreparedStatement ps = db.getPreparedStatement(VEHICLE_UPDATE_STATUS)){
            ps.setInt(1, vs.getOrdinal());
            ps.setInt(2, id);

            success = ps.executeUpdate()>0;
        } catch (SQLException sqlException) {
            throw new DataAccessException(sqlException);
        }
        return success;
    }

    public boolean updateVehicleStatus(Vehicle v) throws DataAccessException, DataSourceException{
        return updateVehicleStatus(v.getId(), v.getStatus());
    }
}
