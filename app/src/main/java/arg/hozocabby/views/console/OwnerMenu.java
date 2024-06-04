package arg.hozocabby.views.console;

public class OwnerMenu extends Console{
    @Override
    public void display() {
        separator('^');
        System.out.println("1: View Fleet");
        System.out.println("2: Retire Car From Fleet");
        System.out.println("3: Add Car To Fleet");
        System.out.println("4: View Earnings");
        System.out.println("5: Exit");
        separator('^');

    }
}
