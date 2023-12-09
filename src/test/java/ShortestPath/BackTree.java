package ShortestPath;
import org.jgrapht.graph.DefaultEdge;
import solutioning.strategy.Action;
import rubikcube.RubikCube;
import rubikcube.RubikSide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackTree {
    public Map<RubikCube, Integer> backTree;
    public FrontTree frontTree;
    public int num_of_nodes;
    public Map<RubiksCubeState, Integer> ArrTree;

    public BackTree() {
        ArrTree = new HashMap<>();
        backTree = new HashMap<>();
        num_of_nodes = 0;
        frontTree = new FrontTree(this);
        generateInitialStates();
    }

    // Method to generate the first 18 combinations and store them in the back tree
    public void generateInitialStates() {
        System.out.println("Expanding Back");
        RubiksCubeState solvedNode = new RubiksCubeState(new RubikCube(3), 0, null);
        ArrTree.put(solvedNode, 0);

        for (Action<RubikCube> action : solvedNode.getRubiksCube().getAllActions()) {
            try {
                RubikCube newState = solvedNode.getRubiksCube().clone();
                newState.performAction(action);
                RubiksCubeState newNode = new RubiksCubeState(newState, solvedNode.getLevel(), solvedNode);
                ArrTree.put(newNode, 1);
                this.num_of_nodes++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (frontTree.check(ArrTree)) {
            System.out.println("Found");
        } else {
            System.out.println("Expanding Front");
            frontTree.generateMoreFrontStates();
        }
    }

    
    public void generateMoreStates() {
        // Get all the nodes at the current level
        Map<RubiksCubeState, Integer> backTreeNodes = new HashMap<>(ArrTree);
        // Generate the next level of states for each node at the current level
        for (Map.Entry<RubiksCubeState, Integer> backEntry : backTreeNodes.entrySet()) {
            RubiksCubeState node = backEntry.getKey();
            Integer level = backEntry.getValue();
            Action<RubikCube>[] actions = node.getRubiksCube().getAllActions();
            for (Action<RubikCube> action : actions) {
                try {
                    RubikCube newState = node.getRubiksCube().clone(); // Create a copy of the node
                    newState.performAction(action); // Apply the action to generate a new state
                    RubiksCubeState newNode = new RubiksCubeState(newState, node.getLevel()+1, node);
                    if (!ArrTree.containsKey(newNode)) {
                        ArrTree.put(newNode, newNode.calculateMisplacedFacelets());
                        this.num_of_nodes++;
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception accordingly
                }
            }
        }
        if (frontTree.check(ArrTree) == true){
            System.out.println("Found");
        }else{
            System.out.println("Expanding Front");
            frontTree.generateMoreFrontStates();

        }
    }



    public List<RubikCube> getAllBackNodes() {
        return new ArrayList<>(backTree.keySet());
    }
    // Other methods related to managing the back tree nodes

    public Map<RubiksCubeState, Integer> getBackTree() {
        return ArrTree;
    }
    
    public int getNumNodes() {
        return this.num_of_nodes;
    }
}