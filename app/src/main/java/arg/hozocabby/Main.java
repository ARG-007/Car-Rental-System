package arg.hozocabby;

import arg.hozocabby.database.Database;
import arg.hozocabby.managers.AuthenticationManager;
import arg.hozocabby.views.View;
import arg.hozocabby.views.console.*;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database db = Database.getDatabase();

        AuthenticationManager authMan = new AuthenticationManager(db);


        View view = new AuthenticationMenu(authMan);
        view.display();
    }
}