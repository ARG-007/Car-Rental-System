package arg.hozocabby.database;

import arg.hozocabby.database.entities.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class AccountManager {
    private Database db;

    private final static String ACCOUNT_CREATE = "INSERT INTO Account(name, mobile, address, password, userType) VALUES (?, ?, ?, ?, ?)";
    private final static String ACCOUNT_QUERY = "SELECT * FROM Account ";
    private final static String ACCOUNT_QUERY_BY_MOBILE = ACCOUNT_QUERY+" WHERE mobile = ?";
    private final static String ACCOUNT_QUERY_BY_ID = ACCOUNT_QUERY+" WHERE id = ?";


    AccountManager(Database db){
        this.db = db;
    }

    public Account createAccount(String name, String phone, String address, String password, Account.UserType type) throws SQLException {

        try(PreparedStatement statement = db.getPreparedStatement(ACCOUNT_CREATE)){
            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setString(3, address);
            statement.setString(4, password);
            statement.setInt(5, type.getOrdinal());

            statement.execute();

            db.getConnection().commit();

            return getAccountByMobile(phone).get();
        }
    }

    public Optional<Account> getAccountByID(int id) throws SQLException{
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY_BY_ID)){
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(!rs.next())
                return Optional.ofNullable(null);

            return Optional.of(constructAccountResultSet(rs));
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

            rs.updateRow();

            while(rs.next()){
                accounts.add(constructAccountResultSet(rs));
            }
        }

        return accounts;
    }

    public Optional<Account> getAccountByMobile(String mobile) throws SQLException{
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY_BY_MOBILE)) {
            ps.setString(1, mobile);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
                return Optional.of(constructAccountResultSet(rs));
            else
                return Optional.ofNullable(null);
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
