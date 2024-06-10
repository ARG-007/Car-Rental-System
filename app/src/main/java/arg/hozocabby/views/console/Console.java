package arg.hozocabby.views.console;

import arg.hozocabby.entities.Account;
import arg.hozocabby.entities.Rental;
import arg.hozocabby.views.View;

import java.util.*;

abstract class Console implements View{
    protected static final int CONSOLE_WIDTH = 214 , CONSOLE_HEIGHT=67;

    protected Scanner input = new Scanner(System.in);

    protected class Menu{
        private char outerSeparator = '\0', innerSeparator = '\0';
        private String title, prompt;
        private LinkedHashMap<Integer, String> menuOptions = new LinkedHashMap<>();

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
        private int[] colSize;
        private List<String[]> rows = new ArrayList<>();
        private String[] headers;

        private int tableWidth = 0;

        private char cornerSeparator = '+';
        private char topSeparator = '┬', middleSeparator = '┼', bottomSeparator='┴';
        private char tlCorner = '┌', trCorner = '┐', brCorner ='┘', blConer = '└';
        private char leftCorner = '├', rightCorner = '┤';
        private char verticalSeparator = '│';
        private char horizontalSeparator = '─';

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
                tableWidth += i + 3;
            }

            tableWidth -= 1;
        }

        public Table addRow(String... entries) {
            rows.add(entries);
            return this;
        }

        public void clearRows(){
            rows.clear();
        }

        public Table addRow(Object... entries) {
            String[] temp = new String[entries.length];
            for(int i = 0; i<entries.length; i++) {
                temp[i] = entries[i].toString();
            }
            rows.add(temp);
            return this;
        }

        private void tableSeparator(int place){
            char middleSep, lCor, rCor;
            if(place == 1){
                lCor = tlCorner;
                rCor = trCorner;
                middleSep = topSeparator;
            } else if(place == 2) {
                lCor = leftCorner;
                rCor = rightCorner;
                middleSep = middleSeparator;
            } else {
                lCor = blConer;
                rCor = brCorner;
                middleSep = bottomSeparator;
            }
            System.out.print(lCor);
            for (int i = 0; i< colSize.length;i++){
                for(int c=0; c<colSize[i]+2;c++){
                    System.out.print(horizontalSeparator);
                }
                if(i!=colSize.length-1)
                    System.out.print(middleSep);
            }
            System.out.println(rCor);

        }

        public void display() {
            reCalculateColSizes();

            tableSeparator(1);
            System.out.print(verticalSeparator);
            for(int i = 0;i< headers.length;i++) {

                System.out.printf(" %-"+colSize[i]+"s "+verticalSeparator, headers[i]);
            }
            System.out.println();
            tableSeparator(2);

            for(int r =0 ; r< rows.size();r++) {
                String[] s = rows.get(r);
                System.out.print(verticalSeparator);
                for(int i = 0;i <s.length;i++) {

                    System.out.printf(" %-"+colSize[i]+"s "+verticalSeparator, s[i]);
                }
                System.out.println();

                if(r!= rows.size()-1)
                    tableSeparator(2);
            }
            tableSeparator(3);
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

    protected static String center(String text, int width) {
        int center = width/2;
        int textCenter = text.length()/2;

        int pad = center-textCenter;

        StringBuilder padding = new StringBuilder();
        for(int i=0;i<pad;i++) padding.append(" ");

        return padding.append(text).toString();
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

    protected void printRentals(List<Rental> rentals) {
        Table t = new Table("ID", "Vehicle Name", "Pickup Location", "Destination Location", "Pickup Time", "Cost", "Status");

        for(Rental r : rentals){
            t.addRow(
                    r.getId(),
                    r.getInfo().getAssignedVehicle().getName(),
                    r.getInfo().getPickup(),
                    r.getInfo().getDestination(),
                    r.getInfo().getPickupTime(),
                    String.format("%.2f",r.getCost()),
                    r.getStatus()
            );
        }

        t.display();
    }

}
