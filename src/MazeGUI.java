
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A class that constructs and maintains a graphical user interface for the Maze
 * Mouse program.
 *
 * @author Michael Quested
 * @version 0.5
 * @date 2012/12/17
 */
public class MazeGUI {

    private final static String ABOUT =
            "\nMAZE MOUSE\n\n"
            + "author: Michael Quested\n"
            + "date: 2012/12/18\n"
            + "version: 1.0\n\n"
            + "The project source can be found at:\n"
            + "https://github.com/mdq3/maze-mouse.git\n\n";
    private final static String INSTRUCTIONS =
            "\nOnce maze has been generated, click on a cell\n"
            + "to place the cheese in the maze, then place the\n"
            + "mouse and watch it hunt for the cheese.\n\n"
            + "A new maze can be started at any time by using\n"
            + "the reset button in the menu.\n\n";
    private JFrame frame;
    private Container contentPane;
    private JPanel panel;
    private JLabel[][] cells;
    private Maze maze;

    /**
     * Constructor for objects of class MazeGUI.
     *
     * @param width The grid width of the maze
     * @param depth The grid depth of the maze
     */
    public MazeGUI(Maze maze) {
        this.maze = maze;
        cells = new JLabel[maze.getWidth()][maze.getDepth()];
        makeFrame();
    }

    /**
     * Setup the frame and its contents.
     */
    private void makeFrame() {
        frame = new JFrame("Maze Mouse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(makeMenuBar());
        contentPane = frame.getContentPane();
        makeCellPanel();
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Initialise the panel that contains the cells of the maze.
     */
    private void makeCellPanel() {
        panel = new JPanel(new GridLayout(maze.getDepth(), maze.getWidth()));
        panel.setBackground(Color.BLACK);
        contentPane.add(panel);
        panel.setPreferredSize(new Dimension(maze.getWidth() * 8, maze.getDepth() * 8));
        panel.setMinimumSize(new Dimension(maze.getWidth() * 8, maze.getDepth() * 8));
        panel.setMaximumSize(new Dimension(maze.getWidth() * 8, maze.getDepth() * 8));
    }

    /**
     * Iterate through the coordinates of the maze and render each cell as
     * either a passage or a wall.
     */
    public void renderMaze() {
        for (int y = 0; y < maze.getDepth(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                cells[x][y] = new JLabel();
                if (maze.isPassage(x, y)) {
                    addPassage(x, y);
                } else {
                    addWall(x, y);
                }
            }
        }
        panel.revalidate();
    }

    /**
     * Represent a cell in the grid as a passage.
     * 
     * @param x The grid position x of the passage
     * @param y The grid position y of the passage
     */
    private void addPassage(int x, int y) {
        final JLabel cell = cells[x][y];
        final int col = x;
        final int row = y;
        cell.setBackground(Color.WHITE);
        cell.setOpaque(true);
        cell.addMouseListener(new MouseListener() {
            Color oldColor;

            @Override
            public void mouseEntered(MouseEvent e) {
                oldColor = cell.getBackground();
                if (!maze.cheeseIsSet) {
                    cell.setBackground(Color.YELLOW);
                } else if (!maze.mouseIsSet) {
                    cell.setBackground(Color.BLUE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cell.setBackground(oldColor);
            }

            @Override
            public void mouseClicked(MouseEvent evt) {
                if (!maze.cheeseIsSet) {
                    oldColor = Color.YELLOW;
                    maze.setReset();
                    maze.setCheese(col, row);
                } else if (!maze.mouseIsSet) {
                    removeListeners();
                    oldColor = Color.WHITE;
                    SwingWorker runMouse = new SwingWorker<Void, Void>() {
                        @Override
                        public Void doInBackground() {
                            try {
                                updateMouse(col, row);
                                maze.setMouse(col, row);
                            } catch (InterruptedException e) {
                            }
                            return null;
                        }
                    };
                    runMouse.execute();
                }
            }

            @Override
            public void mousePressed(MouseEvent evt) {
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
            }
        });
        panel.add(cell);
    }

    /**
     * Represent a cell in the grid as a wall.
     * 
     * @param x The grid position x of the wall
     * @param y The grid position y of the wall
     */
    private void addWall(int x, int y) {
        JLabel cell = cells[x][y];
        cell.setBackground(Color.BLACK);
        cell.setOpaque(true);
        cell.setEnabled(false);
        panel.add(cell);
    }

    /**
     * Update the mouse's position on the grid.
     *
     * @param x The grid position x of the mouse
     * @param y The grid position y of the mouse
     */
    public void updateMouse(int x, int y) {
        cells[x][y].setBackground(Color.BLUE);
        cells[x][y].revalidate();
    }

    /**
     * Restore the cell to represent an empty passage.
     *
     * @param x The grid position x of the passage
     * @param y The grid position y of the passage
     */
    public void restoreCell(int x, int y) {
        cells[x][y].setBackground(Color.WHITE);
        cells[x][y].revalidate();
    }

    /**
     * Make the menu bar for the frame.
     */
    private JMenuBar makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(null);

        //---------------------- The Maze Menu -------------------------

        JMenu mazeMenu = new JMenu("Maze");
        mazeMenu.setBorderPainted(false);
        mazeMenu.getPopupMenu().setBorder(null);
        menuBar.add(mazeMenu);

        // Reset item
        final JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.setBorderPainted(false);
        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker resetWorker = new SwingWorker<Void, Void>() {
                    @Override
                    public Void doInBackground() {
                        resetItem.setEnabled(false);
                        maze.reset();
                        frame.repaint();
                        renderMaze();
                        return null;
                    }

                    @Override
                    public void done() {
                        resetItem.setEnabled(true);
                    }
                };
                panel.removeAll();
                resetWorker.execute();
            }
        });
        mazeMenu.add(resetItem);

        // Quit item
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.setBorderPainted(false);
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mazeMenu.add(quitItem);

        //---------------------- The Help Menu -------------------------

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setBorderPainted(false);
        helpMenu.getPopupMenu().setBorder(null);
        menuBar.add(helpMenu);

        // Instructions item
        JMenuItem instructionsItem = new JMenuItem("Instructions");
        instructionsItem.setBorderPainted(false);
        instructionsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMessage(INSTRUCTIONS, "Instructions");
            }
        });
        helpMenu.add(instructionsItem);

        // About item
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setBorderPainted(false);
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMessage(ABOUT, "About");
            }
        });
        helpMenu.add(aboutItem);
        return menuBar;
    }

    /**
     * Create a popup information message.
     */
    private void infoMessage(String info, String title) {
        JOptionPane.showMessageDialog(frame, info, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Remove 
     */
    private void removeListeners(){
        for(int y = 0; y < maze.getDepth(); y++){
            for(int x = 0; x < maze.getWidth(); x ++){
                MouseListener[] listeners = cells[x][y].getMouseListeners();
                for(MouseListener l : listeners){
                    cells[x][y].removeMouseListener(l);
                }
            }
        }
    }
}