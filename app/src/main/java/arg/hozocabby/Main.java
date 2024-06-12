package arg.hozocabby;

import arg.hozocabby.database.DatabaseManager;
import arg.hozocabby.exceptions.DataSourceException;
import arg.hozocabby.service.ServiceRepository;
import arg.hozocabby.views.View;
import arg.hozocabby.views.console.*;

public class Main {
    public static void main(String[] args){

        try (DatabaseManager db = DatabaseManager.getDatabase()){
            View view = new AuthenticationMenu(new ServiceRepository(db));
            view.display();
        } catch(DataSourceException e) {
            System.out.println("Database Error - " + e.getMessage());

            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }catch(Exception e) {
            System.out.println("Exception: "+e);

            System.err.println(e.getMessage());
            e.printStackTrace(System.err);

        }


    }
}