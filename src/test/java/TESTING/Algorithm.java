package TESTING;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import rubikcube.RubikCube;
import solutioning.strategy.Action;

public class Algorithm {


    public static int calculateCost(RubikCube cube) {
        return 0; // Implement your logic here
    }

    public static void performBFS(RubikCube cube, int limitBFS) {
        Queue<RubikCube> queue = new LinkedList<>();
        Set<RubikCube> visited = new HashSet<>();
        queue.add(cube);
        visited.add(cube);
        int level = 0;

        while (!queue.isEmpty() && level <= limitBFS) {
            int currentLevelNodes = queue.size();
            for (int i = 0; i < currentLevelNodes; i++) {
                RubikCube currentCube = queue.poll();
                int cost = calculateCost(currentCube);

                // Check if the cube is solved (cost is zero)
                if (cost == 0) {
                    System.out.println("Solution found at level: " + level);
                    return;
                }

                // Perform actions on the current cube and add neighboring cubes to the queue
                Action<RubikCube>[] allActions = currentCube.getAllActions();
                for (Action<RubikCube> action : allActions) {
                    try {
                    RubikCube neighbor = currentCube.clone();
                    neighbor.performAction(action);
                    if (!visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle the exception accordingly
                    }
                }
            }
            level++;
        }
        System.out.println("Solution not found within BFS limit.");
    }
}