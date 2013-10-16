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
                    if(map[i-1][j] == MapSquareType.WALL && map[i+1][j] == MapSquareType.WALL &&
                       map[i][j-1] != MapSquareType.WALL && map[i][j+1] != MapSquareType.WALL ||
                       map[i][j-1] == MapSquareType.WALL && map[i][j+1] == MapSquareType.WALL &&
                       map[i-1][j] != MapSquareType.WALL && map[i+1][j] != MapSquareType.WALL) {
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
<<<<<<< HEAD
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
    public int distance(Point p) {
        return dist.distance(p);
    }

=======
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

    
>>>>>>> b51f6dd147c9be4eb71cf34d228808ee1e5288b0
    public boolean isDeadSquare(Point p) {
        return dist.deadSquare(p);
    }
}
