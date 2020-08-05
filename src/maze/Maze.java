package maze;

import java.util.*;

public class Maze {
    private static final Random random = new Random(new Date().getTime());

    public static void solve(int[][] maze) {
        int[][] mazeClone = Arrays.copyOf(maze, maze.length);
        IntPair entry = null;
        for (int i = 0; i < mazeClone.length; i++) {
            if (mazeClone[0][i] == 1) {
                entry = new IntPair(0 , i);
                mazeClone[0][i] = 2;
                break;
            }
        }
        IntPair exit = null;
        for (int i = 0; i < mazeClone.length; i++) {//bottom wall
            if (mazeClone[mazeClone.length - 1][i] == 1) {
                exit = new IntPair(mazeClone.length - 1 , i);
                break;
            }
        }
//        for (int i = 0; i < mazeClone.length; i++) {// left wall
//            if (mazeClone[i][0] == 1) {
//                exit = new IntPair(i , 0);
//                break;
//            }
//        }
        Deque<IntPair> pathStack = new ArrayDeque<>();
        pathStack.add(entry);
        while (true) {
            IntPair currentPoint = pathStack.peekLast();
            if (currentPoint.equals(exit)) {
                break;
            }
            int currentRow = currentPoint.getFirst();
            int currentCol = currentPoint.getSecond();
            if (currentCol > 0) { // not first column
                if (mazeClone[currentRow][currentCol - 1] == 1) { // right is clear
                    pathStack.add(new IntPair(currentRow, currentCol - 1));
                    mazeClone[currentRow][currentCol - 1] = 2;
                    continue;
                }
            }
            if (currentCol < mazeClone[0].length - 1) { // not last column
                if (mazeClone[currentRow][currentCol + 1] == 1) { // left is clear
                    pathStack.add(new IntPair(currentRow, currentCol + 1));
                    mazeClone[currentRow][currentCol + 1] = 2;
                    continue;
                }
            }
            if (currentRow > 0) { // not first row
                if (mazeClone[currentRow - 1][currentCol] == 1) { // up is clear
                    pathStack.add(new IntPair(currentRow - 1, currentCol));
                    mazeClone[currentRow - 1][currentCol] = 2;
                    continue;
                }
            }
            if (currentRow < mazeClone.length - 1) { // not last row
                if (mazeClone[currentRow + 1][currentCol] == 1) { // up is clear
                    pathStack.add(new IntPair(currentRow + 1, currentCol));
                    mazeClone[currentRow + 1][currentCol] = 2;
                    continue;
                }
            }
            //dead end
            mazeClone[currentRow][currentCol] = 3;
            pathStack.pollLast();
        }
        printMaze(mazeClone);
    }

    public static int[][] createMaze (int height, int width, List<IntPair> spanningTree) {
        int[][] maze = new int[height][width];
        // filling every node with 1
        for (int i = 1; i < height - 1; i += 2) {
            for (int j = 1; j < width - 1; j += 2) {
                maze[i][j] = 1;
            }
        }
        // filling edges
        for (IntPair edge : spanningTree) {
            IntPair edgeCords = edgeToMazeCoords(edge, width);
            maze[edgeCords.getFirst()][edgeCords.getSecond()] = 1;
        }
        // 2 entries
        int maxColumnIndex = (width % 2 == 0 ? width - 3 : width - 2);
        int in = (random.nextInt(maxColumnIndex / 2 + 1) * 2) + 1;
        maze[0][in] = 1;
        int maxRowIndex = height % 2 == 0 ? height - 3 : height - 2;

        int out = (random.nextInt(maxColumnIndex / 2 + 1) * 2) + 1;//bottom wall
        maze[height - 1][out] = 1;
        if (height % 2 == 0) {
            maze[height - 2][out] = 1;
        }
//        int out = (random.nextInt(maxRowIndex / 2 + 1) * 2) + 1;//left wall
//        maze[out][0] = 1;
        return maze;
    }
    public static void printMazeRaw (int[][] maze) {
        for (int[] row : maze) {
            for (int e : row) {
                System.out.print(e);
            }
            System.out.println();
        }
    }
    public static void printMaze(int[][] maze) {
        for (int[] row : maze) {
            for (int e : row) {
                String sign = e == 0 ? "\u2588" : (e == 2 ? "/" : " ");
                System.out.print(sign.repeat(2));
            }
            System.out.println();
        }
    }

    private static IntPair edgeToMazeCoords(IntPair edge, int mazeWidth) {
        IntPair startNode = noteIndexToMazeCoords(edge.getFirst(), mazeWidth);
        IntPair endNode = noteIndexToMazeCoords(edge.getSecond(), mazeWidth);
        int col = (startNode.getFirst() + endNode.getFirst()) / 2;
        int row = (startNode.getSecond() + endNode.getSecond()) / 2;
        return new IntPair(row, col);
    }
    private static IntPair noteIndexToMazeCoords(int index, int mazeWidth) {
        int maxColumnIndex = mazeWidth % 2 == 0 ? mazeWidth - 3 : mazeWidth - 2;
        int column = 1;
        int row = index * 2 + 1;
        while (true) {
            if (row > maxColumnIndex) {
                row -= (maxColumnIndex + 1);
                column += 2;
            } else {
                break;
            }
        }
        return new IntPair(row, column);
    }

}
