import java.util.*;

class Solve_Maze_DFS implements MazeSolverMetrics {
    private static long steps = 0;
    private static long elapsedTime = 0;
    private static final List<Integer> previousSteps = new ArrayList<>();
    private static int nodesExplored = 0;
    private static final Runtime runtime = Runtime.getRuntime();
   private static int[][] solution;

    @Override
    public long getSteps() {
        return steps;
    }

    public int[][] getSolution() {
        return solution;
    }

    @Override
    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public double getAverageSteps() {
        if (previousSteps.isEmpty()) return 0;
        return previousSteps.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    @Override
    public int getNodesExplored() {
        return nodesExplored;
    }

    @Override
    public double getMemoryUsed() {
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        return memoryUsed / (1024.0 * 1024.0);
    }

    Solve_Maze_DFS(int[][] maze) {
        if (Solve_Maze(maze)) {
            Maze_Utils.Replace_Ones(maze);
            Maze_Utils.Print_Maze(maze);
        } else {
            System.out.println("No solution found using DFS");
        }
    }

    public static boolean Solve_Maze(int[][] maze) {
        steps = 0;
        nodesExplored = 0;
        long startTime = System.nanoTime();

        int rows = maze.length;
        int cols = maze[0].length;
        solution = new int[rows][cols];

        List<Cell> path = Find_Path_DFS(maze, rows, cols);

        elapsedTime = System.nanoTime() - startTime;

        if (path == null) {
            return false;
        }

        int pathNumber = 1;
        for (Cell cell : path) {
            solution[cell.row][cell.col] = pathNumber++;
        }

        steps = pathNumber - 1;
        previousSteps.add((int) steps);
        if (previousSteps.size() > 10) {
            previousSteps.remove(0);
        }

        return true;
    }

    public static List<Cell> Find_Path_DFS(int[][] maze, int rows, int cols) {
        Cell start = new Cell(0, 0);
        Cell end = new Cell(rows - 1, cols - 1);

        Stack<Cell> stack = new Stack<>();
        stack.push(start);

        Map<Cell, Cell> cameFrom = new HashMap<>();
        Set<Cell> visited = new HashSet<>();

        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            nodesExplored++;

            if (current.equals(end)) {
                return Maze_Utils.Reconstruct_Path(cameFrom, current);
            }

            visited.add(current);

            for (Cell neighbor : Maze_Utils.Get_Neighbors(current, rows, cols, maze)) {
                if (!visited.contains(neighbor)) {
                    cameFrom.put(neighbor, current);
                    stack.push(neighbor);
                }
            }
        }

        return null;
    }
}