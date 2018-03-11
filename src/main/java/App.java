import model.ChunkContainer;
import model.Sourcefile;
import service.FileDivider;

import java.io.*;
import java.util.TreeMap;

public class App {
    private static final int CHUNK_SIZE = 1024;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File file = new File("/home/kyubu/DSC08863.JPG");
        Sourcefile sourcefile = new Sourcefile(file);

        FileDivider fileDivider = new FileDivider(sourcefile);
        ChunkContainer chunkContainer;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("/home/kyubu/cc.ser")));
            chunkContainer = (ChunkContainer) ois.readObject();
            System.out.println("cc loaded from file");
        } catch (FileNotFoundException e) {
            chunkContainer = new ChunkContainer(new TreeMap<>(), sourcefile);
            System.out.println("cc created");
        }

        int chunkAmmount = (int) sourcefile.getFile().length() / CHUNK_SIZE;

        int changeThisVariableTo0And1 = 0;

        for(int i = 0; i<chunkAmmount; i++) {
            if(i % 2 == changeThisVariableTo0And1) {
                System.out.println(i);
                long from = getOffset(i);
                long to = getOffset(i) + CHUNK_SIZE;
                chunkContainer.addChunk(fileDivider.createChunk(from, to));
            }
        }

        System.out.println(chunkContainer.isFullified());

        byte[] data = chunkContainer.merge();

        FileOutputStream fileOutputStream = new FileOutputStream(new File("/home/kyubu/DSC08863-x.JPG"));
        fileOutputStream.write(data);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/home/kyubu/cc.ser")));
        oos.writeObject(chunkContainer);
    }

    private static int getOffset(int chunkNo) {
        return chunkNo * CHUNK_SIZE;
    }
}
