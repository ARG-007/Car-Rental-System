package arg.hozocabby;

import arg.hozocabby.database.Database;
import arg.hozocabby.managers.AuthenticationManager;
import arg.hozocabby.views.View;
import arg.hozocabby.views.console.*;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){

        try (Database db = Database.getDatabase()){

            AuthenticationManager authMan = new AuthenticationManager(db.getAccountManager());


            View view = new AuthenticationMenu(authMan);
            view.display();
        } catch(SQLException e) {
            System.out.println("Database Error" + e.getMessage());
            e.printStackTrace();
        } catch(IOException io){
            System.out.println("IO Error" + io.getMessage());
            io.printStackTrace();
        } catch(Exception e) {
//            System.out.println(e.printStackTrace());
            e.printStackTrace();
        } finally {

        }


    }
}