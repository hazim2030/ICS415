# Advanced Java Ray Tracer

This project is an advanced ray tracing implementation written in Java. It supports rendering realistic images by simulating light interactions such as diffuse reflection, specular reflection, reflection, transparency, and refraction.

## Features

- **Diffuse Reflection**: Realistically simulates matte surfaces.
- **Specular Reflection**: Creates shiny highlights on reflective surfaces.
- **Reflection**: Generates recursive reflections on surfaces.
- **Transparency & Refraction**: Models transparent materials with realistic light refraction.
- **Transformations**: Easily extendable vector operations for transformations.

## Getting Started

### Requirements
- Java JDK 8 or higher.

### Compilation
Compile the project using:

#### Windows
```cmd
javac RayTracer.java
```

#### macOS/Linux
```bash
javac RayTracer.java
```

### Running
Run the project with:

#### Windows
```cmd
java RayTracer
```

#### macOS/Linux
```bash
java RayTracer
```

The rendered image will be saved as `output.png`.

## Project Structure

- `Vector3D`: Vector operations such as normalization, reflection, and arithmetic.
- `Sphere`: Represents spheres in the scene with color, reflection, and transparency properties.
- `RayTracer`: Main class handling ray tracing, image rendering, lighting, and color computations.

## Customization

Modify or add new spheres, adjust reflective and transparency properties, or change light positions by editing the `main` method within the `RayTracer` class.

## License

This project is open-source and freely modifiable. Use as a learning resource or extend for personal projects.
