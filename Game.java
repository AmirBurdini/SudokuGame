import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JFrame
{
    Board gameBoard;
    int difficulty;
    AutoSolver t;

    public Game(int difficulty)
    {
        JButton Check = new JButton("Check");
        JButton AutoSolve = new JButton("Auto Solve");
        JPanel options = new JPanel();

        Check.setSize(100,100);
        Check.setFocusable(false);
        Check.addActionListener(new CheckListener());

        AutoSolve.setSize(100, 100);
        AutoSolve.setFocusable(false);
        AutoSolve.addActionListener(new AutoListener());

        GridLayout g = new GridLayout(1,2);
        options.setLayout(g);
        options.add(Check);
        options.add(AutoSolve);

        int NumofHoles = 0;
        switch(difficulty)
        {
            case(1) : NumofHoles = 45;
            case(2) : NumofHoles = 50;
            case(3) : NumofHoles = 55;
            case(4) : NumofHoles = 60;
        }
        gameBoard = new Board(this,NumofHoles);
        t = new AutoSolver(gameBoard,this);

        this.add(gameBoard,BorderLayout.CENTER);
        this.add(options,BorderLayout.SOUTH);

        addKeyListener(gameBoard);
        setFocusable(true);
        setVisible(true);
        setSize(550, 600);
        setLocation(410, 105);

    }

    
    private class CheckListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            if(Board.BoardSolved(gameBoard.board))
            {
                setBackground(Color.green);
            }
            else gameBoard.check = !gameBoard.check;
            repaint();
        }
    
    }  
    //listener to check all wrong/right fillings

    private class AutoListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            t.start();
        }
    }
    //listener to run the Auto solver
}