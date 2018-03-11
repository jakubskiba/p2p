package model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Chunk implements Serializable {
    private Sourcefile sourcefile;
    private byte[] chunkSHA256;
    private long from;
    private long to;
    private byte[] data;

    public Chunk(Sourcefile sourcefile, byte[] chunkSHA256, long from, long to, byte[] data) {
        this.sourcefile = sourcefile;
        this.chunkSHA256 = chunkSHA256;
        this.from = from;
        this.to = to;
        this.data = data;
    }

    public boolean isValid() {
        try {
            byte[] realSum = MessageDigest.getInstance("SHA-1").digest(this.data);
            return Arrays.equals(this.chunkSHA256, realSum);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Sourcefile getSourcefile() {
        return sourcefile;
    }

    public byte[] getChunkSHA256() {
        return chunkSHA256;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public byte[] getData() {
        return data;
    }
}
