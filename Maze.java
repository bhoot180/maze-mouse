/**
 * A class for generating and maintaining the coordinates for a 2-D 
 * grid-based maze.
 * 
 * @author Michael Quested
 * @version 1.0
 * @date 2012/12/16
 */ 
public class Maze {
	
	private boolean[][] coordinates;
	private boolean[][] visited;
	private int width;
	private int depth;
	
	/**
	 * Constructor for class Maze.
	 * 
	 * @param width The grid width of the maze
	 * @param depth The grid depth of the maze
	 */ 
	public Maze (int width, int depth){
	    if(width % 2 == 0){
			this.width = width + 1;
		}else{
			this.width = width;
		}
		if(depth % 2 == 0){
			this.depth = depth + 1;
		}else{
			this.depth = depth;
		}
		recursiveBacktrackerMaze();
	}
	
	/**
	 * Generates a maze using a recursive backtracking algorithm.
	 */
	public void recursiveBacktrackerMaze(){
		RecursiveBacktracker maze = new RecursiveBacktracker(width, depth);
		coordinates = maze.getCoordinates();
		visited = maze.getVisited();
	}
	
	/**
	 * Get the grid width of the maze.
	 * 
	 * @return the grid width of the maze
	 */ 
	public int getWidth(){
		return width;
	}
	
	/**
	 * Get the grid depth of the maze.
	 * 
	 * @return the grid depth of the maze
	 */ 
	public int getDepth(){
		return depth;
	}
	
	/**
	 * Check if a specified set of coordinates is a passage or a wall.
	 * 
	 * @return true if a passage, false if a wall
	 */ 
	public boolean isPassage(int x, int y){
		return coordinates[x][y];
	}
	
	/**
	 * Prints out whether given cell has been visited or not.
	 * For testing purposes only.
	 * 
	 * @param x Grid position x of cell
	 * @param y Grid position y of cell
	 */ 
	public void printVisited(int x, int y){
		if(visited[x][y]){
			System.out.println("x:" + x + " y:" + y + " visited");
		}
		else{
			System.out.println("x:" + x + " y:" + y + " not visited");
		}
	}
}
