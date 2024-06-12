package arg.hozocabby.views.console;

import arg.hozocabby.entities.Account;
import arg.hozocabby.exceptions.DataAccessException;
import arg.hozocabby.exceptions.DataSourceException;
import arg.hozocabby.service.AuthenticationService;
import arg.hozocabby.service.ServiceRepository;

import java.util.NoSuchElementException;
import java.util.InputMismatchException;
import java.util.Optional;

public class AuthenticationMenu extends Console {
    private static final int EXIT_NUM = Account.UserType.size()+1;
    private Account.UserType selectedRole;
    private final AuthenticationService authService;
    private final ServiceRepository serviceRepository;

    private final Menu roleSelectionMenu = new Menu(), authenticationSelectionMenu = new Menu(), specialAdminMenu = new Menu();

    public AuthenticationMenu(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
        this.authService = serviceRepository.getAuthenticationService();

        roleSelectionMenu
            .setOuterSeparator('=')
            .setInnerSeparator('-')
            .setTitle("User Selection")
            .addOption(Account.UserType.values())
            .addOption("Exit")
            .setPrompt("Enter Your Choice: ");

        authenticationSelectionMenu
            .setOuterSeparator('@')
            .setInnerSeparator('-')
            .addOption("Login", "Register", "Back", "Exit")
            .setPrompt("Select Authentication Mode: ");

        specialAdminMenu
            .setOuterSeparator('@')
            .setInnerSeparator('-')
            .addOption("Login",  "Back", "Exit")
            .setPrompt("Select Authentication Mode: ");

    }


    public void display() throws DataSourceException{
        boolean exit;

        while(true){
            clearScreen();

            int menuOutput = roleSelectionMenu.process();
            clearScreen();

            if(menuOutput == 5)
                return;

            selectedRole = Account.UserType.valueOf(menuOutput);

            clearScreen();

            int authenMode;

            if(selectedRole == Account.UserType.ADMIN) {
                specialAdminMenu.setTitle("Selected User Type: " + selectedRole);
                authenMode = specialAdminMenu.process();
            } else {
                authenticationSelectionMenu.setTitle("Selected User Type: " + selectedRole);
                authenMode = authenticationSelectionMenu.process();
            }

            clearScreen();

            Optional<Account> loggedInAccount = Optional.empty();

            if(selectedRole == Account.UserType.ADMIN) {
                switch (authenMode) {
                    case 1: loggedInAccount = userLogin();break;
                    case 2: break;
                    case 3: return;
                }
            } else {
                switch (authenMode){
                    case 1: loggedInAccount = userLogin();break;
                    case 2: loggedInAccount = userRegister();break;
                    case 3: break;
                    case 4: return;
                }
            }

            if(loggedInAccount.isPresent()){
                Account user = loggedInAccount.get();
                switch(loggedInAccount.get().getType()){
                    case ADMIN -> new AdminMenu(user, serviceRepository.getAdminService()).display();
                    case CUSTOMER -> new CustomerMenu(user, serviceRepository.getCustomerService()).display();
                    case OWNER -> new OwnerMenu(user, serviceRepository.getOwnerService()).display();
                    case DRIVER -> new DriverMenu(user, serviceRepository.getDriverService()).display();
                }
            }

        }


    }



    private Optional<Account> userLogin() throws DataSourceException {
        clearScreen();
        Account account = null;

        int reAttempts = 3;
        separator('+');
        System.out.printf("User Mode : %s\nAuthentication Mode: Login\n", selectedRole);
        separator('-');

        attemptLoop:
        while(reAttempts > 0){
            String mobile = input("Enter Your Mobile: ");
            String password = input("Enter Your Password: ");
            separator('.');

            try {
                account = authService.login(mobile, password, selectedRole);
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

            } catch(DataAccessException dae) {
                System.err.println(dae);

                System.out.println("Internal Error, Try Again or Exit");
            }
            finally {
                separator('-');
            }

        }
        return Optional.ofNullable(account);
    }

    private Optional<Account> userRegister() throws DataSourceException{
        clearScreen();
        separator('=');
        Account acc;
        System.out.printf("Selected User Type: %s\t Authentication Mode: Registration\n", selectedRole);

        separator('+');
        while(true) {

            String name = input("Enter Name: ");
            String phone = input("Enter Mobile Number: ");
            String address = input("Enter Your Address: ");
            String password = input("Enter An Password: ");

            try {
                acc = authService.register(name, phone, address, selectedRole, password);
                break;
            } catch (IllegalArgumentException iea){
                switch(input("An Account for that Mobile Number already Exists, Login Instead [Y/n] ?: ")){
                    case "N":
                    case "n":
                        System.out.println("Redoing Registration, Use Different Mobile Number");
                        break;
                    case "Y":
                    case "y":
                    default:
                        return userLogin();
                }
            } catch(DataAccessException dae) {
                System.err.println(dae);

                System.out.println("Internal Error, Try Again or Exit");
            }
            separator('+');

        }
        return Optional.ofNullable(acc);
    }

    @Deprecated
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

                selectedRole = Account.UserType.valueOf(userType);

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

    @Deprecated
    private int authenticationSelectionMenu(){

        int authenticationMode;
        separator('@');
        System.out.printf("Selected User Type: %s\n", selectedRole);
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
}
