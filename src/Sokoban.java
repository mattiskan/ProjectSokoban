import java.util.*;

public class Sokoban {
    public static void main(String[] args){
	new Sokoban();
    }
    
    public static final int FOUND = -1,
	                    NOT_FOUND = -2;


    public Sokoban(){
	GameState initial = new GameStateFactory().getInitialGameState();
	//System.out.println("dfsfsd:"+initial.hasBox(initial.map.openSquarePoints.get(3)));
	visited = new HashSet<GameState>();
	System.out.println(IDAStar(initial));
    }
    public HashSet<GameState> visited;
    // distances should be measured in ints, since
    // comparisons between floats are buggy.

    // TODO actually record path taken

    String pathToGoal;

    public String IDAStar(GameState initialState) {
	int boundary = distance(initialState);
	while(true) {
	    visited.clear();
	    visited.add(initialState);
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
	/*try{
	    System.out.println(node);
	    Thread.sleep(100);
	} catch(Exception e){
	    
	}//*/
	//System.out.println("Done: "+node.openGoalCount());
	//System.out.println("fsfs");
        if( node.hasAllBoxesOnGoals() ) {
	    pathToGoal = node.generatePath();
            return FOUND;
        }

        if(node.score > boundary) {
            return node.score;
        }
        int min = Integer.MAX_VALUE;
        List<GameState> possibleMoves = node.getPossibleMoves();
        for (GameState gs : possibleMoves) {
            gs.score = g + distance(gs);
        }
        Collections.sort(possibleMoves);

        
        for(GameState succ : possibleMoves) {
            if (visited.contains(succ))
        	continue;
            visited.add(succ);
            if(Deadlock.isDeadlock(succ))
                continue;
            int t = search(succ, g+1, boundary);
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
	HashSet<Point> goals = GameState.map.getGoals();

	for(Point box : boxes) {
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
        return distanceCost;
    }
}
