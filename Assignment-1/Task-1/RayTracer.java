import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Vector3D {
    double x, y, z;

    Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector3D subtract(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }
}

class Sphere {
    Vector3D center;
    double radius;
    Color color;

    Sphere(Vector3D center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    Double intersect(Vector3D origin, Vector3D direction) {
        Vector3D oc = origin.subtract(center);
        double a = direction.dot(direction);
        double b = 2.0 * oc.dot(direction);
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) return null;
        return (-b - Math.sqrt(discriminant)) / (2.0 * a);
    }
}

public class RayTracer {
    static final int WIDTH = 500;
    static final int HEIGHT = 500;
    static final Vector3D CAMERA_POS = new Vector3D(0, 0, 0);
    static final double VIEWPORT_SIZE = 1;
    static final double PROJECTION_PLANE_Z = 1;

    static List<Sphere> spheres = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        spheres.add(new Sphere(new Vector3D(0, -1, 3), 1, Color.RED));
        spheres.add(new Sphere(new Vector3D(2, 0, 4), 1, Color.GREEN));
        spheres.add(new Sphere(new Vector3D(-2, 0, 4), 1, Color.BLUE));

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Vector3D direction = canvasToViewport(x, y);
                Color color = traceRay(CAMERA_POS, direction);
                image.setRGB(x, HEIGHT - y - 1, color.getRGB());
            }
        }

        ImageIO.write(image, "png", new File("output.png"));
    }

    static Vector3D canvasToViewport(int x, int y) {
        return new Vector3D(
                (x - WIDTH / 2.0) * VIEWPORT_SIZE / WIDTH,
                (y - HEIGHT / 2.0) * VIEWPORT_SIZE / HEIGHT,
                PROJECTION_PLANE_Z
        );
    }

    static Color traceRay(Vector3D origin, Vector3D direction) {
        double closestT = Double.POSITIVE_INFINITY;
        Sphere closestSphere = null;

        for (Sphere sphere : spheres) {
            Double t = sphere.intersect(origin, direction);
            if (t != null && t < closestT) {
                closestT = t;
                closestSphere = sphere;
            }
        }

        return (closestSphere != null) ? closestSphere.color : Color.WHITE;
    }
}