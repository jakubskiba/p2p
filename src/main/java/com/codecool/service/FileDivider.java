package com.codecool.service;

import com.codecool.controller.SourcefileController;
import com.codecool.model.Chunk;
import com.codecool.model.Sourcefile;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class FileDivider {
    private Sourcefile sourcefile;
    private SourcefileController sourcefileController;

    public FileDivider(Sourcefile sourcefile, SourcefileController sourcefileController) {
        this.sourcefile = sourcefile;
        this.sourcefileController = sourcefileController;
    }

    private byte[] getChunk(long offset, int chunkSize) throws IOException {
        FileInputStream inputStream = new FileInputStream(getFilePath());

        byte[] chunk = new byte[chunkSize];

        inputStream.skip(offset);
        inputStream.read(chunk);
        inputStream.skip(inputStream.available());

        inputStream.close();

        return chunk;
    }

    public Chunk createChunk(int chunkId) throws IOException {
        if(chunkId < 0 || chunkId > sourcefile.getChunkAmount()) {
            throw new IllegalArgumentException("Invalid chunkId");
        }

        try {
            Integer chunkSize = this.sourcefile.getChunkSize();
            Integer from = chunkId * chunkSize;
            byte[] data = getChunk(from, chunkSize);
            byte[] sha256 = MessageDigest.getInstance("SHA-1").digest(data);


            return new Chunk(sourcefile, sha256, data, chunkId);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public String getFilePath() throws IOException {
        String sha256sum = this.sourcefile.getSha256();
        Optional<Sourcefile> sourcefileOptional = this.sourcefileController.getSourcefile(sha256sum);
        if(sourcefileOptional.isPresent()) {
            return sourcefileOptional.get().getFile().getCanonicalPath();
        }
        return null;
    }
}
