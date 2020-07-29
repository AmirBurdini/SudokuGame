import java.util.Hashtable;
import java.util.Random;
import javax.swing.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements KeyListener,Output
{
    static Game parent;

    // Consts import
    int BOARDSIZE = DEF.BOARD_SIZE;
    int CELLSIZE = DEF.CELL_SIZE;

    Cell[][] board;
    Cell[][] solution;
    
    int Currx;
    int Curry;

    boolean check;

    Hashtable keyEvents;

    
    // constructor, empty board
    public Board(Game g, int NumofHoles) {
        
        Currx = 0; Curry = 0; 
        check = false;

        setBackground(Color.black);
        parent = g;

        Hashtable keyEvents = new Hashtable<KeyEvent, Integer>();
        fillKeyCodes();

        BorderLayout map = new BorderLayout();
        this.setLayout(map);

        GridLayout grid = new GridLayout(BOARDSIZE,BOARDSIZE);
        this.setLayout(grid);

        board = new Cell[BOARDSIZE][BOARDSIZE];
        initBoard(board);
        
        solution = new Cell[BOARDSIZE][BOARDSIZE];
        initBoard(solution);

        // creates a solution
        board = newBoard(0, 0, board);

        copyBoard(board, solution);
        
        // poke holes in the solution to create game
        board = createGame(NumofHoles, board);
        
        repaint();
    }


    // main Board and Game creation methods :

    // recursive function to create a random board
    public Cell[][] newBoard(int x, int y, Cell[][] board) {

        // Stop condition
        if (x == BOARDSIZE){
            return board;
        }

        // Dead End
        if (deadEnd(board)){
            return null;
        }

        // create a new Board for recursion
        Cell[][] aux = new Cell[BOARDSIZE][BOARDSIZE];
        initBoard(aux);
        copyBoard(board, aux);

        // also Dead End option
        int num = randomInlay(x, y, board);
        if (num == -1) {
            return null;
        }

        // in lay random number in the new board
        aux[x][y].fill(num);

        // DeadEnd - backtracking
        if (y == BOARDSIZE - 1) {
            aux = newBoard(x + 1, 0, aux);
        } else {
            aux = newBoard(x, y + 1, aux);
        }

        if (aux == null) {
            
            board[x][y].posibilities[num - 1] = false;
            return newBoard(x, y, board);
        } else board = aux;
        
        return board;
    }
    
    // recursive function to find every legal solution
    public int findAllSolutions(int x, int y, Cell[][] board) {

        // Stop condition
        if(boardSolved(board)){
            return 1;
        }
        
        // Dead End
        if(deadEnd(board)){
            return 0;
        }
        
        // modification to allow the function to Solve incomplete boards
        if(board[x][y].isPlaced){

            if(y == BOARDSIZE - 1){
                return findAllSolutions(x + 1, 0, board);
            }
            return findAllSolutions(x, y + 1, board);
        }
        
        // create a new Board for recursion
        Cell[][] aux = new Cell[BOARDSIZE][BOARDSIZE];
        initBoard(aux);
        copyBoard(board, aux);
        
        // number of solutions
        int res = 0; 
        
        // try all possible numbers in board[x][y]
        for(int num = 1 ; num < BOARDSIZE + 1 ; num++){

            if(isLegal(x, y, aux, num)){

                aux[x][y].fill(num);

                if(y == BOARDSIZE - 1){
                    res += findAllSolutions(x + 1, 0, aux);
                }
                else res += findAllSolutions(x, y + 1, aux);
            }    
        }

        return res;
    }
    
    // poke holes inside the solution board
    public Cell[][] createGame(int NumofHoles, Cell[][] board) {
        
        // finished game board
        if(NumofHoles == 0){

            return board;
        }
        
        Cell[][] aux = new Cell[BOARDSIZE][BOARDSIZE];
        initBoard(aux);
        copyBoard(board, aux);

        int x = randomNum();
        int y = randomNum();

        aux[x][y].hole();

        if(findAllSolutions(0, 0, aux) != 1){

            return createGame(NumofHoles, board);
        }
        return createGame(NumofHoles - 1, aux);
    }
    

    // method used to determine Board conditions :

    // determines if there is any valid value possible in board[x][y]
    public boolean impossible(Cell[][] board, int x, int y) {

        for (int i = 1; i < BOARDSIZE + 1; i++) {

            if (isLegal(x, y, board, i)) {

                return false;
            }
        }
        return true;
    }

    // are there no more possibilities for board[x][y]?
    public boolean deadEnd(Cell[][] board) {

        for (int i = 1; i < BOARDSIZE + 1; i++) {

            for (int x = 0; x < BOARDSIZE; x++) {

                for (int y = 0; y < BOARDSIZE; y++) {

                    if (isLegal(x, y, board, i)) {

                        return false;
                    }
                }
            }
        }
        return true;
    }

    // checks if there isnt the same number in row.column or square
    public boolean isLegal(int x, int y, Cell[][] board, int num) {

        for (int i = 0; i < BOARDSIZE; i++) {

            // check rows
            if (board[x][i].num == num) {
                return false;
            } 

            // check coloumns
            if (board[i][y].num == num) {
                return false;
            } 
        }

        if (sameSquare(x, y, board, num)){
            return false;
        }

        return board[x][y].isPossible(num);
    }

    // checks if the board is already solved
    public boolean boardSolved(Cell[][] board) {

        for(int i = 0 ; i < BOARDSIZE ; i++){

            for(int j = 0 ; j < BOARDSIZE ; j++){

                if(board[i][j].num == 0){

                    return false;
                }
            } 
        }

        return true;
    }

    // checks if there 2 identical number in the same sub square
    public boolean sameSquare(int x, int y, Cell[][] board, int num) {

        int k = (int) Math.sqrt(BOARDSIZE); 
        
        for (int i = 0 ; i < BOARDSIZE; i++){

            for (int j = 0 ; j < BOARDSIZE; j++){
            
                if (i / k == x / k && j / k == y / k
                    && ((i != x)||(j != y))){

                        if (board[i][j].num == num
                            && board[i][j].isPlaced){
                                return true;
                            }
                    }
            }
        }
        return false;
    }

    
    // auxilary methods :

    // initiate Board
    public void initBoard(Cell[][] board) {
        
        for(int i = 0 ; i < BOARDSIZE ; i++){

            for(int j = 0 ; j < BOARDSIZE ; j++){

                board[i][j] = new Cell(i, j);
            } 
        }
    }

    // auxilary function
    public void copyBoard(Cell[][] board, Cell[][] aux) {

        for (int i = 0; i < BOARDSIZE; i++) {

            for (int j = 0; j < BOARDSIZE; j++) {

                aux[i][j].num = board[i][j].num;
                aux[i][j].isPlaced = board[i][j].isPlaced;

                for (int k = 0; k < BOARDSIZE; k++) {

                    aux[i][j].posibilities[k] = board[i][j].posibilities[k];
                }
            }
        }
    }

    // prints the board
    public void print(Cell[][] board) {

        System.out.println();
        for (int i = 0; i < BOARDSIZE; i++) {

            for (int j = 0; j < BOARDSIZE; j++) {

                System.out.print("[" + board[i][j].num + "]");
            }

            System.out.println();
        }

    }

    // random number generator
    public int randomNum() {

        Random r = new Random();
        int num = r.nextInt(BOARDSIZE);

        return num;
    }

    // legal random number generator
    public int randomInlay(int x, int y, Cell[][] board) {

        int num = randomNum() + 1;

        if (impossible(board, x, y)) {

            return -1;
        }

        while (!isLegal(x, y, board, num)) {

            num = randomNum() + 1;
        }

        return num;
    }


    // Paint Component methods :

    // draws the entire panel
    public synchronized void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        drawBoard(g);
        drawLines(g);
        drawCurrent(g);
	}
    
    // draws the cells
    public synchronized void drawBoard(Graphics g) {

        for(int i = 0 ; i < BOARDSIZE ; i++){

            for(int j = 0 ; j < BOARDSIZE ; j++){

                drawCell(g,i,j);
            }
        }
    }
    
    // draws a cell
    public void drawCell(Graphics g, int i, int j) {

        g.setColor(Color.darkGray);
        g.fillOval(i * CELLSIZE, j * CELLSIZE , 50, 50);

        if(board[j][i].num != 0){

            g.setColor(Color.WHITE);
            g.drawString(board[j][i].num + "",(i * CELLSIZE) + (CELLSIZE / 5 * 2)
                , (j * CELLSIZE) + (CELLSIZE / 2));
        }

        if((check)&&(!board[j][i].isPlaced)){

            if(board[j][i].num == solution[j][i].num){

                g.setColor(Color.green);
            }
            else if(board[j][i].num != 0) {

                g.setColor(Color.red);
            }
            
            g.drawString(board[j][i].num + "",i * 60 + 24,j * 60 + 30);
        }
    }
    
    // draws the lines between the cells
    public void drawLines(Graphics g) {

        g.setColor(Color.DARK_GRAY);

        for(int i = 0 ; i < Math.sqrt(BOARDSIZE) ; i++){

            g.drawLine(i * 60 * 3 - 5, 0, i * 60 * 3 - 5, 530);
            g.drawLine(0, i * 60 * 3 - 5, 530,i * 60 * 3 - 5);
        }
    }
    
    // draw a square around the current cell
    public void drawCurrent(Graphics g) {

        g.setColor(Color.yellow);
        g.drawRect(Curry * 60 - 5, Currx * 60 - 5, 60, 60);
    }
    

    // KeyListener methods implementation :

    // fills HashTable with the keycode and value to perform
    public void fillKeyCodes() {

        keyEvents = new Hashtable<KeyEvent,Integer>();

        keyEvents.put(KeyEvent.VK_1, 1);
        keyEvents.put(KeyEvent.VK_2, 2);
        keyEvents.put(KeyEvent.VK_3, 3);
        keyEvents.put(KeyEvent.VK_4, 4);
        keyEvents.put(KeyEvent.VK_5, 5);
        keyEvents.put(KeyEvent.VK_6, 6);
        keyEvents.put(KeyEvent.VK_7, 7);
        keyEvents.put(KeyEvent.VK_8, 8);
        keyEvents.put(KeyEvent.VK_9, 9);
        keyEvents.put(KeyEvent.VK_C, 0);
        keyEvents.put(KeyEvent.VK_UP, 10);
        keyEvents.put(KeyEvent.VK_DOWN, 11);
        keyEvents.put(KeyEvent.VK_LEFT, 12);
        keyEvents.put(KeyEvent.VK_RIGHT, 13);
    }

    // key listener
    public void keyPressed(KeyEvent e) {

        int i = e.getKeyCode();

        if (keyEvents.containsKey(i)) {

            if (!board[Currx][Curry].isPlaced && (int) keyEvents.get(i) < 10) {
                board[Currx][Curry].num = (int) keyEvents.get(i);
            } 
            
            if ((int) keyEvents.get(i) > 9) {

                switch ((int) keyEvents.get(i)) 
                {
                
                    // UP
                    case (10) : {
                        if (Currx == 0) {

                            Currx = 8;
                        } else Currx--;  
                        break;
                    }

                    // DOWN
                    case (11) : {
                        if (Currx == 8) {

                            Currx = 0;
                        } else Currx++;
                        break;
                    }

                    // LEFT
                    case (12) : {

                        if(Curry == 0) {

                            Curry = 8;
                        } else Curry--;
                        break;
                    }
                
                    // RIGHT
                    case (13) : {
                        if(Curry == 8) {

                            Curry = 0;
                        } else Curry++;
                        break;
                    }
                }

            }
            
        }
        repaint();
    }
    
    // not used
    public void keyReleased(KeyEvent arg0) {

    }

    // not used
    public void keyTyped(KeyEvent arg0) {

    }

}