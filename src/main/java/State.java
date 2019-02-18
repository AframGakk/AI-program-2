
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * The state holds all information given at each players move. It holds all valueble information about the game.
 * Main info invludes: environment, the player, results and score
 * */
public class State {

    private Environment environment;
    private Player player;
    private boolean currentPlayer;
    private Result result;
    private int score;

    public State() {}

    public State(Environment environment, Player player) {
        this.environment = environment;
        this.player = player;
        this.result = result.UNKNOWN;
    }

    public State(Environment environment, Player player, boolean currentPlayer) {
        this.environment = environment;
        this.player = player;
        this.result = result.UNKNOWN;
        this.currentPlayer = currentPlayer;
    }

    public Result getResult() {
        return result;
    }

    public int getScore() {
        return score;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Environment getEnvironment() {
        return environment;
    }


    /**
     * Finds out all legal actions available comparing to the environment.
     *
     * @Return a list of actions
     * */
    public List<Action> legalActions() {
        List<Action> retList = new ArrayList<Action>();
        HashSet<Point> pawns;

        if (this.player == Player.WHITE) {
            pawns = environment.getWhites();

            for (Point pawn : pawns) {
                if(environment.canCaptureLeft(pawn, this.player)) {
                    retList.add(new Action(pawn, new Point(pawn.x + 1, pawn.y + 1), true));
                }

                if(environment.canCaptureRight(pawn, this.player)) {
                    retList.add(new Action(pawn, new Point(pawn.x - 1, pawn.y + 1), true));
                }

                if (environment.canGoForward(pawn, player)) {
                    retList.add(new Action(pawn, new Point(pawn.x, pawn.y + 1), false));
                }
            }

        } else {
            pawns = environment.getBlacks();

            for (Point pawn : pawns) {
                if(environment.canCaptureLeft(pawn, this.player)) {
                    retList.add(new Action(pawn, new Point(pawn.x - 1, pawn.y - 1), true));
                }

                if(environment.canCaptureRight(pawn, this.player)) {
                    retList.add(new Action(pawn, new Point(pawn.x + 1, pawn.y - 1), true));
                }

                if (environment.canGoForward(pawn, player)) {
                    retList.add(new Action(pawn, new Point(pawn.x, pawn.y - 1), false));
                }
            }
        }

        return retList;
    }

    /**
     * A function which returns the next state with environment and players switch by a specific action.
     *
     * @Param action an action class with coordinates from and to.
     * @Return a changed state comparing to the action made
     * */
    public State nextState(Action action) {
        HashSet<Point> newBlacks = new HashSet<Point>();
        HashSet<Point> newWhites = new HashSet<Point>();
        newBlacks.addAll(this.environment.getBlacks());
        newWhites.addAll(this.environment.getWhites());
        Environment newEnvironment = new Environment(this.environment.getBoardHeight(), this.environment.getBoardWidth(),
                newWhites, newBlacks);
        newEnvironment.movePlayer(action, this.player);
        State newState = new State(newEnvironment, this.player);
        newState.switchPlayer();
        return newState;
    }

    /**
     * switches this states player around
     * */
    private void switchPlayer() {
        if (this.player == Player.WHITE) {
            this.player = Player.BLACK;
        } else {
            this.player = Player.WHITE;
        }
        this.currentPlayer = !this.currentPlayer;
    }

    /**
     * The terminal function checks whether the state is a terminal state and what result we have.
     * */
    public boolean isTerminal() {
        if (this.goalState()) {
            this.result = Result.WIN;
            return true;
        }
        if (legalActions().isEmpty() || this.environment.getBlacks().isEmpty() || this.environment.getWhites().isEmpty()) {
            this.result = Result.DRAW;
            return true;
        }
        if (player == player.WHITE) {
            for (int i = 1; i < this.environment.getBoardWidth(); i++) {
                if (this.environment.getBlacks().contains(new Point(i, 1))) {
                    this.result = Result.LOSS;
                    return true;
                }
            }
        }
        if (player == player.BLACK) {
            for (int i = 1; i < this.environment.getBoardWidth(); i++) {
                if (this.environment.getWhites().contains(new Point(i, this.environment.getBoardHeight()))) {
                    this.result = Result.LOSS;
                    return true;
                }
            }

        }
        return false;
    }

    //100 points if white won the game
    // 0 points for draw
    //-100 points if white lost the game


    /**
     * The result evaluation function. This gets called when the state is a terminal state.
     * 100 - nonTerminalEvaluation points if white won the game
     * 0 - nonTerminalEvaluation points for draw
     * -100 - nonTerminalEvaluation points if white lost the game
     * */
    public int evaluateTerminal() {
        int extraEval = evaluateNonTerminal();

        switch (result) {
            case WIN: 
                score = 100 - extraEval;
                return 100 - extraEval;
            case LOSS:
                score = -100 - extraEval;
                return -100 - extraEval;
            default:
                score = 0 - extraEval;
                return 0 - extraEval;
        }
    }

    /**
     * The nonTerminal evaluation function is the extra heuristic function and evaluates the SUM of all the players pawn
     * distance to goal subtracted by the SUM of all the enemies pawns distance to goal.
     * */
    public int evaluateNonTerminal() {
        int minBlack = 0;
        int minWhite = 0;
        for (Point point : this.environment.getWhites()) {
            minWhite += environment.getBoardHeight() - point.y;
        }
        for (Point point : this.environment.getBlacks()) {
            minBlack += point.y - 1;
        }

        if(currentPlayer) {
            if(player == player.WHITE) {
                return minWhite - minBlack;
            } else {
                return minBlack - minWhite;
            }
        } else {
            if(player == player.WHITE) {
                return minBlack - minWhite;
            } else {
                return minWhite - minBlack;
            }
        }
    }


    /**
     * A terminal state helper function to check if we are winners
     * */
    public boolean goalState() {
        if(this.player == player.WHITE) {
            for(int i = 1; i < this.environment.getBoardWidth(); i++) {
                if (this.environment.getWhites().contains(new Point(i, this.environment.getBoardHeight()))) {
                    return true;
                }
            }
            return false;
        }
        else {
            for(int i = 1; i < this.environment.getBoardWidth(); i++) {
                if(this.environment.getBlacks().contains(new Point(i, 1))) {
                    return true;
                }
            }
            return false;
        }
    }



}
