package rubikcube.scoring;

import org.junit.jupiter.api.Test;
import rubikcube.RubikCube;
import rubikcube.RubikSide;
import rubikcube.action.ConsolidatedAction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

class RubikCubeScoringByLayerCompletedTest {

    RubikCubeScoringByLayerCompleted scoring = new RubikCubeScoringByLayerCompleted();

    @Test
    void getScoreOfSideWithHighestScore() {
        RubikCube cube = new RubikCube(3);
        Double highestScore = scoring.getScoreOfSideWithHighestScore(cube);
        assertThat(highestScore, equalTo(1.0));

        cube.performAction(new ConsolidatedAction(ConsolidatedAction.FACE.RIGHT_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 0, 3));
        highestScore = scoring.getScoreOfSideWithHighestScore(cube);
        assertThat(highestScore, equalTo(1.0));

        cube.performAction(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 0, 3));
        highestScore = scoring.getScoreOfSideWithHighestScore(cube);
        Double expected  = BigDecimal.valueOf(2.0 / 3.0)
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(highestScore, equalTo(expected));
    }

    @Test
    void getRubikCubeScore_whenNoSideIsComplete_ShouldReturnSideWithHighestScore() {
        RubikCube cube = new RubikCube(3);
        cube.performAction(new ConsolidatedAction(ConsolidatedAction.FACE.RIGHT_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 0, 3));
        cube.performAction(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 0, 3));
        Double expected = scoring.getScoreOfSideWithHighestScore(cube);
        Double actual = scoring.getRubikCubeScore(cube);
        assertThat(actual, equalTo(expected));
    }

    @Test
    void getCompletedFaces() {
        RubikCube cube = new RubikCube(3);
        List<RubikCube.FACE> completedFaces = scoring.getCompletedFaces(cube);
        assertThat(completedFaces.size(), equalTo(6));

        cube.performAction(new ConsolidatedAction(ConsolidatedAction.FACE.TOP_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3));
        completedFaces = scoring.getCompletedFaces(cube);
        assertThat(completedFaces.size(), equalTo(2));

        cube.performAction(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3));
        completedFaces = scoring.getCompletedFaces(cube);
        assertThat(completedFaces.size(), equalTo(0));
    }

    @Test
    void getAdjacentFaces() {
        RubikCube cube = new RubikCube(3);
        List<RubikCube.FACE> adjacentFaces = scoring.getAdjacentFaces(RubikCube.FACE.MAIN);
        assertThat(adjacentFaces, containsInAnyOrder(RubikCube.FACE.TOP, RubikCube.FACE.BOTTOM, RubikCube.FACE.LEFT, RubikCube.FACE.RIGHT));

        adjacentFaces = scoring.getAdjacentFaces(RubikCube.FACE.BACK);
        assertThat(adjacentFaces, containsInAnyOrder(RubikCube.FACE.TOP, RubikCube.FACE.BOTTOM, RubikCube.FACE.LEFT, RubikCube.FACE.RIGHT));

        adjacentFaces = scoring.getAdjacentFaces(RubikCube.FACE.TOP);
        assertThat(adjacentFaces, containsInAnyOrder(RubikCube.FACE.MAIN, RubikCube.FACE.BACK, RubikCube.FACE.LEFT, RubikCube.FACE.RIGHT));

        adjacentFaces = scoring.getAdjacentFaces(RubikCube.FACE.BOTTOM);
        assertThat(adjacentFaces, containsInAnyOrder(RubikCube.FACE.MAIN, RubikCube.FACE.BACK, RubikCube.FACE.LEFT, RubikCube.FACE.RIGHT));

        adjacentFaces = scoring.getAdjacentFaces(RubikCube.FACE.RIGHT);
        assertThat(adjacentFaces, containsInAnyOrder(RubikCube.FACE.MAIN, RubikCube.FACE.BACK, RubikCube.FACE.TOP, RubikCube.FACE.BOTTOM));

        adjacentFaces = scoring.getAdjacentFaces(RubikCube.FACE.LEFT);
        assertThat(adjacentFaces, containsInAnyOrder(RubikCube.FACE.MAIN, RubikCube.FACE.BACK, RubikCube.FACE.TOP, RubikCube.FACE.BOTTOM));

    }

    @Test
    void getAdjacentRows() {

        RubikCube cube = new RubikCube(3);
        List<int[]> adjacentRows = scoring.getAdjacentRows(cube, RubikCube.FACE.MAIN);
        assertThat(adjacentRows, containsInAnyOrder(new int[]{5, 5, 5},new int[]{2, 2, 2},new int[]{6, 6, 6},new int[]{4, 4, 4}));

        adjacentRows = scoring.getAdjacentRows(cube, RubikCube.FACE.TOP);
        assertThat(adjacentRows, containsInAnyOrder(new int[]{1, 1, 1},new int[]{2, 2, 2},new int[]{3, 3, 3},new int[]{4, 4, 4}));

        adjacentRows = scoring.getAdjacentRows(cube, RubikCube.FACE.RIGHT);
        assertThat(adjacentRows, containsInAnyOrder(new int[]{1, 1, 1},new int[]{5, 5, 5},new int[]{3, 3, 3},new int[]{6, 6, 6}));

        adjacentRows = scoring.getAdjacentRows(cube, RubikCube.FACE.BACK);
        assertThat(adjacentRows, containsInAnyOrder(new int[]{5, 5, 5},new int[]{2, 2, 2},new int[]{6, 6, 6},new int[]{4, 4, 4}));

        adjacentRows = scoring.getAdjacentRows(cube, RubikCube.FACE.BOTTOM);
        assertThat(adjacentRows, containsInAnyOrder(new int[]{1, 1, 1},new int[]{2, 2, 2},new int[]{3, 3, 3},new int[]{4, 4, 4}));

        adjacentRows = scoring.getAdjacentRows(cube, RubikCube.FACE.LEFT);
        assertThat(adjacentRows, containsInAnyOrder(new int[]{1, 1, 1},new int[]{5, 5, 5},new int[]{3, 3, 3},new int[]{6, 6, 6}));

    }

    @Test
    void allSame() {
        assertThat(scoring.allSame(new int[]{ 1, 1, 1}), is(true));
        assertThat(scoring.allSame(new int[]{ 1, 2, 1}), is(false));
        assertThat(scoring.allSame(new int[]{ 1, 2, 2}), is(false));
        assertThat(scoring.allSame(new int[]{ 1, 2, 3}), is(false));
    }

    @Test
    void getLevel2Score() throws Exception {
        RubikCube cube = new RubikCube(3);
        Double level2Score = scoring.getMidLevelScore(cube, RubikCube.FACE.MAIN, 0);
        assertThat(level2Score, equalTo(1.0));
        cube.print();
        cube.performAction(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3));

        cube.print();
        System.out.println(cube.check());
        level2Score = scoring.getMidLevelScore(cube, RubikCube.FACE.MAIN, 0);
        assertThat(level2Score, equalTo(1.0));

        cube = new RubikCube(3,
                RubikSide.create(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}),
                RubikSide.create(new int[][]{{2, 2, 2}, {2, 2, 2}, {4, 2, 2}}),
                RubikSide.create(new int[][]{{3, 3, 3}, {3, 3, 3}, {3, 3, 3}}),
                RubikSide.create(new int[][]{{4, 4, 4}, {4, 4, 4}, {4, 4, 2}}),
                RubikSide.create(new int[][]{{5, 5, 5}, {5, 5, 5}, {5, 5, 5}}),
                RubikSide.create(new int[][]{{6, 6, 6}, {6, 6, 6}, {6, 6, 6}}));

        level2Score = scoring.getMidLevelScore(cube, RubikCube.FACE.MAIN, 0);
        assertThat(level2Score, equalTo(0.5));

        cube = new RubikCube(3,
                RubikSide.create(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}),
                RubikSide.create(new int[][]{{2, 2, 2}, {2, 2, 2}, {4, 2, 2}}),
                RubikSide.create(new int[][]{{3, 3, 3}, {3, 3, 3}, {3, 3, 3}}),
                RubikSide.create(new int[][]{{4, 4, 4}, {4, 4, 4}, {4, 4, 2}}),
                RubikSide.create(new int[][]{{5, 5, 5}, {5, 5, 5}, {7, 5, 5}}),
                RubikSide.create(new int[][]{{6, 6, 6}, {6, 6, 6}, {6, 6, 6}}));

        level2Score = scoring.getMidLevelScore(cube, RubikCube.FACE.MAIN, 0);
        assertThat(level2Score, equalTo(0.25));

        cube = new RubikCube(3,
                RubikSide.create(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}),
                RubikSide.create(new int[][]{{2, 2, 2}, {2, 2, 2}, {4, 2, 2}}),
                RubikSide.create(new int[][]{{3, 3, 3}, {3, 3, 3}, {3, 3, 3}}),
                RubikSide.create(new int[][]{{4, 4, 4}, {4, 4, 4}, {4, 4, 2}}),
                RubikSide.create(new int[][]{{5, 5, 5}, {5, 5, 5}, {7, 5, 5}}),
                RubikSide.create(new int[][]{{6, 7, 6}, {6, 6, 6}, {6, 6, 6}}));

        level2Score = scoring.getMidLevelScore(cube, RubikCube.FACE.MAIN, 0);
        assertThat(level2Score, equalTo(0.0));
    }
}