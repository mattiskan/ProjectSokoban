
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
    }
    
    public void calcDist() {
	for (int i=0; i<dist.length * dist[0].length/2; i++) {
	    for (int j=0; j<dist.length; j++) {
		for (int k=0; k<dist.length; k++) {
		    if (dist[j][k]==INFINITY)
			continue;
		    Point cPoint = new Point(k, j);
		    for (int l=0; l<4; l+=2) {
			Point to = cPoint.add(GameState.move[l]);
			if (map.getSquare(to).isOpen() && map.getSquare(GameState.reverseMove[l]).isOpen()) {
			    dist[j+to.y][k+to.x] = Math.min(dist[j+to.y][k+to.x], dist[j][k]+1);
			    dist[j-to.y][k-to.x] = Math.min(dist[j-to.y][k-to.x], dist[j][k]+1);
			}
		    }
		}
	    }
	}
    }
}
