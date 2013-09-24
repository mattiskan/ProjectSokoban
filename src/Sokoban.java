import java.util.*;

public class Sokoban {
    public static final int FOUND = -1;
    public static final int NOT_FOUND = -2;

    public Map map = null;

    // distances should be measured in ints, since
    // comparisons between floats are buggy.

    // TODO actually record path taken

    String pathToGoal;

    public String IDAStar(GameState initialState) {
	int boundary = distance(initialState);
	while(true) {
	    int t = search(initialState, 0, boundary);
	    if(t == FOUND) {
		return pathToGoal;
	    } else if(t == NOT_FOUND) {
		return "Path not found";
	    }
	    boundary = t;
	}
    }

    public int search(GameState node, int g, int boundary) {
        if(node.isGoal()) {
            return FOUND;
        }

        int f = g + distance(node);
        if(f > boundary) {
            return f;
        }
        int min = Integer.MAX_VALUE;
        for(GameState succ : node.getPossibleMoves()) {
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

	Queue<Point> boxes = current.getBoxes();
	HashSet<Point> goals = current.getOpenGoals();

	while(!boxes.isEmpty()) {
	    Point box = boxes.poll();
	    int nearest = Integer.MAX_VALUE;
	    Point nearestOpenGoal = null;
	    for(Point goal : goals){
		int distanceTo = box.manhattanDist(goal);
		    if( distanceTo < nearest ){
			nearest = distanceTo;
			nearestOpenGoal = goal;
		    }
	    }

	    distanceCost += nearest;
	    goals.remove(nearestOpenGoal);
	}
        return 0;
    }
}
