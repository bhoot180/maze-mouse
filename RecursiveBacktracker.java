import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Generates maze coordinates using the recursive backtracker algorithm,
 * and stores them in a 2-D boolean array.
 * 
 * @author Michael Quested
 * @version 1.0
 * @date 2012/12/16
 */
public class RecursiveBacktracker {
	
	private boolean[][] coordinates;
	private boolean[][] visited;
	private int width;
	private int depth;
	
	/**
	 * Constructor for class RecursiveBacktracker.
	 * 
	 * @param width The grid width of the maze
	 * @param depth The grid depth of the maze
	 */ 
	public RecursiveBacktracker (int width, int depth){
		coordinates = new boolean[width][depth];
		visited = new boolean[width][depth];
		this.width = width;
		this.depth = depth;
		setup();
	}
	
	/**
	 * Setup the starting grid, then 'carve' the passages of the maze.
	 */ 
	private void setup(){
		for(int row = 0; row < depth-1; row++){
			row++;
			for(int col = 0; col < depth-1; col++){
				col++;
				coordinates[col][row] = true;
			}
		}
		carvePassagesFrom(1, 1);
	}
	
	/**
	 * Recursive method for carving the 'passages' of the maze.
	 * 
	 * @param x The current x position of the maze
	 * @param y The current y position of the maze
	 */ 
	private void carvePassagesFrom(int x, int y){
		coordinates[x][y] = true;
		visited[x][y] = true;
		//System.out.println(x + " " + y + " " + coordinates[x][y]);
		
		Random random = new Random();
		String directions[] = {"NORTH", "EAST", "SOUTH", "WEST"};
		Collections.shuffle(Arrays.asList(directions), random);
		
		for(String direction : directions){
			int neighbourX = x;
			int neighbourY = y;
			int wallX = 0;
			int wallY = 0;
			if(direction.equals("NORTH")){
				if(y > 2){
					neighbourX = x;
					neighbourY = y-2;
					wallX = x;
					wallY = y-1;
				}
			}else if(direction.equals("EAST")){
				if(x < width-2){
					neighbourX = x+2;
					neighbourY = y;
					wallX = x+1;
					wallY = y;
				}
			}else if(direction.equals("SOUTH")){
				if(y < depth-2){
					neighbourX = x;
					neighbourY = y+2;
					wallX = x;
					wallY = y+1;
				}	
			}else if(direction.equals("WEST")){
				if(x > 2){
					neighbourX = x-2;
					neighbourY = y;
					wallX = x-1;
					wallY = y;
				}
	        }	
	        if(!visited[neighbourX][neighbourY]){
				coordinates[wallX][wallY] = true;
				visited[wallX][wallY] = true;
				carvePassagesFrom(neighbourX, neighbourY);
	        }
		}
	}	
	
	/**
	 * Returns the array of coordinates for the entire maze.
	 * 
	 * @return the 2-D boolean array of coordinates
	 */ 
	public boolean[][] getCoordinates(){
		return coordinates;
	}
	
	/**
	 * Returns the array of visited cell coordinates.
	 * For testing purposes only.
	 * 
	 * @return the 2-D boolean array of visited cell coordinates.
	 */ 
	public boolean[][] getVisited(){
		return visited;
	}
}
