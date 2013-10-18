import java.util.*;

public class Sokoban {
    public static void main(String[] args){
        new Sokoban();
    }

    public static final int FOUND = -1,
	NOT_FOUND = -2;

    public static final int MATTIS_KONSTANT = 3;

    public static final boolean DEBUG = false;

    public Sokoban(){
	GameStateFactory f = new GameStateFactory();
	GameState initial = f.getInitialGameStateNormal();
    
	if(initial.getBoxes().size() ==0){
	    finish("");
	}
        
	List<GameState> reverse = f.getInitialGameStateReverse();

        visited = new HashMap<GameState, GameState>();
	visitedR = new HashMap<GameState, GameState>();
        System.out.println(IDAStar(initial, reverse));
    }
    public HashMap<GameState, GameState> visited;
    public HashMap<GameState, GameState> visitedR;

    String pathToGoal;

    public String IDAStar(GameState initialState, List<GameState> reverseStates) {
        int boundary = distance(initialState);
        while(true) {
            visited.clear();
            int t = search(initialState, 0, boundary);
            if(t == FOUND) {
                return pathToGoal;
            }
	    visitedR.clear();
	    for (GameState gs : reverseStates) {
		t = Math.min(t, rsearch(gs, 0, boundary));
	    }
            boundary = t;
        }
    }

    public int search(GameState node, int g, int boundary) {
	if(DEBUG){
	    try{
		System.out.println(node);
		System.out.println("FORWARD");
		Thread.sleep(300);
	    } catch(Exception e){}//*/
	}
        if( node.hasAllBoxesOnGoals() )
	    finish(node.generatePath());
	
        if(node.score > boundary) {
            return node.score;
        }

        int min = Integer.MAX_VALUE;
        List<GameState> possibleMoves = node.getPossibleMoves();
	if (visited.containsKey(node)) {
	    return Integer.MAX_VALUE - 1000;
	}
	GameState visitedState = visitedR.get(node);
        if (visitedState!=null) {
	    printPath(node, visitedState);
	}
        visited.put(node, node);

        for (GameState gs : possibleMoves) {
            gs.score = g + distance(gs);
        }
        Collections.sort(possibleMoves);

        for(GameState succ : possibleMoves) {
            min = Math.min(min, search(succ, g+1, boundary));
        }
        return min;
    }

    public int rsearch(GameState node, int g, int boundary) {
	if(DEBUG){
	    try{
		System.out.println(node);
		System.out.println(distance(node));
		Thread.sleep(300);
	    } catch(Exception e){}//*/
	}

	if (node.score > boundary) {
	    return node.score;
	}

        int min = Integer.MAX_VALUE;
        List<GameState> possibleMoves = node.getPossibleMoves();
	if (visitedR.containsKey(node)) {
	    return Integer.MAX_VALUE - 1000;
	}
	GameState visitedState = visited.get(node);
        if (visitedState!=null) {
	    printPath(visitedState, node);
	}
	visitedR.put(node, node);
        for (GameState gs : possibleMoves) {
            gs.score = g + distance(gs);
        }
        Collections.sort(possibleMoves);

        for(GameState succ : possibleMoves) {
            int t = rsearch(succ, g+1, boundary);
            if(t == FOUND) {
                return FOUND;
            }
            min = Math.min(t, min);
        }
        return min;
    }

    private void printPath(GameState forward, GameState backward) {
	System.out.print(forward.generatePath());
	System.out.print(findWalkPath(forward, backward.player, forward.player));
	MoveSeq ms = backward.moveSeq;
	while (ms.parent != null) {
	    System.out.print(ms.move);
	    ms = ms.parent;
	}
	System.out.println();
	System.exit(0);
    }


    public int cost(GameState current, GameState goal) {
        return 1;
    }

    static int[][] distanceMatrix;

    public int distance(GameState gState) {
        List<Point> boxes = gState.getBoxes();
        distanceMatrix = cleanMatrix(distanceMatrix, boxes.size(), boxes.size());
	if(!gState.reverse){
	    int i = 0;
	    for(Point box : boxes) {
		for(int z=0;z<boxes.size(); z++) {
		    distanceMatrix[i][z] = gState.map.dist.distance(box, z); //box.manhattanDist(goal);
		}
		i++;
	    }
	} else {
	    ArrayList<Point> goals = gState.map.getGoals();
	    for(int i=0; i<boxes.size(); i++)
		for(int j=0; j<goals.size(); j++)
		    distanceMatrix[i][j] = boxes.get(i).manhattanDist(goals.get(j));
	}

	return Hungarian.hungarianCost(distanceMatrix) * (MATTIS_KONSTANT + (gState.reverse? 2:0));
    }

    public static int[][] cleanMatrix(int[][] m, int r, int c){
	if(m == null)
	    m = new int[r][c];
	else
	    for(int i=0; i<r; i++)
		for(int j=0; j<c; j++)
		    m[i][j] = 0;
	return m;
    }
    

    boolean[][] visited2;
    private String findWalkPath(GameState gs, Point current, Point goal){
	if(current.equals(goal))
	    return "";
	visited2 = new boolean[gs.map.map.length][gs.map.map[0].length];
	StringBuilder sb = new StringBuilder();
	findWalkPathHelper(gs, current, goal, sb);
	return sb.toString();
    }

    private boolean findWalkPathHelper(GameState gs, Point current, Point goal, StringBuilder sb) {
	for(int d=0; d<4; d++){
	    Point dir = GameState.move[d];
	    Point next = current.add(dir);
	            
	    if(visited2[next.y][next.x])
		continue;
	            
	    visited2[next.y][next.x] = true;

	    if(next.equals(goal) || gs.getSquare(next).isOpen() && findWalkPathHelper(gs, next, goal, sb)){
		sb.append( GameState.moveToChar[d+4] );
		return true;
	    }
	}

	return false;
    }

    private void finish(String answer){
	System.out.println(answer);
	System.exit(0);
    }

    /*
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
    return distanceCost*MATTIS_KONSTANT;
}
    */
}
