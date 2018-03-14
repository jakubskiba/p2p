package dto;

import model.Chunk;

import java.io.Serializable;
import java.util.Optional;

public class ChunkEnvelope implements Serializable{
    private static final long serialVersionUID = -5765021819906722357L;

    private Optional<Chunk> chunk;

    public ChunkEnvelope(Chunk chunk) {
        this.chunk = Optional.ofNullable(chunk);
    }

    public Optional<Chunk> getChunk() {
        return chunk;
    }

    public void setChunk(Optional<Chunk> chunk) {
        this.chunk = chunk;
    }
}
