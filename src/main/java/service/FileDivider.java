package service;

import model.Chunk;
import model.Sourcefile;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileDivider {
    private Sourcefile sourcefile;

    public FileDivider(Sourcefile sourcefile) {
        this.sourcefile = sourcefile;
    }

    public byte[] getChunk(long offset, int chunkSize) throws IOException {
        FileInputStream inputStream = new FileInputStream(this.sourcefile.getFile());

        byte[] chunk = new byte[chunkSize];

        inputStream.skip(offset);
        inputStream.read(chunk);
        inputStream.skip(inputStream.available());

        return chunk;
    }

    public Chunk createChunk(long from, long to) throws IOException {
        Long fileLength = sourcefile.getFile().length();
        if(from < 0 || from > fileLength || to > fileLength) {
            throw new IllegalArgumentException("Invalid range. Check file size");
        }

        try {
            Integer chunkSize = Math.toIntExact(to - from);
            byte[] data = getChunk(from, chunkSize);
            byte[] sha256 = MessageDigest.getInstance("SHA-1").digest(data);


            return new Chunk(sourcefile, sha256, from, to, data);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
}
