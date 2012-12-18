import java.util.Random;

/**
 * Class that creates a mouse that uses a wall following algorithm to
 * navigate a 2-D grid-based simply-connected maze.
 * 
 * @author Michael Quested
 * @version 1.0
 * @date 2012/12/18
 */ 
public class WallFollower extends Mouse{

	private String bearing;
	
	/**
	 * Constructor for class WallFollower.
	 * 
	 * @param maze The maze that the mouse will be navigating.
	 */ 
	public WallFollower(Maze maze){
		super(maze);
		Random random = new Random();
		String[] directions = {"NORTH", "SOUTH", "EAST", "WEST"};
		bearing = directions[random.nextInt(4)];		
	}
	
	/**
	 * Move the mouse throughout the maze. Using a wall following 
	 * algorithm, the mouse always follows the wall on its right-
	 * hand side.
	 */ 
	public void move(){
		if(bearing.equals("NORTH")){
			if(maze.coordinates[currentPosX+1][currentPosY]){
				bearing = "EAST";
				currentPosX += 1;
			}
			else if(maze.coordinates[currentPosX][currentPosY-1]){
				currentPosY -= 1;
			}
			else if(maze.coordinates[currentPosX-1][currentPosY]){
				bearing = "WEST";
				currentPosX -= 1;
			}
			else{
				bearing = "SOUTH";
				currentPosY += 1;
			}
		}
		else if(bearing.equals("EAST")){
			if(maze.coordinates[currentPosX][currentPosY+1]){
				bearing = "SOUTH";
				currentPosY += 1;
			}
			else if(maze.coordinates[currentPosX+1][currentPosY]){
				currentPosX += 1;
			}
			else if(maze.coordinates[currentPosX][currentPosY-1]){
				bearing = "NORTH";
				currentPosY -= 1;
			}
			else{
				bearing = "WEST";
				currentPosX -= 1;
			}
		}
		else if(bearing.equals("SOUTH")){
			if(maze.coordinates[currentPosX-1][currentPosY]){
				bearing = "WEST";
				currentPosX -= 1;
			}
			else if(maze.coordinates[currentPosX][currentPosY+1]){
				currentPosY += 1;
			}
			else if(maze.coordinates[currentPosX+1][currentPosY]){
				bearing = "EAST";
				currentPosX += 1;
			}
			else{
				bearing = "NORTH";
				currentPosY -= 1;
			}
		}
		else if(bearing.equals("WEST")){
			if(maze.coordinates[currentPosX][currentPosY-1]){
				bearing = "NORTH";
				currentPosY -= 1;
			}
			else if(maze.coordinates[currentPosX-1][currentPosY]){
				currentPosX -= 1;
			}
			else if(maze.coordinates[currentPosX][currentPosY+1]){
				bearing = "SOUTH";
				currentPosY += 1;
			}
			else{
				bearing = "EAST";
				currentPosX += 1;
			}			
		}
	}
}
