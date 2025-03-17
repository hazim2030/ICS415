# Realistic Spheres with Enhanced Reflections & Shadows

This ShaderToy project demonstrates a real-time raytracing scene featuring multiple spheres on a gray plane. The shader implements:
- **Realistic Shading:** Incorporates both diffuse and specular lighting using a Blinn–Phong model.
- **Hard Shadows:** Casts hard shadows from a directional light using shadow rays.
- **Reflections:** Uses an iterative approach (up to 3 bounces) to simulate reflective surfaces.
- **Scene Composition:** Includes three large spheres (with two mirror-like surfaces and one diffuse) and many small randomly placed spheres.

## Features

- **Diffuse Lighting:** Uses Lambertian shading for matte surfaces.
- **Specular Highlights:** Adds realistic highlights on diffuse surfaces.
- **Iterative Reflection:** Supports multiple reflection bounces to show more detail in mirror-like spheres.
- **Hard Shadows:** Computes simple hard shadows with a secondary shadow ray cast toward a directional light.
- **Scene Randomization:** Generates small spheres randomly within a defined range using a fixed seed for consistency.

## Usage

1. **Open [ShaderToy](https://www.shadertoy.com/).**
2. **Create a new Shader** by clicking on "New Shader."
3. **Copy and paste the provided GLSL code** into the ShaderToy editor.
4. **Run the shader.** You should see a scene with a gray floor, three large spheres, and many small spheres with reflections and shadows.
5. **Adjust parameters as needed:**
   - Change `NUM_SMALL` to increase/decrease the number of small spheres.
   - Modify `MAX_BOUNCES` to adjust the number of reflection bounces.
   - Tweak lighting or material parameters in the `computeLighting` function.

## Customization

- **Lighting:** Modify the `lightDir` vector to change the direction of the light.
- **Material Properties:** Adjust the `metallic` flag for each sphere to control reflectivity.
- **Camera Settings:** Change the camera position and field of view in the `getRayDir` function.
- **Color and Shading:** Experiment with different ambient, diffuse, and specular values to achieve the desired look.

## Requirements

- A web browser that supports WebGL.
- Internet access to use ShaderToy.

## Credits

- **Shader Code:** Created with guidance from ChatGPT.
- **Concept and Inspiration:** Inspired by real-time raytracing demos and ShaderToy experiments.

## License

This project is provided as-is for educational purposes. Feel free to modify and experiment with the code.

---

