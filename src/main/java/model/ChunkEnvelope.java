package model;

import java.io.Serializable;

public class ChunkEnvelope implements Serializable{
    private Chunk chunk;

    public ChunkEnvelope(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }
}
