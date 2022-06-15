import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JPanel;


/**
 * This contains the entirety of the code for the Ai and PVP modes for the
 * Connect 4 game as well as all the helper methods for the game
 *
 * @author Andrew Wang
 * @version May 23, 2020
 */
public class MultiDraw
    extends JPanel
    implements MouseListener
{

    private static final long serialVersionUID = 1L;

    private int               startX           = 20;

    private int               startY           = 20;

    private int               cellWidth        = 80;

    private boolean           redturn          = true;

    private int               rows             = 6;

    private int               cols             = 7;

    private Color[][]         grid             = new Color[rows][cols];


    private Stack<Integer>    redMoves;

    private Stack<Integer>    yellowMoves;

    private Player            red;

    private boolean           redwin           = false;

    private boolean           yellowwin        = false;

    private Player            yellow;

    private boolean           aiMode           = true;

    private Graphics2D        g2;


    /**
     * Create a new MultiDraw object.
     *
     * @param dimension
     *            dimensions of the window
     */
    public MultiDraw(Dimension dimension)
    {
        setSize(dimension);
        setPreferredSize(dimension);
        addMouseListener(this);

        // int x = 0;
        for (int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[0].length; col++)
            {
                grid[row][col] = Color.WHITE;
            }
        }

        red = new Player();
        yellow = new Player();

    }


    /**
     * Creates and draw the strings on the display panel
     *
     * @param g
     *            Graphics component object use to draw
     */
    @Override
    public void paintComponent(Graphics g)
    {
        g2 = (Graphics2D)g;
        Dimension d = getSize();
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 0, d.width, d.height);
        startX = 0;
        startY = 0;

        // 2) draw grid here
        for (int row = 0; row < grid.length; row++)
        {
            for (int col = 0; col < grid[0].length; col++)
            {
                g2.setColor(grid[row][col]);
                g2.fillOval(startX, startY, cellWidth, cellWidth);
                startX += cellWidth;
            }
            startX = 0;
            startY += cellWidth;

        }

        if (redturn)
        {
            g2.drawString("Red's Turn", 800, 40);
        }
        else
        {
            g2.drawString("Yellow's turn", 800, 40);
        }

        g2.setColor(new Color(255, 255, 255));

        if (yellowwin)
        {
            g2.drawString("Yellow Wins!", 800, 80);
            g2.drawString("Reset the board in menu to play again", 800, 120);
        }
        if (redwin)
        {
            g2.drawString("Red Wins!", 800, 80);
            g2.drawString("Reset the board in menu to play again", 800, 120);
        }
        g2.drawString("Score:", 600, 30);
        g2.drawString(red.getScore() + " - " + yellow.getScore(), 645, 30);
    }


    /**
     * Resets the whole game including all the pieces and turns
     */
    public void resetGame()
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                grid[i][j] = Color.WHITE;
            }
        }
        redturn = true;
        redwin = false;
        yellowwin = false;
    }


    /**
     * Undoes the most recent move and resets the board to the state it was at
     * before the most recent move
     */
    public void undoMove()
    {
        int col;
        int r = 0;
        System.out.println("undoing move");
        if (isAiMode())
        {
            int c1 = red.getLastMove();
            int c2 = yellow.getLastMove();

            while (r < rows)
            {
                if (grid[r][c1].equals(Color.RED))
                {
                    grid[r][c1] = Color.WHITE;
                    break;
                }
                r++;
            }
            while (r < rows)
            {
                if (grid[r][c2].equals(Color.YELLOW))
                {
                    grid[r][c2] = Color.WHITE;
                    break;
                }
                r++;
            }
            redturn = true;
        }
        else
        {
            if (!redturn)
            {
                col = red.getMoves().pop();
                while (r < rows)
                {
                    if (grid[r][col].equals(Color.RED))
                    {
                        grid[r][col] = Color.WHITE;
                        break;
                    }
                    r++;
                }
            }

            else
            {
                col = yellow.getMoves().pop();
                while (r < rows)
                {
                    if (grid[r][col].equals(Color.YELLOW))
                    {
                        grid[r][col] = Color.WHITE;
                        break;
                    }
                    r++;
                }
            }
            if (redturn)
            {
                redturn = false;
            }
            else
            {
                redturn = true;
            }
        }

    }


    /**
     * Checks if the specified color has satisfied the four in a row win
     * conditions
     *
     * @param color
     *            color to check win for
     * @return boolean true if that color has satisfied the win conditions
     */
    public boolean checkWin(Color color)
    {
        if (winHorizontal(color) || winVertical(color) || winDiagonal(color))
        {
            return true;
        }
        return false;
    }


    // helper method for checkWin
    private boolean winHorizontal(Color color)
    {
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                if (grid[r][c].equals(color) && c + 3 < cols)
                {
                    if (checkNextThreeHori(r, c, color))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    // helper method for winHorizontal
    private boolean checkNextThreeHori(int row, int col, Color color)
    {
        int c = col + 1;
        int count = 0;
        while (c <= col + 3)
        {
            if (grid[row][c].equals(color))
            {
                count++;
            }
            else
            {
                count = 0;
            }
            c++;
        }
        return count == 3;
    }


    // helper method for checkWin
    private boolean winVertical(Color color)
    {
        for (int c = 0; c < cols; c++)
        {
            for (int r = 0; r < rows; r++)
            {
                if (grid[r][c].equals(color) && r + 3 < rows)
                {
                    if (checkNextThreeVert(r, c, color))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    // helper method for winVerical
    private boolean checkNextThreeVert(int row, int col, Color color)
    {
        int r = row + 1;
        int count = 0;
        while (r <= row + 3)
        {
            if (grid[r][col].equals(color))
            {
                count++;
            }
            r++;
        }
        return count == 3;
    }


    private boolean winDiagonal(Color color)
    {
        // check '\' diagonal
        for (int r = 0; r <= 2; r++)
        {
            for (int c = 0; c <= 3; c++)
            {
                if (grid[r][c].equals(color) && checkNextThreeDia1(r, c, color))
                {
                    return true;
                }
            }
        }

        for (int r = 0; r <= 2; r++)
        {
            for (int c = 3; c <= 6; c++)
            {
                if (grid[r][c].equals(color) && checkNextThreeDia2(r, c, color))
                {
                    return true;
                }
            }
        }

        return false;
    }


    private boolean checkNextThreeDia1(int row, int col, Color color)
    {
        int count = 0;
        int r = row;
        int c = col;
        while (r < rows && c < cols)
        {
            if (grid[r][c].equals(color))
            {
                count++;
            }
            else
            {
                count = 0;
            }
            r++;
            c++;
        }
        return count == 4;
    }


    private boolean checkNextThreeDia2(int row, int col, Color color)
    {
        int count = 0;
        int r = row;
        int c = col;
        while (r < rows && c >= 0)
        {
            if (grid[r][c].equals(color))
            {
                count++;
            }
            else
            {
                count = 0;
            }
            r++;
            c--;
        }
        return count == 4;
    }


    /**
     * Finds all the valid columns where a piece could be placed and returns
     * them in an ArrayList
     *
     * @return ArrayList<Integer> ArrayList of all the columns where a piece
     *         could go
     */
    public ArrayList<Integer> validLocations()
    {
        ArrayList<Integer> valid = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++)
        {

            if (grid[0][i].equals(Color.WHITE))
            {
                // System.out.println("Valid: " + i);
                valid.add(i);
            }

        }
        return valid;

    }


    /**
     * PRECONDITION: Column has white space Places a piece of a desired color in
     * a desired row
     *
     * @param c
     *            Column that the piece is to be placed in
     * @param color
     *            Color of the piece
     */
    public void makeMove(int c, Color color)
    {
        int var = rows - 1;
        while (!grid[var][c].equals(Color.WHITE))
        {
            var--;
        }
        grid[var][c] = color;
    }


    /**
     * Checks if a color has a move that would allow it to satisfy the win
     * conditions
     *
     * @param c
     *            Color to check for winning move
     * @return int[] array with the coordinates of the winning move
     */
    public int[] hasWinningMove(Color c)
    {
        int[] arr = new int[2];
        arr[0] = -1;
        arr[1] = -1;
        ArrayList<Integer> validMoves = validLocations();
        for (int i = 0; i < validMoves.size(); i++)
        {
            int var = rows - 1;
            while (!grid[var][validMoves.get(i)].equals(Color.WHITE))
            {
                var--;
            }

            grid[var][validMoves.get(i)] = c;
            if (checkWin(c))
            {
                arr[0] = var;
                arr[1] = validMoves.get(i);
                grid[var][validMoves.get(i)] = Color.WHITE;
                return arr;
            }

            grid[var][validMoves.get(i)] = Color.WHITE;

        }
        return arr;
    }


    /**
     * Resets scores of both colors on the scoreboard
     */
    public void resetScore()
    {
        red.resetScore();
        yellow.getScore();
    }


    /**
     * action for when ever the mouse is pressed and fills in the the colors for
     * the board depending on red's or yellow's turn
     *
     * @param e
     *            MouseEvent for when a mouse is pressed
     */
    public void mousePressed(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();

        int col = x / cellWidth;
        int row = y / cellWidth;

        System.out.println(x + " " + col + " " + y + " " + row);

        if (x > 560 || y > 476)
        {
            System.out.println("INVALID MOVE");
            return;
        }

        int var = rows - 1;
        if (checkWin(Color.RED) || checkWin(Color.YELLOW))
        {
            System.out.println("Please Reset Board");
        }
        else
        {
            if (!isAiMode())
            {
                if (redturn)
                {
                    red.updateMoves(col);
                    System.out.println(red.getMoves().peek());
                    while (!grid[var][col].equals(Color.WHITE))
                    {
                        var--;
                    }
                    grid[var][col] = Color.RED;
                    if (checkWin(Color.RED))
                    {
                        System.out.println("Red Wins");
                        red.updateScore();
                        redwin = true;
                    }
                    redturn = false;
                }
                else
                {
                    yellow.updateMoves(col);
                    System.out.println(yellow.getMoves().peek());
                    while (!grid[var][col].equals(Color.WHITE))
                    {
                        var--;
                    }
                    grid[var][col] = Color.YELLOW;
                    if (checkWin(Color.YELLOW))
                    {

                        System.out.println("Yellow Wins");
                        yellowwin = true;
                        yellow.updateScore();
                    }
                    redturn = true;

                }

                repaint();
            }
            else
            {
                if (redturn)
                {
                    red.updateMoves(col);
                    System.out.println(red.getMoves().peek());
                    while (!grid[var][col].equals(Color.WHITE))
                    {
                        var--;
                    }
                    grid[var][col] = Color.RED;
                    if (checkWin(Color.RED))
                    {
                        System.out.println("Red Wins");
                        redwin = true;
                        red.updateScore();
                    }

                    redturn = false;

                    int[] winners = hasWinningMove(Color.RED);
                    int[] winners2 = hasWinningMove(Color.YELLOW);
                    System.out.println(winners[0] + " " + winners2[0]);
                    if (winners[0] != -1)
                    {
                        grid[winners[0]][winners[1]] = Color.YELLOW;
                        yellow.updateMoves(winners[1]);
                    }
                    else if (winners2[0] != -1)
                    {
                        grid[winners2[0]][winners2[1]] = Color.YELLOW;
                        yellow.updateMoves(winners[1]);
                    }
                    else
                    {
                        int c = rows - 1;
                        int r = (int)(Math.random() * (6));
                        while (!grid[c][r].equals(Color.WHITE))
                        {
                            c--;
                        }
                        // if (hasWinningMove(Color.YELLOW))

                        grid[c][r] = Color.YELLOW;
                        yellow.updateMoves(r);
                    }

                    redturn = true;
                    if (checkWin(Color.YELLOW))
                    {
                        System.out.println("Yellow Wins");
                        yellowwin = true;
                        yellow.updateScore();
                    }
                    repaint();
                }
            }
        }

    }


    /**
     * Invoked when a mouse button has been released on a component
     *
     * @param e
     *            MouseEvent for when user's mouse is released
     */
    public void mouseReleased(MouseEvent e)
    {
        // intentionally empty
    }


    /**
     * Invoked when the mouse enters a component
     *
     * @param e
     *            MouseEvent for when user's mouse entered
     */
    public void mouseEntered(MouseEvent e)
    {
        // intentionally empty
    }


    /**
     * Invoked when the mouse exits a component
     *
     * @param e
     *            MouseEvent for when user's mouse exited
     */
    public void mouseExited(MouseEvent e)
    {
        // intentionally empty
    }


    /**
     * Invoked when the mouse button has been clicked (pressed and released) on
     * a component
     *
     * @param e
     *            MouseEvent for when user's mouse clicked
     */
    public void mouseClicked(MouseEvent e)
    {
        // intentionally empty
    }


    /**
     * Get the current value of aiMode.
     *
     * @return The value of aiMode for this object.
     */
    public boolean isAiMode()
    {
        return aiMode;
    }


    /**
     * Set the value of aiMode for this object.
     *
     * @param aiMode
     *            The new value for aiMode.
     */
    public void setAiMode(boolean aiMode)
    {
        this.aiMode = aiMode;
    }


    /**
     * Get the current value of yellowMoves.
     *
     * @return The value of yellowMoves for this object.
     */
    public Stack<Integer> getYellowMoves()
    {
        return yellowMoves;
    }


    /**
     * Set the value of yellowMoves for this object.
     *
     * @param yellowMoves
     *            The new value for yellowMoves.
     */
    public void setYellowMoves(Stack<Integer> yellowMoves)
    {
        this.yellowMoves = yellowMoves;
    }


    /**
     * Get the current value of redMoves.
     *
     * @return The value of redMoves for this object.
     */
    public Stack<Integer> getRedMoves()
    {
        return redMoves;
    }


    /**
     * Set the value of redMoves for this object.
     *
     * @param redMoves
     *            The new value for redMoves.
     */
    public void setRedMoves(Stack<Integer> redMoves)
    {
        this.redMoves = redMoves;
    }
}
