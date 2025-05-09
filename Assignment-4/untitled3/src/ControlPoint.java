
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ControlPoint {
    private double x, y;
    public static final int SIZE = 8;

    public ControlPoint(double x, double y) { this.x = x; this.y = y; }
    public double getX() { return x; }
    public double getY() { return y; }
    public void setLocation(double x, double y) { this.x = x; this.y = y; }

    public boolean contains(double px, double py) {
        return new Ellipse2D.Double(x - SIZE/2, y - SIZE/2, SIZE, SIZE)
                .contains(px, py);
    }

    public void draw(Graphics2D g) {
        g.fill(new Ellipse2D.Double(x - SIZE/2, y - SIZE/2, SIZE, SIZE));
    }
}
