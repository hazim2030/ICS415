# Realistic Java Ray Tracer (Task 3)

## Overview

This project implements a realistic ray tracer in Java, generating complex and visually appealing images with advanced rendering techniques such as depth of field, procedural object placement, and sophisticated materials like Lambertian (diffuse) and Metal (reflective).

## Features

- **Depth of Field**: Simulates realistic camera lens effects, producing sharp focus on objects within a specified range and realistic blur elsewhere.
- **Procedural Generation**: Automatically generates a varied and randomized scene filled with numerous small spheres for visual complexity.
- **Advanced Materials**:
  - **Lambertian**: Creates diffuse, matte surfaces.
  - **Metal**: Produces reflective, mirror-like surfaces with configurable fuzziness.
- **Recursive Ray Tracing**: Supports multiple reflections (up to 50 levels), enhancing realism.
- **Gamma Correction**: Correctly adjusts colors for accurate representation in rendered images.

## Technical Specifications

- **Image Dimensions**: 800x450 pixels (16:9 aspect ratio)
- **Samples per Pixel**: 200 (adjustable for higher quality at the cost of rendering time)
- **Output Format**: PPM (`output.ppm`)
- **Camera Settings**:
  - Position: `(13, 2, 3)`
  - Focus: Adjustable distance and aperture for depth of field effects

## Requirements

- Java Development Kit (JDK) 8 or higher

## Compilation and Execution

### Windows

```cmd
javac RayTracer.java
java RayTracer
```

### macOS/Linux

```bash
javac RayTracer.java
java RayTracer
```

The generated image will be saved as `output.ppm` in the project directory.

## Project Structure

```
├── RayTracer.java  # Ray tracer implementation
├── output.ppm      # Rendered image (generated after execution)
├── README.md       # Documentation file (this file)
```

## Customization

Customize the scene, camera settings, or material properties by modifying parameters directly in the Java file.

## License

This project is open-source, intended for educational purposes and experimentation.

