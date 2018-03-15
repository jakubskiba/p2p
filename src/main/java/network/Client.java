package network;

import com.sun.org.apache.regexp.internal.RE;
import controller.PeerController;
import controller.SourcefileController;
import dto.ChunkRequest;
import dto.Request;
import dto.Response;
import model.Chunk;
import model.Manifest;
import model.Sourcefile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private String host;
    private int port;
    private Socket clientSocket;
    private PeerController peerController;
    private SourcefileController sourcefileController;

    public Client(String host, int port, PeerController peerController, SourcefileController sourcefileController) {
        this.host = host;
        this.port = port;
        this.peerController = peerController;
        this.sourcefileController = sourcefileController;
    }

    public Manifest getManifest() throws IOException, ClassNotFoundException {

        Manifest myManifest = this.sourcefileController.getManifest();
        Request request = new Request(myManifest);

        Response response = getResponse(request);

        return response.getManifest();

    }

    public Chunk getChunk(Sourcefile sourcefile, Integer chunkId) throws IOException, ClassNotFoundException {

        ChunkRequest chunkRequest = new ChunkRequest(sourcefile, chunkId);
        Request request = new Request(chunkRequest);

        Response response = getResponse(request);

        return response.getChunk();
    }

    private Response getResponse(Request request) throws IOException, ClassNotFoundException {
        initConnection();
        ObjectOutputStream oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
        oos.writeObject(request);

        ObjectInputStream ois = new ObjectInputStream(this.clientSocket.getInputStream());
        Response response = (Response) ois.readObject();

        oos.close();
        ois.close();
        this.clientSocket.close();

        return response;
    }

    private void initConnection() throws IOException {
        this.clientSocket = new Socket(this.host, this.port);
    }
}
