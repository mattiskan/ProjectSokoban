import java.util.*;

public class PlayerMoves {
    GameState gs;

    ArrayList<GameState> possiblePaths;
    boolean[][] visited;
    ArrayDeque<BFSRecord> q;

    public PlayerMoves(GameState gs){
	this.gs = gs;
	visited = new boolean[GameState.map.map.length][GameState.map.map[0].length];
	q = new ArrayDeque<BFSRecord>();
	possiblePaths = new ArrayList<GameState>();
    }

    public ArrayList<GameState> getPossibleStates(){
	bfs();
	return possiblePaths;
    }

    private void bfs(){
	q.addFirst(new BFSRecord(gs.player, gs.moveSeq));
	visited[gs.player.y][gs.player.x] = true;

	while(!q.isEmpty()){
	    BFSRecord current = q.removeLast();
	    
	    for(int d=0; d<4; d++){
		Point dir = GameState.move[d];
		Point nextSquare = current.p.add(dir);
		if(visited[nextSquare.y][nextSquare.x])
		   continue;
		if(gs.hasBox(nextSquare)){
		    Point to = nextSquare.add(dir);
		    if(gs.getSquare(to).isOpen() && !GameState.map.isDeadSquare(to)){
			possiblePaths.add(new GameState(gs, current.p, dir, new MoveSeq(current.m, GameState.moveToChar[d])));
		    }
		} else if(gs.getSquare(nextSquare).isOpen()) {
		    q.addFirst(new BFSRecord(nextSquare, new MoveSeq(current.m, GameState.moveToChar[d])));
		    visited[nextSquare.y][nextSquare.x] = true;
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
