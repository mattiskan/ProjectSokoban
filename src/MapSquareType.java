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
	case '\'': return FREE;

	default: throw new IllegalArgumentException();
	}
    }

    public MapSquareType getStatic(){
	switch(this){
	case BOX_ON_GOAL:
	case PLAYER_ON_GOAL:
	    return GOAL;
	case BOX:
	case PLAYER:
	    return FREE;
	default:
	    return this;
	}
    }    

    public boolean isPlayer(){
	return (this==PLAYER || this==PLAYER_ON_GOAL);
    }
    
    public boolean isBox(){
	return (this==BOX || this==BOX_ON_GOAL);
    }
    
    public boolean isOpen(){
	return !(this==WALL || isBox());
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
