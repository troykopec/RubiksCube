package ShortestPathTESTING;
//////////////////////////////////////////////////////////
//
//  This code implements a way of solving a rubiks cube
//  by using graph theory. Two trees are expanded, one
//  from a solved cube, and one from a random cube,
//  the front tree is checked for a solution using BFS
//  during creation. If a solution is not found, DFS is
//  used to traverse the back tree with multithreading,
//  if a solution is found, its path will be traced and
//  printed.
//  - By Troy and Kevin
//
//////////////////////////////////////////////////////////
public class main {
    public static void main(String[] args) {
        // Specify limits
        int limitHash = 5; 
        int limitBFS = 5;

        // Show # of proccessors available
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of available processors/cores: " + availableProcessors);

        // Run the test
        BackTree backTest = new BackTree(limitBFS,limitHash);
        backTest.parallelDFSTraversal(backTest.solvedNode, limitHash+1, 16);


    }
}