
public enum MapSquareType {
    FREE, WALL, GOAL, PLAYER, PLAYER_ON_GOAL, BOX, BOX_ON_GOAL, VOID;

    public static MapSquareType fromChar(char c) {
	switch(c) {
	case ' ': return VOID;
	case '#': return WALL;
	case '.': return GOAL;
	case '@': return PLAYER;
	case '$': return BOX;
	case '*': return BOX_ON_GOAL;
	case '+': return PLAYER_ON_GOAL;
	case 'o': return FREE;

	default: throw new IllegalArgumentException();
	}
    }

    @Override
    public String toString(){
	switch(this) {
	case VOID: return "X";
	case WALL: return "#";
	case GOAL: return ".";
	case PLAYER: return "@";
	case BOX: return "$";
	case BOX_ON_GOAL: return "*";
	case PLAYER_ON_GOAL: return "+";
	case FREE: return " ";
	}
	throw new AssertionError();
    }
}
