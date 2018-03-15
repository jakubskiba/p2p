package com.codecool.controller;

import com.codecool.enumeration.Form;
import com.codecool.model.Manifest;
import com.codecool.model.Sourcefile;
import com.codecool.network.Server;
import com.codecool.view.UI;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    private PeerController peerController;
    private SourcefileController sourcefileController;
    private DownloadController downloadController;
    private Server server;
    private UI ui;
    private boolean isRunning;

    public MainController(int port, PeerController peerController,
                          SourcefileController sourcefileController,
                          DownloadController downloadController,
                          UI ui) {
        this.peerController = peerController;
        this.sourcefileController = sourcefileController;
        this.downloadController = downloadController;
        this.server = new Server(port, sourcefileController);
        this.ui = ui;
    }

    public void startController() throws IOException, ClassNotFoundException {
        new Thread(server).start();
        this.sourcefileController.loadSourcefiles();
        printManifest();
        isRunning = true;
        while (isRunning) {
            this.peerController.updateAll();
            ui.printMenu();
            Integer choice = ui.getChoice();
            handleChoice(choice);
        }

    }

    private void handleChoice(Integer choice) throws IOException, ClassNotFoundException {
        switch (choice) {
            case 1:
                printManifest();
                break;
                
            case 2:
                addPeer();
                break;

            case 3:
                downloadFile();
                break;
                
            case 0:
                stop();
                break;
        }
    }

    private void downloadFile() {
        List<Sourcefile> sourcefiles = getSourcefiles();
        if(sourcefiles.size() > 0) {
            ui.printSourcefiles(sourcefiles);
            Integer id = ui.getFileId();
            String destinationPath = ui.getDestinationPath();
            Sourcefile fileToDownload = new Sourcefile(sourcefiles.get(id), Form.CHUNKS);
            this.sourcefileController.addSourcefileFromNet(fileToDownload);
            this.downloadController.download(fileToDownload, destinationPath);
        }

    }

    private List<Sourcefile> getSourcefiles() {
        return this.peerController.getAllSourcefiles()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private void addPeer() throws IOException, ClassNotFoundException {
        String host = ui.getHost();
        Integer port = ui.getPort();
        InetSocketAddress address = new InetSocketAddress(host, port);
        this.peerController.updatePeer(address);

    }

    private void stop() throws IOException {
        this.isRunning = false;
        server.stop();
        downloadController.stop();
    }

    private void printManifest() {
        Manifest manifest = this.sourcefileController.getManifest();
        ui.printManifest(manifest);
    }
}
