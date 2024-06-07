package arg.hozocabby.views.console;

import arg.hozocabby.entities.Account;
import arg.hozocabby.views.View;

import java.util.*;

abstract class Console implements View{
    private static final int CONSOLE_WIDTH = 80;
    protected Scanner input = new Scanner(System.in);

    protected class Menu{
        char outerSeparator = '\0', innerSeparator = '\0';
        String title, prompt;
        LinkedHashMap<Integer, String> menuOptions = new LinkedHashMap<>();

        private int index = 1;

        public Menu addOption(String option){
            menuOptions.put(index++, option);
            return this;
        }

        public Menu addOption(String... options){
            for(String opt : options){
                menuOptions.put(index++, opt);
            }
            return this;
        }

        public Menu addOption(List<?> options){
            for(Object o : options){
                menuOptions.put(index++, o.toString());
            }
            return this;
        }

        public Menu addOption(Object[] options) {
            for(Object o: options){
                menuOptions.put(index++, o.toString());
            }
            return this;
        }

        public Menu changeOption(int index, String newText){
            menuOptions.put(index, newText);
            return this;
        }

        public String getOption(int index) {
            return menuOptions.get(index);
        }

        public Menu setInnerSeparator(char c) {
            this.innerSeparator = c;
            return this;}

        public Menu setOuterSeparator(char c) {
            this.outerSeparator = c;
            return this;
        }

        public Menu setTitle(String title) {
            this.title = title;
            return this;
        }

        public Menu setPrompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public int process(){
            int optionSelected;

            separator(outerSeparator);
            System.out.println(title);
            separator(innerSeparator);

            for(Map.Entry<Integer, String> option : menuOptions.entrySet()){
                System.out.printf("%d: %s\n", option.getKey(), option.getValue());
            }

            separator(outerSeparator);


            while(true){
                try {
                    optionSelected = integerInput(prompt);
                    if(menuOptions.containsKey(optionSelected))
                        return optionSelected;
                    else
                        throw new NoSuchElementException();
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("Enter only the Number");
                    separator(innerSeparator);
                } catch (NoSuchElementException e) {
                    System.out.println("Give Only Displayed Options");
                    separator(innerSeparator);
                }
            }
        }

    }

    class Table {
        int colSize[];
        List<String[]> rows = new ArrayList<>();
        String[] headers;

        private int tableWidth = 0;

        char cornerSeparator = '+';
        char verticalSeparator = '│';
        char horizontalSeparator = '─';

        public Table(){ }

        public Table(String... headers) {
            colSize = new int[headers.length];
            this.headers = headers;
        }

        public Table setHeaders(String... headers) {
            colSize = new int[headers.length];
            this.headers = headers;

            return this;
        }

        private void reCalculateColSizes(){
            for(int i = 0; i<headers.length; i++) {
                if(headers[i].length()>colSize[i])
                    colSize[i] = headers[i].length();
            }

            for(String[] entries : rows) {
                for(int i = 0; i<entries.length; i++) {
                    if(entries[i].length()>colSize[i])
                        colSize[i] = entries[i].length();
                }
            }

            tableWidth = 0;

            for(int i : colSize) {
                tableWidth += i + 2;
            }
        }

        public Table addRow(String... entries) {
            rows.add(entries);
            System.out.println("Entries : ");
            System.out.println(Arrays.toString(entries));
            for(int i = 0; i<entries.length; i++) {
                if(entries[i].length()>colSize[i])
                    colSize[i] = entries[i].length();
            }
            return this;
        }

        public Table addRow(Object... entries) {
            String[] temp = new String[entries.length];
            for(int i = 0; i<entries.length; i++) {
                if(entries[i].toString().length()>colSize[i]) {
                    colSize[i] = entries[i].toString().length();
                }
                temp[i] = entries[i].toString();
            }
            rows.add(temp);
            return this;
        }

        public void display() {
            for(String[] s : rows) {
                for(String c : s) {
                    System.out.print(c);
                    System.out.print('\t');
                }
                System.out.println("\n");
            }
        }

    }

    protected void separator(char c){
        for (int i = 0; i < CONSOLE_WIDTH; i++) {
            System.out.print(c);
        }
        System.out.println();
    }

    protected void clearScreen(){
        System.out.print("\033[2J\033[3J\033[H");
    }

    protected String input(String prompt){
        System.out.print(prompt);
        return input.nextLine();
    }

    protected Integer integerInput(String prompt){
        return Integer.parseInt(input(prompt));
    }

    protected Double doubleInput(String prompt){
        return Double.parseDouble(input(prompt));
    }

    protected Float floatInput(String prompt){
        return Float.parseFloat(input(prompt));
    }

    protected Boolean booleanInput(String prompt){
        return Boolean.parseBoolean(input(prompt));
    }

    protected Long longInput(String prompt){
        return Long.parseLong(input(prompt));
    }

}
