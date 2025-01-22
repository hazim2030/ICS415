import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class RayTracerWithLighting {
    // Canvas settings
    static int imgWidth = 500;      // Image width in pixels
    static int imgHeight = 500;     // Image height in pixels
    static double viewSize = 1.0;   // Viewport size
    static double projPlaneDist = 1.0; // Distance to projection plane
    static int skyColor = 0xFFFFFF; // Background color (white)

    // Light source
    static LightSource light = new LightSource(new Vector3D(5, 5, -10), 1.5);

    // Scene objects
    static SphereObject[] sceneObjects = {
            new SphereObject(new Vector3D(0, -1, 3), 1, 0xFF0000), // Red sphere
            new SphereObject(new Vector3D(2, 0, 4), 1, 0x0000FF),  // Blue sphere
            new SphereObject(new Vector3D(-2, 0, 4), 1, 0x00FF00), // Green sphere
            new SphereObject(new Vector3D(0, -5001, 0), 5000, 0xFFFF00) // Ground
    };

    public static void main(String[] args) {
        BufferedImage outputImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        // Render the image
        render(outputImage);

        // Display the image in a window
        displayImage(outputImage);

        // Save the rendered image to a file
        saveImage(outputImage, "output_with_lighting.png");
    }

    // Converts 2D canvas coordinates to 3D viewport coordinates
    public static Vector3D mapToViewport(int x, int y) {
        return new Vector3D(
                x * viewSize / imgWidth,
                y * viewSize / imgHeight,
                projPlaneDist
        );
    }

    // Renders the scene onto the image
    public static void render(BufferedImage image) {
        for (int x = -imgWidth / 2; x < imgWidth / 2; x++) {
            for (int y = -imgHeight / 2; y < imgHeight / 2; y++) {
                Vector3D direction = mapToViewport(x, y);
                Ray3D ray = new Ray3D(new Vector3D(0, 0, 0), direction);
                int color = findColor(ray, Double.POSITIVE_INFINITY);
                image.setRGB(x + imgWidth / 2, imgHeight / 2 - y - 1, color);
            }
        }
    }

    // Determines the closest intersected object's color with lighting
    public static int findColor(Ray3D ray, double maxDistance) {
        SphereObject closestObj = null;
        double closestDistance = maxDistance;

        for (SphereObject obj : sceneObjects) {
            Intersection hit = obj.checkIntersection(ray);
            if (hit != null && hit.distance < closestDistance) {
                closestDistance = hit.distance;
                closestObj = obj;
            }
        }

        if (closestObj == null) {
            return skyColor; // No object hit, return background color
        }

        Vector3D intersectionPoint = ray.origin.add(ray.direction.scale(closestDistance));
        Vector3D normal = intersectionPoint.subtract(closestObj.center).normalize();

        return applyLighting(intersectionPoint, normal, closestObj.color);
    }

    // Applies lighting to the object color at the intersection point
    public static int applyLighting(Vector3D point, Vector3D normal, int objectColor) {
        Vector3D lightDir = light.position.subtract(point).normalize();

        // Diffuse lighting
        double diffuseIntensity = Math.max(0, normal.dot(lightDir));

        // Specular lighting
        Vector3D viewDir = new Vector3D(0, 0, 0).subtract(point).normalize();
        Vector3D reflectDir = lightDir.reflect(normal).normalize();
        double specularIntensity = Math.pow(Math.max(0, viewDir.dot(reflectDir)), 32);

        double intensity = light.intensity * (diffuseIntensity + specularIntensity);

        return adjustColorBrightness(objectColor, intensity);
    }

    // Adjusts color brightness based on lighting intensity
    public static int adjustColorBrightness(int color, double intensity) {
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) * intensity));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) * intensity));
        int b = Math.min(255, (int) ((color & 0xFF) * intensity));

        return (r << 16) | (g << 8) | b;
    }

    // Displays the image in a window
    public static void displayImage(BufferedImage image) {
        JFrame frame = new JFrame("Ray Tracer with Lighting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        panel.setPreferredSize(new Dimension(imgWidth, imgHeight));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    // Saves the image to a file
    public static void saveImage(BufferedImage image, String fileName) {
        try {
            File outputFile = new File(fileName);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image saved to " + fileName);
        } catch (Exception e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }
}

// 3D Vector class
class Vector3D {
    double x, y, z;

    Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector3D subtract(Vector3D other) {
        return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    Vector3D add(Vector3D other) {
        return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    Vector3D scale(double factor) {
        return new Vector3D(this.x * factor, this.y * factor, this.z * factor);
    }

    double dot(Vector3D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    Vector3D normalize() {
        double mag = this.magnitude();
        return new Vector3D(this.x / mag, this.y / mag, this.z / mag);
    }

    Vector3D reflect(Vector3D normal) {
        return this.subtract(normal.scale(2 * this.dot(normal)));
    }
}

// Light source class
class LightSource {
    Vector3D position;
    double intensity;

    LightSource(Vector3D position, double intensity) {
        this.position = position;
        this.intensity = intensity;
    }
}

// Ray class
class Ray3D {
    Vector3D origin, direction;

    Ray3D(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction;
    }
}

// Sphere class
class SphereObject {
    Vector3D center;
    double radius;
    int color;

    SphereObject(Vector3D center, double radius, int color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    Intersection checkIntersection(Ray3D ray) {
        Vector3D oc = ray.origin.subtract(this.center);

        double a = ray.direction.dot(ray.direction);
        double b = 2 * oc.dot(ray.direction);
        double c = oc.dot(oc) - this.radius * this.radius;

        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) return null;

        double t1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b - Math.sqrt(discriminant)) / (2 * a);

        double t = Math.min(t1, t2);
        if (t < 0) return null;

        return new Intersection(t, this);
    }
}

// Intersection class
class Intersection {
    double distance;
    SphereObject object;

    Intersection(double distance, SphereObject object) {
        this.distance = distance;
        this.object = object;
    }
}

