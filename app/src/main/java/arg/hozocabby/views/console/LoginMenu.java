package arg.hozocabby.views.console;

import arg.hozocabby.database.entities.Account;
import arg.hozocabby.managers.AuthenticationManager;

import java.util.NoSuchElementException;
import java.util.InputMismatchException;

public class LoginMenu extends Console {
    private static final int EXIT_NUM = Account.UserType.size();
    private Account.UserType selectedUserType;
    private AuthenticationManager authMan;

    public LoginMenu(AuthenticationManager authMan){
        this.authMan = authMan;
    }

    private boolean userSelectionMenu(){

        int userType;
        clearScreen();
        separator('=');
        System.out.println("User Selection");
        separator('-');
        for (Account.UserType type : Account.UserType.values()){
            System.out.printf("\t%d: %s\n", type.ordinal(), type);
        }
        System.out.printf("\t%d: Exit\n", EXIT_NUM);
        separator('=');


        while(true){
            try {
                userType = integerInput("Enter Your Choice: ");
                separator('-');

                if(userType == EXIT_NUM)
                    return true;

                selectedUserType = Account.UserType.valueOf(userType);

                break;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Enter only the Number");
            } catch (NoSuchElementException e) {
                System.out.println("Give Only Displayed Options");
            } finally {
                separator('-');
            }
        }

        return false;
    }

    private int authenticationSelectionMenu(){
        clearScreen();
        int authenticationMode;
        separator('@');
        System.out.printf("Selected User Type: %s\n", selectedUserType);
        separator('-');
        System.out.println("1: Login\n2: Register\n3: Back\n4: Exit");
        separator('@');
        while(true){
            try {
                authenticationMode = integerInput("Select Authentication Mode: ");

                if(authenticationMode < 1 || authenticationMode>4)
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
        clearScreen();
        return authenticationMode;
    }


    public void display(){
        boolean exit;

        while(true){
            exit = userSelectionMenu();
            if(exit)
                return;

            int authenMode = authenticationSelectionMenu();

            Account loggedInAccount;

            switch (authenMode){
                case 1: userLogin();break;
                case 2: userRegister();break;
                case 3: break;
                case 4: return;
            }

        }


    }



    private void userLogin() {
        clearScreen();

        int reAttempts = 3;
        separator('+');
        System.out.printf("User Mode : %s\nAuthentication Mode: Login\n", selectedUserType);
        separator('-');

        while(reAttempts > 0){
            String mobile = input("Enter Your Mobile: ");
            String password = input("Enter Your Password: ");

            separator('.');

            try {
                Account account = authMan.login(mobile, password, selectedUserType);
            } catch (IllegalArgumentException e){
                switch(e.getMessage()){
                    case "NO SUCH USER PRESENT":
                        System.out.println("No Such Account Exists, Check Your Mobile Number and Try Again");
                        break;
                    case "INCORRECT PASSWORD":
                        System.out.println("You Have Entered Incorrect Password");
                        break;
                    case "USER IS NOT AUTHORIZED":
                        System.out.println("You Do Not Have Access");
                        System.out.println("You will be redirected to User Mode Selection Menu When You Press Enter");

                        return;
                }

                reAttempts--;
                System.out.printf("You Have %d Re-Attempts Left\n", reAttempts);

            } finally {
                separator('-');
            }
        }
    }

    private void userRegister(){
        clearScreen();
        separator('=');

        System.out.printf("Selected User Type: %s\t Authentication Mode: Registration\n", selectedUserType);

        separator('+');
        while(true) {

            String name = input("Enter Name");
            String phone = input("Enter Mobile Number: ");
            String address = input("Enter Your Address: ");
            String password = input("Enter An Password: ");

            try {
                authMan.register(name, phone, address, selectedUserType, password);
                break;
            } catch (IllegalArgumentException iea){
                switch(input("An Account for that Mobile Number already Exists, Login Instead [Y/n] ?: ")){
                    case "\n":
                    case "Y":
                    case "y":
                        userLogin();
                        return;
                    case "N":
                    case "n":
                        System.out.println("Redoing Registration, Use Different Mobile Number");
                        break;
                    default:
                        System.out.println("Invalid Input, Redoing Registration, Use Different Mobile Number");
                }
            }
            separator('+');

        }
    }
}
