package ShortestPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import rubikcube.RubikCube.FACE;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultEdge;
import solutioning.strategy.Action;
import rubikcube.RubikCube;
import rubikcube.RubikSide;

import org.jgrapht.graph.DefaultDirectedGraph;


public class RubiksSolve {
    public static void main(String[] args) {
        int cubeSize = 3; // Define the size of the Rubik's Cube (e.g., 3x3x3)
        RubikCube startCube = new RubikCube(cubeSize);
        try {/* 
            rubix.turnRowToRight(0);
            rubix.turnRowToRight(1);
            rubix.turnColDown(0);
            rubix.turnRowToRight(2);
            rubix.turnRowToLeft(1);
            rubix.turnColDown(2);
            rubix.turnRowToRight(2);
            rubix.turnColUp(1);
            rubix.turnColDown(0);
            rubix.turnRowToRight(2);
            */
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception accordingly
        }
        // Create a directed graph representing Rubik's Cube moves
        long timeA = System.currentTimeMillis() /1000;
        Graph<RubikCube, DefaultEdge> rubiksGraph = createRubiksGraph(startCube,5);
        
        // Perform BFS to find the best rotation
                /*
                System.out.println("Vertices:");
                rubiksGraph.vertexSet().forEach(cube -> cube.print());
                */

        RubikCube goalCube = new RubikCube(cubeSize);/* Set your goal state here */
        List<DefaultEdge> shortestPath = findShortestPath(rubiksGraph, startCube, goalCube);
        shortestPath.toString();

        long timeB = System.currentTimeMillis()/1000;
        long KMPTime = timeB - timeA;
        System.out.println(KMPTime);
    }

    public static Graph<RubikCube, DefaultEdge> createRubiksGraph(RubikCube randomCube, int depthLimit) {
        Graph<RubikCube, DefaultEdge> rubiksGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
    
        // Generate the graph by creating vertices for each possible cube state
        // and adding edges to represent potential moves (rotations) between states
    
        // Example: Add a random cube state as the starting vertex
        rubiksGraph.addVertex(randomCube);
    
        // Recursive method for depth-limited graph generation
        generateGraph(randomCube, rubiksGraph, depthLimit);
    
        return rubiksGraph;
    }

    private static void generateGraph(RubikCube cube, Graph<RubikCube, DefaultEdge> graph, int depth) {
        if (depth == 0) {
            return;
        }
    
        for (Action<RubikCube> action : cube.getAllActions()) {
            try {
                RubikCube rotatedCube = cube.clone();
                action.performAction(rotatedCube);
                graph.addVertex(rotatedCube); // Add the rotated cube as a vertex
                graph.addEdge(cube, rotatedCube); // Add an edge from cube to rotatedCube
                generateGraph(rotatedCube, graph, depth - 1); // Recursive call for next depth level
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception accordingly
            }
        }
    }

    public static List<DefaultEdge> findShortestPath(Graph<RubikCube, DefaultEdge> graph, RubikCube source, RubikCube target) {
        BFSShortestPath<RubikCube, DefaultEdge> bfs = new BFSShortestPath<>(graph);

        // Iterate through the graph vertices and find the cube that matches the target sides
        RubikCube matchingCube = null;
        for (RubikCube cube : graph.vertexSet()) {
            if (cube.isComplete()) {
                System.out.println("FOUND");
                matchingCube = cube;
                break;
            }
        }
        if (matchingCube != null) {
            return bfs.getPath(source, matchingCube).getEdgeList();
        } else {
            System.out.println("Target configuration not found in the graph.");
            return new ArrayList<>(); // Return an empty list if no matching cube is found
        }
    }
}
