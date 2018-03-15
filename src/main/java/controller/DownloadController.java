package controller;

import model.Sourcefile;
import org.apache.log4j.Logger;
import service.ChunkManager;
import service.FileDownloader;
import service.FileMerger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadController {
    private static final Logger logger = Logger.getLogger(DownloadController.class);
    private PeerController peerController;
    private SourcefileController sourcefileController;
    private ChunkManager chunkManager;
    private FileMerger fileMerger;
    private ExecutorService executorService;

    public DownloadController(int threadPoolSize, PeerController peerController,
                              SourcefileController sourcefileController, ChunkManager chunkManager,
                              FileMerger fileMerger) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.peerController = peerController;
        this.sourcefileController = sourcefileController;
        this.chunkManager = chunkManager;
        this.fileMerger = fileMerger;
    }


    public void stop() {
        this.executorService.shutdown();
    }

    public void download(Sourcefile sourcefile, String destinationPath) {
        FileDownloader fileDownloader = new FileDownloader(sourcefile, this.peerController, this.sourcefileController, this.chunkManager, this.fileMerger, destinationPath);
        executorService.submit(fileDownloader);
    }
}
