import java.util.*;
public class PushDist {
    Map map;
    int[][] distOld;
    int dist[][][];
    boolean[][] isDeadSquare;
    ArrayList<Point> goals;
    private static final int INFINITY = 999999;
    public PushDist(Map map) {
	this.map = map;
	System.out.println(map.goals);
	goals = GameState.getBoxes(map.goals, map);
	dist = new int[goals.size()][map.map.length][map.map[0].length];
	isDeadSquare = new boolean[map.map.length][map.map[0].length];
	for (int i=0; i<isDeadSquare.length; i++) {
	    for (int j=0; j<isDeadSquare[0].length; j++) {
		isDeadSquare[i][j] = true;
	    }
	}
	calcDist();
	//System.out.println(this);
	//System.exit(0);
    }
    
    public void calcDist() {
	int i=0;
	for (Point g : goals) {
	    dist[i] = calcGoal(dist[i], g);
	    i++;
	}
    }

    private int[][] calcGoal(int[][] squares, Point goal) {
	ArrayDeque<SearchState> queue = new ArrayDeque<SearchState>();
	queue.addFirst(new SearchState(goal, 1));
	boolean[][] visited = new boolean[squares.length][squares[0].length];
	visited[goal.y][goal.x] = true;
	while (!queue.isEmpty()) {
	    SearchState state = queue.removeLast();
	    squares[state.to.y][state.to.x] = state.cost;
	    isDeadSquare[state.to.y][state.to.x] = false;
	    for (int i=0; i<4; i++) {
		Point to = state.to.add(GameState.move[i]);
		if (visited[to.y][to.x])
		    continue;
		visited[to.y][to.x] = true;
		Point toto = to.add(GameState.move[i]);
		if (map.getSquare(to).isOpen() && map.getSquare(toto).isOpen()) {
		    queue.addFirst(new SearchState(to, state.cost+1));
		}
	    }
	}
	return squares;
    }



    public int distance(Point p, int goal) {
	int tmp = dist[goal][p.y][p.x];
	if (tmp==0)
	    return 999;
	return tmp-1;
    }
    
    public boolean deadSquare(Point p) {
	return isDeadSquare[p.y][p.x];
    }
    
    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	for (int z=0; z<dist.length; z++) {
	    for (int y=0; y<dist[0].length; y++) {
		for (int x=0; x<dist[0][0].length; x++) {
		    if (dist[z][y][x]==INFINITY)
			sb.append("INF ");
		    else
			sb.append(String.format("%03d ", distance(new Point(x, y), z)));
		}
		sb.append("\n");
	    }
	    sb.append("\n\n");
	}
	for (int y=0; y<isDeadSquare.length; y++) {
	    for (int x=0; x<isDeadSquare[0].length; x++) {
		sb.append((isDeadSquare[y][x])?"DEAD ":"ALIV ");
	    }
	    sb.append("\n");
	}

	return sb.toString();
    }

    private static class SearchState {
	public Point to;
	public int cost;
	public SearchState(Point to, int cost) {
	    this.to = to;
	    this.cost = cost;
	}
    }
}
