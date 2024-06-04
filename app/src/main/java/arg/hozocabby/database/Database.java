package arg.hozocabby.database;

import java.io.*;
import java.sql.*;

import java.util.*;
import arg.hozocabby.database.entities.Place;
import arg.hozocabby.database.entities.RentalInfo;
import arg.hozocabby.database.entities.Account;
import arg.hozocabby.database.entities.Vehicle;
import org.sqlite.SQLiteConfig;

public class Database implements AutoCloseable{
    private static final String CONNECTION_URL = "jdbc:sqlite:HozoCabby.db";
    private static Database db;
    private Connection connection;
    private static final SQLiteConfig CONN_CONFIG = new SQLiteConfig();



    static {
        CONN_CONFIG.setJournalMode(SQLiteConfig.JournalMode.WAL);
        CONN_CONFIG.enforceForeignKeys(true);
    }

    private Database() throws SQLException, IOException{
        if(!validateDatabase()){
            createDatabase();
        }

        loadDatabase();

    }

    Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(CONNECTION_URL, CONN_CONFIG.toProperties());
        }
        return connection;
    }

    Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }


    PreparedStatement getPreparedStatement(String statement) throws SQLException{
        return getConnection().prepareStatement(statement);
    }

    public static Database getDatabase() throws SQLException, IOException{
        if(db == null){
            db = new Database();
        }
        return db;
    }

    public Map<Integer, Place> places = new HashMap<>();
    public Map<Integer, Vehicle> vehicles = new HashMap<>();
    public Map<String, Account> Accounts = new HashMap<>();
    public Map<Integer, RentalInfo> rentals = new HashMap<>();

    private void createDatabase() throws SQLException, IOException{

        executeFromSQLScript("Database/DB_InitializerScript.sql");
        executeFromSQLScript("Database/CityAdderScript.sql");
    }

    private void executeFromSQLScript(String path) throws SQLException, IOException{
        try(
                Statement s = getStatement();
                InputStream initializerFileStream = getClass().getClassLoader().getResourceAsStream(path);
                Scanner scriptReader = new Scanner(new InputStreamReader(initializerFileStream))
        ){
            scriptReader.useDelimiter("(;(\\r)?\\n)|((\\r)?\\n)?(--)?.*(--(\\r)?\\n)");

            while(scriptReader.hasNext()) {
                s.execute(scriptReader.next());
            }

        }
    }

    public AccountManager getAccountManager(){
        return new AccountManager(db);
    }

    private boolean validateDatabase() {


        return false;
    }


    private void loadDatabase(){


    }

    public void close() throws SQLException{
        connection.close();
    }


}
