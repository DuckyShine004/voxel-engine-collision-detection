package com.duckyshine.app.physics;

import org.joml.Vector3f;

public class AABB {
    private Vector3f min;
    private Vector3f max;

    public AABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.min = new Vector3f(minX, minY, minZ);
        this.max = new Vector3f(maxX, maxY, maxZ);
    }

    public AABB(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
    }

    public Vector3f getMin() {
        return this.min;
    }

    public Vector3f getMax() {
        return this.max;
    }
}
