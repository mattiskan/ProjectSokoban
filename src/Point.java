
public class Point {
    public final int x, y;

    public Point(int x, int y){
	this.x=x;
	this.y=y;
    }
    
    public int manhattanDist(Point other){
	return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
    
}
