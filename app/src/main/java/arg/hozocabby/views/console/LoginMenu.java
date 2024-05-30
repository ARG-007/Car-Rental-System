package arg.hozocabby.views.console;

import arg.hozocabby.database.entities.user.UserType;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.InputMismatchException;

public class LoginMenu extends Console {
    private static final int EXIT_NUM = UserType.size();
    private UserType selectedUserType;


    private void userSelectionMenu(){
        int userType;

        separator('=');
        System.out.println("User Selection");
        separator('-');
        for (UserType type : UserType.values()){
            System.out.printf("\t%d: %s\n", type.ordinal(), type);
        }
        System.out.printf("\t%d: Exit\n", EXIT_NUM);
        separator('=');


        while(true){
            try {
                userType = integerInput("Enter Your Choice: ");
                separator('-');

                if(userType == EXIT_NUM)
                    return;

                selectedUserType = UserType.valueOf(userType).orElseThrow();

                break;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Enter only the Number");
            } catch (NoSuchElementException e) {
                System.out.println("Give Only Displayed Options");
            } finally {
                separator('-');
            }
        }
    }

    private int authenticationSelectionMenu(){
        int authenticationMode;
        separator('@');
        System.out.printf("Selected Type: %s\n", selectedUserType);
        separator('-');
        System.out.println("1: Login\n2: Register\n3: Back");
        separator('@');
        while(true){
            try {
                authenticationMode = integerInput("Select Authentication Mode: ");

                if(authenticationMode < 1 || authenticationMode>3)
                    throw new IllegalArgumentException("Input is not an valid option");

                break;
            } catch(InputMismatchException | NumberFormatException e) {
                System.out.println("Non-Integer Input Entered");
            } catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
            } finally {
                separator('.');
            }

        }

        return authenticationMode;
    }


    public void display(){


        while(true){
            userSelectionMenu();
            int authen = authenticationSelectionMenu();
            switch(authen){

            }
        }


    }



    private void userLogin() {

    }

    private void userRegister(){

    }
}
