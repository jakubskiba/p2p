package service;

import model.Chunk;
import model.Sourcefile;

import java.io.*;

public class ChunkManager {
    private static final String CHUNKS_DIR_PATH = ".chunks/";


    public void saveChunk(Chunk chunk) throws IOException {
        createDirIfNotExist(CHUNKS_DIR_PATH);
        String directoryPath = CHUNKS_DIR_PATH +
                chunk.getSourcefile().getSha256() + "/";
        File directory = createDirIfNotExist(directoryPath);

        File chunkFile = new File(directory.getCanonicalPath() + "/" + chunk.getChunkId());
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chunkFile));
        oos.writeObject(chunk);
        oos.close();
    }

    private File createDirIfNotExist(String path) {
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }
}
