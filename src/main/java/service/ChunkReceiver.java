package service;

import model.Chunk;
import model.ChunkEnvelope;
import model.ChunkRequest;
import model.Sourcefile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ChunkReceiver {
    private int port;
    private String host;

    public ChunkReceiver(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public Chunk getChunk(Sourcefile sourcefile, int chunkId) {
        Chunk chunk = null;

        try (
                Socket serverSocket = new Socket(host, port)
        ) {
            ChunkRequest chunkRequest = new ChunkRequest(sourcefile, chunkId);
            this.sendRequest(serverSocket, chunkRequest);
            chunk = this.receiveChunk(serverSocket);
        } catch (SocketException e) {
            System.out.println("Client disconnected");

        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            System.exit(1);

        }

        return chunk;
    }

    private Chunk receiveChunk(Socket serverSocket) {
        Chunk chunk = null;
        try (ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream())) {

            ChunkEnvelope chunkEnvelope =  (ChunkEnvelope) in.readObject();
            chunk = chunkEnvelope.getChunk();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chunk;
    }

    private void sendRequest(Socket serverSocket, ChunkRequest chunkRequest) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(serverSocket.getOutputStream())) {
            out.writeObject(chunkRequest);
            out.flush();
        }
    }
}
