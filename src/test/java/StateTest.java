import org.junit.Test;

import java.awt.*;
import java.util.HashSet;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class StateTest {

    private State state;

    // DATA

    // state at draw. Height = 5, width = 3
    private void terminalDrawState1() {
        HashSet<Point> blacks = new HashSet<Point>();
        HashSet<Point> whites = new HashSet<Point>();
        blacks.add(new Point(2,3));
        whites.add(new Point(2,2));

        this.state = new State(new Environment(5, 3, whites, blacks), Player.WHITE);
    }

    // state at draw no blacks. height = 5, width = 3
    private void terminalDrawState2() {
        HashSet<Point> blacks = new HashSet<Point>();
        HashSet<Point> whites = new HashSet<Point>();
        whites.add(new Point(2,4));

        this.state = new State(new Environment(5, 3, whites, blacks), Player.BLACK);
    }

    private void terminalWinState1() {
        HashSet<Point> blacks = new HashSet<Point>();
        HashSet<Point> whites = new HashSet<Point>();
        whites.add(new Point(1,5));
        whites.add(new Point(3,3));
        blacks.add(new Point(3,4));

        this.state = new State(new Environment(5, 3, whites, blacks), Player.WHITE);
        this.state.setCurrentPlayer(true);
    }

    @Test
    public void drawTest1() {
        terminalDrawState1();

        assertTrue(this.state.legalActions().isEmpty());

        assertTrue(this.state.isTerminal());

        assertEquals(this.state.getResult(), Result.DRAW);

        assertEquals(this.state.getScore(), 0);
    }

    @Test
    public void drawTest2() {
        terminalDrawState2();

        assertTrue(this.state.legalActions().isEmpty());

        assertTrue(this.state.isTerminal());

        assertEquals(this.state.getResult(), Result.DRAW);

        assertEquals(this.state.getScore(), 0);
    }

    @Test
    public void whiteShouldWin() {
        terminalWinState1();

        assertTrue(this.state.isTerminal());

        assertEquals(this.state.getResult(), Result.WIN);

        assertEquals(this.state.getScore(), 100);
    }



}
