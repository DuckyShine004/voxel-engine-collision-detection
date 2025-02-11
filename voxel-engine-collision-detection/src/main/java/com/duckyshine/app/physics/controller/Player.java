package com.duckyshine.app.physics.controller;

import org.joml.Vector3f;

import com.duckyshine.app.physics.AABB;

public class Player {
    private final float WIDTH = 0.8f;
    private final float DEPTH = 0.8f;
    private final float HEIGHT = 1.8f;

    private Vector3f position;
    private Vector3f dimension;

    private AABB aabb;

    public Player(Vector3f position) {
        this.position = position;

        this.initialise();
    }

    private void initialise() {
        this.dimension = new Vector3f(this.WIDTH, this.HEIGHT, this.DEPTH);

        this.aabb = new AABB(
                this.position.x - this.WIDTH / 2.0f,
                this.position.y,
                this.position.z - this.DEPTH / 2.0f,
                this.position.x + this.WIDTH / 2.0f,
                this.position.y + this.HEIGHT,
                this.position.z + this.DEPTH / 2.0f);
    }

    public void updateAABB() {
        this.updateAABBMin();
        this.updateAABBMax();
    }

    public void updateAABBMin() {
        Vector3f min = this.aabb.getMin();

        min.x = this.position.x - this.dimension.x / 2.0f;
        min.y = this.position.y;
        min.z = this.position.z - this.dimension.z / 2.0f;
    }

    public void updateAABBMax() {
        Vector3f max = this.aabb.getMax();

        max.x = this.position.x + this.dimension.x / 2.0f;
        max.y = this.position.y + this.dimension.y;
        max.z = this.position.z + this.dimension.z / 2.0f;
    }

    public AABB getAABB() {
        return this.aabb;
    }

    public Vector3f getPosition() {
        return this.position;
    }
}
