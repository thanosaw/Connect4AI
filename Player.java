import java.util.Stack;

/**
 * The player class that tracks all of the moves and player info
 *
 * @author Jack Tran, Andrew Wang
 * @version May 21, 2020
 */
public class Player
{

    private Stack<Integer> moves;

    private int            score;


    /**
     * Create a new Player object.
     */
    public Player()
    {
        score = 0;
        moves = new Stack<Integer>();
    }


    /**
     * Updates the score for a Player on the scoreboard
     */
    public void updateScore()
    {
        score++;
    }


    /**
     * Resets the score for a player
     */
    public void resetScore()
    {
        score = 0;
    }


    /**
     * Returns a score for the player
     *
     * @return int score that player has
     */
    public int getScore()
    {
        return score;
    }


    /**
     * Updates the moves and adds the index of the last move to the stack that
     * stores the moves
     *
     * @param col
     *            Index of the previous move
     */
    public void updateMoves(int col)
    {
        moves.add(col);
    }


    /**
     * Gets the stack that contains all the moves
     *
     * @return Stack<Integer> Stack with all the moves in it
     */
    public Stack<Integer> getMoves()
    {
        return moves;
    }


    /**
     * Gets the most recent move
     *
     * @return Integer index of the most recent move
     */
    public Integer getLastMove()
    {
        return moves.pop();
    }
}
