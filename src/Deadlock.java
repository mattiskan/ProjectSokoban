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
                if(isDeadlock(gs, box)) {
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
        if(canMove(gs, point)) {
            return false;
        }
        visited.add(point);
        for(Point p : neighbours(gs, point)) {
            if(visited.contains(p)) {
                continue;
            }
            if(!isDeadlock(gs, p, visited)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDeadlock(GameState gs, Point point) {
        return isDeadlock(gs, point, new HashSet<Point>());
    }

    public static List<Point> neighbours(GameState gs, Point point) {
        ArrayList<Point> neighbours = new ArrayList<Point>();
        if(isBox(gs, new Point(point.x-1, point.y))) {
            neighbours.add(new Point(point.x-1, point.y));
        }
        if(isBox(gs, new Point(point.x+1, point.y))) {
            neighbours.add(new Point(point.x+1, point.y));
        }
        if(isBox(gs, new Point(point.x, point.y-1))) {
            neighbours.add(new Point(point.x, point.y-1));
        }
        if(isBox(gs, new Point(point.x, point.y+1))) {
            neighbours.add(new Point(point.x, point.y+1));
        }
        return neighbours;
    }

    /*
     * Returns true if the point isn't a wall or box.
     */
    public static boolean walkable(GameState gs, Point point) {
        if(gs.getSquare(point) == MapSquareType.BOX ||
           gs.getSquare(point) == MapSquareType.BOX_ON_GOAL ||
           gs.getSquare(point) == MapSquareType.WALL) {
            return false;
        }
        return true;
    }

    /*
     * Returns true if there is a box on point in gs.
     */
    public static boolean isBox(GameState gs, Point point) {
        return gs.getSquare(point) == MapSquareType.BOX || gs.getSquare(point) == MapSquareType.BOX_ON_GOAL;
    }

    /*
     * Returns true if there is a box on point in gs that can move
     * unhindered in any direction, false otherwise. Doesn't consider
     * the player's position.
     */
    public static boolean canMove(GameState gs, Point point) {
        if(isBox(gs, point)) {
            if(walkable(gs, new Point(point.x-1, point.y)) && walkable(gs, new Point(point.x+1, point.y))) {
                return true;
            }
            if(walkable(gs, new Point(point.x, point.y-1)) && walkable(gs, new Point(point.x, point.y+1))) {
                return true;
            }
        }
        return false;
    }
}
