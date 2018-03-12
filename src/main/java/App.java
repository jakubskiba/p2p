import controller.SourcefileController;
import model.Chunk;
import model.Sourcefile;
import service.ChunkManager;
import service.FileDivider;
import service.FileMerger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SourcefileController sfController = new SourcefileController();
        sfController.loadSourcefiles();
        sfController.getAll().forEach(System.out::println);

        String fromPath = "/home/kyubu/DSC08863.JPG";
        String toPath = "/home/kyubu/DSC08863-x.JPG";

        File file = new File(fromPath);
        Sourcefile sourcefile = new Sourcefile(file);


        //divide file
        FileDivider fileDivider = new FileDivider(sourcefile);
        ChunkManager chunkManager = new ChunkManager();
        for(Integer i = 0; i<sourcefile.getChunkAmount(); i++) {
            Chunk chunk = fileDivider.createChunk(i);
            chunkManager.saveChunk(chunk);
        }

        //merge file
        if(chunkManager.hasAllChunks(sourcefile)) {
            new FileMerger().mergeChunks(sourcefile.getSha256(), toPath);
            chunkManager.removeAllChunks(sourcefile);
        }


    }

}
