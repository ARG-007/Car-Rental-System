package arg.hozocabby.database;

import arg.hozocabby.database.entities.Account;
import arg.hozocabby.database.entities.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class VehicleManager {
    private final Database db;

    VehicleManager(Database db){
        this.db = db;
    }

    private final HashMap<Integer, Vehicle> vehicleRef = new HashMap<>();

    private Vehicle createVehicleFromResultSet(ResultSet rs)  throws SQLException{
        return new Vehicle(
                rs.getInt("id"),
                Vehicle.VehicleType.valueOf(rs.getInt("vehicleType")),
                db.getAccountManager().getAccountByID(rs.getInt("owner")).get(),
                rs.getInt("seats"),
                rs.getDouble("chargePerKm"),
                rs.getDouble("mileage"),
                Vehicle.FuelType.valueOf(rs.getInt("fuelType"))
        );
    }

    public ArrayList<Vehicle> getVehiclesOfOwner(int ownerId) throws SQLException {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        try(PreparedStatement ps = db.getPreparedStatement("SELECT * FROM Vehicles WHERE owner = ?")){
            ps.setInt(1, ownerId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

            }
        }

        return vehicles;
    }

    public ArrayList<Vehicle> getVehiclesOfOwner(Account acc) throws SQLException{
        return getVehiclesOfOwner(acc.getId());
    }
}
