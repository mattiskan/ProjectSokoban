import java.io.*;
import java.util.*;

public class GameState {

    public static final Point[] move = {
	new Point(-1, 0),
	new Point(1, 0),
	new Point(0, 1),
	new Point(0, -1)
    };

    public static final String[] movesToString = {
	"L", "R", "D", "U"
    };

    public static Map map;
    public BitSet boxes;
    public Point player;
    String howIGotHere;
    
    public GameState parent;

    public GameState(GameState prev, Point newPlayerPosBeforeMovement, Point direction, String howIGotHere) {
	parent = prev;
	this.boxes = prev.boxes;
	Point newPos = newPlayerPosBeforeMovement.add(direction);
	pushBox(newPos, direction);
	player = newPos;
	this.howIGotHere=howIGotHere;
    }

    public GameState(Point player, BitSet boxes, Map cmap) {
	this.player = player;
	this.boxes = boxes;
	map = cmap;
	howIGotHere = "";
    }

    public List<GameState> getPossibleMoves() {
	ArrayList<GameState> possibleMoves = new ArrayList<GameState>();
	for(int d=0; d<4; d++){
	    Point direction = move[d];
	    Point nextPos = player.add(direction);

	    MapSquareType nextSquare = getSquare(nextPos);
	    if(nextSquare.isOpen() || nextSquare.isBox() && getSquare(nextPos.add(direction)).isOpen())
		possibleMoves.add(new GameState(this, player, direction, movesToString[d]));
	}
	return possibleMoves;
    }

    public ArrayList<Point> getBoxes() {
	ArrayList<Point> boxList = new ArrayList<Point>();
	for (int i = boxes.nextSetBit(0); i >= 0; i = boxes.nextSetBit(i+1)) {
	    boxList.add( map.openSquarePoints.get(i) );
	}
	return boxList;
    }
    
    public boolean hasBox(Point coord) {
	return boxes.get(map.openSquareNumbers[coord.y][coord.x]);
    }
    
    public void pushBox(Point coord, Point direction){
	if(!getSquare(coord).isBox()){
	    System.err.println("Pushed null box");
	    return;
	}
	boxes.set(map.openSquareNumbers[coord.y][coord.x],false);
	coord.add(direction);
	boxes.set(map.openSquareNumbers[coord.y][coord.x],true);
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
	    if( map.getSquare(box) == MapSquareType.GOAL )
		openGoalCount--;
	}
	return openGoalCount;
    }


    boolean hasAllBoxesOnGoals(){
	return openGoalCount() == 0;
    }
    
    public String generatePath(){
	StringBuilder sb = new StringBuilder();
	generatePathHelper(this, sb);
	return sb.toString();
    }

    private void generatePathHelper(GameState child, StringBuilder sb){
	if(child == null)
	    return;
	generatePathHelper(this.parent, sb);
	sb.append(child.howIGotHere);
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
