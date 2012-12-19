
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
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Setup the frame and its contents.
     */
    private void makeFrame() {
        frame = new JFrame("Maze Mouse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(makeMenuBar());
        contentPane = frame.getContentPane();
        panel = new JPanel(new GridLayout(maze.getDepth(), maze.getWidth()));
        panel.setBackground(Color.BLACK);
        contentPane.add(panel);
        panel.setPreferredSize(new Dimension(maze.getWidth() * 8, maze.getDepth() * 8));
        panel.setMinimumSize(new Dimension(maze.getWidth() * 8, maze.getDepth() * 8));
        panel.setMaximumSize(new Dimension(maze.getWidth() * 8, maze.getDepth() * 8));
        frame.setResizable(false);
    }

    public void loadMaze() {
        SwingWorker mazeRenderer = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                renderMaze();
                return null;
            }

            @Override
            public void done() {
                contentPane.revalidate();
            }
        };
        mazeRenderer.execute();
    }

    /**
     * Iterate through the coordinates of the maze and render each cell
     * appropriately.
     */
    private void renderMaze() {
        for (int y = 0; y < maze.getDepth(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                final int col = x;
                final int row = y;
                final JLabel cell = new JLabel();
                cell.setOpaque(true);
                if (maze.isPassage(x, y)) {
                    cell.setBackground(Color.WHITE);
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
                                oldColor = Color.WHITE;
                                SwingWorker runMouse = new SwingWorker<Void, Void>() {
                                    @Override
                                    public Void doInBackground() {
                                        try {
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
                    cells[x][y] = cell;
                } else {
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
    public void setCheese(int x, int y) {
        cells[x][y].setBackground(Color.YELLOW);
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
     * Make the menubar for the frame.
     */
    private JMenuBar makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //---------------------- The Maze Menu -------------------------

        JMenu mazeMenu = new JMenu("Maze");
        menuBar.add(mazeMenu);

        // Reset item
        final JMenuItem resetItem = new JMenuItem("Reset");
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
                        frame.revalidate();
                        return null;
                    }

                    @Override
                    public void done() {
                        resetItem.setEnabled(true);
                    }
                };
                panel.removeAll();
                frame.revalidate();
                resetWorker.execute();
            }
        });
        mazeMenu.add(resetItem);

        // Quit item
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mazeMenu.add(quitItem);

        //---------------------- The Help Menu -------------------------

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        // Instructions item
        JMenuItem instructionsItem = new JMenuItem("Instructions");
        instructionsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMessage("Once maze has been generated, click on a cell\n"
                        + "to place the cheese in the maze.\n\n"
                        + "Then place the mouse and watch it hunt for the\ncheese.",
                        "Instructions");
            }
        });
        helpMenu.add(instructionsItem);

        // About item
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoMessage("MAZE MOUSE\n\n"
                        + "author: Michael Quested\n"
                        + "date: 2012/12/18\n"
                        + "version: 1.0\n\n"
                        + "The project source can be found at:\n"
                        + "https://github.com/mdq3/maze-mouse.git",
                        "About");
            }
        });
        helpMenu.add(aboutItem);
        return menuBar;
    }

    /**
     * Create a popup information message.
     */
    protected void infoMessage(String info, String title) {
        JOptionPane.showMessageDialog(frame, info, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
