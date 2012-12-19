
/**
 * Class that is responsible for the creation and behaviour of a mouse.
 *
 * @author Michael Quested
 * @version 1.0
 * @date 2012/12/18
 */
public abstract class Mouse {

    protected int currentPosX, currentPosY;
    protected boolean foundCheese;
    protected Maze maze;

    /**
     * Constructor for class Mouse.
     *
     * @param maze The maze that the mouse will be navigating.
     */
    public Mouse(Maze maze) {
        this.maze = maze;
    }

    /**
     * Move the mouse throughout the maze.
     */
    public abstract void move();

    /**
     * Begin the mouse's search for the cheese.
     */
    public void go() throws InterruptedException {
        while (!foundCheese && !maze.isReset()) {
            maze.restoreCell(currentPosX, currentPosY);
            move();
            maze.updateMouse(currentPosX, currentPosY);
            if (maze.cheeseIsFound()) {
                foundCheese = true;
            }
        }
    }
}
