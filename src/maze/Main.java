package maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static maze.Maze.*;

public class Main {
    //1 - clear
    //0 - wall
    //2 - path
    //3 - dead end

    private static final Random random = new Random(new Date().getTime());
    private static final Scanner scanner = new Scanner(System.in);

    private static final String MENU =
            "1. Generate a new maze.\n" +
            "2. Load a maze.\n" +
            "3. Save the maze.\n" +
            "4. Display the maze.\n" +
            "5. Find the escape.\n" +
            "0. Exit.";
    private static final String MENU_NO_MAZE =
            "1. Generate a new maze.\n" +
            "2. Load a maze.\n" +
            "0. Exit.";

    public static void main(String[] args) {
        int choice;
        int [][] maze = null;
        while (true) {
            if (maze == null) {
                System.out.println(MENU_NO_MAZE);
            } else {
                System.out.println(MENU);
            }
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 0) {
                break;
            }
            switch (choice) {
                case 1:
                    try {
                        maze = choiceCreate();
                        printMaze(maze);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Maze is too small, try again");
                    }
                    break;
                case 2:
                    maze = loadMaze();
                    break;
                case 3:
                    if (maze != null) {
                        saveMaze(maze);
                    } else {
                        System.out.println("There is no maze");
                    }
                    break;
                case 4:
                    if (maze != null) {
                        printMaze(maze);
                    } else {
                        System.out.println("There is no maze");
                    }
                    break;
                case 5:
                    if (maze != null) {
                        solve(maze);
                    } else {
                        System.out.println("There is no maze");
                    }
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }
        System.out.println("Bye!");
    }

    private static int[][] loadMaze() {
        System.out.println("Enter file name");
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        int[][] maze = null;
        try (Scanner fileScanner = new Scanner(file)){
            int size = Integer.parseInt(fileScanner.nextLine().trim());
            maze = new int[size][size];
            int row = 0;
            while (fileScanner.hasNext()) {
                String[] line = fileScanner.nextLine().trim().split("\\s+");
                for (int i = 0; i < line.length; i++) {
                    maze[row][i] = Integer.parseInt(line[i]);
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            System.out.printf("The file %s does not exist\n", fileName);;
        }
        return maze;
    }
    private static void saveMaze(int[][] maze) {
        System.out.println("Enter file name");
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file)){
            int size = maze.length;
            writer.write(size + "\n");
            for (int[] row : maze) {
                for (int e : row) {
                    writer.write(e + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[][] choiceCreate() {
        System.out.println("Please, enter the size of a maze");
        int mazeHeight = scanner.nextInt();
        scanner.nextLine();
        int mazeWidth = mazeHeight; //scanner.nextInt();
        int graphRows = (mazeHeight - 1) / 2;
        int graphCols = (mazeWidth - 1) / 2;
        int [][] graph = randomWeightGraph(graphRows, graphCols);
        List<IntPair> spanningTree = spanningTreePrim(graph);
        return createMaze(mazeHeight, mazeWidth, spanningTree);
    }

    private static List<IntPair> spanningTreePrim (int[][] adjacencyMatrix) {
        int notes = adjacencyMatrix.length;
        List<IntPair> edgesOfTree = new ArrayList<>();
        Set<Integer> notesInTree = new HashSet<>();
        notesInTree.add(0);
        while (notesInTree.size() < notes) {
            IntPair minEdge = new IntPair(-1, -1);
            int min = 10;
            for (int note : notesInTree) {
                for (int i = 0; i < notes; i++) {
                    if (adjacencyMatrix[note][i] < min && adjacencyMatrix[note][i] > 0) {
                        if (edgesOfTree.contains(new IntPair(note, i)) || notesInTree.contains(i)) {
                            continue;
                        }
                        minEdge = new IntPair(note, i);
                        min = adjacencyMatrix[note][i];
                    }
                }
            }
            edgesOfTree.add(minEdge);
            notesInTree.add(minEdge.getSecond());
        }
        return edgesOfTree;
    }

    private static int[][] randomWeightGraph(int rows, int cols) {
        if (rows < 2 || cols < 2) {
            throw new IllegalArgumentException("Maze is too small");
        }
        int notes = rows * cols;
        int[][] matrix = new int[notes][notes];
        for (int i = 0; i < notes; i++) {
            if (i % cols == 0) { //most left column
                matrix[i][i + 1] = random.nextInt(9) + 1;
                matrix[i + 1][i] = matrix[i][i + 1];
            } else if (i % cols == cols - 1) {// most right column
                matrix[i][i - 1] = random.nextInt(9) + 1;
                matrix[i - 1][i] = matrix[i][i - 1];
            } else {
                matrix[i][i + 1] = random.nextInt(9) + 1;
                matrix[i + 1][i] = matrix[i][i + 1];
                matrix[i][i - 1] = random.nextInt(9) + 1;
                matrix[i - 1][i] = matrix[i][i - 1];
            }
            if (i < cols) { // top row
                matrix[i][i + cols] = random.nextInt(9) + 1;
                matrix[i + cols][i] = matrix[i][i + cols];
            } else if (i >= cols * (rows - 1)) { // bottom row
                matrix[i][i - cols] = random.nextInt(9) + 1;
                matrix[i - cols][i] = matrix[i][i - cols];
            } else {
                matrix[i][i + cols] = random.nextInt(9) + 1;
                matrix[i + cols][i] = matrix[i][i + cols];
                matrix[i][i - cols] = random.nextInt(9) + 1;
                matrix[i - cols][i] = matrix[i][i - cols];
            }
        }
        return matrix;
    }
}

