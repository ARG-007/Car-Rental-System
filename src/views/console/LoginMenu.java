package views.console;

public class LoginMenu extends Console {
    public void display(){
        separator('=');
        System.out.println("User Selection");
        separator('-');
        System.out.println("1: Owner\n2: Customer");
        separator('-');

        switch(integerInput("Enter Your Choice: ")){
            case 1: break;
            case 2: break;
            case 3: return;
            default:
                System.out.println("Wrong Choice, Choose Wisely Next Time");
        }
    }
}
