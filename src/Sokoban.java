public class Sokoban {
    public static final int FOUND = -1;
    public static final int NOT_FOUND = -2;

    // distances should be measured in ints, since
    // comparisons between floats are buggy.

    // TODO actually record path taken
    public String IDAStar(GameState initialState) {
       int boundary = distance(initialState);
       while(true) {
           int t = search(initialState, 0, boundary);
           if(t == FOUND) {
               return pathToGoal;
           } else if(t == NOT_FOUND) {
               return null;
           }
           boundary = t;
       }
    }

    public int search(GameState node, int g, int boundary, String path) {
        if(isGoal(node)) {
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
        return min;
    }

    public int cost(GameState node1, GameState node2) {
        return 0;
    }

    public int distance(GameState node) {
        return 0;
    }
}
