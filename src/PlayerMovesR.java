import java.util.*;

public class PlayerMovesR {
    GameState gs;

    ArrayList<GameState> possiblePaths;
    static boolean[][] visited;
    ArrayDeque<BFSRecord> q;

    public PlayerMovesR(GameState gs){
	this.gs = gs;
	if(visited == null)
	    visited = new boolean[gs.map.map.length][gs.map.map[0].length];
	else
	    clearVisitedMatrix();
	q = new ArrayDeque<BFSRecord>();
	possiblePaths = new ArrayList<GameState>();
    }

    private void clearVisitedMatrix(){
	for(int i=0; i<visited.length; i++)
	    for(int j=0; j<visited[0].length; j++)
	        visited[i][j] = false;
    }

    public ArrayList<GameState> getPossibleStates(){
	bfs();
	gs.setLeftmostPos(leftmostPos);
	//System.out.println("Size: "+possiblePaths.size());
	//System.exit(0);
	return possiblePaths;
    }

    private Point leftmostPos;
    private void bfs(){
	q.addFirst(new BFSRecord(gs.player, gs.moveSeq));

	leftmostPos = gs.player;
	visited[gs.player.y][gs.player.x] = true;

	while(!q.isEmpty()){
	    BFSRecord current = q.removeLast();

	    for(int d=0; d<4; d++){
		Point dir = GameState.move[d];
		Point nextSquare = current.p.add(dir);
		if(visited[nextSquare.y][nextSquare.x])
		   continue;
		if(gs.hasBox(nextSquare)){
		    Point rDir = GameState.reverseMove[d];
		    Point to = nextSquare.add(rDir);
		    Point toto = to.add(rDir);
		    //System.out.println("1");
		    if(gs.getSquare(toto).isOpen()){
			//System.out.println("2");
			GameState possibleNextState = new GameState(gs, current.p, dir, new MoveSeq(current.m, GameState.moveToChar[d]));

			possiblePaths.add(possibleNextState);

		    }
		} else if(gs.getSquare(nextSquare).isOpen()) {
		    q.addFirst(new BFSRecord(nextSquare, new MoveSeq(current.m, GameState.moveToChar[d+4])));
		    visited[nextSquare.y][nextSquare.x] = true;

		    if(isLeftermostPos(nextSquare)) {
			leftmostPos = nextSquare;
		    }
		}
	    }
	}
    }

    private boolean isLeftermostPos(Point p){
	return p.x < leftmostPos.x ||  p.x == leftmostPos.x && p.y < leftmostPos.y;
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
