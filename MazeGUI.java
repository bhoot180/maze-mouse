import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

/**
 * A class that constructs and maintains a graphical user interface
 * for the Maze Mouse program.
 * 
 * @author Michael Quested
 * @version 0.5
 * @date 2012/12/16
 */
public class MazeGUI{
	
    private JFrame frame;
    private Container contentPane;
    private JPanel panel;
    private Maze maze;
    private JButton[][] cells;
    
    /**
     * Constructor for objects of class MazeGUI. 
     * 
     * @param width The grid width of the maze
     * @param depth The grid depth of the maze
     */
    public MazeGUI(int width, int depth) 
    {
		maze = new Maze(width, depth);
		cells = new JButton[maze.getWidth()][maze.getDepth()];
		
        frame = new JFrame("Maze Mouse");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        contentPane = frame.getContentPane();
        panel = new JPanel(new GridLayout(maze.getDepth(), maze.getWidth()));
        contentPane.add(panel);
        frame.setMinimumSize(new Dimension(maze.getWidth()*12, maze.getDepth()*12));
        frame.setMaximumSize(new Dimension(maze.getWidth()*12, maze.getDepth()*12));
          
        renderMaze();
        
		frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Contructor for MazeGUI. This is a text-based version of the maze
     * to run in the terminal.
     * 
     * @param width The grid width of the maze
     * @param depth The grid depth of the maze
     * @param passage String that is used to represent a passage
     * @param wall String that is used to represent a wall
     */ 
    public MazeGUI(int width, int depth, String passage, String wall)
    {
		maze = new Maze(width, depth);
		width = maze.getWidth();
		depth = maze.getDepth();
		for(int y = 0; y < depth; y++){
			for(int x = 0; x < width; x++){
				if(x == 0){
					System.out.print("\n");
				}
				if(maze.isPassage(x, y)){
					System.out.print(passage);
				}
				else{
					System.out.print(wall);
				}
			}
		}
	}
	
	/**
	 * Iterate through the coordinates of the maze and render each cell
	 * appropriately.
	 */ 
    public void renderMaze()
    {
		for(int y = 0; y < maze.getDepth(); y++){
			for(int x = 0; x < maze.getWidth(); x++){
				final int col = x;
				final int row = y;
				final JButton cell = new JButton();
				cell.setPreferredSize(new Dimension(8, 8));
				cell.setBorderPainted(false);
				if(maze.isPassage(x, y)){
					cell.setBackground(Color.WHITE);
					cell.addMouseListener(new MouseListener(){
						public void mouseEntered(MouseEvent e){
							cell.setBackground(Color.BLUE);}
						public void mouseExited(MouseEvent e){
							cell.setBackground(Color.WHITE);}
						public void mouseClicked(MouseEvent evt){
							maze.printVisited(col, row);
						}  
					    public void mousePressed(MouseEvent evt){}  
					    public void mouseReleased(MouseEvent evt){}
					 });
					 cells[x][y] = cell;
				}else{
					cell.setBackground(Color.BLACK);
					cell.setEnabled(false);
					cells[x][y] = cell;
				}
				panel.add(cell);
			}
		}
	}
}
