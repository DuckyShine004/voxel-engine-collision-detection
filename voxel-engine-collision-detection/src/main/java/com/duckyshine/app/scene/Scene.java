package com.duckyshine.app.scene;

import java.util.Map;
import java.util.HashMap;

import org.joml.Vector3i;

import com.duckyshine.app.model.Mesh;
import com.duckyshine.app.math.Math;
import com.duckyshine.app.math.Vector3;
import com.duckyshine.app.model.Block;
import com.duckyshine.app.model.Chunk;

import com.duckyshine.app.shader.Shader;

// Only two places where I need to change the constants, here and Chunk
public class Scene {
    private final int CHUNK_WIDTH = 16;
    private final int CHUNK_DEPTH = 16;
    private final int CHUNK_HEIGHT = 16;

    private Shader shader;

    private Map<Vector3i, Chunk> chunks;

    public Scene() {
        this.chunks = new HashMap<>();
    }

    public Scene(Shader shader) {
        this.chunks = new HashMap<>();

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

    public Vector3i getChunkPosition(Vector3i position) {
        int x = (position.x / this.CHUNK_WIDTH) * this.CHUNK_WIDTH;
        int y = (position.y / this.CHUNK_HEIGHT) * this.CHUNK_HEIGHT;
        int z = (position.z / this.CHUNK_DEPTH) * this.CHUNK_DEPTH;

        return new Vector3i(x, y, z);
    }

    public Vector3i getBlockPosition(Vector3i position, Vector3i chunkPosition) {
        return position.sub(chunkPosition, new Vector3i());
    }

    public boolean isBlockActive(Vector3i position) {
        Vector3i chunkPosition = this.getChunkPosition(position);

        Chunk chunk = this.getChunk(chunkPosition);

        return chunk.isBlockActive(position);
    }

    public Block getBlock(Vector3i position) {
        Vector3i chunkPosition = this.getChunkPosition(position);

        Chunk chunk = this.getChunk(chunkPosition);

        Vector3i blockPosition = this.getBlockPosition(position, chunkPosition);

        return (chunk.isBlockActive(blockPosition)) ? chunk.getBlock(blockPosition) : null;
    }

    public void render() {
        for (Chunk chunk : chunks.values()) {
            Mesh mesh = chunk.getMesh();

            mesh.render();
        }
    }

    public void cleanup() {
        for (Chunk chunk : chunks.values()) {
            Mesh mesh = chunk.getMesh();

            mesh.cleanup();
        }
    }
}
