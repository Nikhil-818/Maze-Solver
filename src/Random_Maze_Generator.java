import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

class Random_Maze_Generator {
    private final int rows;
    private final int cols;
    private final int[][] maze;

    public Random_Maze_Generator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new int[rows][cols];
        Generate_Maze();
    }

    private void Generate_Maze() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 1;
            }
        }

        Stack<int[]> stack = new Stack<>();
        Random random = new Random();

        int startX = random.nextInt(rows);
        int startY = random.nextInt(cols);
        maze[startX][startY] = 0;
        stack.push(new int[]{startX, startY});

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0];
            int y = current[1];

            int[][] directions = {{2, 0}, {-2, 0}, {0, 2}, {0, -2}};
            Collections.shuffle(Arrays.asList(directions), random);

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols) {
                    if (maze[newX][newY] == 1) {
                        maze[newX][newY] = 0;
                        maze[x + dir[0] / 2][y + dir[1] / 2] = 0;
                        stack.push(new int[]{newX, newY});
                    }
                }
            }
        }

        for (int i = 1; i < rows; i += 2) {
            maze[i][0] = 0;
            maze[i][cols - 1] = 0;
        }
    }

    public int[][] Get_Maze() {
        return maze;
    }
}


