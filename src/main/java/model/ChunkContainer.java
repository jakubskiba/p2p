package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

public class ChunkContainer implements Serializable {
    SortedMap<Long, Chunk> chunks;
    Sourcefile sourcefile;

    public ChunkContainer(SortedMap<Long, Chunk> chunks, Sourcefile sourcefile) {
        this.chunks = chunks;
        this.sourcefile = sourcefile;
    }

    public void addChunk(Chunk chunk) {
        if(chunk.getSourcefile().getSha256().equals(sourcefile.getSha256())) {
            this.chunks.put(chunk.getFrom(), chunk);
        }
    }

    public long sumChunkSize() {
        long sum = 0;
        for(Chunk chunk : chunks.values()) {
            long chunkSize = chunk.getTo() - chunk.getFrom();
            sum += chunkSize;
        }

        return sum;
    }

    public boolean isFullified() {
        return this.sourcefile.getFile().length() == sumChunkSize();
    }

    public byte[] merge() {
        Integer fileLength = (int) this.sourcefile.getFile().length();
        int i = 0;
        byte[] data = new byte[fileLength];
        for(Chunk chunk : chunks.values()) {
            if(chunk.getFrom() < i) {
                System.err.println("Gap during merge files");
                i = (int) chunk.getFrom();
            }

            int chunkSize = Math.toIntExact(chunk.getTo() - chunk.getFrom());
            byte[] chunkData = chunk.getData();
            for(int j = 0; j<chunkSize; j++) {
                data[i++] = chunkData[j];
            }
        }

        return data;
    }
}
