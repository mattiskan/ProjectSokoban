import java.io.*;
import java.util.*;

public class GameState implements Comparable<GameState> {

    public static final Point[] move = {
	new Point(-1, 0),
	new Point(1, 0),
	new Point(0, 1),
	new Point(0, -1)
    };
    public static final Point[] reverseMove = {
	new Point(1, 0),
	new Point(-1, 0),
	new Point(0, -1),
	new Point(0, 1)
    };

    public static final char[] moveToChar = {
        'L', 'R', 'D', 'U'
    };
    
    public static Map map;
    public BitSet boxes;
    public Point player;
    public Point leftmostPos; //vänstraste positionen spelaren kan nå, används vid hash.
    MoveSeq moveSeq;
    public int score;

    
    public GameState parent;

    public GameState(GameState prev, Point newPlayerPosBeforeMovement, Point direction, MoveSeq moveSeq) {
	parent = prev;
	this.boxes = (BitSet)prev.boxes.clone();
	Point newPos = newPlayerPosBeforeMovement.add(direction);
	if(getSquare(newPos).isBox()) {
	    pushBox(newPos, direction);
	}
	player = newPos;
	this.moveSeq=moveSeq;
    }

    public GameState(Point player, BitSet boxes, Map cmap) {
	this.player = player;
	leftmostPos = player;
	this.boxes = boxes;
	map = cmap;
	moveSeq = new MoveSeq();
    }

    public List<GameState> getPossibleMoves() {
	return new PlayerMoves(this).getPossibleStates();
    }

    public void setLeftmostPos(Point leftmostPos){
	this.leftmostPos = leftmostPos;
    }

    private ArrayList<Point> boxList;
    public ArrayList<Point> getBoxes() {
	if(boxList != null)
	    return boxList;

	//annars skapar vi den:
	boxList = new ArrayList<Point>();
	for (int i = boxes.nextSetBit(0); i >= 0; i = boxes.nextSetBit(i+1)) {
	    boxList.add( map.openSquarePoints.get(i) );
	}
	return boxList;
    }
    
    public boolean hasBox(Point coord) {
	if (map.getSquare(coord) == MapSquareType.WALL)
	    return false;

	return boxes.get(map.openSquareNumbers[coord.y][coord.x]);

    }
    
    public void pushBox(Point coord, Point direction){
	boxes.set(map.openSquareNumbers[coord.y][coord.x],false);
	coord = coord.add(direction);
	boxes.set(map.openSquareNumbers[coord.y][coord.x],true);
    }
    
    public MapSquareType getSquare(Point coord){
	if (coord.equals(player)) {
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
	    if( map.getSquare(box) == MapSquareType.GOAL )
		openGoalCount--;
	}
	return openGoalCount;
    }


    boolean hasAllBoxesOnGoals(){
	return openGoalCount() == 0;
    }
    
    public String generatePath(){
	return moveSeq.toString();
    }

    @Override
    public String toString(){
	StringBuilder sb = new StringBuilder();
	System.out.println(boxes);
	for (int y=0; y<map.map.length;y++) {
	    for (int x=0;x<map.map[y].length; x++) {
		sb.append(getSquare(new Point(x, y)).toString());
	    }
	    sb.append("  ");
	    for (int x=0;x<map.map[y].length; x++) {
		sb.append(String.format("%02d ",map.openSquareNumbers[y][x]));
	    }
	    sb.append("\n");
	}
	return sb.toString();
    }
    
    @Override
    public int hashCode() {
	return boxes.hashCode() + leftmostPos.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
	if (other==null)
	    return false;
	GameState o = (GameState)other;
	return (o.boxes.equals(boxes) && o.leftmostPos.equals(leftmostPos));
	    
    }
    
    @Override
    public int compareTo(GameState o) {
	return score - o.score;
    }
}
