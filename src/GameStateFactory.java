import java.io.*;
import java.util.*;

/**
 *    Creates the initial GameState object with a reference to a static Map,
 *    both created based on stdin.
 */
public class GameStateFactory {

    private int mostColumns, rowCount;
    private ArrayList<String> buffer = new ArrayList<String>();
    
    private MapSquareType[][] cmap; //the constant map
    private int[][] openSquareNumbers;
    private ArrayList<Point> openSquarePoints;
    private Point playerPos;
    private BitSet boxes = new BitSet();
    private BitSet goals = new BitSet();
    

    public GameStateFactory(){
	populateBuffer();
	readMapsFromBuffer();
	flowFill();
    }

    public GameState getInitialGameStateNormal(){
	Map map = new Map(cmap, goals, openSquareNumbers, openSquarePoints);
	map.calculatePushDist();
	return new GameState(playerPos, boxes, map, false);
    }
    //Here be dragons
    public List<GameState> getInitialGameStateReverse() {
	Map rmap = new Map(cmap, boxes, openSquareNumbers, openSquarePoints);
	//BFS to find all staring positions
	ArrayList<GameState> startStates = new ArrayList<GameState>();
	boolean[][] visited = new boolean[cmap.length][cmap[0].length];
	for (int i = goals.nextSetBit(0); i >= 0; i = goals.nextSetBit(i+1)) {
	    Point box = openSquarePoints.get(i);
	    visited[box.y][box.x] = true;
	}
	for (Point b : GameState.getBoxes(goals, rmap)) {
	    for (Point dir : GameState.move) {
		Point startPos = b.add(dir);
		if (visited[startPos.y][startPos.x] || !rmap.getSquare(startPos).isOpen())
		    continue;
		startStates.add(new GameState(startPos, goals, rmap, true));
		ArrayDeque<Point> queue = new ArrayDeque<Point>();
		queue.addFirst(startPos);
		visited[startPos.y][startPos.x] = true;
		while (!queue.isEmpty()) {
		    Point currPos = queue.removeLast();
		    for (Point bfsDir : GameState.move) {
			Point bfsTo = currPos.add(bfsDir);
			if (visited[bfsTo.y][bfsTo.x] || !rmap.getSquare(bfsTo).isOpen())
			    continue;
			visited[bfsTo.y][bfsTo.x] = true;
			queue.addFirst(bfsTo);
		    }
		}
	    }
	}
	//BFS end
	return startStates;
    }

    private void populateBuffer() {
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	try {
	    String line;
	    while((line = in.readLine()) != null){
		mostColumns = Math.max(line.length(), mostColumns);
		buffer.add(line);
	    }
	    in.close();
	} catch(IOException ioe){
	    throw new RuntimeException("Nu gick något fel med inläsning av kartan :S");
	}
	rowCount = buffer.size();
    }
    
    private void readMapsFromBuffer(){
	cmap = new MapSquareType[rowCount][mostColumns];
	openSquareNumbers = new int[rowCount][mostColumns];
	openSquarePoints = new ArrayList<Point>();

	int boxPointCounter = 0;
	for (int y=0; y<rowCount; y++) {
	    cmap[y] = new MapSquareType[mostColumns];
	    openSquareNumbers[y] = new int[mostColumns];

	    String currentLine = buffer.get(y);
	    
	    for (int x=0; x<currentLine.length(); x++) {
		MapSquareType square = MapSquareType.fromChar(currentLine.charAt(x));
		cmap[y][x] = square;//.getStatic();

		if(square.isPlayer()) {
		    if(square == MapSquareType.PLAYER_ON_GOAL)
			cmap[y][x] = MapSquareType.GOAL;
		    else
			cmap[y][x] = MapSquareType.VOID;

		    playerPos = new Point(x,y);
		}
	    }
	}
    }

    private void flowFill(){
	HashSet<Point> visited = new HashSet<Point>();
	ArrayDeque<Point> queue = new ArrayDeque<Point>();

	queue.addFirst(playerPos);
	int boxPointCounter = 1;
	openSquarePoints.add(new Point(0,0));

	while (!queue.isEmpty()) {
	    Point p = queue.removeLast();
	    if (visited.contains(p) || cmap[p.y][p.x] == MapSquareType.WALL) {
		continue;
	    }
	    openSquareNumbers[p.y][p.x] = boxPointCounter++;
	    openSquarePoints.add(p);
	    if(cmap[p.y][p.x].isGoal())
		goals.set(boxPointCounter-1);
	    if(cmap[p.y][p.x].isBox()) {
		boxes.set(boxPointCounter-1);
	    }
	    cmap[p.y][p.x] = cmap[p.y][p.x].getStatic();
       	    visited.add(p);
	    if(cmap[p.y][p.x] == MapSquareType.VOID)
		cmap[p.y][p.x] = MapSquareType.FREE;
	    for (Point nP : GameState.move) {
		queue.addFirst(p.add(nP));
	    }
	}
    }
}
