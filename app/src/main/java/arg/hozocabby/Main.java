package arg.hozocabby;

import arg.hozocabby.views.View;
import arg.hozocabby.views.console.*;

public class Main {
    public static void main(String[] args) {
        View view = new LoginMenu();
        view.display();
    }
}