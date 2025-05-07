package org.example.core.entity;

import org.joml.Vector3f;

import java.util.Vector;

public class Entity {

    private Model model;
    private Vector3f pos, rotaion;
    private float scale;

    public Entity(Model model, Vector3f pos, Vector3f rotaion, float scale) {
        this.model = model;
        this.pos = pos;
        this.rotaion = rotaion;
        this.scale = scale;
    }

    public void incPos(float x, float y, float z) {
        this.pos.x += x;
        this.pos.y += y;
        this.pos.z += z;
    }
    public void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void incRotation(float x, float y, float z) {
       this.rotaion.x += x;
       this.rotaion.y += y;
       this.rotaion.z += z;
    }
    public void setRotation(float x, float y, float z) {
        this.rotaion.x = x;
        this.rotaion.y = y;
        this.rotaion.z = z;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotaion() {
        return rotaion;
    }

    public float getScale() {
        return scale;
    }
}
