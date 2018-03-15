package com.codecool.service;

import com.codecool.controller.PeerController;
import com.codecool.controller.SourcefileController;
import com.codecool.enumeration.ChunkStatus;
import com.codecool.model.Chunk;
import com.codecool.model.Peer;
import com.codecool.model.Sourcefile;
import com.codecool.network.Client;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class FileDownloader implements Runnable {
    private static final Logger logger = Logger.getLogger(FileDownloader.class);
    private Sourcefile sourcefile;
    private PeerController peerController;
    private SourcefileController sourcefileController;
    private Queue<Integer> chunkIdQueue;
    private ChunkManager chunkManager;
    private FileMerger fileMerger;
    private String destinationPath;

    public FileDownloader(Sourcefile sourcefile, PeerController peerController,
                          SourcefileController sourcefileController, ChunkManager chunkManager,
                          FileMerger fileMerger, String destinationPath) {
        this.sourcefile = sourcefile;
        this.peerController = peerController;
        this.sourcefileController = sourcefileController;
        this.chunkManager = chunkManager;
        this.destinationPath = destinationPath;
        this.fileMerger = fileMerger;
        this.chunkIdQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        populateQueue();
        logger.info("Start download: " + sourcefile.toString());
        while (!sourcefile.isComplete()) {
            String sha256sum = this.sourcefile.getSha256();
            Integer nextChunkId = getNextChunkId();
            logger.info("Search for chunk id: "+nextChunkId.toString());
            sourcefile.setChunkStatus(nextChunkId, ChunkStatus.IN_PROGRESS);
            Optional<Peer> optionalOwner = this.findOwner(sha256sum, nextChunkId);
            if(optionalOwner.isPresent()) {
                downloadChunk(optionalOwner.get(), nextChunkId);
                sourcefile.setChunkStatus(nextChunkId, ChunkStatus.COMPLETE);
            } else {
                this.chunkIdQueue.add(nextChunkId);
                sourcefile.setChunkStatus(nextChunkId, ChunkStatus.VOID);

            }
        }
        String sha256sum = sourcefile.getSha256();
        try {
            String realSha256sum = sourcefile.computeSHA();
            if(sha256sum.equals(realSha256sum)) {
                this.fileMerger.mergeChunks(sha256sum, destinationPath);
                this.chunkManager.removeAllChunks(sourcefile);
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage());
        }

        logger.info(String.format("Download of file %s completed", sourcefile.toString()));
    }

    private void downloadChunk(Peer owner, Integer chunkId) {
        String host = owner.getAddress().getHostName();
        Integer port = owner.getAddress().getPort();
        Client client = new Client(host, port, this.peerController, this.sourcefileController);
        try {
            Chunk chunk = client.getChunk(this.sourcefile, chunkId);
            saveChunkIfValid(chunk);
        }  catch (ClassNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void saveChunkIfValid(Chunk chunk) throws IOException {
        if(chunk != null && chunk.isValid()) {
            this.chunkManager.saveChunk(chunk);
        } else {
            this.chunkIdQueue.add(chunk.getChunkId());
        }
    }

    private void populateQueue() {
        for(int i = 0; i < this.sourcefile.getChunkAmount(); i++) {
            this.chunkIdQueue.add(i);
        }
    }

    private Integer getNextChunkId() {
        return this.chunkIdQueue.poll();
    }

    private Optional<Peer> findOwner(String sha256sum, Integer chunkId) {
        List<Peer> owners = this.peerController.getOwners(sha256sum, chunkId);
        return owners.stream()
                    .filter(owner -> !owner.getConnected())
                    .findAny();
    }

}
