package model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Chunk implements Serializable {
    private static final long serialVersionUID = 5166075077057977388L;
    private Sourcefile sourcefile;
    private byte[] chunkSHA256;
    private int chunkId;
    private byte[] data;

    public Chunk(Sourcefile sourcefile, byte[] chunkSHA256, byte[] data, Integer chunkId) {
        this.sourcefile = sourcefile;
        this.chunkSHA256 = chunkSHA256;
        this.chunkId = chunkId;
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

    public int getChunkId() {
        return chunkId;
    }

    public byte[] getData() {
        return data;
    }
}
