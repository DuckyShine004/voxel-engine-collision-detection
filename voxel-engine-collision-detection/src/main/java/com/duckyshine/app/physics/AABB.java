package com.duckyshine.app.physics;

import org.joml.Vector3f;

import com.duckyshine.app.math.Axis;

public class AABB {
    private final int[] indices = {
            0, 1, 1, 2, 2, 3, 3, 0,
            4, 5, 5, 6, 6, 7, 7, 4,
            0, 4, 1, 5, 2, 6, 3, 7
    };

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

    public AABB getOffset(float offset, Axis axis) {
        switch (axis) {
            case X:
                return new AABB(
                        this.min.x + offset,
                        this.min.y,
                        this.min.z,
                        this.max.x + offset,
                        this.max.y,
                        this.max.z);
            case Y:
                return new AABB(
                        this.min.x,
                        this.min.y + offset,
                        this.min.z,
                        this.max.x,
                        this.max.y + offset,
                        this.max.z);
            case Z:
                return new AABB(
                        this.min.x,
                        this.min.y,
                        this.min.z + offset,
                        this.max.x,
                        this.max.y,
                        this.max.z + offset);
            default:
                return this;
        }
    }

    public float getCentre(Axis axis) {
        switch (axis) {
            case X:
                return (this.min.x + this.max.x) / 2.0f;
            case Y:
                return (this.min.y + this.max.y) / 2.0f;
            case Z:
                return (this.min.z + this.max.z) / 2.0f;
            default:
                return 0.0f;
        }
    }

    public Vector3f getMin() {
        return this.min;
    }

    public Vector3f getMax() {
        return this.max;
    }
}
