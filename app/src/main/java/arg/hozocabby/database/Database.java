package arg.hozocabby.database;

import java.io.*;
import java.sql.*;

import java.util.*;

import arg.hozocabby.exceptions.DataSourceException;
import org.sqlite.SQLiteConfig;

public class Database implements AutoCloseable{
    private static final String CONNECTION_URL = "jdbc:sqlite:HozoCabby.db";
    private static Database db;
    private Connection connection;
    private static final SQLiteConfig CONN_CONFIG = new SQLiteConfig();

    private final AccountManager accountManager;

    static {
        CONN_CONFIG.setJournalMode(SQLiteConfig.JournalMode.WAL);
        CONN_CONFIG.enforceForeignKeys(true);
    }

    private Database() throws DataSourceException{
        if(!validateDatabase()){
            createDatabase();
        }

        loadDatabase();

        accountManager = new AccountManager(db);
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

        executeFromSQLScript("Database/DB_InitializerScript.sql");
        executeFromSQLScript("Database/CityAdderScript.sql");
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

    public AccountManager getAccountManager(){
        return accountManager;
    }

    private boolean validateDatabase() {


        return false;
    }


    private void loadDatabase(){


    }

    public void close() throws DataSourceException {
        try {
            connection.close();
        } catch (SQLException sqlEx) {
            throw new DataSourceException("Exception In Closing Connection: "+sqlEx.getMessage(), sqlEx);
        }
    }


}
