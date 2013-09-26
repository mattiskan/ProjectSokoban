public class MoveSeq {
    final MoveSeq parent;
    final char move;

    public MoveSeq(){
	parent = null;
	move = 0;
    }
    
    public MoveSeq(MoveSeq parent, char move){
	this.parent = parent;
	this.move = move;
    }
    @Override
    public String toString(){
	StringBuilder sb = new StringBuilder();
	buildStringHelper(sb);
	return sb.toString();
    }

    private void buildStringHelper(StringBuilder sb){
	if(parent == null)
	    return;
	parent.buildStringHelper(sb);
	sb.append(move);
    }	

}
