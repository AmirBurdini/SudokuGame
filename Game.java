import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame implements Runnable {

    static int SOLVER_VELOCITY = 100; // solver velocity default

    Board gameBoard; // the board the game is played on
    AutoSolver solver; // the game automatic solver

    int solverSpeed; // how fast the solver is displayed
    boolean buttonPressed;

    public Game(DEF.DIFFICULTY gameLevel) {

        JButton Check = new JButton("Check");
        JButton AutoSolve = new JButton("Auto Solve");
        JPanel options = new JPanel();
        JSlider solverSpeed = new JSlider(DEF.MIN_VELOCITY,DEF.MAX_VELOCITY);

        Check.setFocusable(false);
        Check.addActionListener(new CheckListener());

        AutoSolve.setFocusable(false);
        AutoSolve.addActionListener(new AutoListener());

        solverSpeed.setFocusable(false);
        solverSpeed.addChangeListener(new SpeedListener());

        GridLayout g = new GridLayout(1, 3);
        options.setLayout(g);
        options.add(Check);
        options.add(solverSpeed);
        options.add(AutoSolve);

        buttonPressed = false;
        int NumofHoles = 0;
        switch (gameLevel) {
            
            case EASY:
                NumofHoles = 55;
            case MEDIUM:
                NumofHoles = 57;
            case HARD:
                NumofHoles = 60;
            case INSANE:
                NumofHoles = 64;
        }
        
        gameBoard = new Board(this, NumofHoles);
        solver = new AutoSolver(gameBoard, this);
        run();

        this.add(gameBoard, BorderLayout.CENTER);
        this.add(options, BorderLayout.SOUTH);

        addKeyListener(gameBoard);
        setFocusable(true);
        setVisible(true);
        setSize(DEF.GAME_HEIGHT, DEF.GAME_WIDTH);
        setLocation(410, 105);

    }

    // listener to check all wrong/right fillings
    private class CheckListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (gameBoard.boardSolved(gameBoard.board)) {

                setBackground(Color.green);
            } else
                gameBoard.check = !gameBoard.check;
            repaint();
        }

    }

    // listener to run the Auto solver
    private class AutoListener implements ActionListener {

        public synchronized void actionPerformed(ActionEvent e) {

            solver.pause = !solver.pause;
        }

    }

    // listener to determine solver speed
    private class SpeedListener implements ChangeListener {

        // determines how fast the solver runs
        public void stateChanged(ChangeEvent e) {

            JSlider x = (JSlider) e.getSource();
            SOLVER_VELOCITY = x.getValue();
        }
    }

    // auxilary method, enables the run and pause of the solver
    public synchronized void run() {
        
        solver.start();
    }
}
