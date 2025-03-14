import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

class Vector3D {
    double x, y, z;

    Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector3D add(Vector3D v) {
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }

    Vector3D subtract(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    Vector3D multiply(double scalar) {
        return new Vector3D(x * scalar, y * scalar, z * scalar);
    }

    Vector3D normalize() {
        double length = Math.sqrt(x*x + y*y + z*z);
        return new Vector3D(x / length, y / length, z / length);
    }

    double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    Vector3D reflect(Vector3D normal) {
        return subtract(normal.multiply(2 * this.dot(normal)));
    }
}

class Sphere {
    Vector3D center;
    double radius;
    Color color;
    double specular;
    double reflective;
    double transparency;

    Sphere(Vector3D center, double radius, Color color, double specular, double reflective, double transparency) {
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.specular = specular;
        this.reflective = reflective;
        this.transparency = transparency;
    }

    Double intersect(Vector3D origin, Vector3D direction) {
        Vector3D oc = origin.subtract(center);
        double a = direction.dot(direction);
        double b = 2.0 * oc.dot(direction);
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) return null;
        double t1 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
        double t2 = (-b + Math.sqrt(discriminant)) / (2.0 * a);

        if (t1 > 0) return t1;
        if (t2 > 0) return t2;
        return null;
    }
}

public class RayTracer {
    static final int WIDTH = 500;
    static final int HEIGHT = 500;
    static final Vector3D CAMERA_POS = new Vector3D(0, 0, 0);
    static final double VIEWPORT_SIZE = 1;
    static final double PROJECTION_PLANE_Z = 1;

    static List<Sphere> spheres = new ArrayList<>();

    static Vector3D light = new Vector3D(5, 5, -10);

    public static void main(String[] args) throws IOException {
        spheres.add(new Sphere(new Vector3D(0, -1, 3), 1, Color.RED, 500, 0.2, 0));
        spheres.add(new Sphere(new Vector3D(2, 0, 4), 1, Color.GREEN, 500, 0.3, 0.1));
        spheres.add(new Sphere(new Vector3D(-2, 0, 4), 1, Color.BLUE, 10, 0.4, 0.2));

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Vector3D direction = canvasToViewport(x, y);
                Color color = traceRay(CAMERA_POS, direction, 1, Double.POSITIVE_INFINITY, 3);
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

    static Color traceRay(Vector3D origin, Vector3D direction, double t_min, double t_max, int depth) {
        double closestT = Double.POSITIVE_INFINITY;
        Sphere closestSphere = null;

        for (Sphere sphere : spheres) {
            Double t = sphere.intersect(origin, direction);
            if (t != null && t < closestT && t >= t_min && t <= t_max) {
                closestT = t;
                closestSphere = sphere;
            }
        }

        if (closestSphere == null) return Color.WHITE;

        Vector3D point = origin.add(direction.multiply(closestT));
        Vector3D normal = point.subtract(closestSphere.center).normalize();
        Vector3D view = direction.multiply(-1);

        double lighting = computeLighting(point, normal, view, closestSphere.specular);

        Color localColor = multiplyColor(closestSphere.color, lighting);

        if (depth <= 0 || closestSphere.reflective <= 0 && closestSphere.transparency <= 0) return localColor;

        Vector3D reflected = direction.reflect(normal);
        Color reflectedColor = traceRay(point, reflected, 0.001, Double.POSITIVE_INFINITY, depth - 1);

        localColor = blendColors(localColor, reflectedColor, closestSphere.reflective);

        return localColor;
    }

    static double computeLighting(Vector3D point, Vector3D normal, Vector3D view, double specular) {
        Vector3D lightDir = light.subtract(point).normalize();
        double diff = Math.max(0, normal.dot(lightDir));
        double spec = 0;

        if (specular >= 0) {
            Vector3D reflect = lightDir.reflect(normal);
            spec = Math.pow(Math.max(reflect.dot(view), 0), specular);
        }

        return Math.min(diff + spec, 1);
    }

    static Color multiplyColor(Color c, double factor) {
        return new Color((int)(c.getRed()*factor), (int)(c.getGreen()*factor), (int)(c.getBlue()*factor));
    }

    static Color blendColors(Color c1, Color c2, double reflectivity) {
        double r = c1.getRed()*(1-reflectivity) + c2.getRed()*reflectivity;
        double g = c1.getGreen()*(1-reflectivity) + c2.getGreen()*reflectivity;
        double b = c1.getBlue()*(1-reflectivity) + c2.getBlue()*reflectivity;
        return new Color((int)r, (int)g, (int)b);
    }
}