package arg.hozocabby.database;

import arg.hozocabby.entities.Account;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AccountDataAccess {
    private final Database db;

    private final static String ACCOUNT_CREATE = "INSERT INTO Account(name, mobile, address, password, userType) VALUES (?, ?, ?, ?, ?)";
    private final static String ACCOUNT_QUERY = "SELECT * FROM Account ";
    private final static String ACCOUNT_QUERY_BY_MOBILE = ACCOUNT_QUERY+" WHERE mobile = ?";
    private final static String ACCOUNT_QUERY_BY_ID = ACCOUNT_QUERY+" WHERE id = ?";

    private final HashMap<Integer, Account> accountReferenceMap = new HashMap<>();

    AccountDataAccess(Database db){
        this.db = db;
    }

    public Account createAccount(String name, String phone, String address, String password, Account.UserType type) throws DataAccessException, DataSourceException {

        try(PreparedStatement statement = db.getPreparedStatement(ACCOUNT_CREATE)){
            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setString(3, address);
            statement.setString(4, password);
            statement.setInt(5, type.getOrdinal());

            statement.execute();

            return getAccountByMobile(phone).get();
        } catch (SQLException ex){
            throw new DataAccessException(ex);
        }
    }

    private void saveToInternalReferenceMap(Account acc){
        accountReferenceMap.put(acc.getId(), acc);
    }

    public Optional<Account> getAccountByID(int id) throws DataAccessException, DataSourceException{
        if(accountReferenceMap.containsKey(id))
            return Optional.of(accountReferenceMap.get(id));
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY_BY_ID)){
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(!rs.next())
                return Optional.empty();

            Account queriedAccount = constructAccountResultSet(rs);

            saveToInternalReferenceMap(queriedAccount);

            return Optional.of(queriedAccount);
        } catch (SQLException ex){
            throw new DataAccessException(ex);
        }
    }

    private Account constructAccountResultSet(ResultSet rs) throws DataAccessException{

        try {
            return new Account(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("mobile"),
                    rs.getString("password"),
                    Account.UserType.valueOf(rs.getInt("userType"))
            );
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    public List<Account> getAllAccounts() throws DataAccessException, DataSourceException{
        ArrayList<Account> accounts = new ArrayList<>();

        Account queriedAccount = null;
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY)) {
            ResultSet rs = ps.executeQuery();

            rs.updateRow();

            while(rs.next()){
                queriedAccount = constructAccountResultSet(rs);

                saveToInternalReferenceMap(queriedAccount);

                accounts.add(queriedAccount);
            }
        } catch (SQLException ex){
            throw new DataAccessException(ex);
        }

        return accounts;
    }

    public Optional<Account> getAccountByMobile(String mobile) throws DataSourceException, DataAccessException{
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY_BY_MOBILE)) {
            ps.setString(1, mobile);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Account createdAccount = constructAccountResultSet(rs);

                saveToInternalReferenceMap(createdAccount);

                return Optional.of(createdAccount);
            }
            else
                return Optional.ofNullable(null);
        } catch (SQLException ex){
            throw new DataAccessException(ex);
        }
    }

    public boolean containsAccount(String mobile) throws DataAccessException, DataSourceException{
        try(PreparedStatement ps = db.getPreparedStatement(ACCOUNT_QUERY_BY_MOBILE)){
            ps.setString(1, mobile);

            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException ex){
            throw new DataAccessException(ex);
        }
    }

}
