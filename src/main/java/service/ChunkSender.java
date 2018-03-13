package service;

import model.Chunk;
import model.ChunkEnvelope;
import model.ChunkRequest;
import model.Sourcefile;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ChunkSender {

    private static int port = 8000;
    private Sourcefile sourceFile;

    {
        try {
            sourceFile = new Sourcefile(new File("ubuntu-17.10.1-desktop-amd64.iso"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        boolean isActive = true;

        while (isActive) {
            System.out.println("Waiting for client...");


            try (
                    ServerSocket serverSocket = new ServerSocket(port);
                    Socket clientSocket = serverSocket.accept()
            ) {
                ChunkRequest chunkRequest = this.receiveRequest(clientSocket);
                this.send(clientSocket, chunkRequest);
            } catch (SocketException e) {
                System.out.println("Client disconnected");

            } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                    System.exit(1);

            }
        }
    }

    private ChunkRequest receiveRequest(Socket clientSocket) {
        ChunkRequest chunkRequest = null;
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            chunkRequest =  (ChunkRequest) in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chunkRequest;
    }

    private void send(Socket clientSocket, ChunkRequest chunkRequest) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
            System.out.println("Connection established");
            long from = chunkRequest.getFrom();
            long to = chunkRequest.getTo();

            Chunk chunk = new FileDivider(this.sourceFile).createChunk(from, to);
            ChunkEnvelope chunkEnvelope = new ChunkEnvelope(chunk);
            out.writeObject(chunkEnvelope);
            out.flush();

        }
    }
}