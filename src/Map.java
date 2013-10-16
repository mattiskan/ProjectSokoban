import java.io.*;
import java.util.*;

public class Map {
    public MapSquareType[][] map;
    private boolean[][] tunnel;
    public int openSquares;
    public int[][] openSquareNumbers;
    public ArrayList<Point> openSquarePoints;
    public HashSet<Point> goals;
    public PushDist dist;

    public static void main(String[] args){
    }

    public Map(MapSquareType[][] map, HashSet<Point> goals, int[][] openSquareNumbers, ArrayList<Point> openSquarePoints){
        this.map = map;
        tunnel = new boolean[map.length][map[0].length];
        this.openSquareNumbers = openSquareNumbers;
        this.openSquarePoints = openSquarePoints;
        this.goals = goals;
        dist = new PushDist(this);
        findAndSetTunnelSquares();
	//System.out.println(this);
	//System.exit(0);
    }

    public MapSquareType getSquare(Point coord){
        if (map[coord.y][coord.x]==null)
            return MapSquareType.VOID;
        return map[coord.y][coord.x];
    }

    public boolean isTunnel(Point coord) {
        return tunnel[coord.y][coord.x];
    }

    private void findAndSetTunnelSquares() {
        for(int i = 1; i < map.length-1; i++) {
            for(int j = 1; j < map[0].length-1; j++) {
                if(map[i][j] != null && map[i][j] != MapSquareType.WALL) {
		    int wallCount = 0;
		    for (int x=0; x<4; x++) {
			if (map[i+GameState.move[x].y][j+GameState.move[x].x] == MapSquareType.WALL) {
			    wallCount++;
			}
		    }
		    if (wallCount >= 2) {
			tunnel[i][j] = true;
		    }
                } 
            }
        }
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
	for (int i=0; i<tunnel.length;i++) {
	    for (int j=0; j<tunnel[0].length; j++) {
		sb.append(tunnel[i][j] ? "T ":"-");
	    }
	    sb.append("\n");
	}
	return sb.toString();
    }



    public boolean isDeadSquare(Point p) {
        return dist.deadSquare(p);
    }
}
