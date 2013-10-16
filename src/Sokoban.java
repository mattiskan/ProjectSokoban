import java.util.*;

public class Sokoban {
    public static void main(String[] args){
        new Sokoban();
    }

    public static final int FOUND = -1,
           NOT_FOUND = -2;

    public static final int MATTIS_KONSTANT = 3;

    public Sokoban(){
        GameState initial = new GameStateFactory().getInitialGameState();
        //System.out.println("dfsfsd:"+initial.hasBox(initial.map.openSquarePoints.get(3)));
        visited = new HashSet<GameState>();
        System.out.println(IDAStar(initial));
    }
    public HashSet<GameState> visited;

    String pathToGoal;

    public String IDAStar(GameState initialState) {
        int boundary = distance(initialState);
        while(true) {
	    //System.out.println(boundary);
            visited.clear();
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
	/*        try{
          System.out.println(node);
          Thread.sleep(300);
          } catch(Exception e){

          }//*/
        if( node.hasAllBoxesOnGoals() ) {
            pathToGoal = node.generatePath();
            return FOUND;
        }

        if(node.score > boundary) {
            return node.score;
        }

        int min = Integer.MAX_VALUE;
        List<GameState> possibleMoves = node.getPossibleMoves();

        if (visited.contains(node))
            return Integer.MAX_VALUE - 1000;//ett stort värde
        visited.add(node);

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


    public int cost(GameState current, GameState goal) {
        return 1;
    }

    static int[][] distanceMatrix;

    public int distance(GameState gState) {
        List<Point> boxes = gState.getBoxes();
        HashSet<Point> goals = GameState.map.getGoals();
	
        distanceMatrix = cleanMatrix(distanceMatrix, boxes.size(), goals.size());

        int i = 0;
        for(Point box : boxes) {
            for(int z=0;z<gState.map.goals.size(); z++) {
                distanceMatrix[i][z] = gState.map.dist.distance(box, z);//box.manhattanDist(goal);
            }
            i++;
        }

        return Hungarian.hungarianCost(distanceMatrix) * MATTIS_KONSTANT;
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
