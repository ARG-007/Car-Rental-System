package arg.hozocabby;

import arg.hozocabby.database.Database;
import arg.hozocabby.managers.AuthenticationManager;
import arg.hozocabby.views.View;
import arg.hozocabby.views.console.*;

public class Main {
    public static void main(String[] args) {
        Database db = Database.getDatabase();

        AuthenticationManager authMan = new AuthenticationManager(db);


        View view = new LoginMenu(authMan);
        view.display();
    }
}