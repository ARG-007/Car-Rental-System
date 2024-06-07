package arg.hozocabby.views.console;

public class DriverMenu extends Console{

    @Override
    public void display() {
        separator('%');
        System.out.println("1: Current Assignment");
        System.out.println("2: End Assignment");
        System.out.println("3: Previous Assignments");
        System.out.println("4: Get Commissions");
        System.out.println("5: Exit");
        separator('%');
    }
}
