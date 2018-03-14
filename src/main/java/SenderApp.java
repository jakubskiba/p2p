import network.ChunkSender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SenderApp {
    public static void main(String[] args) throws IOException {
        int port = 12345;
        String path = "/home/kyubu/Downloads/The.Big.Bang.Theory.S11E13.HDTV.x264-SVA[ettv]/The.Big.Bang.Theory.S11E13.HDTV.x264-SVA[ettv].mkv";
        ChunkSender chunkSender = new ChunkSender(path, port);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/home/kyubu/sf.ser"));
        oos.writeObject(chunkSender.getSourcefile());
        oos.close();

        chunkSender.start();
    }
}
