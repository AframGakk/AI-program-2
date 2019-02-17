
import java.awt.*;
import java.util.Random;

public class NewAgent implements Agent {
	private Random random = new Random();

	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	private Stopwatch stopwatch;

	private StateNode root;

	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
	public void init(String role, int width, int height, int playclock) {
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("white");
		this.width = width;
		this.height = height;
		// TODO: add your own initialization code here

		Player player;
		if (role.equals("white")) {
			player = Player.WHITE;
		} else {
			player = Player.BLACK;
		}

		Environment environment = new Environment(height, width);
		environment.init_env();

		State firstState = new State(environment, player, true);
		root = new StateNode(firstState);

	}

	// lastMove is null the first time nextAction gets called (in the initial state)
	// otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
	public String nextAction(int[] lastMove) {
		if (lastMove != null) {
			int x1 = lastMove[0], y1 = lastMove[1], x2 = lastMove[2], y2 = lastMove[3];
			String roleOfLastPlayer;
			if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
				roleOfLastPlayer = "white";
			} else {
				roleOfLastPlayer = "black";
			}
			System.out.println(roleOfLastPlayer + " moved from " + x1 + "," + y1 + " to " + x2 + "," + y2);
			// TODO: 1. update your internal world model according to the action that was just executed

			Action action;

			if(x1 != x2) {
				// capturing
				action = new Action(new Point(x1, y1), new Point(x2, y2), true);

			} else {
				// not capturing
				action = new Action(new Point(x1, y1), new Point(x2, y2), false);
			}

			root.setState(root.getState().nextState(action));

		}

		// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) {
			// TODO: 2. run alpha-beta search to determine the best move

			this.root.getState().getEnvironment().printEnvironment();
			Action best = MiniMax(root);

			return "(move " + best.from.x + " " + best.from.y + " " + best.to.x + " " + best.to.y + ")";
		} else {
			return "noop";
		}
	}

	// is called when the game is over or the match is aborted
	//@Override
	public void cleanup() {
		// TODO: cleanup so that the agent is ready for the next match

	}

	public Action MiniMax(StateNode node)  {

		int maxVal = 0;
		stopwatch = new Stopwatch();

		try {
			if (this.playclock < this.stopwatch.elapsedTime()) {
				throw new Exception();
			}

			maxVal = MiniMax(node, true);
		} catch (Exception ex) {
			System.out.println("Playclock exceeded!");
		}

		Action action = null;

		System.out.println("EXCEPTION PASSED");

		for(StateNode childNode : node.successors()) {
			if(maxVal < childNode.getState().getScore()) {
				maxVal = childNode.getState().getScore();
				action = childNode.getAction();
			}
		}

		return action;
	}

	public int MiniMax(StateNode node, boolean player) throws Exception {


		if(node.getState().isTerminal()) {
			// Hérna returnum við action
			return node.getState().getScore();
		}

		int bestValue;
		int value;

		if (player) {
			bestValue = Integer.MIN_VALUE;
			for (StateNode child : node.successors()) {
				value = MiniMax(child, !player);
				bestValue = Math.max(value, bestValue);
			}
		} else {
			bestValue = Integer.MAX_VALUE;
			for (StateNode child : node.successors()) {
				value = MiniMax(child, !player);
				bestValue = Math.min(value, bestValue);
			}
		}
		return bestValue;
	}
}