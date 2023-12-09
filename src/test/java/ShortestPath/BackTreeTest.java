package ShortestPath;
import rubikcube.RubikCube;
import rubikcube.RubikSide;


public class BackTreeTest {
    public static void main(String[] args) {

        int limitHash = 5; // Set your limitHash value
        long timeA = System.currentTimeMillis() /1000;
        BackTree backTest = new BackTree();
        System.out.println("front tree nodes:"+backTest.frontTree.getNumNodes());
        System.out.println("back tree nodes:"+backTest.getNumNodes());
        long timeB = System.currentTimeMillis()/1000;
        long KMPTime = timeB - timeA;
        System.out.println(KMPTime);

    }
}