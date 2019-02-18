import java.awt.*;
import java.util.Random;
import java.util.Stack;

public class MiniMaxAgent implements Agent {
    private Random random = new Random();

    private String role; // the name of this agent's role (white or black)
    private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
    private boolean myTurn; // whether it is this agent's turn or not
    private int width, height; // dimensions of the board
    private Stopwatch stopwatch;
    private int depth;

    private State state;

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
        depth = 10;//((height -1) * 2 + (height -2) * 2) * width +1;
        // TODO: add your own initialization code here

        Player player;
        if (role.equals("white")) {
            player = Player.WHITE;
        } else {
            player = Player.BLACK;
        }

        Environment environment = new Environment(height, width);
        environment.init_env();

        this.state = new State(environment, player, true);

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
            state = state.nextState(action);
            //System.out.println("After");
            //root.getState().getEnvironment().printEnvironment();

        }

        // update turn (above that line it myTurn is still for the previous state)
        myTurn = !myTurn;
        if (myTurn) {
            // TODO: 2. run alpha-beta search to determine the best move


            Action best = MiniMax(this.state);
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

    public Action MiniMax(State state)  {

        int maxVal = Integer.MIN_VALUE;
        stopwatch = new Stopwatch();

        Action action = null;

        try {
            for (Action legalAction : state.legalActions()) {
                State successor = state.nextState(legalAction);

                if (successor.isTerminal()) {
                    return legalAction;
                }

                int value = MiniMax(successor, false, 1);
                maxVal = Math.max(maxVal, value);

                if (maxVal >= value) {
                    action = legalAction;
                }

                System.out.println("MiniMax Value: " + value);

            }
        } catch (Exception ex) {
            System.out.println("Playclock exceeded!");
            return action;
        }

        return action;
    }

    public int MiniMax(State state, boolean player, int depth) throws Exception {
        if (this.stopwatch.elapsedTime() > this.playclock) {
            throw new Exception();
        }

        if(state.isTerminal() || this.depth <= depth) {
            // Hérna returnum við action
            //System.out.println("Is in Terminal MINIMAX");
            state.getEnvironment().printEnvironment();
            System.out.println("Score " + state.getScore());
            System.out.println("Player: " + player);
            //System.out.println("Result " + node.getState().getResult());

            return state.evaluateTerminal();
        }

        int bestValue = 0;
        int value;
        state.legalActions();

        for (Action action : state.legalActions()) {
            State successor = state.nextState(action);
            if (player) {
                bestValue = Integer.MIN_VALUE;
                value = MiniMax(successor, !player, depth++);
                bestValue = Math.max(value, bestValue);
            } else {
                bestValue = Integer.MAX_VALUE;
                value = MiniMax(state, !player, depth++); //!player
                bestValue = Math.min(value, bestValue);
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
