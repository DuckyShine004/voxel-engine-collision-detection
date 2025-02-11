package com.duckyshine.app.physics.controller;

import org.joml.Vector3f;

import com.duckyshine.app.math.Vector3;

import com.duckyshine.app.camera.Camera;
import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.physics.AABB;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private final float SPEED = 10.0f;

    private final float WIDTH = 0.8f;
    private final float DEPTH = 0.8f;
    private final float HEIGHT = 1.8f;

    private Vector3f position;
    private Vector3f velocity;

    private Vector3f dimension;

    private AABB aabb;

    private Camera camera;

    public Player() {
        this.position = new Vector3f();

        this.initialise();
    }

    public Player(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);

        this.initialise();
    }

    public Player(Vector3f position) {
        this.position = position;

        this.initialise();
    }

    private void initialise() {
        this.camera = new Camera(this.position);

        this.dimension = new Vector3f(this.WIDTH, this.HEIGHT, this.DEPTH);

        this.velocity = new Vector3f();

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

    public void updateVelocity(long window) {
        Vector3f up = this.camera.getUp();
        Vector3f front = this.camera.getFront();

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            this.velocity.add(front);
        }

        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            this.velocity.sub(front);
        }

        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            this.velocity.add(Vector3.cross(front, up).normalize());
        }

        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            this.velocity.sub(Vector3.cross(front, up).normalize());
        }

        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            this.velocity.add(up);
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            this.velocity.sub(up);
        }
        // this.updatePosition(deltaTime);
    }

    public Vector3f getNextPosition(float deltaTime) {
        Vector3f position = new Vector3f(this.position);

        float speed = this.SPEED * deltaTime;

        if (this.velocity.length() != 0.0f) {
            position.add(this.velocity.normalize().mul(speed));
            this.velocity.zero();
        }

        return position;
    }

    public void updatePosition(float deltaTime) {
        float speed = this.SPEED * deltaTime;

        if (this.velocity.length() != 0.0f) {
            this.position.add(this.velocity.normalize().mul(speed));
            this.velocity.zero();
        }

        this.camera.updateMatrices();
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);

        this.updateAABB();

        this.camera.setPosition(position);

        this.camera.updateMatrices();
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

    public Camera getCamera() {
        return this.camera;
    }

    public AABB getAABB() {
        return this.aabb;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getVelocity() {
        return this.velocity;
    }
}
