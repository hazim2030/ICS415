package org.example.core;

import org.example.test.Launcher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANOSECOND = 1000000000L;

    private static int fps;
    private static float framerate = 1000;
    public static float framTime = 1.0f/framerate;
    public static float currentFrameTime = 0;

    private boolean isRunning;

    private WindowManager windowManager;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        windowManager = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        mouseInput = new MouseInput();
        windowManager.init();
        gameLogic.init();
        mouseInput.init();
    }

    public void start() throws Exception {
        init();
        if (isRunning)
            return;
        run();
        }
        public void run() throws Exception {
        this.isRunning = true;

            int frames = 0;
        long frameCounter = 0;
            long lastTime = System.nanoTime();

        double unprocessedTime = 0;
        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime +=  passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            input();

            while(unprocessedTime > framTime) {
                render = true;
                unprocessedTime -= framTime;
                if(windowManager.windowShouldClose())
                    stop();
                if (frameCounter>=NANOSECOND){
                    setFps(frames);
                    currentFrameTime = 1.0f/fps;
                    windowManager.setTitle("Kinos Engine FPS: "+getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render) {
                updtae(framTime);
                render();
                frames++;
            }
        }
        cleanup();
        }
        private void stop(){
        if(!isRunning)
            return;
        isRunning = false;


        }
        private void input() throws Exception {
        mouseInput.input();
        gameLogic.input();
        }

        private void render() {
       gameLogic.render();
        windowManager.update();

        }


        private void cleanup() {
        windowManager.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
        }

        private void updtae(float interval) {gameLogic.update(interval, mouseInput);}
    public static float getFrameTime() {
        return framTime;
    }
    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
