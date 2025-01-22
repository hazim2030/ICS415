import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SimpleRayTracer {
    // Canvas properties
    static int width = 500;     // Canvas width in pixels
    static int height = 500;    // Canvas height in pixels
    static double viewSize = 1; // Viewport size
    static double projZ = 1;    // Projection plane distance
    static int bgColor = 0xFFFFFF; // Background color (white)

    // Scene with spheres
    static Ball[] objects = {
            new Ball(new Vec(0, -1, 3), 1, 0xFF0000), // Red ball
            new Ball(new Vec(2, 0, 4), 1, 0x0000FF),  // Blue ball
            new Ball(new Vec(-2, 0, 4), 1, 0x00FF00)  // Green ball
    };

    public static void main(String[] args) {
        // Create image buffer
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Render the scene
        drawScene(img);

        // Show the image in a window
        showImage(img);

        // Save the image as a file
        saveToFile(img, "output.png");
    }

    // Converts canvas (2D) to viewport (3D)
    public static Vec toViewport(int x, int y) {
        return new Vec(
                x * viewSize / width,
                y * viewSize / height,
                projZ
        );
    }

    // Casts rays and determines pixel color
    public static void drawScene(BufferedImage img) {
        System.out.println("Rendering...");
        for (int x = -width / 2; x < width / 2; x++) {
            for (int y = -height / 2; y < height / 2; y++) {
                Vec dir = toViewport(x, y); // Map pixel to ray
                Ray ray = new Ray(new Vec(0, 0, 0), dir); // Ray from camera
                int color = trace(ray, Double.POSITIVE_INFINITY); // Find color
                img.setRGB(x + width / 2, height / 2 - y - 1, color);
            }
        }
        System.out.println("Done!");
    }

    // Finds the closest intersected object
    public static int trace(Ray ray, double maxDist) {
        Ball closest = null;
        double minDist = maxDist;

        for (Ball obj : objects) {
            Hit hit = obj.hit(ray);
            if (hit != null && hit.dist < minDist) {
                minDist = hit.dist;
                closest = obj;
            }
        }

        return closest == null ? bgColor : closest.color;
    }

    // Display the image in a window
    public static void showImage(BufferedImage img) {
        JFrame frame = new JFrame("Simple Ray Tracer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, null);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    // Save image to file
    public static void saveToFile(BufferedImage img, String fileName) {
        try {
            File file = new File(fileName);
            ImageIO.write(img, "png", file);
            System.out.println("Image saved to " + fileName);
        } catch (Exception e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }
}

// 3D Vector class
class Vec {
    double x, y, z;

    Vec(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vec subtract(Vec v) {
        return new Vec(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    double dot(Vec v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    Vec scale(double factor) {
        return new Vec(this.x * factor, this.y * factor, this.z * factor);
    }
}

// Ray class
class Ray {
    Vec origin, dir;

    Ray(Vec origin, Vec dir) {
        this.origin = origin;
        this.dir = dir;
    }
}

// Sphere class (renamed to Ball)
class Ball {
    Vec center;
    double radius;
    int color;

    Ball(Vec center, double radius, int color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    Hit hit(Ray ray) {
        Vec oc = ray.origin.subtract(this.center);

        double a = ray.dir.dot(ray.dir);
        double b = 2 * oc.dot(ray.dir);
        double c = oc.dot(oc) - this.radius * this.radius;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) return null; // No intersection

        double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);

        double t = Math.min(t1, t2);
        if (t < 0) return null; // Intersection behind ray

        return new Hit(t, this);
    }
}

// Intersection class (renamed to Hit)
class Hit {
    double dist;
    Ball obj;

    Hit(double dist, Ball obj) {
        this.dist = dist;
        this.obj = obj;
    }
}
