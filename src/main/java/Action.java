import java.awt.*;

public class Action {
    public Point from;
    public Point to;

    private boolean capturing;

    public Action () {

    }

    public Action (Point from, Point to, boolean capturing) {
        this.from = from;
        this.to = to;
        this.capturing = capturing;
    }

    public boolean isCapturing () {
        return capturing;
    }

    @Override
    public String toString() {
        return  "(move " + this.from.x + " " + this.from.y + " " + this.to.x + " " + this.to.y + ")";
    }
}
