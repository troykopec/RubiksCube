package ShortestPathTESTING;
import solutioning.strategy.Action;
import rubikcube.RubikCube;

import java.util.concurrent.atomic.AtomicBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackTree {
    private AtomicBoolean solutionFound = new AtomicBoolean(false);
    public Map<RubikCube, Integer> backTree;
    public FrontTree frontTree;
    public int num_of_nodes;
    public Map<RubiksCubeState, Integer> ArrTree;
    public int limitBFS;
    public int limitHash;
    public RubiksCubeState solvedNode;
    public int currentLevel = 0;
    private long timeA;

    public BackTree(int limitBFS, int limitHash) {
        this.timeA = System.currentTimeMillis() / 1000;
        this.limitBFS = limitBFS;
        this.limitHash = limitHash;
        ArrTree = new HashMap<>();
        backTree = new HashMap<>();
        num_of_nodes = 0;
        frontTree = new FrontTree(this);

        // Create front trees with multi-threading
        Thread frontTreeThread = new Thread(() -> frontTree.generate());
        Thread backTreeThread = new Thread(() -> generateInitialStates());
        frontTreeThread.start();
        backTreeThread.start();
        try {
            frontTreeThread.join();
            backTreeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to generate the first 18 combinations and store them in the back tree
    public void generateInitialStates() {
        System.out.println("Expanding Back");
        this.solvedNode = new RubiksCubeState(new RubikCube(3), 0, null);
        ArrTree.put(this.solvedNode, 0);

        // Generate all actions and store into the backtree
        for (Action<RubikCube> action : solvedNode.getRubiksCube().getAllActions()) {
            try {
                RubikCube newState = solvedNode.getRubiksCube().clone();
                newState.performAction(action);
                RubiksCubeState newNode = new RubiksCubeState(newState, solvedNode.getLevel()+1, solvedNode);
                solvedNode.addChild(newNode);
                ArrTree.put(newNode, 1);
                this.num_of_nodes++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        currentLevel +=1;
        // Generate the rest of the backtree
        for (int i = 0; i < limitHash-1; i++) {
            generateMoreStates();
        }
        
    }
    
    // Generates more states in the tree
    public void generateMoreStates() {
        System.out.println("Expanding Back");
        Map<RubiksCubeState, Integer> backTreeNodes = new HashMap<>(ArrTree);

        // I
        for (Map.Entry<RubiksCubeState, Integer> backEntry : backTreeNodes.entrySet()) {
            RubiksCubeState node = backEntry.getKey();
            Action<RubikCube>[] actions = node.getRubiksCube().getAllActions();

            if (currentLevel == node.getLevel()) {
                for (Action<RubikCube> action : actions) {
                    try {
                        RubikCube newState = node.getRubiksCube().clone(); // Create a copy of the node
                        newState.performAction(action); // Apply the action to generate a new state
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
        currentLevel +=1;
    }
    
    // Get number of nodes in the backtree
    public int getNumNodes() {
        return this.num_of_nodes;
    }

    // Method to perform DFS traversal in the back tree
    public boolean DFS(RubiksCubeState currentNode, int depthLimit) {
        if (depthLimit <= 0) {
            return false; // Reached the depth limit, exit this path
        }

        // Check if the current node exists in the front tree
        if (frontTree.ArrTree.containsKey(currentNode)) {
            List<RubiksCubeState> path = frontTree.getMatchingPath(currentNode);
            if (!path.isEmpty()) {
                RubiksCubeState current = currentNode;
                while (current != null) {
                    path.add(current);
                    current = current.getParent();
                }
                for (RubiksCubeState step : path) {
                    step.getRubiksCube().print();
                }
                solutionFound.set(true);
                return true; // Match found, this is a potential solution
            }
        }

        // Expand to child nodes and perform DFS recursively
        List<RubiksCubeState> children = currentNode.getChildren(); // Get children nodes
        for (RubiksCubeState child : children) {
            if (DFS(child, depthLimit - 1)) {
                solutionFound.set(true);
                return true; // Match found in this subtree, propagate it upwards
            }
        }
        return false; // No match found in this subtree
    }

    //Function to multithread DFS
    public void parallelDFSTraversal(RubiksCubeState root, int depthLimit, int numThreads) {
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<RubiksCubeState> rootChildren = getChildrenNodes(root); //get the root nodes children
        
        for (RubiksCubeState subRoot : rootChildren) {
            executorService.submit(() -> { // Perform DFS for the subtree assigned to this thread
                boolean found = DFS(subRoot, depthLimit - 1);
                if (found) {
                    synchronized (solutionFound) {
                        solutionFound.set(true); // Signal that a solution is found
                    }
                    executorService.shutdown(); // Stop other threads if a solution is found
                    printInfo(this); // printing info
                    System.exit(0);
                }
            });
        }
        executorService.shutdown();
    }

    // Get all the root nodes children
    public List<RubiksCubeState> getChildrenNodes(RubiksCubeState rootNode) {
        List<RubiksCubeState> childrenNodes = new ArrayList<>();
    
        List<RubiksCubeState> children = rootNode.getChildren();
        for (RubiksCubeState child : children) {
            childrenNodes.add(child);
        }
    
        return childrenNodes;
    }

    public void printInfo(BackTree backTest){
        //print nodes in each tree
        System.out.println("front tree nodes:"+backTest.frontTree.getNumNodes());
        System.out.println("back tree nodes:"+backTest.getNumNodes());

        //timing
        long timeB = System.currentTimeMillis()/1000;
        long KMPTime = timeB - timeA;
        System.out.println("TIME TO COMPLETE: "+ KMPTime);
    }
}