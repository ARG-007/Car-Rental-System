package arg.hozocabby.database;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;

import java.util.*;

import arg.hozocabby.exceptions.DataSourceException;
import org.sqlite.SQLiteConfig;

import arg.hozocabby.Helper;

public class Database implements AutoCloseable{
    private static final Properties props;


    private static final String dbName;
    private static final String CONNECTION_URL;

    private static Database db;

    private Connection connection;
    private static final SQLiteConfig CONN_CONFIG = new SQLiteConfig();

    static {
        try {
            props = Helper.getPropertiesFromResource("config.properties");
            dbName = props.getProperty("db_name");
            CONNECTION_URL = "jdbc:sqlite:"+dbName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String LIST_TABLES = "PRAGMA table_list";

    private final AccountDataAccess accountDataAccess;
    private final VehicleDataAccess vehicleDataAccess;

    static {
        CONN_CONFIG.setJournalMode(SQLiteConfig.JournalMode.WAL);
        CONN_CONFIG.enforceForeignKeys(true);
    }

    private Database() throws DataSourceException {

        if(Boolean.parseBoolean(props.getProperty("force_replace_db")) ){
            close();
            createDatabase();
            getConnection();
        }
        accountDataAccess = new AccountDataAccess(this);
        vehicleDataAccess = new VehicleDataAccess(this);

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

    public static Database getDatabase() throws DataSourceException{
        if(db == null){
            db = new Database();
        }
        return db;
    }

    private void createDatabase() throws DataSourceException{

        System.out.println("The Path: " + Path.of(dbName).toAbsolutePath());

        try(InputStream is = Helper.getResourceAsStream("Database/template.db");
        ) {
            Files.copy(is, Path.of(dbName).toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            validateDatabase();
        } catch (IOException e){
            throw new DataSourceException("Error When Copying File: "+e.getMessage(), e);
        }

    }

    private void executeFromSQLScript(String path) throws DataSourceException{
        try(
                Statement s = getStatement();
                InputStream initializerFileStream = getClass().getClassLoader().getResourceAsStream(path);
                Scanner scriptReader = new Scanner(new InputStreamReader(initializerFileStream))
        ){
            scriptReader.useDelimiter("(;(\\r)?\\n)|((\\r)?\\n)?(--)?.*(--(\\r)?\\n)");

            while(scriptReader.hasNext()) {
                s.execute(scriptReader.next());
            }
        } catch (SQLException | IOException ex){
            throw new DataSourceException("Execution From Script Exception: "+ex.getMessage(), ex);
        }
    }

    public AccountDataAccess getAccountDataAccess(){
        return accountDataAccess;
    }

    public VehicleDataAccess getVehicleDataAccess(){
        return vehicleDataAccess;
    }

    private boolean validateDatabase() throws DataSourceException{
        try(Statement s = getStatement()) {

            s.executeQuery(LIST_TABLES);
        } catch(SQLException sql) {
            throw new DataSourceException("Cannot Validate Database: "+sql);
        }


        return true;
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
