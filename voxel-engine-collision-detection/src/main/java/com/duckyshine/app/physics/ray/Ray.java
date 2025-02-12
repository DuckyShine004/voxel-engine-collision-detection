package com.duckyshine.app.physics.ray;

import org.joml.Vector3f;
import org.joml.Vector3i;

import com.duckyshine.app.scene.Scene;

public class Ray {
    private float distance;

    private Vector3f origin;
    private Vector3f direction;

    public Ray(Vector3f origin, Vector3f direction, float distance) {
        this.distance = distance;

        this.origin = origin;
        this.direction = direction;
    }

    public Vector3i march(Scene scene) {
        Vector3i step = new Vector3i();
        Vector3i position = this.getBlockPosition();

        Vector3f tMax = new Vector3f();
        Vector3f tDelta = new Vector3f();

        if (this.direction.x > 0) {
            step.x = 1;
            tMax.x = ((position.x + 1) - this.origin.x) / this.direction.x;
            tDelta.x = 1.0f / this.direction.x;
        } else if (this.direction.x < 0) {
            step.x = -1;
            tMax.x = (this.origin.x - position.x) / -this.direction.x;
            tDelta.x = 1.0f / -this.direction.x;
        } else {
            tMax.x = Float.MAX_VALUE;
            tDelta.x = Float.MAX_VALUE;
        }

        if (this.direction.y > 0) {
            step.y = 1;
            tMax.y = ((position.y + 1) - this.origin.y) / this.direction.y;
            tDelta.y = 1.0f / this.direction.y;
        } else if (this.direction.y < 0) {
            step.y = -1;
            tMax.y = (this.origin.y - position.y) / -this.direction.y;
            tDelta.y = 1.0f / -this.direction.y;
        } else {
            tMax.y = Float.MAX_VALUE;
            tDelta.y = Float.MAX_VALUE;
        }

        if (this.direction.z > 0) {
            step.z = 1;
            tMax.z = ((position.z + 1) - this.origin.z) / this.direction.z;
            tDelta.z = 1.0f / this.direction.z;
        } else if (this.direction.z < 0) {
            step.z = -1;
            tMax.z = (this.origin.z - position.z) / -this.direction.z;
            tDelta.z = 1.0f / -this.direction.z;
        } else {
            tMax.z = Float.MAX_VALUE;
            tDelta.z = Float.MAX_VALUE;
        }

        float t = 0.0f;

        while (t <= this.distance) {
            if (scene.isBlockActive(position)) {
                return position;
            }

            if (tMax.x < tMax.y && tMax.x < tMax.z) {
                position.x += step.x;
                t = tMax.x;
                tMax.x += tDelta.x;
            } else if (tMax.y < tMax.z) {
                position.y += step.y;
                t = tMax.y;
                tMax.y += tDelta.y;
            } else {
                position.z += step.z;
                t = tMax.z;
                tMax.z += tDelta.z;
            }
        }

        return null;
    }

    private Vector3i getBlockPosition() {
        int x = (int) Math.floor(this.origin.x);
        int y = (int) Math.floor(this.origin.y);
        int z = (int) Math.floor(this.origin.z);

        return new Vector3i(x, y, z);
    }

    public Vector3f getOrigin() {
        return this.origin;
    }

    public Vector3f getDirection() {
        return this.direction;
    }

    public float getDistance() {
        return this.distance;
    }
}
