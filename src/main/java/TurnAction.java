import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TurnAction implements RubikCubeAction{

    public enum DIRECTION { LEFT, RIGHT, UP, DOWN}
    public enum TURN_TYPE { ROW, COL }
    public int turnPosition;
    public DIRECTION direction;
    public TURN_TYPE turnType;

    private TurnAction(TURN_TYPE turnType, DIRECTION direction, int turnPosition){
        this.turnType = turnType;
        this.direction = direction;
        this.turnPosition = turnPosition;
    }

    public static TurnAction[] allActions(int size){
        return IntStream.range(0, size)
                        .boxed()
                        .flatMap(i -> Stream.of(
                            new TurnAction(TURN_TYPE.ROW, DIRECTION.LEFT, i),
                            new TurnAction(TURN_TYPE.ROW, DIRECTION.RIGHT, i),
                            new TurnAction(TURN_TYPE.COL, DIRECTION.UP, i),
                            new TurnAction(TURN_TYPE.COL, DIRECTION.DOWN, i)
                        ))
                        .toArray(TurnAction[]::new);
    }

    @Override
    public String getName(){
        return "TURN_" + turnType + "_" + turnPosition + "_" + direction;
    }

    @Override
    public void performAction(RubikCube rubikCube){
        try {
            if(turnType == TURN_TYPE.COL){
                if(direction == DIRECTION.UP) rubikCube.turnColUp(turnPosition);
                if(direction == DIRECTION.DOWN) rubikCube.turnColDown(turnPosition);
            }else if(turnType == TURN_TYPE.ROW){
                if(direction == DIRECTION.LEFT) rubikCube.turnRowToLeft(turnPosition);
                if(direction == DIRECTION.RIGHT) rubikCube.turnRowToRight(turnPosition);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public RubikCubeAction oppositeAction(){
        if(turnType == TURN_TYPE.COL){
            if(direction == DIRECTION.UP) return new TurnAction(TURN_TYPE.COL, DIRECTION.DOWN, turnPosition);
            if(direction == DIRECTION.DOWN) return new TurnAction(TURN_TYPE.COL, DIRECTION.UP, turnPosition);
        }else if(turnType == TURN_TYPE.ROW){
            if(direction == DIRECTION.LEFT) return new TurnAction(TURN_TYPE.ROW, DIRECTION.RIGHT, turnPosition);
            if(direction == DIRECTION.RIGHT) return new TurnAction(TURN_TYPE.ROW, DIRECTION.LEFT, turnPosition);
        }

        return null;
    }
}