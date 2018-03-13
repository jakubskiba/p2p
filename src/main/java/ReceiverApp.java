import model.Chunk;
import model.Sourcefile;
import service.ChunkManager;
import service.ChunkReceiver;
import service.FileMerger;

import java.io.*;

public class ReceiverApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("sf.ser"));
        Sourcefile sf = (Sourcefile) ois.readObject();
        ois.close();

        ChunkManager chunkManager = new ChunkManager();
        ChunkReceiver chunkReceiver = new ChunkReceiver(8000, "192.168.10.77");
        int chunkAmount = sf.getChunkAmount();
        for(int i = 0; i< chunkAmount; i++) {
            Chunk receivedChunk = chunkReceiver.getChunk(sf, i);
            chunkManager.saveChunk(receivedChunk);
        }

        new FileMerger().mergeChunks(sf.getSha256(), "ubuntu.iso");
    }
}
