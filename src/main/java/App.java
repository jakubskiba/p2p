import model.ChunkContainer;
import model.Sourcefile;
import service.FileDivider;

import java.io.*;
import java.util.TreeMap;

public class App {
    private static final int CHUNK_SIZE = 1024;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String fromPath = "/home/kyubu/DSC08863.JPG";
        String toPath = "/home/kyubu/DSC08863-x.JPG";
        String chunkContainerPath = "/home/kyubu/cc.ser";

        File file = new File(fromPath);
        Sourcefile sourcefile = new Sourcefile(file);

        FileDivider fileDivider = new FileDivider(sourcefile);
        ChunkContainer chunkContainer;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(chunkContainerPath)));
            chunkContainer = (ChunkContainer) ois.readObject();
            System.out.println("cc loaded from file");
        } catch (FileNotFoundException e) {
            chunkContainer = new ChunkContainer(new TreeMap<>(), sourcefile);
            System.out.println("cc created");
        }

        int chunkAmount = (int) sourcefile.getFile().length() / CHUNK_SIZE;

        int changeThisVariableTo0And1 = 0;


        //Simulation of incomplete transfer
        for(int i = 0; i<chunkAmount; i++) {
            if(i % 2 == changeThisVariableTo0And1) {
//                System.out.println(i);
                long from = getOffset(i);
                long to = getOffset(i) + CHUNK_SIZE;
                chunkContainer.addChunk(fileDivider.createChunk(from, to));
            }
        }

        //run second time and change variable from line 31 to 1 to fullify file

        System.out.println(chunkContainer.isFullified());

        byte[] data = chunkContainer.merge();

        FileOutputStream fileOutputStream = new FileOutputStream(new File(toPath));
        fileOutputStream.write(data);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(chunkContainerPath)));
        oos.writeObject(chunkContainer);
    }

    private static int getOffset(int chunkNo) {
        return chunkNo * CHUNK_SIZE;
    }
}
