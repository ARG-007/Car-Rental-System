package arg.hozocabby.views.console;

import arg.hozocabby.views.View;

import java.util.Scanner;

abstract class Console implements View{
    private static final int CONSOLE_WIDTH = 80;
    protected Scanner input = new Scanner(System.in);

    public enum INPUT_TYPE{
        INTEGER,
        DOUBLE,
        LONG,
        STRING,
        BOOLEAN,
        FLOAT
    }

    protected void separator(char c){
        for (int i = 0; i < CONSOLE_WIDTH; i++) {
            System.out.print(c);
        }
        System.out.println();
    }

    protected String input(String prompt){
        System.out.print(prompt);
        return input.nextLine();
    }

    protected void clearScreen(){
        System.out.print("\033[2J\033[3J\033[H");
    }


    protected Integer integerInput(String prompt){
        return Integer.parseInt(input(prompt));
    }
    protected Double doubleInput(String prompt){
        return Double.parseDouble(input(prompt));
    }

    protected  Float floatInput(String prompt){
        return Float.parseFloat(input(prompt));
    }

    protected Boolean booleanInput(String prompt){
        return Boolean.parseBoolean(input(prompt));
    }

    protected Long longInput(String prompt){
        return Long.parseLong(input(prompt));
    }

}
