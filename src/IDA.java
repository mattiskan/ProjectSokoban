import java.util.ArrayList;
import java.util.HashSet;

public class IDA {
    public static final int FOUND = -1,
	    		NOT_FOUND = -2;

    HashSet<GameState> visited = new HashSet<GameState>();
    String pathToGoal;
    public String IDAStar(GameState initialState) {
	int boundary = distance(initialState);
	while(true) {
	    visited.clear();
	    visited.add(initialState);
	    int t = search(initialState, 0, boundary);
	    if(t == FOUND) {
		return "done: " + pathToGoal;
	    } else if(t == NOT_FOUND) {
		return "Path not found";
	    }
	    boundary = t;
	}
    }

    public int search(GameState node, int g, int boundary) {
	//System.out.println("fsfs");
        if( node.hasAllBoxesOnGoals() ) {
	    pathToGoal = node.generatePath();
            return FOUND;
        }
        int f = g + distance(node);
        if(f > boundary) {
            return f;
        }
        int min = Integer.MAX_VALUE;
        for(GameState succ : node.getPossibleMoves()) {
            if (visited.contains(succ))
        	continue;
            visited.add(succ);
            int t = search(succ, g + cost(node, succ), boundary);
            if(t == FOUND) {
                return FOUND;
            }
            min = Math.min(t, min);
        }
        return min;
    }


    public int cost(GameState current, GameState goal) {
        return 1;
    }

    public int distance(GameState current) {
	int distanceCost = 0;

	ArrayList<Point> boxes = current.getBoxes();
	//HashSet<Point> goals = GameState.map.getGoals();

	for(Point box : boxes) {
	    /*int nearest = Integer.MAX_VALUE;
	    Point nearestOpenGoal = null;
	    for(Point goal : goals){
		int distanceTo = box.manhattanDist(goal);
		if( distanceTo < nearest ){
		    nearest = distanceTo;
		    nearestOpenGoal = goal;
		}
	    }*/

	    //distanceCost += GameState.map.distance(box);//nearest;
	    //goals.remove(nearestOpenGoal);
	}
        return distanceCost;
    }
}
