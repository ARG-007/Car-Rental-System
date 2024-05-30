package arg.hozocabby;

import arg.hozocabby.database.Database;
import arg.hozocabby.views.View;
import arg.hozocabby.views.console.*;

public class Main {
    public static void main(String[] args) {
        Database db = Database.getDatabase();



        View view = new LoginMenu();
        view.display();
    }
}