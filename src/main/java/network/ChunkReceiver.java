package network;

import model.Chunk;
import dto.ChunkEnvelope;
import dto.ChunkRequest;
import model.Sourcefile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class ChunkReceiver {
    private String host;
    private int port;
    private Socket clientSocket;

    public ChunkReceiver(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Optional<Chunk> receive(Sourcefile sourcefile, int chunkId) throws IOException {
        Optional<Chunk> chunk = Optional.ofNullable(null);

        this.clientSocket = new Socket(this.host, this.port);
        ObjectOutputStream oos = new ObjectOutputStream(this.clientSocket.getOutputStream());

        ChunkRequest chunkRequest = new ChunkRequest(sourcefile, chunkId);
        oos.writeObject(chunkRequest);


        ObjectInputStream ois = new ObjectInputStream(this.clientSocket.getInputStream());
        try {
            ChunkEnvelope chunkEnvelope = (ChunkEnvelope) ois.readObject();
            chunk = chunkEnvelope.getChunk();
        } catch (ClassNotFoundException e) {
        }

        ois.close();
        oos.close();

        return chunk;
    }
}
