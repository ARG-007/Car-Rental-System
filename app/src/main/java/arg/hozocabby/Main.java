package arg.hozocabby;

import arg.hozocabby.database.Database;
import arg.hozocabby.exceptions.DataSourceException;
import arg.hozocabby.managers.AuthenticationManager;
import arg.hozocabby.views.View;
import arg.hozocabby.views.console.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args){

        try (Database db = Database.getDatabase()){
            AuthenticationManager authMan = new AuthenticationManager(db.getAccountDataAccess());
            View view = new AuthenticationMenu(authMan);
            view.display();
        } catch(DataSourceException e) {
            System.out.println("Database Error - " + e.getMessage());

            System.err.println(e.getMessage());
            e.printStackTrace();
        }catch(Exception e) {
            System.out.println("Exception: "+e);

            System.err.println(e.getMessage());
            e.printStackTrace();

        }


    }
}