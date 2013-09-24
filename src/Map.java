import java.io.*;
import java.util.*;

public class Map {
    public MapSquareType[][] map;
    GameState state;
    public int openSquares;
    public int[][] freeSquareNumbers;
    public ArrayList<Point> boxToPoint;
    private HashSet<Point> goals;

    public static void main(String[] args){
    }

    public Map(MapSquareType[][] map, HashSet<Point> goals, int[][] freeSquareNumbers){
	this.map = map;
	this.freeSquareNumbers = freeSquareNumbers;
	this.goals = goals;
    }

    public MapSquareType getSquare(Point coord){
	return map[coord.y][coord.x];
    }

    @SuppressWarnings("unchecked")
    public HashSet<Point> getGoals(){
	//Do a clone
	return (HashSet<Point>) goals.clone();
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
