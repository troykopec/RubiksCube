package ShortestPath;
import org.jgrapht.graph.DefaultEdge;
import solutioning.strategy.Action;
import rubikcube.RubikCube;
import rubikcube.RubikSide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RubiksCubeState {
    private RubikCube rubiksCube;
    private int level;
    private RubiksCubeState parent;

    // Define necessary attributes to represent the state of the Rubik's Cube
    // For example, an array or data structure that captures the state
    
    public RubiksCubeState(RubikCube rubiksCube, int level, RubiksCubeState parent) {
        this.rubiksCube = rubiksCube;
        this.level = level;
        this.parent = parent;
        // Set other attributes based on rubiksCube to represent its state
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RubiksCubeState)) return false;
        RubiksCubeState other = (RubiksCubeState) o;
        // Compare state representations for equality
        List<List<Integer>> thisState = this.getRubiksCube().generateArray();
        List<List<Integer>> otherState = other.getRubiksCube().generateArray();

        return Arrays.deepEquals(thisState.toArray(), otherState.toArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRubiksCube().generateArray());
    }


    public RubikCube getRubiksCube() {
        return rubiksCube;
    }

    public RubiksCubeState getParent() {
        return parent;
    }

    public int getLevel() {
        return level;
    }

    public int calculateMisplacedFacelets() {
        // Implement logic to count misplaced facelets
        // Compare current cube state with the solved state
        int misplacedCount = 0;
        // Get the current cube state
        List<List<Integer>> currentState = this.getRubiksCube().generateArray();
        // Get the solved state of the cube
        RubikCube solved = new RubikCube(3);
        List<List<Integer>> solvedState = solved.generateArray(); // Assuming a method to get the solved state

        // Compare each facelet in the cube with the solved state
        for (int i = 0; i < currentState.size(); i++) {
            for (int j = 0; j < currentState.get(i).size(); j++) {
                if (!currentState.get(i).get(j).equals(solvedState.get(i).get(j))) {
                    // Facelet is misplaced if it doesn't match the solved state
                    misplacedCount++;
                }
            }
        }
        return misplacedCount;
    }
    // Override equals and hashCode methods to compare RubiksCubeState instances
    // ... (equals and hashCode implementations remain the same as mentioned before)
}