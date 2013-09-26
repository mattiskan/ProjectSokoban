import java.util.*;

public class PlayerMoves {
    GameState gs;

    ArrayList<GameState> possiblePaths;
    HashSet<Point> visited;
    Queue<BFSRecord> q;

    public PlayerMoves(GameState gs){
	this.gs = gs;
	visited = new HashSet<Point>();
	q = new LinkedList<BFSRecord>();
	possiblePaths = new ArrayList<GameState>();
    }

    public ArrayList<GameState> getPossibleStates(){
	bfs();
	return possiblePaths;
    }

    private void bfs(){
	q.add(new BFSRecord(gs.player, gs.moveSeq));
	visited.add(gs.player);

	while(!q.isEmpty()){
	    BFSRecord current = q.poll();
	    
	    for(int d=0; d<4; d++){
		Point dir = GameState.move[d];
		Point nextSquare = current.p.add(dir);
		if(visited.contains(nextSquare))
		   continue;
		if(gs.hasBox(nextSquare)){
		    if(gs.getSquare(nextSquare.add(dir)).isOpen()){
			possiblePaths.add(new GameState(gs, current.p, dir, new MoveSeq(current.m, gs.moveToChar[d])));
		    }
		} else if(gs.getSquare(nextSquare).isOpen()) {
		    q.add(new BFSRecord(nextSquare, new MoveSeq(current.m, GameState.moveToChar[d])));
		    visited.add(nextSquare);
		}
	    }
	}
    }

    private class BFSRecord {
	Point p;
	MoveSeq m;
	public BFSRecord(Point p, MoveSeq m){
	    this.p = p;
	    this.m = m;
	}
    }
}
