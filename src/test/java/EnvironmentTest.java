import com.sun.webkit.dom.EntityReferenceImpl;
import org.junit.Test;

import java.awt.*;
import java.util.HashSet;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class EnvironmentTest {

    private Environment environment;

    // DATA
    private HashSet<Point> whites;
    private HashSet<Point> blacks;

    private void initWhitesAndBlacks() {
        this.whites = new HashSet<Point>();
        this.blacks = new HashSet<Point>();

        whites.add(new Point(1,1));
        whites.add(new Point(2,1));
        whites.add(new Point(3,1));
        whites.add(new Point(1,2));
        whites.add(new Point(2,2));
        whites.add(new Point(3,2));

        blacks.add(new Point(1,4));
        blacks.add(new Point(2,4));
        blacks.add(new Point(3,4));
        blacks.add(new Point(1,5));
        blacks.add(new Point(2,5));
        blacks.add(new Point(3,5));
    }

    @Test
    public void PrintMap() {

    }

    // ASSERT
    @Test
    public void InitEnvironment3x5Test() {
        this.environment = new Environment(5, 3);
        this.environment.init_env();
        initWhitesAndBlacks();

        assertTrue(this.whites.equals(environment.getWhites()));
        assertTrue(this.blacks.equals(environment.getBlacks()));
    }

}
