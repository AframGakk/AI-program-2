import java.awt.*;
import java.util.HashSet;

public class Environment {
    private int boardWidth;
    private int boardHeight;
    // 0 = empty,   1 = white,  2 = black
    //private int[][] map;

    HashSet<Point> whites;
    HashSet<Point> blacks;

    public Environment(int boardHeight, int boardWidth, HashSet<Point> whites, HashSet<Point> blacks) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        //this.map = new int[boardHeight][boardWidth];
        this.whites = whites;
        this.blacks = blacks;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public HashSet<Point> getBlacks() {
        return blacks;
    }

    public HashSet<Point> getWhites() {
        return whites;
    }

    public void setBlacks(HashSet<Point> blacks) {
        this.blacks = blacks;
    }

    public void setWhites(HashSet<Point> whites) {
        this.whites = whites;
    }

    // TODO: move black in environment
    public void moveBlack() {

    }

    // TODO: move white in environment
    public void moveWhite() {

    }
}
