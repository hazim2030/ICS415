package org.example.core.rendering;

import org.example.core.Camera;
import org.example.core.ShaderManager;
import org.example.core.WindowManager;
import org.example.core.entity.Entity;
import org.example.core.entity.terrain.Terrain;
import org.example.core.lighting.DirectionalLight;
import org.example.core.lighting.PointLight;
import org.example.core.lighting.SpotLight;
import org.example.core.utils.Consts;
import org.example.test.Launcher;
import org.lwjgl.opengl.GL11;


import java.util.ArrayList;
import java.util.List;

public class RenderManager {

    private final WindowManager window;
    private EntityRenderer entityRenderer;

    private TerrainRenderer terrainRenderer;
    public RenderManager() {
        window = Launcher.getWindow();
    }

    public void init() throws Exception {
        entityRenderer = new EntityRenderer();
        terrainRenderer = new TerrainRenderer();
        entityRenderer.init();
        terrainRenderer.init();

    }



    public static void renderLight(PointLight[] pointLights, SpotLight[] spotLights,
                                   DirectionalLight directionalLight, ShaderManager shader) {
        shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
        shader.setUniform("specularPower", Consts.SPECULAR_POWER);

        int numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            shader.setUniform("spotLights", spotLights[i], i);
        }

        numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            shader.setUniform("pointLights", pointLights[i], i);
        }
        shader.setUniform("directionalLight", directionalLight);
    }

    public void render(Camera camera, DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights) {
        clear();

        if(window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(false);
        }

        entityRenderer.render(camera, pointLights, spotLights, directionalLight);
        terrainRenderer.render(camera, pointLights, spotLights, directionalLight);
    }

    public void processEntity(Entity entity) {
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if(entityList != null)
            entityList.add(entity);
        else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }

        }

    public void processTerrain(Terrain terrain) {
        terrainRenderer.getTerrain().add(terrain);
    }
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        entityRenderer.cleanup();
        terrainRenderer.cleanup();
    }

}
