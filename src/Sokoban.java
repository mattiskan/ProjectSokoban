import java.util.*;

public class Sokoban {
    public static void main(String[] args){
        new Sokoban();
    }

    public static final int FOUND = -1,
           NOT_FOUND = -2;

    public static final int MATTIS_KONSTANT = 5;
    public static final int NICLAS_KONSTANT = 3;
    public static final int GUNNINGS_KONSTANT = 9;
    public static final int AXELS_KONSTANT = 2;

    public static final boolean DEBUG = false;

    public Sokoban(){
	GameStateFactory f = new GameStateFactory();
        GameState initial = f.getInitialGameStateNormal();
	List<GameState> reverse = f.getInitialGameStateReverse();
        //System.out.println("dfsfsd:"+initial.hasBox(initial.map.openSquarePoints.get(3)));
        visited = new HashMap<GameState, GameState>();
	visitedR = new HashMap<GameState, GameState>();
        System.out.println(IDAStar(initial, reverse));
    }
    public HashMap<GameState, GameState> visited;
    public HashMap<GameState, GameState> visitedR;

    String pathToGoal;

    public String IDAStar(GameState initialState, List<GameState> reverseStates) {
        int boundary = 30;//distance(initialState);
	//search(initialState,0,1);
        while(true) {
	    //System.out.println(boundary);
            visited.clear();
            int t = 999999;//search(initialState, 0, boundary);
	    visitedR.clear();
	    for (GameState gs : reverseStates) {
		t = Math.min(t, rsearch(gs, 0, boundary));
	    }
            if(t == FOUND) {
                return pathToGoal;
            } else if(t == NOT_FOUND) {
                return "Path not found";
	    }
            boundary = t;
	    System.out.println("Boundary: " + boundary);
        }
    }

    public int search(GameState node, int g, int boundary) {
	if(DEBUG){
	    try{
		System.out.println(node);
		Thread.sleep(300);
	    } catch(Exception e){}//*/
	}
        if( node.hasAllBoxesOnGoals() && false ) {
            pathToGoal = node.generatePath();
            return FOUND;
        }

        if(node.score > boundary) {
            return node.score;
        }

        int min = Integer.MAX_VALUE;
        List<GameState> possibleMoves = node.getPossibleMoves();
	if (visited.containsKey(node)) {
	    return Integer.MAX_VALUE - 1000;
	}
	GameState visitedState = visited.get(node);
        if (visitedState!=null) {
	    if (visitedState.reverse) {
		System.out.println("fsf");
		printPath(node, visitedState);
	    } else {
		return Integer.MAX_VALUE - 1000;//ett stort värde
	    }
	}
        visited.put(node, node);

        for (GameState gs : possibleMoves) {
            gs.score = g + distance(gs);
        }
        Collections.sort(possibleMoves);

        for(GameState succ : possibleMoves) {
            int t = search(succ, g+1, boundary);
            if(t == FOUND) {
                return FOUND;
            }
            min = Math.min(t, min);
        }
        return min;
    }

    public int rsearch(GameState node, int g, int boundary) {
	if(DEBUG){
	    try{
		System.out.println(node);
		Thread.sleep(300);
	    } catch(Exception e){}//*/
	}
	if (node.hasAllBoxesOnGoals()){
	    printPath(node, node);
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
	    if (!visitedState.reverse) {
		printPath(visitedState, node);
	    } else {
		return Integer.MAX_VALUE - 1000;//ett stort värde
	    }
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
	System.out.println("\nPRINT PATH BIACH!");
	MoveSeq ms = backward.moveSeq;
	while (ms != null) {
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
        List<Point> boxes = gState.getBoxesIgnoringReverse();
        distanceMatrix = cleanMatrix(distanceMatrix, boxes.size(), boxes.size());
	if(!gState.reverse){
	    int i = 0;
	    for(Point box : boxes) {
		for(int z=0;z<boxes.size(); z++) {
		    distanceMatrix[i][z] = gState.map.dist.distance(box, z);//box.manhattanDist(goal);
		}
		i++;
	    }
	} else {
	    ArrayList<Point> goals = gState.map.getGoals();
	    for(int i=0; i<boxes.size(); i++)
		for(int j=0; j<goals.size(); j++)
		    distanceMatrix[i][j] = boxes.get(i).manhattanDist(goals.get(j));
	    
	}

        return Hungarian.hungarianCost(distanceMatrix) * NICLAS_KONSTANT;
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
