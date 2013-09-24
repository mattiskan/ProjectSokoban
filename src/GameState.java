import java.io.*;
import java.util.*;

public class GameState {
    public static Map map;
    public BitSet boxes;
    public Point player;

    public GameState(GameState prev, int moveDir) {
	player = new Point(prev.player.x+move[moveDir].x, prev.player.y+move[moveDir].y);
	
    }

    public GameState(Point player, BitSet boxes, Map cmap) {
	this.player = player;
	this.boxes = boxes;
	map = cmap;
    }

    public List<GameState> getPossibleMoves() {
	return null;
    }

    public ArrayList<Point> getBoxes() {
	ArrayList<Point> boxList = new ArrayList<Point>();
	for (int i = boxes.nextSetBit(0); i >= 0; i = boxes.nextSetBit(i+1)) {
	    boxList.add( map.openSquarePoints.get(i) );
	}
	return boxList;
    }

    public static final Point[] move = {
	new Point(-1, 0),
	new Point(1, 0),
	new Point(0, 1),
	new Point(0, -1)
    };
    
    public boolean hasBox(Point coord) {
	return boxes.get(map.openSquareNumbers[coord.y][coord.x]);
	
    }
    
    public MapSquareType getSquare(Point coord){
	if (coord==player) {
	    if (map.getSquare(coord) == MapSquareType.GOAL) {
		return MapSquareType.PLAYER_ON_GOAL;
	    } else {
		return MapSquareType.PLAYER;
	    }
	} else if (hasBox(coord)) {
	    if (map.getSquare(coord) == MapSquareType.GOAL) {
		return MapSquareType.BOX_ON_GOAL;
	    } else {
		return MapSquareType.BOX;
	    }
	}
	return map.getSquare(coord);
    }
    
    int openGoalCount(){
	ArrayList<Point> boxes = getBoxes();
	int openGoalCount = boxes.size();

	for(Point box : boxes){
	    if( map.getSquare(box) != MapSquareType.GOAL )
		openGoalCount--;
	}
	return openGoalCount;
    }


    boolean hasAllBoxesOnGoals(){
	return openGoalCount() == 0;
    }
    
    @Override
    public String toString(){
	StringBuilder sb = new StringBuilder();
	for (int y=0; y<map.map.length;y++) {
	    for (int x=0;x<map.map[y].length; x++) {
		sb.append(getSquare(new Point(x, y)).toString());
	    }
	    sb.append("\n");
	}
	return sb.toString();
    }
}
