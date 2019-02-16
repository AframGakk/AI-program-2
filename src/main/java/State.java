import com.sun.tools.doclint.Env;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class State {

    private Environment environment;
    private Player player;
    //successorsStates;

    public State() {}

    public State(Environment environment, Player player) {
        this.environment = environment;
        this.player = player;
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

        return new State(newEnvironment, switchPlayer());
    }

    private Player switchPlayer() {
        if (this.player == Player.WHITE) {
            return Player.BLACK;
        } else {
            return Player.WHITE;
        }
    }


}
