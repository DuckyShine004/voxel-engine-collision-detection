package com.duckyshine.app.scene;

import java.util.Map;
import java.util.HashMap;

import org.joml.Vector3f;
import org.joml.Vector3i;

import com.duckyshine.app.physics.AABB;

import com.duckyshine.app.physics.controller.Player;

import com.duckyshine.app.asset.AssetPool;

import com.duckyshine.app.camera.Camera;

import com.duckyshine.app.math.Axis;

import com.duckyshine.app.model.Mesh;
import com.duckyshine.app.model.Block;
import com.duckyshine.app.model.Chunk;

import com.duckyshine.app.shader.Shader;
import com.duckyshine.app.shader.ShaderType;

import com.duckyshine.app.debug.Debug;

// Only two places where I need to change the constants, here and Chunk
public class Scene {
    private final int CHUNK_WIDTH = 16;
    private final int CHUNK_DEPTH = 16;
    private final int CHUNK_HEIGHT = 16;

    private Shader shader;

    private Player player;

    private Map<Vector3i, Chunk> chunks;

    public Scene() {
        this.chunks = new HashMap<>();

        this.player = new Player(0.0f, 20.0f, 0.0f);

        this.shader = AssetPool.getShader(ShaderType.WORLD.get());
    }

    public Scene(Shader shader) {
        this.chunks = new HashMap<>();

        this.player = new Player();

        this.shader = shader;
    }

    public void generate() {
        Chunk chunk = new Chunk(0, 0, 0);

        chunk.generate();

        this.chunks.put(chunk.getPosition(), chunk);
    }

    public boolean isChunkActive(Vector3i position) {
        return this.chunks.containsKey(position);
    }

    public boolean isChunkActive(int x, int y, int z) {
        Vector3i position = new Vector3i(x, y, z);

        return this.isChunkActive(position);
    }

    public Chunk getChunk(int x, int y, int z) {
        Vector3i position = new Vector3i(x, y, z);

        return this.getChunk(position);
    }

    public Chunk getChunk(Vector3i position) {
        return this.chunks.get(position);
    }

    public Block getBlock(int x, int y, int z) {
        Vector3i position = new Vector3i(x, y, z);

        return this.getBlock(position);
    }

    public Vector3i getChunkPosition(int x, int y, int z) {
        Vector3i position = new Vector3i(x, y, z);

        return this.getChunkPosition(position);
    }

    public Vector3i getChunkPosition(Vector3i position) {
        int x = (int) Math.floor((float) position.x / this.CHUNK_WIDTH) * this.CHUNK_WIDTH;
        int y = (int) Math.floor((float) position.y / this.CHUNK_HEIGHT) * this.CHUNK_HEIGHT;
        int z = (int) Math.floor((float) position.z / this.CHUNK_DEPTH) * this.CHUNK_DEPTH;

        return new Vector3i(x, y, z);
    }

    public Vector3i getBlockPosition(Vector3i position, Vector3i chunkPosition) {
        return position.sub(chunkPosition, new Vector3i());
    }

    public boolean isBlockActive(Vector3i position) {
        Vector3i chunkPosition = this.getChunkPosition(position);
        Vector3i blockPosition = this.getBlockPosition(position, chunkPosition);

        Chunk chunk = this.getChunk(chunkPosition);

        // Debug.debug(this.player.getPosition(), chunkPosition, blockPosition);

        return (chunk != null) ? chunk.isBlockActive(blockPosition) : false;
    }

    public boolean isBlockActive(int x, int y, int z) {
        Vector3i position = new Vector3i(x, y, z);

        return this.isBlockActive(position);
    }

    public Block getBlock(Vector3i position) {
        Vector3i chunkPosition = this.getChunkPosition(position);

        Chunk chunk = this.getChunk(chunkPosition);

        Vector3i blockPosition = this.getBlockPosition(position, chunkPosition);

        return chunk.getBlock(blockPosition);
    }

    public boolean isColliding(AABB aabb) {
        Vector3f min = aabb.getMin();
        Vector3f max = aabb.getMax();

        int minX = (int) Math.floor(min.x);
        int minY = (int) Math.floor(min.y);
        int minZ = (int) Math.floor(min.z);

        int maxX = (int) Math.ceil(max.x);
        int maxY = (int) Math.ceil(max.y);
        int maxZ = (int) Math.ceil(max.z);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // Debug.debug(this.player.getPosition());
                    if (this.isBlockActive(x, y, z)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void checkCollisions(long window, float deltaTime) {
        this.player.updateVelocity(window);

        Vector3f position = this.player.getPosition();
        Vector3f target = this.player.getNextPosition(deltaTime);

        target.x = this.getAxisCollision(position.x, target.x, Axis.X);
        target.y = this.getAxisCollision(position.y, target.y, Axis.Y);
        target.z = this.getAxisCollision(position.z, target.z, Axis.Z);

        this.player.setPosition(target);
    }

    public float getAxisCollision(float position, float target, Axis axis) {
        AABB aabb = this.player.getAABB();

        AABB offsetAABB = aabb.getOffset(target - position, axis);

        return this.isColliding(offsetAABB) ? position : target;

    }

    public void update(long window, float deltaTime) {
        this.checkCollisions(window, deltaTime);
    }

    public void render() {
        this.useShader();

        for (Chunk chunk : chunks.values()) {
            Mesh mesh = chunk.getMesh();

            mesh.render();
        }
    }

    private void useShader() {
        Camera camera = this.getCamera();

        this.shader = AssetPool.getShader(ShaderType.WORLD.get());

        this.shader.use();

        this.shader.setMatrix4f("projectionViewMatrix", camera.getProjectionView());
    }

    public void cleanup() {
        for (Chunk chunk : chunks.values()) {
            Mesh mesh = chunk.getMesh();

            mesh.cleanup();
        }
    }

    public Camera getCamera() {
        return this.player.getCamera();
    }
}
