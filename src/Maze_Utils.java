import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Maze_Utils {
    public static void Replace_Ones(int[][] maze) {
        int pathNumber = 1;

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 1) {
                    maze[i][j] = pathNumber++;
                }
            }
        }
    }

    public static void Print_Maze(int[][] maze) {
        for (int[] row : maze) {
            for (int cell : row) {
                if (cell == 0) {
                    System.out.print(". ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
    }

    public static List<Cell> Get_Neighbors(Cell cell, int rows, int cols, int[][] maze) {
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        List<Cell> neighbors = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int newRow = cell.row + dx[i];
            int newCol = cell.col + dy[i];

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && maze[newRow][newCol] == 0) {
                neighbors.add(new Cell(newRow, newCol));
            }
        }

        return neighbors;
    }

    public static List<Cell> Reconstruct_Path(Map<Cell, Cell> cameFrom, Cell current) {
        List<Cell> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(0, current);
        }

        return path;
    }
}
