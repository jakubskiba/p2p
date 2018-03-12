package service;

import model.Chunk;
import model.Sourcefile;

import java.io.*;

public class ChunkManager {
    private static final String CHUNKS_DIR_PATH = ".chunks/";


    public void saveChunk(Chunk chunk) throws IOException {
        createDirIfNotExist(CHUNKS_DIR_PATH);
        String directoryPath = getChunkDirPath(chunk.getSourcefile().getSha256());
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

    private File createDirIfNotExist(String path) {
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }
}
