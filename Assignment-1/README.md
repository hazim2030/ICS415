# Ray Tracing Assignment 1

This assignment includes three Java ray tracing implementations:

1. **Task 1:** Simple Java Ray Tracer
2. **Task 2:** Advanced Java Ray Tracer with enhanced features
3. **Task 3:** Realistic Java Ray Tracer with depth of field and procedural generation

---

## Task 1: Simple Java Ray Tracer

### Overview

This task implements a basic ray tracer rendering three colored spheres. It determines object visibility using ray-sphere intersections and generates a PNG image.

### Features

- Renders three spheres (red, green, blue).
- Basic ray-sphere intersection calculation.
- Generates output as `output.png`.
- Uses Java's BufferedImage and ImageIO.

### Technical Details

- **Camera Position:** (0, 0, 0)
- **Viewport Size:** 1 unit
- **Projection Plane:** Located at `z = 1`
- **Spheres:**
  - Red: `(0, -1, 3)`, radius 1
  - Green: `(2, 0, 4)`, radius 1
  - Blue: `(-2, 0, 4)`, radius 1
- **Background Color:** White

---

## Task 2: Advanced Java Ray Tracer

### Overview

This advanced ray tracer simulates realistic lighting, reflections, transparency, and other optical effects.

### Features

- **Diffuse Reflection:** Realistic matte surfaces.
- **Specular Reflection:** Highlights on reflective surfaces.
- **Reflection:** Recursive reflections.
- **Transparency & Refraction:** Simulates transparent materials.
- **Vector Transformations:** Extendable for object transformations.

### Technical Details

- Advanced vector operations (`Vector3D`).
- Spheres with adjustable reflective and transparency properties (`Sphere`).
- Lighting and shading calculations.

---

## Task 3: Realistic Java Ray Tracer

### Overview

This realistic ray tracer features depth of field, random procedural generation of numerous spheres, and advanced materials to create highly realistic scenes.

### Features

- **Depth of Field:** Realistic camera focus and blur.
- **Procedural Generation:** Randomly generated spheres with varied materials.
- **Lambertian & Metal Materials:** Realistic diffuse and mirror-like surfaces.
- **Recursive Ray Bounce:** Up to 50 depth levels.
- **Gamma Correction:** Proper color representation.

### Technical Details

- **Camera Settings:** Adjustable position, focus distance, and aperture.
- **Scene Setup:** Ground plane and randomly positioned spheres.
- **Image Output:** `output.ppm` in PPM format.
- **Samples per Pixel:** 200 (configurable).

---

## Getting Started

### Requirements
- Java JDK 8 or higher

### Compilation & Running

#### Windows

```cmd
javac RayTracer.java
java RayTracer
```

#### macOS/Linux

```bash
javac RayTracer.java
java RayTracer
```

Generated images (`output.png` or `output.ppm`) will appear in the project directory.

---

## Project Structure

```
├── RayTracer.java  # Ray tracing implementation
├── output.png      # Rendered image for Tasks 1 and 2
├── output.ppm      # Rendered image for Task 3
├── README.md       # Documentation file (this file)
```

---

## Customization

Modify scene parameters, spheres, camera settings, and lighting conditions by editing the respective Java classes.

---

## License

This project is open-source for educational use and experimentation.
