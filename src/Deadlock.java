
import java.util.*;

public class Deadlock {

    /*
* Checks whether there are any boxes that aren't on a goal and that
* can't move.
*/
    public static boolean isDeadlock(GameState gs) {
        List<Point> boxes = gs.getBoxes();
        for(Point box : boxes) {
            if(!isBoxOnGoal(gs, box)) {
                if(isDeadlock(gs, box, new HashSet<Point>())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBoxOnGoal(GameState gs, Point point) {
        return gs.getSquare(point) == MapSquareType.BOX_ON_GOAL;
    }

    /*
* Checks whether the box at point is deadlocked.
*/
    public static boolean isDeadlock(GameState gs, Point point, HashSet<Point> visited) {
        if (gs.getSquare(point) == MapSquareType.WALL)
            return true;
        if (gs.getSquare(point).isOpen())
            return false;
        if (visited.contains(point))
            return true;
        visited.add(point);
        for (int i=0; i<4; i+=2) {
            if (!isDeadlock(gs, point.add(GameState.move[i]), visited) && !isDeadlock(gs, point.add(GameState.reverseMove[i]), visited)) {
        	return false;
            } 
        }
        return true;
    }


    /*
* Returns true if there is a box on point in gs.
*/
    public static boolean isBox(GameState gs, Point point) {
        return gs.getSquare(point).isBox();
    }

}