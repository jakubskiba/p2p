import controller.DownloadController;
import controller.MainController;
import controller.PeerController;
import controller.SourcefileController;
import model.Chunk;
import model.Sourcefile;
import service.ChunkManager;
import service.FileDivider;
import service.FileMerger;
import view.UI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
