package arg.hozocabby.database;

import arg.hozocabby.database.entities.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountManager {
    private Database db;

    private final static String ACCOUNT_CREATE = "INSERT INTO Account(name, mobile, address, password, userType) VALUES (%s, %s, %s, %s, %d)";
    private final static String ACCOUNT_QUERY = "SELECT * FROM Account ";
    private final static String ACCOUNT_QUERY_BY_MOBILE = ACCOUNT_QUERY+" WHERE mobile = %s";
    private final static String ACCOUNT_QUERY_BY_ID = ACCOUNT_QUERY+" WHERE id=%d";


    AccountManager(Database db){
        this.db = db;
    }

    public void createAccount(Account account, String password) throws SQLException {

        try(PreparedStatement statement = db.getPreparedStatement(ACCOUNT_CREATE)){
            statement.setString(1, account.getName());
            statement.setString(2, account.getPhone());
            statement.setString(3, account.getAddress());
            statement.setString(4, password);
            statement.setInt(5, account.getType().getOrdinal());

            statement.execute();

            db.getConnection().commit();
        }
    }

    public Account getAccountByID(int id) throws SQLException{
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY_BY_ID)){
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(!rs.next())
                throw new IllegalArgumentException("NO_ACCOUNT_EXISTS");

            return constructAccountResultSet(rs);
        }
    }

    private Account constructAccountResultSet(ResultSet rs) throws SQLException{
        return new Account(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("mobile"),
                rs.getString("password"),
                Account.UserType.valueOf(rs.getInt("userType"))
        );
    }

    public ArrayList<Account> getAllAccounts() throws SQLException{
        ArrayList<Account> accounts = new ArrayList<>();
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY)) {
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                accounts.add(constructAccountResultSet(rs));
            }
        }

        return accounts;
    }

    public Account getAccount(String mobile) throws SQLException{
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY_BY_MOBILE)) {
            ps.setString(1, mobile);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return constructAccountResultSet(rs);
            else
                return null;
        }
    }

    public boolean containsAccount(String mobile) throws SQLException{
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY_BY_MOBILE)){
            ps.setString(1, mobile);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        }
    }

}
