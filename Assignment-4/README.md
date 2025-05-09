# Bézier Curve Editor

A Java Swing application for visually creating and editing Bézier and poly-Bézier curves.

## Features
- Click to add control points.
- Drag points to reshape curves.
- **Single-Segment Mode:** Every 4 points creates one standalone Bézier curve.
- **Poly-Bézier Mode (Bonus):** First segment uses 4 points; each additional segment is created with 3 new points, chaining smoothly.

## File Structure
```
src/com/example/curves/
├── ControlPoint.java   # Draggable control point class
├── BezierCurve.java    # Single Bézier segment logic using De Casteljau
├── CurveCanvas.java    # JPanel with drawing logic and mouse handling
└── CurveEditorApp.java # Main application launcher
```

## Prerequisites
- Java 8 or higher
- A Java IDE (IntelliJ IDEA, Eclipse) or JDK command-line tools

## Build & Run

### IntelliJ IDEA
1. Open IntelliJ and select **File → New → Project from Existing Sources…**.
2. Point to the project root. Ensure `src/` is marked as a **Sources Root**.
3. In the **Run Configurations**, set the main class to `com.example.curves.CurveEditorApp`.
4. Run the application.

### Command-Line
```bash
# From project root:
javac -d out src/com/example/curves/*.java
java -cp out com.example.curves.CurveEditorApp
```

## Usage
1. **Add Control Points**: Click anywhere on the white canvas.
2. **Create Curves**:
   - In Single-Segment mode, after 4 clicks you'll see one red curve.
   - In Poly-Bézier mode, after the initial 4 clicks, every 3 additional clicks adds a new connected segment.
3. **Drag Points**: Click and drag any blue control point to adjust the curve in real time.

## Poly-Bézier Bonus Logic
The chaining logic in `CurveCanvas.java`:
```java
int n = points.size();
if (n >= 4 && (n == 4 || (n - 1) % 3 == 0)) {
    int start = (n == 4) ? 0 : n - 4;
    curves.add(new BezierCurve(
        points.get(start),
        points.get(start + 1),
        points.get(start + 2),
        points.get(start + 3)
    ));
}
```

This reuses the last endpoint of the previous segment as the first point of the next, giving C⁰ continuity.

## License
MIT License.
