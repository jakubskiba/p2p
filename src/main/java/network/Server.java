package network;

import controller.SourcefileController;
import dto.ChunkRequest;
import dto.Request;
import dto.Response;
import model.Chunk;
import model.Manifest;
import org.apache.log4j.Logger;
import service.ChunkFinder;


import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class Server implements Runnable {
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean isRunning;
    private ChunkFinder chunkFinder;
    private SourcefileController sourcefileController;
    private static final Logger logger = Logger.getLogger(Server.class);

    public Server(int port, SourcefileController sourcefileController) {
        this.port = port;
        this.chunkFinder = new ChunkFinder(sourcefileController);
        this.sourcefileController = sourcefileController;
    }

    public void run() {
        logger.info("Start server on port: " + port);
        try {
            startServer();
        } catch (ClassNotFoundException | IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void startServer() throws IOException, ClassNotFoundException {
        this.serverSocket = new ServerSocket(this.port);
        this.serverSocket.setSoTimeout(30000);
        this.isRunning = true;
        while (isRunning) {
            try {
                this.socket = this.serverSocket.accept();
                InetSocketAddress inetSocketAddress = (InetSocketAddress) this.socket.getRemoteSocketAddress();
                ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
                Request request = (Request) ois.readObject();

                ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());

                handleRequest(request, oos, inetSocketAddress);

                ois.close();
                oos.close();
            } catch (InterruptedIOException e) {
                logger.error(e.getMessage());
            }
        }
        logger.info("Stop server");
    }

    public void stop() throws IOException {
        this.isRunning = false;
    }

    private void handleRequest(Request request, ObjectOutputStream oos, InetSocketAddress inetSocketAddress) throws IOException, ClassNotFoundException {
        Response response = new Response("Bad request");
        logger.info("Handle request from " + inetSocketAddress.getHostName());
        switch (request.getType()) {
            case CHUNK_REQUEST:
                response = handleChunkRequest(request);
                break;

            case MANIFEST_REQUEST:
                response = handleManifestRequest(request);
                break;
        }

        oos.writeObject(response);
    }

    private Response handleManifestRequest(Request request) throws IOException {
        Manifest manifest = this.sourcefileController.getManifest();
        return new Response(manifest);
    }

    private Response handleChunkRequest(Request request) throws IOException, ClassNotFoundException {
        ChunkRequest chunkRequest = request.getChunkRequest();
        Optional<Chunk> optionalChunk = this.chunkFinder.find(chunkRequest);

        if(optionalChunk.isPresent()) {
            return new Response(optionalChunk.get());
        } else {
            return new Response("No such chunk!");
        }
    }
}
