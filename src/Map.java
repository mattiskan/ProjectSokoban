import java.io.*;
import java.util.*;

public class Map {
    public MapSquareType[][] map;
    GameState state;
    public int openSquares;
    public int[][] freeSquareNumbers;
    public ArrayList<Point> boxToPoint;

    public static void main(String[] args){
    }

    public Map(MapSquareType[][] map, int[][] freeSquareNumbers){
	this.map = map;
	this.freeSquareNumbers = freeSquareNumbers;
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
