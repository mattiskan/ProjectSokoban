
public class PushDist {
    Map map;
    int[][] dist;
    private static final int INFINITY = 999999;
    public PushDist(Map map) {
	this.map = map;
	dist = new int[map.map.length][map.map[0].length];
	for (int y=0; y<dist.length; y++) {
	    for (int x=0; x<dist[0].length; x++) {
		if (map.map[y][x] == MapSquareType.GOAL) {
		    dist[y][x] = 0;
		} else {
		    dist[y][x] = INFINITY;
		}
	    }
	}
	calcDist();
    }
    
    public void calcDist() {
	for (int i=0; i<dist.length * dist[0].length/2; i++) {
	    boolean updated = false;
	    for (int j=0; j<dist.length; j++) {
		for (int k=0; k<dist[0].length; k++) {
		    if (dist[j][k]==INFINITY)
			continue;
		    Point cPoint = new Point(k, j);
		    for (int l=0; l<4; l++) {
			Point to = cPoint.add(GameState.move[l]);
			Point toto = to.add(GameState.move[l]);
			if (toto.x<0 || toto.y<0 || toto.x>=dist[0].length || toto.y>=dist.length)
			    continue;
			if (map.getSquare(to).isOpen() && map.getSquare(toto).isOpen()) {
			    if (dist[cPoint.y][cPoint.x]+1<dist[to.y][to.x]) {
				dist[to.y][to.x] = dist[cPoint.y][cPoint.x]+1;
				updated=true;
			    }
			}
		    }
		}
	    }
	    if (!updated)
		break;
	}
    }
    
    public int distance(Point p) {
	return dist[p.y][p.x];
    }
    
    public boolean deadSquare(Point p) {
	return distance(p)==INFINITY;
    }
    
    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	for (int y=0; y<dist.length; y++) {
	    for (int x=0; x<dist[0].length; x++) {
		if (dist[y][x]==INFINITY)
		    sb.append("INF ");
		else
		    sb.append(String.format("%03d ", dist[y][x]));
	    }
	    sb.append("\n");
	}
	return sb.toString();
    }
}
