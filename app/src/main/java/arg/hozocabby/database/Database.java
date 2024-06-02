package arg.hozocabby.database;

import java.io.*;
import java.sql.*;

import java.util.*;
import arg.hozocabby.database.entities.Place;
import arg.hozocabby.database.entities.RentalInfo;
import arg.hozocabby.database.entities.Account;
import arg.hozocabby.database.entities.Vehicle;
import org.sqlite.SQLiteConfig;


public class Database {
    private static final String CONNECTION_URL = "jdbc:sqlite:HozoCabby.db";
    private static Database db;
    private Connection connection;
    private static final SQLiteConfig CONN_CONFIG = new SQLiteConfig();



    static {
        CONN_CONFIG.setJournalMode(SQLiteConfig.JournalMode.WAL);
        CONN_CONFIG.enforceForeignKeys(true);

    }

    private Database() throws SQLException{
        initDatabase();

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

    public static Database getDatabase() throws SQLException{
        if(db == null){
            db = new Database();
        }
        return db;
    }

    public Map<Integer, Place> places = new HashMap<>();
    public Map<Integer, Vehicle> vehicles = new HashMap<>();
    public Map<String, Account> Accounts = new HashMap<>();
    public Map<Integer, RentalInfo> rentals = new HashMap<>();

    private void initDatabase() throws SQLException{

        try(
                Statement s = getStatement();
                InputStream initializerFileStream = getClass().getClassLoader().getResourceAsStream("Database/DB_InitializerScript.sql");
                BufferedReader initializeReader = new BufferedReader(new InputStreamReader(initializerFileStream));
        ){
                Scanner scan = new Scanner(initializeReader);

                while(scan.hasNext()){
                    System.out.println(scan.nextLine());
                }
        } catch (IOException fnf){

        }
    }

    private boolean validateDatabase() {


        return false;
    }


    private void loadDatabase(){


    }


}
