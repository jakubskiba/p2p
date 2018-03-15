package com.codecool;

import com.codecool.controller.DownloadController;
import com.codecool.controller.MainController;
import com.codecool.controller.PeerController;
import com.codecool.controller.SourcefileController;
import com.codecool.service.ChunkManager;
import com.codecool.service.FileMerger;
import com.codecool.view.UI;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SourcefileController sourcefileController = new SourcefileController();
        PeerController peerController = new PeerController(sourcefileController);
        ChunkManager chunkManager = new ChunkManager();
        FileMerger fileMerger = new FileMerger();
        DownloadController downloadController = new DownloadController(5, peerController, sourcefileController, chunkManager, fileMerger);
        UI ui = new UI(new Scanner(System.in));
        int port = 12345;
        new MainController(port, peerController, sourcefileController, downloadController, ui).startController();
    }

}
