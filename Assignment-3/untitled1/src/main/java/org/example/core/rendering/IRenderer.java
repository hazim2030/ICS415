package org.example.core.rendering;

import org.example.core.Camera;
import org.example.core.entity.Model;
import org.example.core.lighting.DirectionalLight;
import org.example.core.lighting.PointLight;
import org.example.core.lighting.SpotLight;

public interface IRenderer<T> {
    public void init() throws Exception;

    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights,
                       DirectionalLight directionalLight);

    abstract void bind(Model model);

    public void unbind();

    public void prepare(T t, Camera camera);

    public void cleanup();
}
