#  RayTracer - Simple Java Ray Tracer

## Overview

This project is a simple ray tracer implemented in Java that renders a scene containing three colored spheres. The ray tracing algorithm determines the visibility of objects based on ray-sphere intersection calculations and generates an image accordingly.

## Features

- Renders a scene with three spheres (red, green, and blue) positioned at different locations.
- Uses basic ray tracing techniques to calculate intersections.
- Saves the output as a PNG image (`output.png`).
- Uses Java's built-in `BufferedImage` and `ImageIO` for image processing.

## Prerequisites

- Java Development Kit (JDK) 8 or higher installed.

## How to Compile and Run

### On Windows
1. Open Command Prompt (`cmd`) and navigate to the project directory:
   ```sh
   cd path\to\your\project
   ```
2. Compile the Java file:
   ```sh
   javac RayTracer.java
   ```
3. Run the program:
   ```sh
   java RayTracer
   ```
4. The rendered image will be saved as `output.png` in the same directory.

### On macOS/Linux
1. Open a terminal and navigate to the project directory:
   ```sh
   cd /path/to/your/project
   ```
2. Compile the Java file:
   ```sh
   javac RayTracer.java
   ```
3. Run the program:
   ```sh
   java RayTracer
   ```
4. The rendered image will be saved as `output.png` in the same directory.

## Project Structure

```
├── RayTracer.java  # Main ray tracing program
├── output.png      # Rendered image (generated after execution)
├── README.md       # Documentation file (this file)
```

## Technical Details

- **Camera Position**: (0, 0, 0)
- **Viewport Size**: 1 unit
- **Projection Plane**: Located at `z = 1`
- **Spheres**:
  - Red Sphere: (0, -1, 3), Radius = 1
  - Green Sphere: (2, 0, 4), Radius = 1
  - Blue Sphere: (-2, 0, 4), Radius = 1
- **Background Color**: White

## Notes

- The scene is hardcoded but can be easily modified by adjusting the sphere positions and colors.
- No shading or lighting effects are applied; this is a basic ray tracer focusing on object visibility.

## License

This project is open-source and free to use for learning and experimentation.

