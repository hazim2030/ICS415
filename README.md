
# Simple Ray Tracer

This project is a basic implementation of a ray tracer in Java. It generates an image with three spheres of different colors (red, green, and blue) rendered on a white background. The final output is saved as a `.png` file.

## Features
- Basic ray tracing algorithm to detect sphere intersections.
- Three spheres with different colors (red, green, blue).
- Outputs a PNG file (`output.png`) representing the rendered scene.

---

## Getting Started

Follow the steps below to set up your development environment and run the program.

### Prerequisites

You will need:
- Java Development Kit (JDK 8 or later)
- A text editor or IDE (e.g., Visual Studio Code, IntelliJ IDEA, or Eclipse)

---

## Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/hazim2030/simple-ray-tracer.git
cd simple-ray-tracer
```

### 2. Compile the Code
Navigate to the directory where the file is located and compile the code:
```bash
javac SimpleRayTracer.java
```

### 3. Run the Program
Run the program to generate the output image:
```bash
java SimpleRayTracer
```

### 4. View the Output
The rendered image (`output.png`) will be created in the same directory as the program. Open it with any image viewer.

---

## Platform-Specific Instructions

### Windows
1. Open Command Prompt or PowerShell.
2. Navigate to the folder containing `SimpleRayTracer.java`.
3. Follow the compile and run instructions above.

### macOS
1. Open Terminal.
2. Navigate to the folder containing `SimpleRayTracer.java`.
3. Follow the compile and run instructions above.

### Linux
1. Open Terminal.
2. Navigate to the folder containing `SimpleRayTracer.java`.
3. Follow the compile and run instructions above.

---

## File Structure
```
simple-ray-tracer/
│
├── SimpleRayTracer.java   # Main source code file
├── output.png             # Generated image after running the program
└── README.md              # Project documentation
```

---

## Sample Output
Below is an example of the rendered image:

![Rendered Image](output.png)

---

## References
- **Ray Tracing in One Weekend** by Peter Shirley (inspiration for the ray tracing algorithm).
- Java Graphics API for image rendering.

---
