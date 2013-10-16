import java.io.*;
import java.util.*;

public class Map {
    public MapSquareType[][] map;
    public int openSquares;
    public int[][] openSquareNumbers;
    public ArrayList<Point> openSquarePoints;
    public HashSet<Point> goals;
    public PushDist dist;
    
    public static void main(String[] args){
    }

    public Map(MapSquareType[][] map, HashSet<Point> goals, int[][] openSquareNumbers, ArrayList<Point> openSquarePoints){
	this.map = map;
	this.openSquareNumbers = openSquareNumbers;
	this.openSquarePoints = openSquarePoints;
	this.goals = goals;
	dist = new PushDist(this);
    }

    public MapSquareType getSquare(Point coord){
	if (map[coord.y][coord.x]==null)
	    return MapSquareType.VOID;
	return map[coord.y][coord.x];

    }

    @SuppressWarnings("unchecked")
    public HashSet<Point> getGoals(){
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

    
    public boolean isDeadSquare(Point p) {
	return dist.deadSquare(p);
    }
}
