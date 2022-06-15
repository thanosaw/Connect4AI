
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.*;

/**
 * Class for the GUI for the Connect 4 Game.
 *
 * @author Andrew Wang
 * @version May 23, 2020
 */
public class Connect4app
    implements ActionListener, MenuListener
{
    private static JFrame     frame;

    private MultiDraw         game;

    private JMenuBar          menuBar;

    private JMenu             menu;

    private JMenuItem         undo;

    private JMenuItem         reset;

    private JMenuItem         exit;

    private JMenuItem         newGame;

    private JCheckBoxMenuItem choice1, choice2;


    /**
     * Create a new Connect4app object.
     */
    public Connect4app()
    {

        frame = new JFrame("DrawGrid");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        game = new MultiDraw(frame.getSize());
        frame.add(game);
        frame.pack();

        frame.setJMenuBar(createMenuBar());

        frame.setVisible(true);

    }


    /**
     * Main method that starts the connect 4 game
     *
     * @param argv
     *            args
     */
    @SuppressWarnings("unused")
    public static void main(String... argv)
    {

        new Connect4app();

    }


    /**
     * Create a menu with new game, undo move, rest board, and quit option
     *
     * @return menuBar JMenuBar
     */
    private JMenuBar createMenuBar()
    {

        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        menu = new JMenu("Menu");
        menuBar.add(menu);

        newGame = new JMenu("New Game");

        menu.add(newGame);

        choice1 = new JCheckBoxMenuItem("Player v. Player");
        choice1.addActionListener(this);
        newGame.add(choice1);

        choice2 = new JCheckBoxMenuItem("Player v. AI");
        choice2.addActionListener(this);
        newGame.add(choice2);

        undo = new JMenuItem("Undo Move");
        menu.add(undo);
        undo.addActionListener(this);
        reset = new JMenuItem("Reset Board");

        menu.add(reset);
        reset.addActionListener(this);
        exit = new JMenuItem("Quit");

        menu.add(exit);
        exit.addActionListener(this);

        return menuBar;

    }


    /**
     * Invoked when a menu is selected
     *
     * @param e
     *            MenuEvent for when menu selected
     */
    @Override
    public void menuSelected(MenuEvent e)
    {
        // Intentionally empty
    }


    /**
     * invoked when menu is Deselected
     *
     * @param e
     *            MenuEvent for when menu Deselected
     */
    @Override
    public void menuDeselected(MenuEvent e)
    {
        // Intentionally empty

    }


    /**
     * action for when menu is canceled
     *
     * @param e
     *            MouseEvent for when menu canceled
     */
    @Override
    public void menuCanceled(MenuEvent e)
    {
        // Intentionally empty

    }


    @Override
    /**
     * response to action listener from the buttons created in the JMenu Bar
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(undo))
        {
            game.undoMove();
            game.repaint();
        }
        if (e.getSource().equals(reset))
        {
            game.resetGame();
            game.repaint();
        }
        if (e.getSource().equals(exit))
        {
            System.exit(0);
        }
        if (e.getSource().equals(choice1))
        {
            game.resetGame();
            game.repaint();
            game.setAiMode(false);
            game.resetScore();
        }
        if (e.getSource().equals(choice2))
        {
            game.resetGame();
            game.repaint();
            game.setAiMode(true);
            game.resetScore();
        }

    }

}
