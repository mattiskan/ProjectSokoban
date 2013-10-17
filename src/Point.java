
public class Point {
    public final int x, y;

    public Point(int x, int y){
	this.x=x;
	this.y=y;
    }
    
    public int manhattanDist(Point other){
	return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
    
    @Override
    public int hashCode() {
	return x<<8 + y;
    }
    
    @Override
    public boolean equals(Object other) {
	if (other==null)
	    return false;
	Point o = (Point)other;
	return o.x==x && o.y==y;
    }

    @Override
    public String toString(){
	return String.format("(%d, %d)", x, y);
    }

    public Point reverse(){
	return new Point(-x,-y);
    }
    
    public Point add(Point other) {
	return new Point(x+other.x, y+other.y);
    }
    
}
