import com.sun.tools.doclint.Env;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class State {

    private Environment environment;
    private Player player;
    private boolean currentPlayer;
    private Result result;
    //successorsStates;

    public State() {}

    public State(Environment environment, Player player) {
        this.environment = environment;
        this.player = player;
        this.result = result.UNKNOWN;
    }

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

    public State nextState(Action action) {
        HashSet<Point> newBlacks = new HashSet<Point>();
        HashSet<Point> newWhites = new HashSet<Point>();
        this.environment.getBlacks().addAll(newBlacks);
        this.environment.getWhites().addAll(newWhites);
        Environment newEnvironment = new Environment(this.environment.getBoardHeight(), this.environment.getBoardWidth(),
                newWhites, newBlacks);
        newEnvironment.movePlayer(action, this.player);
        State newState = new State(newEnvironment, this.player);
        newState.switchPlayer();
        return newState;
    }

    private void switchPlayer() {
        if (this.player == Player.WHITE) {
            this.player = Player.BLACK;
        } else {
            this.player = Player.WHITE;
        }
        this.currentPlayer = !this.currentPlayer;
    }

    //returns true if isTerminal else false
    public boolean isTerminal() {
        for(int i = 1; i < this.environment.getBoardWidth(); i++) {
            if (this.environment.getWhites().contains(new Point(i, this.environment.getBoardHeight()))) {
                if(player == player.WHITE && currentPlayer) {
                    this.result = Result.WIN;
                }
                else {
                    this.result = Result.LOSS;
                }
                return true;
            }
            if(this.environment.getBlacks().contains(new Point(i, 0))) {
                if(player == player.BLACK && currentPlayer) {
                    this.result = Result.WIN;
                }
                else {
                    this.result = Result.LOSS;
                    return true;
                }
            }
        }
        if(legalActions().isEmpty()) {
            this.result = Result.DRAW;
            return true;
        }
        return false;
    }

    //100 points if white won the game
    // 0 points for draw
    //-100 points if white lost the game
    
    public int evaluateTerminal() {
        switch (result) {
            case WIN: 
                return 100;
            case LOSS:
                return -100;
            default:
                return 0;
        }
    }
    //distance of most advanced black pawn to row 1 - distance of maost advanced white 
    //pawn to row Height, for non-terminal states
    public int evaluateNonTerminal() {
        return 0;
    }




}
