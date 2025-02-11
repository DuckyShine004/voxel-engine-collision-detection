package com.duckyshine.app.camera;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.duckyshine.app.math.Matrix4;
import com.duckyshine.app.math.Vector3;

import com.duckyshine.app.display.Display;

import com.duckyshine.app.debug.Debug;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private final float YAW = 90.0f;
    private final float PITCH = 0.0f;
    private final float PITCH_LIMIT = 89.0f;

    private final float FAR = 100.0f;
    private final float NEAR = 0.1f;
    private final float FIELD_OF_VIEW = 45.0f;

    private final float SPEED = 10.0f;
    private final float SENSITIVITY = 0.05f;

    private float yaw;
    private float pitch;

    private float lastTime;

    private float aspectRatio;

    private Vector2f lastMousePosition;

    private Vector3f position;
    private Vector3f direction;

    private Vector3f up;
    private Vector3f front;

    private Vector3f velocity;

    private Matrix4f view;
    private Matrix4f projection;
    private Matrix4f projectionView;

    public Camera() {
        this.position = new Vector3f();

        this.initialise();
    }

    public Camera(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);

        this.initialise();
    }

    public void initialise() {
        this.yaw = -this.YAW;
        this.pitch = this.PITCH;

        this.lastTime = 0.0f;

        this.lastMousePosition = null;

        this.direction = new Vector3f();

        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);

        this.velocity = new Vector3f();

        this.view = new Matrix4f();
        this.projection = new Matrix4f();
        this.projectionView = new Matrix4f();

        this.initialiseAspectRatio();
    }

    private void initialiseAspectRatio() {
        Display display = Display.get();

        this.aspectRatio = (float) display.getWidth() / display.getHeight();
    }

    public void update(long window, float time) {
        move(window, time);
    }

    private void move(long window, float time) {
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            this.velocity.add(this.front);
        }

        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            this.velocity.sub(this.front);
        }

        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            this.velocity.add(Vector3.cross(this.front, this.up).normalize());
        }

        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            this.velocity.sub(Vector3.cross(this.front, this.up).normalize());
        }

        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            this.velocity.add(this.up);
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            this.velocity.sub(this.up);
        }

        this.updatePosition(time);
    }

    private void updatePosition(float time) {
        float deltaTime = time - this.lastTime;
        float speed = this.SPEED * deltaTime;

        this.lastTime = time;

        if (this.velocity.length() != 0.0f) {
            this.position.add(this.velocity.normalize().mul(speed));
            this.velocity.zero();
        }

        this.updateMatrices();
    }

    public void updateAspectRatio(int width, int height) {
        this.aspectRatio = (float) width / height;
    }

    private void updateMatrices() {
        float fieldOfView = Math.toRadians(this.FIELD_OF_VIEW);

        this.view.identity().lookAt(this.position, Vector3.add(this.position, this.front), this.up);

        this.projection.identity().perspective(fieldOfView, this.aspectRatio, this.NEAR, this.FAR);

        this.projectionView = Matrix4.mul(this.projection, this.view);
    }

    public void rotate(double mouseX, double mouseY) {
        float floatMouseX = (float) mouseX;
        float floatMouseY = (float) mouseY;

        float theta;
        float omega;

        if (this.lastMousePosition == null) {
            this.lastMousePosition = new Vector2f((float) mouseX, (float) mouseY);
        }

        float offsetX = (floatMouseX - lastMousePosition.x) * this.SENSITIVITY;
        float offsetY = (lastMousePosition.y - floatMouseY) * this.SENSITIVITY;

        this.lastMousePosition.x = floatMouseX;
        this.lastMousePosition.y = floatMouseY;

        this.yaw += offsetX;
        this.pitch = Math.clamp(-this.PITCH_LIMIT, this.PITCH_LIMIT, this.pitch + offsetY);

        theta = Math.toRadians(this.yaw);
        omega = Math.toRadians(this.pitch);

        this.direction.zero();

        this.direction.x = Math.cos(theta) * Math.cos(omega);
        this.direction.y = Math.sin(omega);
        this.direction.z = Math.sin(theta) * Math.cos(omega);

        this.front = this.direction.normalize();
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Matrix4f getProjectionView() {
        return this.projectionView;
    }
}
