package com.codecool.service;

import com.codecool.model.Chunk;
import com.codecool.model.Sourcefile;

import java.io.*;

public class ChunkManager {
    private static final String CHUNKS_DIR_PATH = ".chunks/";


    public void saveChunk(Chunk chunk) throws IOException {
        createDirIfNotExist(CHUNKS_DIR_PATH);
        Sourcefile sourcefile = chunk.getSourcefile();
        String directoryPath = getChunkDirPath(sourcefile.getSha256());
        File directory = createDirIfNotExist(directoryPath);

        File chunkFile = new File(directory.getCanonicalPath() + "/" + chunk.getChunkId());
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chunkFile));
        oos.writeObject(chunk);
        oos.close();
    }

    private String getChunkDirPath(String sha256sum) {
        return CHUNKS_DIR_PATH +
                sha256sum + "/";
    }

    public boolean hasAllChunks(Sourcefile sourcefile) {
        String sha256sum = sourcefile.getSha256();
        String directoryPath = getChunkDirPath(sha256sum);

        for(int i = 0; i<sourcefile.getChunkAmount(); i++) {
            File chunkFile = new File(directoryPath + String.valueOf(i));
            if(!chunkFile.exists()) {
                return false;
            }
        }
        return true;
    }

    public void removeAllChunks(Sourcefile sourcefile) {
        String sha256sum = sourcefile.getSha256();
        String directoryPath = getChunkDirPath(sha256sum);

        for(int i = 0; i<sourcefile.getChunkAmount(); i++) {
            File chunkFile = new File(directoryPath + String.valueOf(i));
            if(chunkFile.exists()) {
                chunkFile.delete();
            }
        }
    }

    private File createDirIfNotExist(String path) {
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }
}
