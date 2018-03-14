import model.Chunk;
import model.Sourcefile;
import network.ChunkReceiver;
import service.ChunkManager;
import service.FileMerger;

import java.io.*;

public class ReceiverApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/home/kyubu/sf.ser"));
        Sourcefile sf = (Sourcefile) ois.readObject();
//        ois.close();

        ChunkManager chunkManager = new ChunkManager();
        ChunkReceiver chunkReceiver = new ChunkReceiver( "localhost", 12345);

        int chunkAmount = sf.getChunkAmount();
        for(int i = 0; i<= chunkAmount; i++) {
            System.out.printf("receiving chunk %d of %d%n", i, chunkAmount);
            Chunk receivedChunk = chunkReceiver.receive(sf, i);
            if(receivedChunk != null && receivedChunk.isValid()) {
                chunkManager.saveChunk(receivedChunk);
            } else {
                i--;
            }
        }

        new FileMerger().mergeChunks(sf.getSha256(), "ubuntu.iso");
    }
}
