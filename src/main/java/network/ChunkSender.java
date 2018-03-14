package network;

import model.Chunk;
import dto.ChunkEnvelope;
import dto.ChunkRequest;
import model.Sourcefile;
import service.FileDivider;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChunkSender {
    private int port;
    private Sourcefile sourcefile;
    private ServerSocket serverSocket;
    private Socket socket;

    public ChunkSender(String filePath, int port) throws IOException {
        this.sourcefile = new Sourcefile(new File(filePath));
        this.port = port;
    }

    public void start() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
        while (true) {
            System.out.println("waiting for client: ");
            this.socket = this.serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
            try {
                ChunkRequest chunkRequest = (ChunkRequest) ois.readObject();

                Chunk chunk = new FileDivider(chunkRequest.getSourcefile()).createChunk(chunkRequest.getId());
                ChunkEnvelope chunkEnvelope = new ChunkEnvelope(chunk);

                ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
                oos.writeObject(chunkEnvelope);
                oos.close();
                ois.close();
            } catch (ClassNotFoundException e) {
                ois.close();
            }
            this.socket.close();
        }
    }

    public Sourcefile getSourcefile() {
        return sourcefile;
    }
}
