package org.example.core.utils;

import org.example.core.Camera;
import org.example.core.entity.Entity;
import org.example.core.entity.terrain.Terrain;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(entity.getPos()).
                rotateX( Math.toRadians(entity.getRotaion().x)).
                rotateY( Math.toRadians(entity.getRotaion().y)).
                rotateZ( Math.toRadians(entity.getRotaion().z)).
                scale(entity.getScale());
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Terrain terrain) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(terrain.getPosition()).scale(1);
        return matrix;
    }

    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate( Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate( Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate( Math.toRadians(rot.z), new Vector3f(0, 0, 1));
        matrix.translate(-pos.x, -pos.y, -pos.z);
        return matrix;

    }
}
