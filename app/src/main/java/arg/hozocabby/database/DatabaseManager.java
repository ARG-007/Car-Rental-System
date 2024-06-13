package arg.hozocabby.database;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

import java.util.*;

import arg.hozocabby.exceptions.DataSourceException;
import org.sqlite.SQLiteConfig;

import arg.hozocabby.util.Helper;

public class DatabaseManager implements AutoCloseable{
    private static final Properties props;


    private static final String dbName;
    private static final String CONNECTION_URL;

    private static DatabaseManager dbMan;

    private Connection connection;
    private static final SQLiteConfig CONN_CONFIG = new SQLiteConfig();

    private final AccountDataAccess accountDataAccess;
    private final VehicleDataAccess vehicleDataAccess;
    private final RentalDataAccess rentalDataAccess;
    private final PlaceDataAccess placeDataAccess;

    static {
        try {
            props = Helper.getPropertiesFromResource("config.properties");
            dbName = props.getProperty("db_name");
            CONNECTION_URL = "jdbc:sqlite:"+dbName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CONN_CONFIG.setJournalMode(SQLiteConfig.JournalMode.WAL);
        CONN_CONFIG.enforceForeignKeys(true);
    }

    private DatabaseManager() throws DataSourceException {

        if(!validateDatabase() || Boolean.parseBoolean(props.getProperty("create_db"))){
            createDatabase();
        }
        getConnection();

        accountDataAccess = new AccountDataAccess(this);
        vehicleDataAccess = new VehicleDataAccess(this);
        placeDataAccess = new PlaceDataAccess(this);
        rentalDataAccess = new RentalDataAccess(this);

    }

    public static DatabaseManager getDatabase() throws DataSourceException{
        if(dbMan == null){
            dbMan = new DatabaseManager();
        }
        return dbMan;
    }

    public AccountDataAccess getAccountDataAccess(){
        return accountDataAccess;
    }

    public VehicleDataAccess getVehicleDataAccess(){
        return vehicleDataAccess;
    }

    public RentalDataAccess getRentalDataAccess() {
        return rentalDataAccess;
    }

    public PlaceDataAccess getPlaceDataAccess() {
        return placeDataAccess;
    }

    private Connection getConnection() throws DataSourceException{
        try {
            if(connection == null || connection.isClosed()){
                connection = DriverManager.getConnection(CONNECTION_URL, CONN_CONFIG.toProperties());
            }
            return connection;
        } catch (SQLException sql) {
            throw new DataSourceException("Cannot Create Connection To DB: "+sql.getMessage(), sql);
        }
    }

    static Properties getProps() {
        return props;
    }

    Statement getStatement() throws DataSourceException {
        try {
            return getConnection().createStatement();
        } catch(SQLException sqlEx) {
            throw new DataSourceException("Cannot Create Statement: "+ sqlEx.getMessage(), sqlEx);
        }
    }

    PreparedStatement getPreparedStatement(String statement) throws DataSourceException{
        try {
            return getConnection().prepareStatement(statement);
        } catch (SQLException ex){
            throw new DataSourceException("Cannot Create Prepared Statement: "+ex.getMessage(), ex);
        }
    }

    private void createDatabase() throws DataSourceException{



        if(Boolean.parseBoolean(props.getProperty("use_script_execute"))){
            executeFromSQLScript(Helper.getResourceAsStream(props.getProperty("db_create_script")));
            executeFromSQLScript(Helper.getResourceAsStream(props.getProperty("db_data_add_script")));
        } else {
            try(InputStream is = Helper.getResourceAsStream("Database/template.db");
            ) {
                Files.copy(is, Path.of(dbName).toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e){
                throw new DataSourceException("Error When Copying File: "+e.getMessage(), e);
            }
        }

    }

    private void executeFromSQLScript(InputStream scriptStream) throws DataSourceException{
        try(
                Statement s = getStatement();
                Scanner scriptReader = new Scanner(new InputStreamReader(scriptStream))
        ){
            scriptReader.useDelimiter("(;(\\r)?\\n)|((\\r)?\\n)?(--)?.*(--(\\r)?\\n)");

            while(scriptReader.hasNext()) {
                s.execute(scriptReader.next());
            }
        } catch (SQLException ex){
            throw new DataSourceException("Execution From Script Exception: "+ex.getMessage(), ex);
        }
    }

    private boolean validateDatabase() throws DataSourceException{
       return Files.exists(Path.of(dbName));
    }


    /**
     * Returns Last Inserted RowID, for correct outputs
     * Call This As Soon As an Update Operation Took Place
     * @return {@code rowId}
     * @throws DataSourceException Database Error
     */
    synchronized int getLastInsertedId() throws DataSourceException{
        try (Statement s = getStatement()) {
            ResultSet rs = s.executeQuery("SELECT last_insert_rowid() as lir");
            rs.next();
            int id = rs.getInt("lir");

            return id;
        } catch (SQLException sql) {
            throw new DataSourceException("Exception in Retriving lastRowid: ",sql);
        }
    }

    public void close() throws DataSourceException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqlEx) {
            throw new DataSourceException("Exception In Closing Connection: "+sqlEx.getMessage(), sqlEx);
        }
    }




}
