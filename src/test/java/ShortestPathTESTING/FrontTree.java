package ShortestPathTESTING;
import solutioning.strategy.Action;
import rubikcube.RubikCube;

import java.util.*;

public class FrontTree {
    public Map<RubikCube, Integer> frontTree;
    public Map<RubiksCubeState, Integer> ArrTree;
    public BackTree backTree;
    public int num_of_nodes;
    public int limitBFS;
    public int currentLevel;

    public FrontTree(BackTree backTree) {
        limitBFS = backTree.limitBFS;
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
    
    // Start generation of front tree
    public void generate(){
        for (int i = 0; i < limitBFS; i++) {
            generateMoreFrontStates();
        }
    }

    // Checks if backTreeNode matches any front tree nodes, if so, return the path
    public List<RubiksCubeState> getMatchingPath(RubiksCubeState backTreeNode) {
        Map<RubiksCubeState, Integer> frontTreeNodes = ArrTree; 
        List<RubiksCubeState> path = new ArrayList<>();
        
        for (Map.Entry<RubiksCubeState, Integer> frontEntry : frontTreeNodes.entrySet()) {
            RubiksCubeState frontNode = frontEntry.getKey();
            Integer frontCost = frontEntry.getValue();
            
            if (frontCost.equals(backTreeNode.calculateMisplacedFacelets())) { // If misplaced facelets match, check further if it matches
                List<List<Integer>> frontArray = frontNode.getRubiksCube().generateArray();
                List<List<Integer>> backArray = backTreeNode.getRubiksCube().generateArray();
                
                // Gets the faces of each front and back node
                Integer[][] frontArrayAsArray = frontArray.stream()
                                            .map(inner -> inner.toArray(new Integer[0]))
                                            .toArray(Integer[][]::new);
                Integer[][] backArrayAsArray = backArray.stream()
                                            .map(inner -> inner.toArray(new Integer[0]))
                                            .toArray(Integer[][]::new);
                
                // Checks if the faces match
                if (Arrays.deepEquals(frontArrayAsArray, backArrayAsArray)) { 
                    System.out.println("Match found!");
                    frontNode = frontNode.getParent();
                    while (frontNode != null) {
                        path.add(frontNode);
                        frontNode = frontNode.getParent(); // Move to the parent node
                    }
                    Collections.reverse(path); // Reversing to get the path in the correct order
                    return path; // Returning path to the DLS
                }
            }
        }
        currentLevel +=1;
        return path; // Return an empty list if no match is found
    }

    // Generates more front levels
    public void generateMoreFrontStates() {
        System.out.println("Expanding Front");
        Map<RubiksCubeState, Integer> frontTreeNodes = new HashMap<>(ArrTree);
        
        
        // Generate the next level of states for each node at the current level
        for (Map.Entry<RubiksCubeState, Integer> frontEntry : frontTreeNodes.entrySet()) {
            RubiksCubeState node = frontEntry.getKey();
            Action<RubikCube>[] actions = node.getRubiksCube().getAllActions();

            if (currentLevel == node.getLevel()) {
                // Iterate over every possible action
                for (Action<RubikCube> action : actions) {
                    try {
                        RubikCube newState = node.getRubiksCube().clone(); // Create a copy of the node
                        newState.performAction(action); // Apply the action to generate a new state
                        // If node is the solution, output solution
                        if (newState.isComplete()){
                            System.out.println("Match found during BFS.");
                            List<RubiksCubeState> path = new ArrayList<>();
                            RubiksCubeState current = node;
                            while (current != null) {
                                path.add(current);
                                current = current.getParent();
                            }
                            Collections.reverse(path);
                            for (RubiksCubeState step : path) {
                                step.getRubiksCube().print();
                            }
                            System.exit(0);
                        }
                        // Else, add the new state to the tree
                        RubiksCubeState newNode = new RubiksCubeState(newState, node.getLevel()+1, node);
                        if (!ArrTree.containsKey(newNode)) {
                            node.addChild(newNode);
                            ArrTree.put(newNode, newNode.calculateMisplacedFacelets());
                            this.num_of_nodes++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle the exception accordingly
                    }
                }
            }
        }
        currentLevel += 1;
    }

    // Returns # of nodes in the front tree
    public int getNumNodes() {
        return this.num_of_nodes;
    }
}
