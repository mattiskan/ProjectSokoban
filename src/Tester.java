
public class Tester {
    String test = "DRRDDLLLLDDRUDRRULDRRRULDLLULDRRRULRUUULLDRDRDDLUDLLURDLLLURUURRDUURRDDUULLDDRULLLDDDRRRUDLULDRRRRULDLLLLLURUURRDURRDULLLLDDDRRUULRDDLLURUULDRRRULDLDDRRULDRRRULDLLUUURRDLULDLLURRUULDRDDLLULURDDRRULRUULDRDDRRUULLRRDRRULLDDLLLLUDRRURRDLLLLURRUULDRDDLLULURRLDDRRULUURDLDDRRRUURRDLULLLULD";
    private static Point[] charToDir = new Point[255];
    GameState initial;
    public static void main(String[] args) {
	new Tester();
    }
    static {
	charToDir['L'] = new Point(-1, 0);
	charToDir['R'] = new Point(1, 0);
	charToDir['D'] = new Point(0, 1);
	charToDir['U'] = new Point(0, -1);
	
    }
    
    public Tester() {
	GameStateFactory f = new GameStateFactory();
        initial = f.getInitialGameStateNormal();
	play();
    }

    public void play() {
	for (int i=0; i<test.length(); i++) {
	    char c = test.charAt(i);
	    //System.out.println(i);
	    move(c);
	    try {
		if (i>1000) {
		    Thread.sleep(200);
		}
	    } catch (Exception e) {
		
	    }
	}
	if (initial.hasAllBoxesOnGoals()) {
	    System.out.println("CORRECT!");
	} else {
	    System.out.println("WRONG!");
	}
    }

    public void move(char c) {
	Point to = initial.player.add(charToDir[c]);
	Point toto = to.add(charToDir[c]);
	if (initial.hasBox(to)) {
	    if (!initial.map.getSquare(toto).isOpen()) {
		printError("Bad move: "+c);
	    } else {
		//System.out.println(initial.boxes);
		initial.pushBox(to, charToDir[c]);
		//System.out.println(initial.boxes);
		initial.player = to;
		//System.out.println("Pushed: "+c);
		//System.out.println(initial);
	    }
	} else if (initial.map.getSquare(to).isOpen()) {
	    initial.player = to;
	    //System.out.println("Moved: "+c);
	    //System.out.println(initial);
	} else {
	    printError("Square is not open: "+c);
	}
    }

    public void printError(String error) {
	//System.out.println(error);
	//System.out.println(initial);
	System.exit(0);
    }
}
