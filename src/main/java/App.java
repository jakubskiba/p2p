import model.Chunk;
import model.Sourcefile;
import service.FileDivider;
import service.FileMerger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class App {
    private static final int CHUNK_SIZE = 1024;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String fromPath = "/home/kyubu/DSC08863.JPG";
        String toPath = "/home/kyubu/DSC08863-x.JPG";

        File file = new File(fromPath);
        Sourcefile sourcefile = new Sourcefile(file);

        File chunksDir = new File(".chunks/");
        if(!chunksDir.exists()) {
            chunksDir.mkdir();
        }

        File directory = new File(".chunks/" + sourcefile.getSha256() +"/");
        if(!directory.exists()) {
            directory.mkdir();
        }

        int chunkAmount = (int) sourcefile.getFile().length() / CHUNK_SIZE;

        //divide file
        FileDivider fileDivider = new FileDivider(sourcefile);

        for(Integer i = 0; i<chunkAmount; i++) {

            long from = getOffset(i);
            long to = getOffset(i) + CHUNK_SIZE;

            File chunkFile = new File(directory.getCanonicalPath() + "/" + i.toString());
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(chunkFile));
            Chunk chunk = fileDivider.createChunk(from, to);
            oos.writeObject(chunk);
            oos.close();
        }

        //merge file
        new FileMerger().mergeChunks(sourcefile.getSha256(), toPath);

    }



    private static int getOffset(int chunkNo) {
        return chunkNo * CHUNK_SIZE;
    }
}
