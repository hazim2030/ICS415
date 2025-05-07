package org.example.core;

public interface ILogic {
    void init() throws Exception;

    void input() throws Exception;

    void update(float interval, MouseInput mouseInput);

    void render();

    void cleanup();
}
