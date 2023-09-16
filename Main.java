import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.Arrays;


class MazeSolver {
    private int[][] maze;
    private int numRows;
    private int numCols;
    private boolean[][] visited;
    private int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public MazeSolver(int[][] maze) {
        this.maze = maze;
        this.numRows = maze.length;
        this.numCols = maze[0].length;
        this.visited = new boolean[numRows][numCols];
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols && maze[row][col] == 0 && !visited[row][col];
    }

    public String bfs(int[] start, int[] end) {
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start[0]][start[1]] = true;
        int[][] parent = new int[numRows][numCols];

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            if (current[0] == end[0] && current[1] == end[1]) {
                return constructPath(parent, start, end);
            }

            for (int[] direction : directions) {
                int newRow = current[0] + direction[0];
                int newCol = current[1] + direction[1];
                if (isValidMove(newRow, newCol)) {
                    queue.add(new int[]{newRow, newCol});
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current[0] * numCols + current[1];
                }
            }
        }

        return null;
    }

    public String dfs(int[] start, int[] end) {
        Stack<int[]> stack = new Stack<>();
        stack.push(start);
        int[][] parent = new int[numRows][numCols];

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            if (current[0] == end[0] && current[1] == end[1]) {
                return constructPath(parent, start, end);
            }

            for (int[] direction : directions) {
                int newRow = current[0] + direction[0];
                int newCol = current[1] + direction[1];
                if (isValidMove(newRow, newCol)) {
                    stack.push(new int[]{newRow, newCol});
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current[0] * numCols + current[1];
                }
            }
        }

        return null;
    }

    private String constructPath(int[][] parent, int[] start, int[] end) {
        StringBuilder path = new StringBuilder();
        int[] current = end;

        while (!Arrays.equals(current, start)) {
            int row = current[0];
            int col = current[1];
            path.insert(0, "(" + row + ", " + col + ") ");
            int parentIndex = parent[row][col];
            current = new int[]{parentIndex / numCols, parentIndex % numCols};
        }

        path.insert(0, "(" + start[0] + ", " + start[1] + ") ");
        return path.toString();
    }
}

public class Main
{
	public static void main(String[] args) {
	    if (args.length != 1) {
            System.out.println("Usage: java MazeSolver <maze_file>");
            return;
        }
		 try {
            BufferedReader br = new BufferedReader(new FileReader(args[0]));

            String line;
            int[][] maze = new int[10][10]; // Assuming a fixed maze size of 10x10

            int row = 0;
            while ((line = br.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    maze[row][col] = Character.getNumericValue(line.charAt(col));
                }
                row++;
            }

            br.close(); // Close the BufferedReader

            MazeSolver solver = new MazeSolver(maze);
            int[] start = {0, 0};
            int[] end = {9, 9};

            long startTime = System.currentTimeMillis();
            String bfsPath = solver.bfs(start, end);
            long endTime = System.currentTimeMillis();
            long bfsTime = endTime - startTime;

            startTime = System.currentTimeMillis();
            String dfsPath = solver.dfs(start, end);
            endTime = System.currentTimeMillis();
            long dfsTime = endTime - startTime;

            int bsteps = bfsPath.split(",").length;
            int dsteps = dfsPath != null ? dfsPath.split(",").length : 0;

            System.out.println("BFS Path: " + (bfsPath != null ? bfsPath : "No solution"));
            System.out.println("BFS Steps: " + (bfsPath != null ? bsteps : "N/A"));
            System.out.println("BFS Time (ms): " + bfsTime);
            System.out.println("DFS Path: " + (dfsPath != null ? dfsPath : "No solution"));
            System.out.println("DFS Steps: " + (dfsPath != null ? dsteps : "N/A"));
            System.out.println("DFS Time (ms): " + dfsTime);

            try {
                FileWriter writer = new FileWriter("maze_solution.txt");
                writer.write("BFS Path: " + (bfsPath != null ? bfsPath : "No solution") + "\n");
                writer.write("BFS Steps: " + (bfsPath != null ? bsteps : "N/A") + "\n");
                writer.write("BFS Time (ms): " + bfsTime + "\n");
                writer.write("DFS Path: " + (dfsPath != null ? dfsPath : "No solution") + "\n");
                writer.write("DFS Steps: " + (dfsPath != null ? dsteps : "N/A") + "\n");
                writer.write("DFS Time (ms): " + dfsTime);
                writer.close();
                System.out.println("Solution written to maze_solution.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Error reading the maze file: " + e.getMessage());
        }
    }
}
