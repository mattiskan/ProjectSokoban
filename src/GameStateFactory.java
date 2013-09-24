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

    private HashSet<Point> goals = new HashSet<Point>();
    private GameState gs;

    public GameStateFactory(){
	populateBuffer();
	readMapsFromBuffer();
	flowFill();
	Map map = new Map(cmap, goals, openSquareNumbers, openSquarePoints);
	gs = new GameState(playerPos, boxes, map);
    }

    public GameState getInitialGameState(){
	return gs;
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
		cmap[y][x] = square;//square.getStatic();

		if(!square.isOpen())
		    continue;//stängda rutor har inte spelare/lådor/mål

		if(square.getStatic() == MapSquareType.GOAL)
		    goals.add(new Point(x,y));
		else if(square.isPlayer())
		    playerPos = new Point(x,y);
	    }
	}
    }

    private void flowFill(){
	HashSet<Point> visited = new HashSet<Point>();
	ArrayDeque<Point> queue = new ArrayDeque<Point>();

	queue.addFirst(playerPos);
	int boxPointCounter = 0;
	while (!queue.isEmpty()) {
	    Point p = queue.removeLast();
	    if (visited.contains(p) || cmap[p.y][p.x] == MapSquareType.WALL) {
		continue;
	    }
	    openSquareNumbers[p.y][p.x] = boxPointCounter++;
	    openSquarePoints.add(p);
	    if(cmap[p.y][p.x].isBox())
		boxes.set(boxPointCounter-1);
       	    visited.add(p);
	    if(cmap[p.y][p.x] == MapSquareType.VOID)
		cmap[p.y][p.x] = MapSquareType.FREE;
	    for (Point nP : GameState.move) {
		queue.addFirst(p.add(nP));
	    }
	}
	System.out.println(boxes);
    }
}
