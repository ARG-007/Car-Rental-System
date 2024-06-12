package arg.hozocabby.database;

import arg.hozocabby.entities.Place;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaceDataAccess {

    private final DatabaseManager dbMan;

    private static final String PLACE_QUERY= "SELECT * FROM Place";
    private static final String PLACE_QUERY_BY_ID= PLACE_QUERY + " WHERE place_id = ?";
    private static final String PLACE_CREATE = "INSERT INTO Place(name, lat, long) VALUES (?, ?, ?)";

    PlaceDataAccess(DatabaseManager dbMan) {
        this.dbMan = dbMan;
    }

    private Place constructPlace(ResultSet rs) throws SQLException{
        return new Place(rs.getInt("place_id"), rs.getString("name"), rs.getDouble("lat"), rs.getDouble("long"));
    }

    public Optional<Place> getPlaceById(int id) throws DataAccessException, DataSourceException {
        try(PreparedStatement ps = dbMan.getPreparedStatement(PLACE_QUERY_BY_ID)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return Optional.of(constructPlace(rs));

        } catch (SQLException sql){
            throw new DataAccessException(sql);
        }

        return Optional.empty();
    }

    public List<Place> getPlaces() throws DataSourceException, DataAccessException{
        ArrayList<Place> places = new ArrayList<>();
        try(Statement s = dbMan.getStatement()){
            ResultSet rs = s.executeQuery(PLACE_QUERY);

            while(rs.next()){
                places.add(constructPlace(rs));
            }
        } catch(SQLException sql){
            throw new DataAccessException(sql);
        }

        return places;
    }

    public Place addPlace(String name, double lat, double lon) throws DataSourceException, DataAccessException{
        try(PreparedStatement ps = dbMan.getPreparedStatement(PLACE_CREATE)){
            ps.setString(1, name);
            ps.setDouble(2, lat);
            ps.setDouble(3, lon);

            ps.executeUpdate();

            return new Place(dbMan.getLastInsertedId(), name, lat, lon);
        } catch (SQLException sql) {
            throw new DataAccessException(sql);
        }
    }

}
