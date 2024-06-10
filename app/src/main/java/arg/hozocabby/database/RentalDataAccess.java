package arg.hozocabby.database;

import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Rental;
import arg.hozocabby.entities.RentalInfo;
import arg.hozocabby.entities.Vehicle;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentalDataAccess {
    private final Database db;

    private static final String RENTAL_QUERY = "SELECT * FROM Rental FULL JOIN RentalInfo USING(info_id)";

    private static final String RENTAL_UPDATE_DRIVER= "UPDATE Rental SET driver_id = ? WHERE rental_id = ?";
    private static final String RENTAL_UPDATE_STATUS = "UPDATE Rental SET rentalStatus_id = ? WHERE rental_id = ?";

    private static final String RENTAL_INFO_CREATE = "INSERT INTO RentalInfo (requester_id, requestedVehicle_id, requestedVehicleType_id, pickupPlace_id, destinationPlace_id, pickupTime) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String RENTAL_CREATE = "INSERT INTO Rental(info_id, driver_id, distance, fareEndTime, fareCost, rentedOn) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String RENTAL_QUERY_BY_ID = RENTAL_QUERY + " WHERE rental_id = ? ";
    private static final String RENTAL_QUERY_BY_DRIVER_ID = RENTAL_QUERY + " WHERE driver_id = ? ";
    private static final String RENTAL_QUERY_BY_DRIVER_ID_WITH_STATUS = RENTAL_QUERY_BY_DRIVER_ID + " and rentalStatus_id = ? ";
    private static final String RENTAL_QUERY_BY_STATUS = RENTAL_QUERY + " WHERE rentalStatus_id = ?";
    private static final String RENTAL_QUERY_BY_CUSTOMER = RENTAL_QUERY + " WHERE requester_id = ? ";
    private static final String RENTAL_QUERY_BY_VEHICLE_ID = RENTAL_QUERY + " WHERE requestedVehicle_id = ?";

    RentalDataAccess(Database db) {
        this.db = db;
    }

    private Rental constructInfoFrom(ResultSet rs) throws DataSourceException, DataAccessException {
        try {

            int vID = rs.getInt("requestedVehicle_id");
            Vehicle vehicle = vID!=0 ? db.getVehicleDataAccess().getVehicleById(vID).get():null;

            RentalInfo ri = new RentalInfo(
                    rs.getInt("info_id"),
                    db.getAccountDataAccess().getAccountByID(rs.getInt("requester_id")).get(),
                    vehicle,
                    Vehicle.VehicleType.valueOf(rs.getInt("requestedVehicleType_id")),
                    db.getPlaceDataAccess().getPlaceById(rs.getInt("pickupPlace_id")).get(),
                    db.getPlaceDataAccess().getPlaceById(rs.getInt("destinationPlace_id")).get(),
                    rs.getTimestamp("pickupTime")
            );

            int dID = rs.getInt("driver_id");
            Account driver = dID!=0 ? db.getAccountDataAccess().getAccountByID(dID).get() : null;

            Rental ren = new Rental(
                    rs.getInt("rental_id"),
                    ri,
                    driver,
                    rs.getDouble("distance"),
                    rs.getTimestamp("fareEndTime"),
                    rs.getDouble("fareCost"),
                    Rental.RentalStatus.valueOf(rs.getInt("rentalStatus_id")),
                    rs.getTimestamp("rentedOn")
            );

            return ren;
        } catch(SQLException sql) {
            throw new DataAccessException(sql);
        }
    }

    public Rental getRentalOfId(int id) throws DataSourceException, DataAccessException {
        try(PreparedStatement ps = db.getPreparedStatement(RENTAL_QUERY_BY_ID)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            rs.next();

            return constructInfoFrom(rs);
        } catch (SQLException se) {
            throw new DataAccessException(se);
        }
    }

    public List<Rental> getAllRentals() throws DataSourceException, DataAccessException{
        ArrayList<Rental> rentals = new ArrayList<>();

        try(PreparedStatement ps = db.getPreparedStatement(RENTAL_QUERY)) {
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                rentals.add(constructInfoFrom(rs));
            }
        } catch (SQLException sq){
            throw new DataAccessException(sq);
        }

        return rentals;
    }

    private List<Rental> getRentalsBy(String statement, Integer... param) throws DataSourceException, DataAccessException{
        ArrayList<Rental> rentals = new ArrayList<>();

        try(PreparedStatement ps = db.getPreparedStatement(statement)) {
            int index = 1;
            for(int i : param){
                ps.setInt(index++, i);
            }
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                rentals.add(constructInfoFrom(rs));
            }
        } catch (SQLException sq){
            throw new DataAccessException(sq);
        }

        return rentals;
    }

    public List<Rental> getRentalsWithDriver(int driverid) throws DataSourceException, DataAccessException{
        return getRentalsBy(RENTAL_QUERY_BY_DRIVER_ID, driverid);
    }

    public List<Rental> getRentalsWithDriverWithStatus(int driverid, Rental.RentalStatus rs) throws DataSourceException, DataAccessException{
        return getRentalsBy(RENTAL_QUERY_BY_DRIVER_ID_WITH_STATUS, driverid, rs.getOrdinal());
    }

    public List<Rental> getRentalWithStatus(Rental.RentalStatus status) throws DataSourceException, DataAccessException {
        return getRentalsBy(RENTAL_QUERY_BY_STATUS, status.getOrdinal());
    }

    public List<Rental> getRentalOfCustomer(int customerId) throws DataSourceException, DataAccessException {
        return getRentalsBy(RENTAL_QUERY_BY_CUSTOMER, customerId);
    }

    public List<Rental> getRentalWithVehicle(int vehicleId) throws DataSourceException, DataAccessException {
        return getRentalsBy(RENTAL_QUERY_BY_VEHICLE_ID, vehicleId);
    }

    public boolean updateRentalDriver(int rentalId, int driverId) throws DataAccessException, DataSourceException {
        try(PreparedStatement ps = db.getPreparedStatement(RENTAL_UPDATE_DRIVER)) {
            ps.setInt(1, driverId);
            ps.setInt(2, rentalId);

            int i = ps.executeUpdate();

            return i > 0;

        } catch (SQLException sq){
            throw new DataAccessException(sq);
        }
    }

    public boolean updateRentalStatus(int rentalId, Rental.RentalStatus status) throws DataAccessException, DataSourceException {
        try(PreparedStatement ps = db.getPreparedStatement(RENTAL_UPDATE_STATUS)) {
            ps.setInt(1, status.getOrdinal());
            ps.setInt(2, rentalId);

            int i = ps.executeUpdate();

            return i > 0;

        } catch (SQLException sq){
            throw new DataAccessException(sq);
        }
    }

    public Rental addRental(Rental rental) throws DataSourceException, DataAccessException{
        try(
            PreparedStatement rentInFo = db.getPreparedStatement(RENTAL_INFO_CREATE);
            PreparedStatement rent = db.getPreparedStatement(RENTAL_CREATE);
        ){
            RentalInfo reIf = rental.getInfo();
            rentInFo.setInt(1, reIf.getRequester().getId());
            rentInFo.setInt(2, reIf.getAssignedVehicle() != null? reIf.getAssignedVehicle().getId() : 0);
            rentInFo.setInt(3,reIf.getRequestedVehicleType().getOrdinal());
            rentInFo.setInt(4, reIf.getPickup().id());
            rentInFo.setInt(5, reIf.getDestination().id());
            rentInFo.setTimestamp(6, reIf.getPickupTime());

            rentInFo.executeUpdate();

            int rentInfoId = db.getLastInsertedId();

            rent.setInt(1, rentInfoId);
            rent.setInt(2, rental.getDriver()!=null?rental.getDriver().getId():0);
            rent.setDouble(3, rental.getDistance());
            rent.setTimestamp(4, rental.getFareEndTime());
            rent.setDouble(5, rental.getCost());
            rent.setTimestamp(6, rental.getRentedOn());

            rent.executeUpdate();

            int rentId = db.getLastInsertedId();

            RentalInfo newInfo = new RentalInfo(rentInfoId, reIf.getRequester(), reIf.getAssignedVehicle(), reIf.getRequestedVehicleType(), reIf.getPickup(), reIf.getDestination(), reIf.getPickupTime());

            return new Rental(rentId, newInfo, rental.getDriver(), rental.getDistance(), rental.getFareEndTime(), rental.getCost(), rental.getStatus(), rental.getRentedOn());

        } catch (SQLException sql){
            throw new DataAccessException(sql);
        }
    }
}
