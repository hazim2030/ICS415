# 3D Rendering Engine with LWJGL

This Java project is a basic 3D engine built using LWJGL 3, providing features such as model loading, terrain rendering, and multiple light types.

## Features

- **Model Loading**: Loads OBJ models using `ObjectLoader`.
- **Texturing**: Applies textures to models and terrains.
- **Terrain Rendering**: Renders multiple terrain tiles with different textures.
- **Lighting**:
  - **DirectionalLight** simulating sun/moon light.
  - **PointLight** for localized light sources.
  - **SpotLight** for flashlight effects.
- **Camera Controls**: First-person camera movement with **W/A/S/D**, **SPACE**, and **SHIFT**, and mouse look.
- **Entity and Terrain Management**: Entities loaded and rendered via `RenderManager`.
- **Shader Management**: Centralized GLSL shader handling via `ShaderManager`.

## Prerequisites

- Java 11 or higher
- Gradle (wrapper included)
- OpenGL 3.3+ capable GPU

## Usage

1. **Unzip** the archive:
   ```bash
   unzip untitled1_with_README.zip
   cd untitled1
   ```
2. **Run** using Gradle:
   ```bash
   ./gradlew run      # Linux/Mac
   gradlew.bat run    # Windows
   ```

## Controls

| Action             | Key or Mouse          |
|--------------------|-----------------------|
| Move Forward       | W                     |
| Move Backward      | S                     |
| Strafe Left        | A                     |
| Strafe Right       | D                     |
| Move Up            | SPACE                 |
| Move Down          | LEFT SHIFT            |
| Look Around        | Mouse Movement        |
| Exit               | ESC or Close Window   |

## Project Structure

```
src/main/java/org/example/core/
├── Camera.java
├── EngineManager.java
├── ObjectLoader.java
├── RenderManager.java
├── ShaderManager.java
├── utils/
│   ├── Consts.java
│   ├── Transformation.java
│   └── Utils.java
└── entity/
    ├── Model.java
    ├── Texture.java
    ├── Entity.java
    └── terrain /
        └── Terrain.java

src/main/java/org/example/test/
├── Launcher.java
└── TestGame.java

src/main/resources/
├── models/
│   └── cube.obj
└── textures/
    ├── grassblock.png
    ├── terrain.png
    └── flowers.png

shaders/
├── vertex.glsl
└── fragment.glsl

build.gradle
settings.gradle
```

## License

MIT License
