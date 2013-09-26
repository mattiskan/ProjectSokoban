import java.util.*;

public class BestFirst {
    public Map map;
    String path;
    public BestFirst(Map map) {
	this.map = map;
    }
    
    public void search(GameState firstState) {
	PriorityQueue<SearchState> queue = new PriorityQueue<SearchState>();
	queue.add(new SearchState(firstState, 0));
	HashSet<GameState> visited = new HashSet<GameState>();
	while (!queue.isEmpty()) {
	    SearchState sState = queue.remove();
	    System.out.println("Distance: "+sState.value);

		/*try{
		    System.out.println("Distance: "+sState.value);
		    System.out.println(sState.state);
		    Thread.sleep(100);
		} catch(Exception e){
		    
		}*/
	    for (GameState nextState : sState.state.getPossibleMoves()) {
		if (visited.contains(nextState))
		    continue;
		if (nextState.hasAllBoxesOnGoals()) {
		    path = nextState.generatePath();
		    return;
		}
		visited.add(nextState);
		queue.add(new SearchState(nextState, distance(nextState)));
	    }
	}
    }
    
    public String getPath() {
	return null;
    }
    
    private class SearchState implements Comparable<SearchState> {
	int value;
	GameState state;
	public SearchState(GameState state, int value) {
	    this.state = state;
	    this.value = value;
	}
	@Override
	public int compareTo(SearchState other) {
	    return value - other.value;
	}
    }
    public int distance(GameState current) {
	int distanceCost = 0;
	ArrayList<Point> boxes = current.getBoxes();
	HashSet<Point> goals = current.map.getGoals();

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

	    distanceCost += GameState.map.distance(box);
	    goals.remove(nearestOpenGoal);
	}
        return distanceCost;
    }
    /*private int distance(GameState state) {
	int distance = 0;
	for (Point b : state.getBoxes()) {
	    int best = Integer.MAX_VALUE;
	    for (Point g : GameState.map.getGoals()) {
		best = Math.min(best, b.manhattanDist(g));
	    }
	    distance += best;
	}
	return distance;
    }*/
}
