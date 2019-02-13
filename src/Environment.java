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

    public void init_env(int boardHeight, int boardWidth) {
        //init for white pawns position
        for(int y = 0; y < 2; y++) {
            for(int x = 0; x < boardWidth; x++) {
                whites.add(new Point(x,y));
            }
        }
        //init for blacks pawns position
        for(int y = boardHeight - 2; y < boardHeight ; y++) {
            for(int x = 0; x < boardWidth; x++) {
                blacks.add(new Point(x,y));
            }
        }
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

    // move black in environment
    public void moveBlack(Action action) {
		if(blacks.contains(action.from)) {
            blacks.remove(action.from);
            blacks.add(action.to);
        }
        else {
            System.out.println("ERROR");
            System.out.println("Im inside ENV moveBlack");
        }
    }

    // move white in environment
    public void moveWhite(Action action) {
        if(whites.contains(action.from)) {
            whites.remove(action.from);
            whites.add(action.to);
        }
        else {
            System.out.println("ERROR");
            System.out.println("Im inside ENV moveWhite");
        }
    }
}
