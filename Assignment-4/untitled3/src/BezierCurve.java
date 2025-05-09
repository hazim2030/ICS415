

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BezierCurve {
    private final ControlPoint[] pts = new ControlPoint[4];

    public BezierCurve(ControlPoint p0, ControlPoint p1, ControlPoint p2, ControlPoint p3) {
        pts[0] = p0; pts[1] = p1; pts[2] = p2; pts[3] = p3;
    }

    public void draw(Graphics2D g) {
        // draw control polygon
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 3; i++)
            g.drawLine((int)pts[i].getX(), (int)pts[i].getY(),
                    (int)pts[i+1].getX(), (int)pts[i+1].getY());

        // draw curve
        g.setColor(Color.RED);
        List<Point> curve = computeCurve(200);
        for (int i = 0; i < curve.size()-1; i++) {
            Point a = curve.get(i), b = curve.get(i+1);
            g.drawLine(a.x, a.y, b.x, b.y);
        }
    }

    private List<Point> computeCurve(int steps) {
        List<Point> ptsList = new ArrayList<>(steps+1);
        for (int i = 0; i <= steps; i++) {
            double t = i/(double)steps;
            double x = deCasteljau(0, t), y = deCasteljau(1, t);
            ptsList.add(new Point((int)x, (int)y));
        }
        return ptsList;
    }

    private double deCasteljau(int coord, double t) {
        // coord=0 ⇒ x; coord=1 ⇒ y
        double[] v = new double[4];
        for (int i = 0; i < 4; i++)
            v[i] = coord==0 ? pts[i].getX() : pts[i].getY();
        for (int r = 1; r < 4; r++) {
            for (int i = 0; i < 4 - r; i++)
                v[i] = (1 - t)*v[i] + t*v[i+1];
        }
        return v[0];
    }
}
