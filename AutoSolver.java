import java.util.ArrayList;

public class AutoSolver extends Thread {
    
    static ArrayList<Cell[][]> steps;
    static ArrayList<Integer> currIndex;

    Game parent;
    Board parentBoard;

    boolean isAlive;
    boolean pause;
    

    public AutoSolver(Board b, Game parent) {

        this.parent = parent;
        parentBoard = b;

        findSolution(0, 0, givenBoard(parentBoard.board));
        isAlive = true;
        pause = true;
    }

    // the thread to print all the steps
    public synchronized void run() {

        int i = 0, j = 0;

        while (isAlive) {

            if (!pause) {
                
                if (i < steps.size()) {

                    // display the board
                    parent.gameBoard.board = steps.get(i);
                    
                    // highlights the current cell
                    // saved pairs of X's and Y's in the same Arr
                    parent.gameBoard.Currx = currIndex.get(j);
                    j++;
                    parent.gameBoard.Curry = currIndex.get(j);
                    
                    parent.repaint();
                    i++;
                    j++;

                } else isAlive = false;
                
            } 
               
            try {
                sleep(DEF.SOLVER_VELOCITY);
            } catch (InterruptedException event) {
                event.printStackTrace();
            }
        }
    }

    // modification to the original newBoard function
    public Cell[][] findSolution(int x, int y, Cell[][] board) {

        steps.add(board);
        currIndex.add(x);
        currIndex.add(y);

        // Stop condition
        if (parentBoard.boardSolved(board)) {

            return board;
        }

        // Dead End
        if (parentBoard.deadEnd(board)) {

            return null;
        }

        // modification to allow the function to Solve incomplete boards
        if (board[x][y].isPlaced) {

            if (y == 8) {
                return findSolution(x + 1, 0, board);
            }
            return findSolution(x, y + 1, board);
        }

        // create a new Board for recursion
        Cell[][] aux = new Cell[9][9];
        parentBoard.initBoard(aux);
        parentBoard.initBoard(aux);
        parentBoard.copyBoard(board, aux);
        
        // also Dead End option
        int num = parentBoard.randomInlay(x, y, board);
        
        if (num == -1) {

            return null;
        }

        // in lay random number in the new board
        aux[x][y].fill(num);

        // DeadEnd - backtracking
        if (y == 8) {
            aux = findSolution(x + 1, 0, aux); 
        } else {
            aux = findSolution(x, y + 1, aux);
        }
        if (aux == null) {
            board[x][y].Posibilities[num - 1] = false;
            return findSolution(x, y, board);
        } else board = aux;

        return board;
    }

    // checking cells already filled correctly
    public Cell[][] givenBoard(Cell[][] board) {

        steps = new ArrayList<Cell[][]>();
        currIndex = new ArrayList<Integer>();

        for (int i = 0 ; i < DEF.BOARD_SIZE; i++) {
            
            for (int j = 0 ; j < DEF.BOARD_SIZE; j++) {

                if (board[i][j].num == parentBoard.solution[i][j].num) {

                    board[i][j].isPlaced = true;
                }
            }
        }

        return board;
    }

}
