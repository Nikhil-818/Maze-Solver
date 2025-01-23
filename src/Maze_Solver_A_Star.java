import java.util.*;

class Solve_Maze_A_Star implements MazeSolverMetrics {
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

    Solve_Maze_A_Star(int[][] maze) {
        if (Solve_Maze(maze)) {
            Maze_Utils.Replace_Ones(maze);
            Maze_Utils.Print_Maze(maze);
        } else {
            System.out.println("No solution found using A Star");
        }
    }

    public static boolean Solve_Maze(int[][] maze) {
        steps = 0;
        nodesExplored = 0;
        long startTime = System.nanoTime();

        int rows = maze.length;
        int cols = maze[0].length;
        solution = new int[rows][cols];

        List<Cell> path = Find_Path(maze, rows, cols);

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

    public static List<Cell> Find_Path(int[][] maze, int rows, int cols) {
        Cell start = new Cell(0, 0);
        Cell end = new Cell(rows - 1, cols - 1);

        Set<Cell> visited = new HashSet<>();
        PriorityQueue<Cell> openSet = new PriorityQueue<>(Comparator.comparingInt(cell -> cell.f));

        Map<Cell, Cell> cameFrom = new HashMap<>();
        Map<Cell, Integer> gScore = new HashMap<>();
        Map<Cell, Integer> fScore = new HashMap<>();

        gScore.put(start, 0);
        fScore.put(start, Heuristic(start, end));
        start.f = fScore.get(start);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Cell current = openSet.poll();
            nodesExplored++;

            if (current.equals(end)) {
                return Maze_Utils.Reconstruct_Path(cameFrom, current);
            }

            visited.add(current);

            for (Cell neighbor : Maze_Utils.Get_Neighbors(current, rows, cols, maze)) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;

                if (!gScore.containsKey(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    int f = tentativeGScore + Heuristic(neighbor, end);
                    fScore.put(neighbor, f);
                    neighbor.f = f;

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    public static int Heuristic(Cell a, Cell b) {
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }
}