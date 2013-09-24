import java.util.*;

public class Sokoban {
    public static final int FOUND = -1;
    public static final int NOT_FOUND = -2;

    public Map map = null;

    // distances should be measured in ints, since
    // comparisons between floats are buggy.

    // TODO actually record path taken
    public String IDAStar(GameState initialState) {
	/*int boundary = distance(initialState);
       while(true) {
           int t = search(initialState, 0, boundary);
           if(t == FOUND) {
               return pathToGoal;
           } else if(t == NOT_FOUND) {
               return null;
           }
           boundary = t;
       }*/
	return null;
    }

    public int search(GameState node, int g, int boundary, String path) {
        /*if(isGoal(node)) {
            pathToGoal = path;
            return FOUND;
        }

        int f = g + distance(node);
        if(f > boundary) {
            return f;
        }
        int min = Integer.MAX_VALUE;
        for(GameState succ : node.getPossibleMoves()) {
            int t = search(succ, g + cost(node, succ), boundary, path + succ.lastMovePath());
            if(t == FOUND) {
                return FOUND;
            }
            min = Math.min(t, min);
        }
        return min;*/return 0;
    }


    public int cost(GameState current, GameState goal) {
        return 1;
    }


    public int distance(GameState current) {
	int distanceCost = 0;

	Queue<Point> boxes = current.getBoxes();
	HashSet<Point> goals = map.getOpenGoals();

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
