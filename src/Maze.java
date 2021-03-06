
/**
 * A class for generating and maintaining the coordinates for a 2-D grid-based
 * maze.
 *
 * @author Michael Quested
 * @version 1.0
 * @date 2012/12/16
 */
public abstract class Maze {

    protected boolean[][] coordinates, visited;
    protected int width, depth, cheesePosX, cheesePosY;
    protected Mouse mouse;
    protected boolean mouseIsSet, cheeseIsSet, reset;
    protected MazeGUI gui;

    /**
     * Constructor for class Maze.
     *
     * @param width The grid width of the maze
     * @param depth The grid depth of the maze
     */
    public Maze(int width, int depth) {
        if (width % 2 == 0) {
            this.width = width + 1;
        } else {
            this.width = width;
        }
        if (depth % 2 == 0) {
            this.depth = depth + 1;
        } else {
            this.depth = depth;
        }
        coordinates = new boolean[this.width][this.depth];
        visited = new boolean[this.width][this.depth];
        setup();
        gui = new MazeGUI(this);
        gui.renderMaze();
    }

    /**
     * Setup the maze.
     */
    public abstract void setup();

    /**
     * Reset the maze.
     */
    public abstract void reset();

    /**
     * @return the state of reset
     */
    public boolean isReset() {
        return reset;
    }

    /**
     * Set the reset value to false.
     */
    public void setReset() {
        reset = false;
    }

    /**
     * Place the cheese into the maze at the specified coordinates.
     *
     * @param x The grid position x of the cheese
     * @param y The grid position y of the cheese
     */
    public void setCheese(int x, int y) {
        cheeseIsSet = true;
        cheesePosX = x;
        cheesePosY = y;
        //gui.setCheese(x, y);
    }

    /**
     * Put the mouse down into the maze at the specified coordinates.
     *
     * @param x The grid position x of the mouse
     * @param y The grid position y of the mouse
     */
    public void setMouse(int x, int y) throws InterruptedException {
        mouseIsSet = true;
        mouse = new WallFollower(this);
        mouse.currentPosX = x;
        mouse.currentPosY = y;
        if (!cheeseIsFound()) {
            mouse.go();
        }
    }

    /**
     * Check if the cheese has been found.
     *
     * @return true if the cheese has been found, false if not
     */
    public boolean cheeseIsFound() {
        return cheesePosX == mouse.currentPosX && cheesePosY == mouse.currentPosY;
    }

    /**
     * Update the graphical position of the mouse within the maze grid.
     *
     * @param x The new frid position x of the mouse
     * @param y The new grid position y of the mouse
     */
    public void updateMouse(int x, int y) throws InterruptedException {
        gui.updateMouse(x, y);
        Thread.sleep(50);
    }

    /**
     * Restore the cell of the maze to represent an empty passage.
     *
     * @param x The grid position x to restore
     * @param y The grid position y to restore
     */
    public void restoreCell(int x, int y) {
        gui.restoreCell(x, y);
    }

    /**
     * Get the grid width of the maze.
     *
     * @return the grid width of the maze
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the grid depth of the maze.
     *
     * @return the grid depth of the maze
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Check if a specified set of coordinates is a passage or a wall.
     *
     * @param x The grid position x of the passage to check
     * @param y The grid position y of the passage to check
     * @return true if a passage, false if a wall
     */
    public boolean isPassage(int x, int y) {
        return coordinates[x][y];
    }

    /**
     * Main method. Initialises program.
     */
    public static void main(String[] args) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); // Prevent bug in jdk throwing weird exception.
        new RecursiveBacktracker(80, 60);
    }
}