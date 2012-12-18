import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
//import javax.swing.event.*;

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
    private JButton[][] cells;
    private Maze maze;
    
    /**
     * Constructor for objects of class MazeGUI. 
     * 
     * @param width The grid width of the maze
     * @param depth The grid depth of the maze
     */
    public MazeGUI(Maze maze) 
    {
		this.maze = maze;
		cells = new JButton[maze.getWidth()][maze.getDepth()];
		makeFrame();
        renderMaze();
		frame.pack();
		frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Setup the frame and its contents.
     */ 
    public void makeFrame(){
        frame = new JFrame("Maze Mouse");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        contentPane = frame.getContentPane();
        panel = new JPanel(new GridLayout(maze.getDepth(), maze.getWidth()));
        contentPane.add(panel);
        frame.setMinimumSize(new Dimension(maze.getWidth()*12, maze.getDepth()*12));
        frame.setMaximumSize(new Dimension(maze.getWidth()*12, maze.getDepth()*12));
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
						Color oldColor;
						public void mouseEntered(MouseEvent e){
							oldColor = cell.getBackground();
							if(!maze.cheeseIsSet){
								cell.setBackground(Color.YELLOW);
							}
							else if(!maze.mouseIsSet){
								cell.setBackground(Color.BLUE);
							}
						}
						public void mouseExited(MouseEvent e){
								cell.setBackground(oldColor);
						}
						public void mouseClicked(MouseEvent evt){
							if(!maze.cheeseIsSet){
								oldColor = Color.YELLOW;
								maze.setCheese(col, row);
							}
							else if(!maze.mouseIsSet){
								oldColor = Color.WHITE;
								Thread worker = new Thread(){
									@Override
									public void run(){
										try{
											maze.setMouse(col, row);
										}catch(InterruptedException e){}
									}
								};
								worker.start();
							}
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
	
	/**
	 * Set the cheese on the grid.
	 * 
	 * @param x The grid position x of the cheese
	 * @param y The grid position y of the cheese
	 */ 
	public void setCheese(int x, int y){
		cells[x][y].setBackground(Color.YELLOW);	
	}
	
	/**
	 * Update the mouse's position on the grid.
	 * 
	 * @param x The grid position x of the mouse
	 * @param y The grid position y of the mouse
	 */ 
	public void updateMouse(int x, int y){
		cells[x][y].setBackground(Color.BLUE);
		cells[x][y].revalidate();
	}
	
	/**
	 * Restore the cell to represent an empty passage.
	 * 
	 * @param x The grid position x of the passage
	 * @param y The grid position y of the passage
	 */ 
	public void restoreCell(int x, int y){
		cells[x][y].setBackground(Color.WHITE);
		cells[x][y].revalidate();
	}
}
