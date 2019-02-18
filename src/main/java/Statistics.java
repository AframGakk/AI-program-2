// TODO: Statistics
/**
 * State expansions
 * current depth limit iterative deepening
 * runtime of search for each iteration
 * */

public class Statistics {
    private int expansions;
    private int currentDepth;
    private Stopwatch stopwatch;

    public Statistics() {
        this.stopwatch = new Stopwatch();
    }

    public void setCurrentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public void incrExpansion() {
        this.expansions++;
    }

    public void printStats() {
        System.out.println("===== Statistics =====");
        System.out.println("Expansions: " + this.expansions);
        System.out.println("Current Depth: " + this.currentDepth);
        System.out.println("Runtime: " + this.stopwatch.elapsedTime() + " s");
        System.out.println("======================");
    }


}
