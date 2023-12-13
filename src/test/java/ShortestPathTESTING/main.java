package ShortestPathTESTING;

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