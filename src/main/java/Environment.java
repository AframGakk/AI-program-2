import java.awt.*;
import java.util.HashSet;

public class Environment {
    private int boardWidth;
    private int boardHeight;

    private HashSet<Point> whites;
    private HashSet<Point> blacks;

    public Environment(int boardHeight, int boardWidth) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.whites = new HashSet<Point>();
        this.blacks = new HashSet<Point>();
    }

    public Environment(int boardHeight, int boardWidth, HashSet<Point> whites, HashSet<Point> blacks) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        //this.map = new int[boardHeight][boardWidth];
        this.whites = whites;
        this.blacks = blacks;
    }

    public void init_env() {

        //init for white pawns position
        for(int y = 1; y <= 2; y++) {
            for(int x = 1; x <= boardWidth; x++) {
                whites.add(new Point(x,y));
            }
        }
        //init for blacks pawns position
        for(int y = boardHeight - 1; y <= boardHeight; y++) {
            for(int x = 1; x <= boardWidth; x++) {
                blacks.add(new Point(x,y));
            }
        }
    }



    public boolean canCaptureLeft(Point pawn, Player player) {
        if(player == Player.WHITE) {
            return blacks.contains(new Point(pawn.x + 1, pawn.y + 1));
        } else {
            return whites.contains(new Point(pawn.x - 1, pawn.y - 1));
        }
    }

    public boolean canCaptureRight(Point pawn, Player player) {
        if(player == Player.WHITE) {
            return blacks.contains(new Point(pawn.x - 1, pawn.y + 1));
        } else {
            return whites.contains(new Point(pawn.x + 1, pawn.y - 1));
        }
    }

    public boolean canGoForward(Point pawn, Player player) {
        if(player == Player.WHITE) {
            return !(blacks.contains(new Point(pawn.x, pawn.y + 1)) || pawn.y == this.boardHeight);
        } else {
            return !(whites.contains(new Point(pawn.x, pawn.y - 1)) || pawn.y == 0);
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


    public void movePlayer(Action action, Player player) {
        if (player == Player.WHITE) {
            if(whites.contains(action.from)) {
                if(action.isCapturing()) {
                    blacks.remove(action.to);
                }
                whites.remove(action.from);
                whites.add(action.to);
            }
            else {
                System.out.println("ERROR");
                System.out.println("Im inside ENV moveWhite");
            }
        } else {
            if(blacks.contains(action.from)) {
                if(action.isCapturing()) {
                    whites.remove(action.to);
                }
                blacks.remove(action.from);
                blacks.add(action.to);
            }
            else {
                System.out.println("ERROR");
                System.out.println("Im inside ENV moveBlack");
            }
        }
    }

    public void printEnvironment() {
        System.out.println("========    Environment   ========");
        for(int y = 1; y <= this.boardHeight; y++) {
            for(int x = 1; x <= this.boardWidth; x++) {
                if(whites.contains(new Point(x, y))) {
                    System.out.print("W");
                } else if (blacks.contains(new Point(x, y))) {
                    System.out.print("B");
                } else {
                    System.out.print("O");
                }
            }
            System.out.println();
        }
    }
}
