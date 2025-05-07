package org.example.test;

import org.example.core.*;
import org.example.core.entity.Entity;
import org.example.core.entity.Material;
import org.example.core.entity.Model;
import org.example.core.entity.Texture;
import org.example.core.entity.terrain.Terrain;
import org.example.core.lighting.DirectionalLight;
import org.example.core.lighting.PointLight;
import org.example.core.lighting.SpotLight;
import org.example.core.rendering.RenderManager;
import org.example.core.utils.Consts;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestGame implements ILogic {

    private static final float CAMERA_MOVE_SPEED = 0.05f;

    private final RenderManager renderer;

    private final WindowManager window;
    private final ObjectLoader loader;


    private List<Entity> entities;
    private List<Terrain> terrains;
    private Camera camera;

    Vector3f cameraInc;

    private float lightAngle, spotAngle = 0, spotInc = 1;
    private DirectionalLight directionalLight;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;
    public TestGame() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = -90;
    }


    @Override
    public void init() throws Exception {
    renderer.init();

        Model model = loader.loadOBJModel("/models/cube.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")), 1f);

        terrains = new ArrayList();
        Terrain terrain = new Terrain(new Vector3f(0, -1, -800), loader, new Material(new Texture(loader.loadTexture("textures/terrain.png")), 0.1f));
        Terrain terrain2 = new Terrain(new Vector3f(-800, -1, -800), loader, new Material(new Texture(loader.loadTexture("textures/flowers.png")), 0.1f));
        terrains.add(terrain); terrains.add(terrain2);

        entities = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < 200; i++) {
            float x = rnd.nextFloat() * 100 -50;
            float y = rnd.nextFloat() * 100 -50;
            float z = rnd.nextFloat() * -300;
            entities.add(new Entity(model, new Vector3f(x, y, z),
                    new Vector3f(rnd.nextFloat() * 180, rnd.nextFloat() * 180, 0), 1));
        }


        float lightIntensity = 1.0f;


        Vector3f lightPosition = new Vector3f(-0.5f, -0.5f, -3.2f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity,0 ,0, 1);

        Vector3f coneDir = new Vector3f(0, 0, -1);
        float cutoff = Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(new PointLight(lightColour, new Vector3f(0, 0, -3.6f),
                lightIntensity, 0, 0, 0.2f), coneDir, cutoff);
         SpotLight spotLight1 = new SpotLight(new PointLight(lightColour, lightPosition, lightIntensity, 0, 0, 1)
                 , coneDir, cutoff);
         spotLight1.getPointLight().setPosition(new Vector3f(0.5f, 0.5f, -3.6f));


         lightPosition = new Vector3f(-1, -10, 0);
         lightColour = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);

        pointLights = new PointLight[]{pointLight};
        spotLights = new SpotLight[]{spotLight, spotLight1};

    }

    @Override
    public void input() throws Exception {


        Model model = loader.loadOBJModel("/models/cube.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")), 1f);
        cameraInc.set(0, 0, 0);
        if(window.iskeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if(window.iskeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;
        if(window.iskeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if(window.iskeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;
        if(window.iskeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
            cameraInc.y = -1;
        if(window.iskeyPressed(GLFW.GLFW_KEY_SPACE))
            cameraInc.y = 1;
        if(window.iskeyPressed(GLFW.GLFW_KEY_H))
            entities.add(new Entity(model, new Vector3f(0, 0, -2f), new Vector3f(0, 0, 1), 1));
        if(window.iskeyPressed(GLFW.GLFW_KEY_Z))
            entities.removeLast();


        float lightPos = spotLights[0].getPointLight().getPosition().z;
        if(window.iskeyPressed(GLFW.GLFW_KEY_N)){
            spotLights[0].getPointLight().getPosition().z = lightPos + 0.1f;
        }

        if(window.iskeyPressed(GLFW.GLFW_KEY_M)) {
            spotLights[0].getPointLight().getPosition().z = lightPos - 0.1f;
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED, cameraInc.y * CAMERA_MOVE_SPEED, cameraInc.z * CAMERA_MOVE_SPEED);

        if(mouseInput.isRightButton()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);
        }

        //entity.incRotation(0.0f, 0.25f, 0.0f);

        spotAngle += spotInc * 0.05f;
        if(spotAngle > 4)
            spotInc = -1;
        else if(spotAngle < -4)
            spotInc = 1;

        double spotAngleRad = Math.toRadians(spotAngle);
        Vector3f coneDir = spotLights[0].getPointLight().getPosition();
        coneDir.y = (float) Math.sin(spotAngleRad);



        lightAngle += 0.5f;
        if(lightAngle > 90){
            directionalLight.setIntensity(0);
            if(lightAngle >=360)
                lightAngle = -90;
        } else if(lightAngle <= -80 || lightAngle >= 80){
            float factor = (Math.abs(lightAngle)- 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColour().y = Math.max(factor,0.9f);
            directionalLight.getColour().z = Math.max(factor,0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColour().x= 1;
            directionalLight.getColour().y = 1;
            directionalLight.getColour().z = 1;
        }

        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);

        for(Entity entity : entities){
            renderer.processEntity(entity);
        }

        for(Terrain terrain : terrains){
            renderer.processTerrain(terrain);
        }

    }

    @Override
    public void render() {



        renderer.render(camera, directionalLight, pointLights, spotLights);
    }



    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
