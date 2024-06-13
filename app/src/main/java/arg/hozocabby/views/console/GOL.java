package arg.hozocabby.views.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static java.lang.Thread.sleep;

public class GOL extends Console{

    private int LIFE_HEIGHT = CONSOLE_HEIGHT-15, LIFE_WIDTH = CONSOLE_WIDTH-2;
    private char empty = ' ', fill = '#';
    private int fps = 5;

//    private static final List<String> headerLine = Helper.getFileContent("nameArt/BigMoney-ne.txt");

    private boolean gameLoop = true;

    private static final String STAT_STRING = "FPS: %d \t POPULATION: %d \t GENERATION: %d";

    private static final String instructions = center("Press(+Enter) [e] To Exit; [space] To Pause; [.] to Step One Generation; [s] to Resume; [r] to reset; [i] to increase fps; [k] to decrease fps", CONSOLE_WIDTH-2);

    Life life = new Life(LIFE_HEIGHT, LIFE_WIDTH);

    private class Life {
        private int height;
        private int width;

        private int population = 0;

        private int generation = 0;

        public byte[][] oldGen;
        public byte[][] curGen;

        public Life(int height, int width) {
            this.height = height;
            this.width = width;

            oldGen = new byte[height+2][width+2];
            curGen = new byte[height+2][width+2];
        }

        private boolean isWithinBoundary(int x, int y){
            return (x>=0) && (x<height) && (y>=0) && (y<width);
        }

        public byte get(int r, int c) {
            if(isWithinBoundary(r, c))
                return curGen[r+1][c+1];
            return 0;
        }

        public void randFill(int coverage){
            for(int i=1;i<height+1; i++) {
                for (int j = 1; j < width+1; j++) {
                    curGen[i][j] = (byte) (Math.random() * 100 < coverage ? 1 : 0);

                    if(curGen[i][j]==1) population++;
                }
            }
            generation=0;
        }

        private int  neighbourCount(int x, int y){
            return curGen[x-1][y-1] + curGen[x-1][y] + curGen[x-1][y+1]
                    + curGen[x][y-1]   + curGen[x][y+1]
                    + curGen[x+1][y-1] + curGen[x+1][y] + curGen[x+1][y+1];
        }

        public void evolve() {
            int count;
            int newPopulation = 0;
            for(int i=1;i<height+1;i++){
                for(int j=1;j<width+1;j++) {
                    count = neighbourCount(i,j);

                    oldGen[i][j] = (byte)(((count==2 && curGen[i][j]!=0) || count==3) ? 1 : 0);

                    if(oldGen[i][j]==1) newPopulation ++;
                }
            }
            population = newPopulation;

            generation++;
            byte[][] temp = curGen;
            curGen = oldGen;
            oldGen = temp;
        }

    }

    static {
        StringBuilder sb = new StringBuilder();

    }

    private String buildScreenOutput() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<LIFE_HEIGHT;i++){
            for (int j=0;j<LIFE_WIDTH;j++) {
                sb.append(life.get(i,j)==1?fill:empty);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private long getSleepTime(){
        return 1000/fps;
    }

    private void increaseFps(){
        if(fps!=60)
            fps++;
    }

    private void decreaseFps(){
        if(fps!=1)
            fps--;
    }


    @Override
    public void display()  {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        life.randFill(50);


        while(true) {
            clearScreen();
            separator('=');
            System.out.println(center(STAT_STRING.formatted(fps, life.population, life.generation), CONSOLE_WIDTH));
            separator('-');
            System.out.println(center(instructions, CONSOLE_WIDTH));
            separator('~');

            if(gameLoop){
               life.evolve();
           }
           try{
               if(br.ready()){
                   switch (br.read()){
                       case ' ': gameLoop=false;break;
                       case 's' | 'S' : gameLoop = true; break;
                       case '.': life.evolve(); break;
                       case 'r': life.randFill(50);break;
                       case 'i': increaseFps(); break;
                       case 'k': decreaseFps(); break;
                       case 'E' | 'e': return;
                   }
               }

               System.out.print(buildScreenOutput());

               sleep(getSleepTime());

           } catch (Exception e){
               e.printStackTrace(System.err);
           }
        }

    }


    public static void main(String[] args) throws Exception {
        GOL test = new GOL();

        test.display();
    }
}
