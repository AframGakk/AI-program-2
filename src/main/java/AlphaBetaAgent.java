import java.awt.*;
import java.util.Random;
import java.util.Stack;

public class AlphaBetaAgent implements Agent {
    private Random random = new Random();

    private String role; // the name of this agent's role (white or black)
    private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
    private boolean myTurn; // whether it is this agent's turn or not
    private int width, height; // dimensions of the board
    private Stopwatch stopwatch;
    private int maxDepth;
    private Statistics stats;
    private State state;

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
        maxDepth = depthEstimation(height, width);
        this.stats = new Statistics();

        Player player;
        if (role.equals("white")) {
            player = Player.WHITE;
        } else {
            player = Player.BLACK;
        }

        Environment environment = new Environment(height, width);
        environment.init_env();

        this.state = new State(environment, player, true);

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

            if (roleOfLastPlayer == "white") {
                state.setPlayer(Player.WHITE);
            } else {
                state.setPlayer(Player.BLACK);
            }

            Action action;

            if(x1 != x2) {
                // capturing
                action = new Action(new Point(x1, y1), new Point(x2, y2), true);

            } else {
                // not capturing
                action = new Action(new Point(x1, y1), new Point(x2, y2), false);
            }

            state = state.nextState(action);
        }

        // update turn (above that line it myTurn is still for the previous state)
        myTurn = !myTurn;
        if (myTurn) {

            stats = new Statistics();
            Action best = rootSearch(state, Integer.MIN_VALUE, Integer.MAX_VALUE);

            //stats.printStats();
            return "(move " + best.from.x + " " + best.from.y + " " + best.to.x + " " + best.to.y + ")";
        } else {
            return "noop";
        }
    }

    // is called when the game is over or the match is aborted
    //@Override
    public void cleanup() {

    }


    /**
     * The alpha beta search made for the root node only. This includes the iterative deepening loop.
     *
     * @Param state the root state or beginning state of the move
     * @Param alpha the max value for pruning
     * @Param beta the min value for pruning
     * */
    public Action rootSearch(State state, int alpha, int beta) {

        int maxVal = Integer.MIN_VALUE;
        stopwatch = new Stopwatch();

        Action action = null;

        try {
            for(int depth = 1; depth <= this.maxDepth; depth++) {

                stats.setCurrentDepth(depth);

                for(Action childAction : state.legalActions()) {
                    State successor = state.nextState(childAction);

                    int value = -AlphaBetaSearch(depth, successor, -alpha, -beta);

                    maxVal = Math.max(maxVal, value);

                    if(maxVal > alpha) {
                        alpha = maxVal;
                        action = childAction;
                    }
                }
            }
        } catch (TimeException ex) {
            System.out.println(ex.getMessage());
            return action;
        }
        return action;
    }

    /**
     * The recursive function for alpha beta search. Every expanded node goes through this function and
     * returns an integer
     *
     * @Param depth how deep in the tree recursion is the algorithm
     * @Param state the state being checked for terminal or child states
     * @Param alpha the maximum value for pruning
     * @Param beta the minimum value for pruning
     * */
    public int AlphaBetaSearch(int depth, State state, int alpha, int beta) throws TimeException {

        stats.incrExpansion();

        if (this.stopwatch.elapsedTime() > this.playclock) {
            throw new TimeException("Playclock Exceeded!");
        }

        if(state.isTerminal() || depth >= this.maxDepth ) {
            this.maxDepth = depth;
            return state.evaluateTerminal(); //return evaluate(s);
        }
        int maxValue = Integer.MIN_VALUE;
        int value;

        for (Action action : state.legalActions()) {
            State successor = state.nextState(action);

            value = -AlphaBetaSearch(depth + 1, successor, - beta, -alpha);

            maxValue = Math.max(value, maxValue);
            if(maxValue > alpha) {
                alpha = maxValue;
                if(alpha >= beta) break;
            }
        }

        return maxValue;
    }

    /**
     * A function to estimate the state space size at initialitation.
     * */
    private int depthEstimation(int boardHeight, int boardWidth) {
        return (((boardHeight - 1) + (boardHeight - 2))*2) * boardWidth + 1;
    }
}
