
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CurveCanvas extends JPanel {
    private final List<ControlPoint> points = new ArrayList<>();
    private final List<BezierCurve> curves = new ArrayList<>();
    private ControlPoint selected = null;
    private Point mouseOffset = new Point();

    public CurveCanvas() {
        setBackground(Color.WHITE);
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (ControlPoint p : points) {
                    if (p.contains(e.getX(), e.getY())) {
                        selected = p;
                        mouseOffset.setLocation(e.getX() - p.getX(), e.getY() - p.getY());
                        return;
                    }
                }
                // add new point
                ControlPoint np = new ControlPoint(e.getX(), e.getY());
                points.add(np);
                int n = points.size();
                if (n >= 4 && (n == 4 || (n - 1) % 3 == 0)) {
                    int start = (n == 4) ? 0 : n - 4;
                    curves.add(new BezierCurve(
                            points.get(start),
                            points.get(start + 1),
                            points.get(start + 2),
                            points.get(start + 3)
                    ));
                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selected = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (selected != null) {
                    selected.setLocation(e.getX() - mouseOffset.x, e.getY() - mouseOffset.y);
                    repaint();
                }
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D)g0;
        // draw curves
        for (BezierCurve c : curves) c.draw(g);
        // draw points on top
        g.setColor(Color.BLUE);
        for (ControlPoint p : points) p.draw(g);
    }
}
