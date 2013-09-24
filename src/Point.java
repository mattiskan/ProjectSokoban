
public class Point {
    public final int X, Y;

    public Point(int x, int y){
	X=x;
	Y=y;
    }
    
    public int manhattanDist(Point other){
	return Math.abs(X - other.X) + Math.abs(Y - other.Y);
    }
    
}
