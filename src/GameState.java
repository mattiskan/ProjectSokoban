import java.util.*;

public class GameState {
    public static Map map;
    public BitSet boxes;
    public Point player;

    public GameState(GameState prev, int moveDir) {
	player = new Point(prev.player.x+move[moveDir].x, prev.player.y+move[moveDir].y);
	
    }

    public GameState(Map map) {
	
    }
    

    public List<GameState> getPossibleMoves() {
	return null;
    }
    public LinkedList<Point> getBoxes() {
	LinkedList<Point> boxList = new LinkedList<Point>();
	for (int i = boxes.nextSetBit(0); i >= 0; i = boxes.nextSetBit(i+1)) {
	    boxList.add(map.boxToPoint.get(i));
	}
	return boxList;
    }  

    public HashSet<Point> getOpenGoals() {
	return null;
    }

    Point[] move = {
	new Point(-1, 0),
	new Point(1, 0),
	new Point(0, 1),
	new Point(0, -1)
    };
    public void countOpen() {
	HashSet<Point> visited = new HashSet<Point>();
	ArrayDeque<Point> queue = new ArrayDeque<Point>();
	queue.addFirst(player);
	int count = 0;
	while (!queue.isEmpty()) {
	    Point p = queue.removeLast();
	    if (visited.contains(p) || map.map[p.y][p.x] != MapSquareType.WALL) {
		continue;
	    }
	    map.pointToBox[p.x][p.y] = count;
	    map.boxToPoint.add(p);
	    count++;
	    visited.add(p);
	    map.map[p.y][p.x] = MapSquareType.FREE;
	    for (Point nP : move) {
		queue.addFirst(nP);
	    }
	}
        map.openSquares = count;
    }
	/*map = new MapSquareType[tmpStorage.size()][longestLine];
	for (int y=0; y<map.length; y++) {
	    map[y] = new MapSquareType[longestLine];
	    for (int x=0; x<map[0].length; x++) {
		String currentLine = tmpStorage.get(y);
		map[y][x] = MapSquareType.fromChar(currentLine.charAt(col));
		if (cSquare!=MapSquareType.PLAYER || MapSquareType.PLAYER_ON_GOAL) {
		    state = new GameState(new Point(x, y));
		    break;
		}
	    }
	}
	countOpen();
	state.boxes = new BitSet(openSquares);
	openCounter = 0;
	for(int row=0; row < tmpStorage.size(); row++){
	    for(int col=0; col < currentLine.length(); col++){
		 MapSquareType cSquare = map[row][col];
		 if (cSquare!=MapSquareType.WALL) {
		     if (cSquare==MapSquareType.BOX) {
			 state.boxes.set(openCounter);
			 map[row][col]
		     } else if (cSquare==MapSquareType.BOX_ON_GOAL) {
			 state.boxes.set(openCounter);
		     }
		     openCounter++;
		 }
	    }
	    }*/
    boolean isGoal(){
	return false;
    }
}