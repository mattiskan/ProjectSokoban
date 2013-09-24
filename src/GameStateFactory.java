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
    private int[][] freeSquareNumbers;
    private Point playerPos;
    private BitSet boxes = new BitSet();

    private HashSet<Point> goals = new HashSet<Point>();
    private GameState gs;

    public GameStateFactory(){
	populateBuffer();
	readMapsFromBuffer();
	Map map = new Map(cmap, goals, freeSquareNumbers);
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
	freeSquareNumbers = new int[rowCount][mostColumns];

	int boxPointCounter = 0;

	for (int y=0; y<rowCount; y++) {
	    cmap[y] = new MapSquareType[mostColumns];
	    freeSquareNumbers[y] = new int[mostColumns];

	    String currentLine = buffer.get(y);

	    for (int x=0; x<currentLine.length(); x++) {
		MapSquareType square = MapSquareType.fromChar(currentLine.charAt(x));
		cmap[y][x] = square.getStatic();

		if(!square.isOpen())
		    continue;//stängda rutor har inte spelare/lådor/mål

		if(cmap[y][x] == MapSquareType.GOAL)
		    goals.add(new Point(x,y));

		freeSquareNumbers[y][x] = boxPointCounter++;

		if(square.isBox())
		    boxes.set(boxPointCounter-1);
		else if(square.isPlayer())
		    playerPos = new Point(x,y);
	    }
	}
    }

}
