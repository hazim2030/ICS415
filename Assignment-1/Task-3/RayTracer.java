import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RayTracer {
    public static void main(String[] args) throws IOException {
        // Image settings (feel free to adjust these for higher quality)
        final double aspectRatio = 16.0 / 9.0;
        final int imageWidth = 800;
        final int imageHeight = (int) (imageWidth / aspectRatio);
        final int samplesPerPixel = 200; // Increase for smoother (but slower) images.
        final int maxDepth = 50;         // Maximum ray bounce depth

        // World (scene) setup using a HittableList and a materials system
        HittableList world = new HittableList();

        // Ground: a huge sphere with a gray Lambertian material.
        Material groundMaterial = new Lambertian(new Vec3(0.5, 0.5, 0.5));
        world.add(new Sphere(new Vec3(0, -1000, 0), 1000, groundMaterial));

        // Randomly add many small spheres.
        Random rand = new Random();
        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double chooseMat = rand.nextDouble();
                Vec3 center = new Vec3(a + 0.9 * rand.nextDouble(), 0.2, b + 0.9 * rand.nextDouble());
                // Avoid placing spheres too close to the large spheres.
                if (center.sub(new Vec3(4, 0.2, 0)).length() > 0.9) {
                    Material sphereMaterial;
                    if (chooseMat < 0.1) {
                        // About 10% of the small spheres become mirror (metal) surfaces.
                        Vec3 albedo = new Vec3(0.5 * (1 + rand.nextDouble()),
                                0.5 * (1 + rand.nextDouble()),
                                0.5 * (1 + rand.nextDouble()));
                        double fuzz = 0.0; // Zero fuzz gives a nearly perfect mirror.
                        sphereMaterial = new Metal(albedo, fuzz);
                    } else {
                        // The rest are diffuse (Lambertian) with a random color.
                        Vec3 albedo = new Vec3(rand.nextDouble() * rand.nextDouble(),
                                rand.nextDouble() * rand.nextDouble(),
                                rand.nextDouble() * rand.nextDouble());
                        sphereMaterial = new Lambertian(albedo);
                    }
                    world.add(new Sphere(center, 0.2, sphereMaterial));
                }
            }
        }

        // Three large spheres in the center:
        // Center sphere: diffuse.
        Material centerMaterial = new Lambertian(new Vec3(0.4, 0.2, 0.1));
        world.add(new Sphere(new Vec3(0, 1, 0), 1.0, centerMaterial));
        // Left sphere: high-quality mirror.
        Material leftMaterial = new Metal(new Vec3(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vec3(-4, 1, 0), 1.0, leftMaterial));
        // Right sphere: high-quality mirror.
        Material rightMaterial = new Metal(new Vec3(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vec3(4, 1, 0), 1.0, rightMaterial));

        // Camera settings with depth of field.
        Vec3 lookFrom = new Vec3(13, 2, 3);
        Vec3 lookAt = new Vec3(0, 0, 0);
        Vec3 vup = new Vec3(0, 1, 0);
        double distToFocus = 10.0;
        double aperture = 0.1;
        Camera cam = new Camera(lookFrom, lookAt, vup, 20, aspectRatio, aperture, distToFocus);

        // Open output file.
        PrintWriter out = new PrintWriter(new FileWriter("output.ppm"));
        out.println("P3");
        out.println(imageWidth + " " + imageHeight);
        out.println("255");

        // Render loop: for each pixel, take many samples and average them.
        for (int j = imageHeight - 1; j >= 0; j--) {
            System.out.println("Scanlines remaining: " + j);
            for (int i = 0; i < imageWidth; i++) {
                Vec3 pixelColor = new Vec3(0, 0, 0);
                for (int s = 0; s < samplesPerPixel; s++) {
                    double u = (i + randomDouble()) / (imageWidth - 1);
                    double v = (j + randomDouble()) / (imageHeight - 1);
                    Ray r = cam.getRay(u, v);
                    pixelColor = pixelColor.add(rayColor(r, world, maxDepth));
                }
                writeColor(out, pixelColor, samplesPerPixel);
            }
        }
        out.close();
        System.out.println("Done.");
    }

    // Write the color for one pixel, applying gamma correction (gamma = 2).
    static void writeColor(PrintWriter out, Vec3 pixelColor, int samplesPerPixel) {
        double scale = 1.0 / samplesPerPixel;
        double r = pixelColor.x * scale;
        double g = pixelColor.y * scale;
        double b = pixelColor.z * scale;

        // Gamma correction.
        r = Math.sqrt(r);
        g = Math.sqrt(g);
        b = Math.sqrt(b);

        int ir = (int) (256 * clamp(r, 0.0, 0.999));
        int ig = (int) (256 * clamp(g, 0.0, 0.999));
        int ib = (int) (256 * clamp(b, 0.0, 0.999));
        out.println(ir + " " + ig + " " + ib);
    }

    static double clamp(double x, double min, double max) {
        if (x < min)
            return min;
        if (x > max)
            return max;
        return x;
    }

    // Return a random double in [0, 1).
    static double randomDouble() {
        return Math.random();
    }

    // Return a random double in [min, max).
    static double randomDouble(double min, double max) {
        return min + (max - min) * Math.random();
    }

    // Recursive function that returns the color seen along a given ray.
    static Vec3 rayColor(Ray r, Hittable world, int depth) {
        if (depth <= 0)
            return new Vec3(0, 0, 0);

        HitRecord rec = world.hit(r, 0.001, Double.POSITIVE_INFINITY);
        if (rec != null) {
            ScatterRecord srec = new ScatterRecord();
            if (rec.material.scatter(r, rec, srec)) {
                return srec.attenuation.mul(rayColor(srec.scattered, world, depth - 1));
            }
            return new Vec3(0, 0, 0);
        }

        // Background: a light blue gradient sky.
        Vec3 unitDirection = r.direction.normalize();
        double t = 0.5 * (unitDirection.y + 1.0);
        return new Vec3(1.0, 1.0, 1.0).mul(1.0 - t).add(new Vec3(0.5, 0.7, 1.0).mul(t));
    }

    // Return a random point inside a unit sphere.
    static Vec3 randomInUnitSphere() {
        while (true) {
            Vec3 p = new Vec3(randomDouble(-1, 1), randomDouble(-1, 1), randomDouble(-1, 1));
            if (p.lengthSquared() >= 1)
                continue;
            return p;
        }
    }

    // Return a random unit vector.
    static Vec3 randomUnitVector() {
        return randomInUnitSphere().normalize();
    }

    // Return a random point inside a unit disk (for depth-of-field).
    static Vec3 randomInUnitDisk() {
        while (true) {
            Vec3 p = new Vec3(randomDouble(-1, 1), randomDouble(-1, 1), 0);
            if (p.lengthSquared() >= 1)
                continue;
            return p;
        }
    }

    // Reflect vector v about the normal n.
    static Vec3 reflect(Vec3 v, Vec3 n) {
        return v.sub(n.mul(2 * v.dot(n)));
    }

    // -------------------------------------------------
    // Basic 3D vector class.
    static class Vec3 {
        double x, y, z;

        public Vec3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vec3 add(Vec3 v) {
            return new Vec3(x + v.x, y + v.y, z + v.z);
        }

        public Vec3 sub(Vec3 v) {
            return new Vec3(x - v.x, y - v.y, z - v.z);
        }

        public Vec3 mul(double t) {
            return new Vec3(x * t, y * t, z * t);
        }

        // Component-wise multiplication.
        public Vec3 mul(Vec3 v) {
            return new Vec3(x * v.x, y * v.y, z * v.z);
        }

        public Vec3 div(double t) {
            return mul(1 / t);
        }

        public double dot(Vec3 v) {
            return x * v.x + y * v.y + z * v.z;
        }

        public Vec3 cross(Vec3 v) {
            return new Vec3(
                    y * v.z - z * v.y,
                    z * v.x - x * v.z,
                    x * v.y - y * v.x);
        }

        public double length() {
            return Math.sqrt(x * x + y * y + z * z);
        }

        public double lengthSquared() {
            return x * x + y * y + z * z;
        }

        public Vec3 normalize() {
            return this.div(length());
        }
    }

    // -------------------------------------------------
    // Ray: defined by an origin and a direction.
    static class Ray {
        Vec3 origin;
        Vec3 direction;

        public Ray(Vec3 origin, Vec3 direction) {
            this.origin = origin;
            this.direction = direction;
        }

        public Vec3 at(double t) {
            return origin.add(direction.mul(t));
        }
    }

    // -------------------------------------------------
    // HitRecord: stores information about a ray–object intersection.
    static class HitRecord {
        Vec3 p;
        Vec3 normal;
        double t;
        boolean frontFace;
        Material material;

        public void setFaceNormal(Ray r, Vec3 outwardNormal) {
            frontFace = r.direction.dot(outwardNormal) < 0;
            normal = frontFace ? outwardNormal : outwardNormal.mul(-1);
        }
    }

    // -------------------------------------------------
    // Hittable: an interface for objects that can be intersected by rays.
    interface Hittable {
        HitRecord hit(Ray r, double tMin, double tMax);
    }

    // Sphere: implements the Hittable interface.
    static class Sphere implements Hittable {
        Vec3 center;
        double radius;
        Material material;

        public Sphere(Vec3 center, double radius, Material material) {
            this.center = center;
            this.radius = radius;
            this.material = material;
        }

        public HitRecord hit(Ray r, double tMin, double tMax) {
            Vec3 oc = r.origin.sub(center);
            double a = r.direction.lengthSquared();
            double half_b = oc.dot(r.direction);
            double c = oc.lengthSquared() - radius * radius;
            double discriminant = half_b * half_b - a * c;
            if (discriminant < 0)
                return null;
            double sqrtd = Math.sqrt(discriminant);

            double root = (-half_b - sqrtd) / a;
            if (root < tMin || root > tMax) {
                root = (-half_b + sqrtd) / a;
                if (root < tMin || root > tMax)
                    return null;
            }

            HitRecord rec = new HitRecord();
            rec.t = root;
            rec.p = r.at(rec.t);
            Vec3 outwardNormal = rec.p.sub(center).div(radius);
            rec.setFaceNormal(r, outwardNormal);
            rec.material = material;
            return rec;
        }
    }

    // HittableList: a list of Hittable objects.
    static class HittableList implements Hittable {
        List<Hittable> objects = new ArrayList<>();

        public HittableList() {}

        public HittableList(List<Hittable> objects) {
            this.objects = objects;
        }

        public void clear() {
            objects.clear();
        }

        public void add(Hittable object) {
            objects.add(object);
        }

        public HitRecord hit(Ray r, double tMin, double tMax) {
            HitRecord tempRec = null;
            double closestSoFar = tMax;
            for (Hittable object : objects) {
                HitRecord rec = object.hit(r, tMin, closestSoFar);
                if (rec != null) {
                    closestSoFar = rec.t;
                    tempRec = rec;
                }
            }
            return tempRec;
        }
    }

    // -------------------------------------------------
    // Material: an interface for different surface materials.
    interface Material {
        // If scattering occurs, the scatter() method sets srec.scattered and srec.attenuation.
        boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec);
    }

    // ScatterRecord: stores information about a scattered ray.
    static class ScatterRecord {
        Ray scattered;
        Vec3 attenuation;
    }

    // Lambertian: diffuse material.
    static class Lambertian implements Material {
        Vec3 albedo;

        public Lambertian(Vec3 albedo) {
            this.albedo = albedo;
        }

        public boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec) {
            Vec3 scatterDirection = rec.normal.add(randomUnitVector());
            // Catch degenerate scatter direction.
            if (scatterDirection.lengthSquared() < 1e-8)
                scatterDirection = rec.normal;
            srec.scattered = new Ray(rec.p, scatterDirection);
            srec.attenuation = albedo;
            return true;
        }
    }

    // Metal: reflective (mirror-like) material.
    static class Metal implements Material {
        Vec3 albedo;
        double fuzz;

        public Metal(Vec3 albedo, double fuzz) {
            this.albedo = albedo;
            this.fuzz = fuzz < 1 ? fuzz : 1;
        }

        public boolean scatter(Ray r_in, HitRecord rec, ScatterRecord srec) {
            Vec3 reflected = reflect(r_in.direction.normalize(), rec.normal);
            Vec3 scatteredDirection = reflected.add(randomInUnitSphere().mul(fuzz));
            srec.scattered = new Ray(rec.p, scatteredDirection);
            srec.attenuation = albedo;
            return (srec.scattered.direction.dot(rec.normal) > 0);
        }
    }

    // -------------------------------------------------
    // Camera: a simple pinhole camera with depth-of-field.
    static class Camera {
        Vec3 origin;
        Vec3 lowerLeftCorner;
        Vec3 horizontal;
        Vec3 vertical;
        Vec3 u, v, w;
        double lensRadius;

        public Camera(Vec3 lookFrom, Vec3 lookAt, Vec3 vup, double vfov, double aspectRatio,
                      double aperture, double focusDist) {
            lensRadius = aperture / 2;
            double theta = Math.toRadians(vfov);
            double h = Math.tan(theta / 2);
            double viewportHeight = 2.0 * h;
            double viewportWidth = aspectRatio * viewportHeight;

            w = lookFrom.sub(lookAt).normalize();
            u = vup.cross(w).normalize();
            v = w.cross(u);

            origin = lookFrom;
            horizontal = u.mul(viewportWidth * focusDist);
            vertical = v.mul(viewportHeight * focusDist);
            lowerLeftCorner = origin.sub(horizontal.div(2)).sub(vertical.div(2)).sub(w.mul(focusDist));
        }

        public Ray getRay(double s, double t) {
            Vec3 rd = randomInUnitDisk().mul(lensRadius);
            Vec3 offset = u.mul(rd.x).add(v.mul(rd.y));
            return new Ray(origin.add(offset),
                    lowerLeftCorner.add(horizontal.mul(s))
                            .add(vertical.mul(t))
                            .sub(origin).sub(offset));
        }
    }
}




