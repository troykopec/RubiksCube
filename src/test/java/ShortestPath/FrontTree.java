package ShortestPath;
import solutioning.strategy.Action;
import rubikcube.RubikCube;
import rubikcube.RubikSide;
import rubikcube.RubikCube.FACE;

import java.util.*;

public class FrontTree {
    public Map<RubikCube, Integer> frontTree;
    public Map<RubiksCubeState, Integer> ArrTree;
    public BackTree backTree;
    public int num_of_nodes;

    public FrontTree(BackTree backTree) {
        frontTree = new HashMap<>();
        ArrTree = new HashMap<>();
        this.backTree = backTree;
        RubikCube rubix = new RubikCube(3);
        num_of_nodes = 0;
        try {
            rubix.turnRowToRight(0);
            rubix.turnRowToRight(1);
            rubix.turnColDown(0);
            rubix.turnRowToRight(2);
            rubix.turnRowToLeft(1);
            rubix.turnColDown(0);
            rubix.turnRowToRight(2);
            rubix.turnColDown(0);
            rubix.turnRowToRight(2);
            rubix.turnColUp(1);

        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception accordingly
        }
        RubiksCubeState rubix1 = new RubiksCubeState(rubix, 0, null);
        ArrTree.put(rubix1, 1);
    }

    public Map<RubikCube, Integer> getTree(){
        return frontTree;
    }

    public boolean check(Map<RubiksCubeState, Integer> backTreeNodes) {
        Map<RubiksCubeState, Integer> frontTreeNodes = ArrTree; // Assuming you have a method to retrieve front tree nodes with levels

        for (Map.Entry<RubiksCubeState, Integer> frontEntry : frontTreeNodes.entrySet()) {
            RubiksCubeState frontNode = frontEntry.getKey();
            Integer frontLevel = frontEntry.getValue();
            
            // Check if the front tree state exists in the back tree states and compare levels
            if (backTreeNodes.containsKey(frontNode)) {
                Integer backLevel = backTreeNodes.get(frontNode);
                if (frontLevel.equals(backLevel)) {
                    System.out.println("Solution found at level: " + frontLevel);
                    System.out.println(frontNode.getRubiksCube().generatePrintableString());
                    System.out.println(frontNode.getLevel() + "<----- level");
                    return true;
                }
            }
        }
        return false;
    }

    public void generateMoreFrontStates() {
        // Get all the nodes at the current level of the front tree
        Map<RubiksCubeState, Integer> frontTreeNodes = new HashMap<>(ArrTree);
    
        // Generate the next level of states for each node at the current level
        for (Map.Entry<RubiksCubeState, Integer> frontEntry : frontTreeNodes.entrySet()) {
            RubiksCubeState node = frontEntry.getKey();
            Integer level = frontEntry.getValue();
            Action<RubikCube>[] actions = node.getRubiksCube().getAllActions();
            for (Action<RubikCube> action : actions) {
                try {
                    RubikCube newState = node.getRubiksCube().clone(); // Create a copy of the node
                    newState.performAction(action); // Apply the action to generate a new state
                    RubiksCubeState newNode = new RubiksCubeState(newState, node.getLevel()+1, node);
                    if (!ArrTree.containsKey(newNode)) {
                        //frontTree.put(newState, level); // Store the new state in the front tree if it's not already present
                        ArrTree.put(newNode, newNode.calculateMisplacedFacelets());
                        this.num_of_nodes++;
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception accordingly
                }
            }
        }
        
        if (check(backTree.getBackTree())) {
            System.out.println("Found");
        } else {
            System.out.println("Expanding Back");
            backTree.generateMoreStates();
        }
    }
    
    public List<RubikCube> getAllNodes() {
        return new ArrayList<>(frontTree.keySet());
    }

    public int getNumNodes() {
        return this.num_of_nodes;
    }
}
