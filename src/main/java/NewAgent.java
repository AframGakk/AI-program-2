
import java.awt.*;
import java.util.Random;
import java.util.Stack;

public class NewAgent implements Agent {
	private Random random = new Random();

	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	private Stopwatch stopwatch;

	private StateNode root;

	private Stack<StateNode> testList;

	/*
		init(String role, int playclock) is called once before you have to select the first action. 
		Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
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

		//vantar depth

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

			//System.out.println("Before");
			//root.getState().getEnvironment().printEnvironment();
			root.setState(root.getState().nextState(action));
			//System.out.println("After");
			//root.getState().getEnvironment().printEnvironment();

		}

		// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) {
			// TODO: 2. run alpha-beta search to determine the best move

			this.root.getState().getEnvironment().printEnvironment();
			Action best = MiniMax(root);
			//System.out.println("Inni nextaction");
			//System.out.println(best.from.x + " " + best.from.y);
			//System.out.println(+ best.to.x + " " + best.to.y);

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

	private Action bestAction(StateNode node) {
		int maxVal = Integer.MIN_VALUE;
		Action action = null;

		for(StateNode childNode : node.getChildren()) {
			//System.out.println("Inni ForEach inni ACTIONMINIMAX");
			//System.out.println(childNode.getState().getScore());
			if(maxVal < childNode.getState().getScore()) {
				maxVal = childNode.getState().getScore();
				action = childNode.getAction();
			}
		}
		//System.out.println("Eftir FOREACH ACTIONMINIMAX");
		//System.out.println(action);
		return action;
	};

	public Action MiniMax(StateNode node)  {

		int maxVal = Integer.MIN_VALUE;
		stopwatch = new Stopwatch();
		node.getSuccessors();
		Action action = null;
		try {
			maxVal = MiniMax(node, true);
			//maxVal = AlphaBetaSearch(MaxDepth, s, -INF, INF);
		} catch (Exception ex) {
			System.out.println("Playclock exceeded!");
			action = bestAction(node);
			return action;
		}

		while (!testList.isEmpty()) {
			StateNode tmp = testList.pop();
			System.out.println("Node check");
			tmp.getState().getEnvironment();
			tmp.getState().getScore();
		}

		//System.out.println("EXCEPTION PASSED");
		//System.out.println("children length:");
		//System.out.println(node.getChildren().size());

		action = bestAction(node);
		return action;
	}

	public int MiniMax(StateNode node, boolean player) throws Exception {
		if (this.stopwatch.elapsedTime() > this.playclock) {
			throw new Exception();
		}

		if(node.getState().isTerminal()) {
			// Hérna returnum við action
			//System.out.println("Is in Terminal MINIMAX");
			//node.getState().getEnvironment().printEnvironment();
			//System.out.println("Score " + node.getState().getScore());
			//System.out.println("Result " + node.getState().getResult());

			pushTestToStack(node);

			return node.getState().getScore();
		}

		int bestValue;
		int value;
		node.getSuccessors();

		if (player) {
			bestValue = Integer.MIN_VALUE;
			for (StateNode child : node.getChildren()) {
				value = MiniMax(child, !player);
				bestValue = Math.max(value, bestValue);
				node.getState().setScore(bestValue);
			}
		} else {
			bestValue = Integer.MAX_VALUE;
			for (StateNode child : node.getChildren()) {
				value = MiniMax(child, !player); //!player
				bestValue = Math.min(value, bestValue);
				node.getState().setScore(bestValue);
			}
		}
		return bestValue;
	}

	//Pseduo-code
	public int AlphaBetaSearch(int depth, StateNode node, int alpha, int beta) {
		if(node.getState().isTerminal() || depth <= 0) {
			return node.getState().getScore(); //return evaluate(s);
		}
		int bestValue = Integer.MIN_VALUE;
		int value;
		node.getSuccessors();

		for(StateNode child : node.getChildren()) {
			value = -AlphaBetaSearch(depth - 1, child, - beta, -alpha);

			bestValue = Math.max(value, bestValue);
			if(bestValue > alpha) {
				alpha = bestValue;
				if(alpha >= beta) break;
			}
		}
		return bestValue;
	};

	public void iterateToRootTest(StateNode node) {
		if (node.getParent() == null) {
			System.out.println("=======	ROOT FOUND =====");
			return;
		}

		iterateToRootTest(node.getParent());

		System.out.println("Iterate to Root");
		System.out.println("Score: " + node.getState().getScore());
		node.getState().getEnvironment().printEnvironment();


	}

	public void pushTestToStack(StateNode node) {
		if(node.getParent() == null) {
			return;
		}

		testList.push(node);
		pushTestToStack(node);
	}
}