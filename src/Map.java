import java.io.*;
import java.util.*;

public class Map {
    public MapSquareType[][] map;
    GameState state;
    public int openSquares;
    public int[][] pointToBox;
    public ArrayList<Point> boxToPoint;
    public static void main(String[] args){
	System.out.println("Testing Map");
	Map testMap = new Map();
	System.out.println("Created map:\n" + testMap);
    }

    public Map(){
	 builFromStream(new BufferedReader(new InputStreamReader(System.in)));
    }

    /**
     *    Generates a new map based on a move
     */
    public Map(char lastMove, Map previousMap){

    }

    private void builFromStream(BufferedReader in){
	ArrayList<String> tmpStorage = new ArrayList<String>();

	int longestLine = 0;
	try {
	    String line;
	    while((line = in.readLine()) != null){
		longestLine = Math.max(line.length(), longestLine);
		tmpStorage.add(line);
	    }
	} catch(IOException ioe){
	    throw new RuntimeException("Nu gick något fel med inläsning av kartan :S");
	}
	map = new MapSquareType[tmpStorage.size()][longestLine];
	pointToBox = new int[tmpStorage.size()][longestLine];
	for (int y=0; y<map.length; y++) {
	    map[y] = new MapSquareType[longestLine];
	    pointToBox[y] = new int[longestLine];
	    for (int x=0; x<map[0].length; x++) {
		String currentLine = tmpStorage.get(y);
		map[y][x] = MapSquareType.fromChar(currentLine.charAt(x));

	    }
	}	


    }


     @Override
     public String toString(){
	 StringBuilder sb = new StringBuilder();
	 for(MapSquareType[] row : map){
	     for(MapSquareType col : row)
		 if(col != null)
		     sb.append(col.toString());
	     
	     sb.append('\n');
	 }
	 return sb.toString();
     }
    

}
