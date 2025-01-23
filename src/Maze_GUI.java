import javax.swing.*;
import java.awt.*;

class Maze_GUI extends JPanel {
    private static final int METRICS_START_X = 220;
    private static final int METRICS_START_Y = 40;
    private static final int METRICS_LINE_HEIGHT = 20;
    private static final int CELL_SIZE = 60;
    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color EMPTY_COLOR = Color.WHITE;
    private static final Color PATH_COLOR = Color.GREEN;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private static final int MAZE_BORDER_WIDTH = 2;
    private MazeSolverMetrics currentSolver;
    private int[][] maze;
    private int[][] solution;
    private boolean solved;
    private JButton solveButton;
    private boolean buttonClicked;
    private boolean messageDisplayed = false;
    private int selectedAlgorithm = 1;

    public Maze_GUI(int[][] maze) {
        this.maze = maze;
        this.solution = new int[maze.length][maze[0].length];
        int panelWidth = maze[0].length * CELL_SIZE + MAZE_BORDER_WIDTH * 2;
        int panelHeight = maze.length * CELL_SIZE + MAZE_BORDER_WIDTH * 2;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        Initialize_UI();
    }

    public void solveMaze() {
        solved = false;
        solution = new int[maze.length][maze[0].length];

        switch (selectedAlgorithm) {
            case 1:
                solved = Solve_Maze_AS(maze);
                currentSolver = solved ? new Solve_Maze_A_Star(maze) : null;
                break;
            case 2:
                solved = Solve_Maze_BF(maze);
                currentSolver = solved ? new Solve_Maze_BFS(maze) : null;
                break;
            case 3:
                solved = Solve_Maze_DF(maze);
                currentSolver = solved ? new Solve_Maze_DFS(maze) : null;
                break;
            case 4:
                solved = Solve_Maze_DI(maze);
                currentSolver = solved ? new Solve_Maze_Dijkstra(maze) : null;
                break;
        }

        if (solved) {
            solution = currentSolver.getSolution();
        }
    }

    private void Select_Algorithm(int algorithm) {
        selectedAlgorithm = algorithm;
    }

    public static boolean Solve_Maze_AS(int[][] maze) {
        return Solve_Maze_A_Star.Solve_Maze(maze);
    }

    public static boolean Solve_Maze_BF(int[][] maze) {
        return Solve_Maze_BFS.Solve_Maze(maze);
    }

    public static boolean Solve_Maze_DF(int[][] maze) {
        return Solve_Maze_DFS.Solve_Maze(maze);
    }

    public static boolean Solve_Maze_DI(int[][] maze) {
        return Solve_Maze_Dijkstra.Solve_Maze(maze);
    }

    private void Initialize_UI() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1));
        buttonPanel.setPreferredSize(new Dimension(200, FRAME_HEIGHT));

        JButton algorithm1Button = new JButton("A* Star Algorithm");
        JButton algorithm2Button = new JButton("Breadth First Search");
        JButton algorithm3Button = new JButton("Depth First Search");
        JButton algorithm4Button = new JButton("Dijkstra's Algorithm");

        JButton generateButton = new JButton("Generate New Maze");
        solveButton = new JButton("Solve Maze");

        algorithm1Button.addActionListener(e -> Select_Algorithm(1));
        algorithm2Button.addActionListener(e -> Select_Algorithm(2));
        algorithm3Button.addActionListener(e -> Select_Algorithm(3));
        algorithm4Button.addActionListener(e -> Select_Algorithm(4));

        generateButton.addActionListener(e -> {
            generateNewMaze();
            repaint();
        });

        solveButton.addActionListener(e -> {
            if (!buttonClicked) {
                buttonClicked = true;
                solveMaze();
                repaint();
                solveButton.setEnabled(false);
            }
        });

        buttonPanel.add(generateButton);
        buttonPanel.add(solveButton);
        buttonPanel.add(algorithm1Button);
        buttonPanel.add(algorithm2Button);
        buttonPanel.add(algorithm3Button);
        buttonPanel.add(algorithm4Button);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.WEST);
    }

    private void drawMetrics(Graphics g) {
        if (currentSolver == null) return;

        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        int y = METRICS_START_Y;
        g.drawString("Time taken: " + (currentSolver.getElapsedTime() / 1_000_000) + " ms", METRICS_START_X, y);
        y += METRICS_LINE_HEIGHT;

        g.drawString("Steps taken: " + currentSolver.getSteps(), METRICS_START_X, y);
        y += METRICS_LINE_HEIGHT;

        g.drawString("Average steps: " + String.format("%.2f", currentSolver.getAverageSteps()), METRICS_START_X, y);
        y += METRICS_LINE_HEIGHT;

        g.drawString("Nodes explored: " + currentSolver.getNodesExplored(), METRICS_START_X, y);
        y += METRICS_LINE_HEIGHT;

        g.drawString("Memory used: " + String.format("%.2f MB", currentSolver.getMemoryUsed()), METRICS_START_X, y);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (buttonClicked) {
            drawMetrics(g);
        }

        if (!messageDisplayed && buttonClicked && !solved) {
            drawMessage(g, "Solution not found.");
            messageDisplayed = true;
            return;
        }

        int offsetX = (getWidth() - maze[0].length * CELL_SIZE) / 2;
        int offsetY = (getHeight() - maze.length * CELL_SIZE) / 2;
        drawMaze(g, offsetX, offsetY);

        if (buttonClicked && solved) {
            drawSolution(g, offsetX, offsetY);
        }

        g.setColor(Color.BLACK);
        g.drawRect(offsetX, offsetY, maze[0].length * CELL_SIZE, maze.length * CELL_SIZE);
    }

    private void drawMaze(Graphics g, int offsetX, int offsetY) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                int x = col * CELL_SIZE + MAZE_BORDER_WIDTH + offsetX;
                int y = row * CELL_SIZE + MAZE_BORDER_WIDTH + offsetY;
                g.setColor(maze[row][col] == 0 ? EMPTY_COLOR : WALL_COLOR);
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawSolution(Graphics g, int offsetX, int offsetY) {
        for (int row = 0; row < solution.length; row++) {
            for (int col = 0; col < solution[0].length; col++) {
                if (solution[row][col] > 0) {
                    int x = col * CELL_SIZE + MAZE_BORDER_WIDTH + offsetX;
                    int y = row * CELL_SIZE + MAZE_BORDER_WIDTH + offsetY;
                    g.setColor(PATH_COLOR);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.setColor(TEXT_COLOR);
                    g.drawString(String.valueOf(solution[row][col]), x + 10, y + 20);
                }
            }
        }
    }

    private void drawMessage(Graphics g, String message) {
        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(message)) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2 - 30;
        g.drawString(message, x, y);
    }

    private void generateNewMaze() {
        Random_Maze_Generator RMG = new Random_Maze_Generator(11, 11);
        int[][] newMaze = RMG.Get_Maze();

        if (newMaze != null) {
            maze = newMaze;
            solution = new int[maze.length][maze[0].length];
            solved = false;
            buttonClicked = false;
            messageDisplayed = false;
            solveButton.setEnabled(true);
            repaint();
        } else {
            System.out.println("Error !! , Maze not Generated");
        }
    }

    public static void main(String[] args) {
        Random_Maze_Generator RMG = new Random_Maze_Generator(11, 11);
        int[][] maze = RMG.Get_Maze();
        JFrame frame = new JFrame("Maze Solver");
        Maze_GUI Maze_GUI = new Maze_GUI(maze);
        frame.add(Maze_GUI);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setVisible(true);
    }
}