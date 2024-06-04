package arg.hozocabby.views.console;

import arg.hozocabby.database.entities.Account;
import arg.hozocabby.managers.AuthenticationManager;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;
import java.util.Optional;

public class AuthenticationMenu extends Console {
    private static final int EXIT_NUM = Account.UserType.size()+1;
    private Account.UserType selectedUserType;
    private final AuthenticationManager authMan;

    public AuthenticationMenu(AuthenticationManager authMan){
        this.authMan = authMan;
    }

    private boolean userSelectionMenu(){

        int userType;
        separator('=');
        System.out.println("User Selection");
        separator('-');
        for (Account.UserType type : Account.UserType.values()){
            System.out.printf("\t%d: %s\n", type.getOrdinal(), type);
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

        return authenticationMode;
    }


    public void display() {
        boolean exit;

        while(true){
            clearScreen();
            exit = userSelectionMenu();
            clearScreen();

            if(exit)
                return;

            clearScreen();
            int authenMode = authenticationSelectionMenu();
            clearScreen();

            Optional<Account> loggedInAccount = Optional.empty();

            try {
                switch (authenMode){
                    case 1: loggedInAccount = userLogin();break;
                    case 2: loggedInAccount = userRegister();break;
                    case 3: break;
                    case 4: return;
                }
            } catch(SQLException se){
                System.out.println("Database Error: "+ se.getMessage());
                System.exit(1);
            }

            if(loggedInAccount.isPresent()){
                switch(loggedInAccount.get().getType()){
                    case ADMIN -> new AdminMenu().display();
                    case CUSTOMER -> new CustomerMenu(loggedInAccount.get()).display();
                    case OWNER -> new OwnerMenu().display();
                    case DRIVER -> new DriverMenu().display();
                }
            }

        }


    }



    private Optional<Account> userLogin() throws SQLException {
        clearScreen();
        Account account = null;

        int reAttempts = 3;
        separator('+');
        System.out.printf("User Mode : %s\nAuthentication Mode: Login\n", selectedUserType);
        separator('-');

        attemptLoop:
        while(reAttempts > 0){
            String mobile = input("Enter Your Mobile: ");
            String password = input("Enter Your Password: ");
            separator('.');

            try {
                account = authMan.login(mobile, password, selectedUserType);
                break;
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
                        break attemptLoop;
                }

                reAttempts--;
                System.out.printf("You Have %d Re-Attempts Left\n", reAttempts);

            } finally {
                separator('-');
            }

        }
        return Optional.ofNullable(account);
    }

    private Optional<Account> userRegister() throws SQLException{
        clearScreen();
        separator('=');
        Account acc;
        System.out.printf("Selected User Type: %s\t Authentication Mode: Registration\n", selectedUserType);

        separator('+');
        while(true) {

            String name = input("Enter Name: ");
            String phone = input("Enter Mobile Number: ");
            String address = input("Enter Your Address: ");
            String password = input("Enter An Password: ");

            try {
                acc = authMan.register(name, phone, address, selectedUserType, password);
                break;
            } catch (IllegalArgumentException iea){
                switch(input("An Account for that Mobile Number already Exists, Login Instead [Y/n] ?: ")){
                    case "\n":
                    case "Y":
                    case "y":
                        return userLogin();
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
        return Optional.ofNullable(acc);
    }
}
