package service;

import model.Chunk;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class FileMerger {
    public void mergeChunks(String sha256sum, String destinationPath) throws IllegalArgumentException, IOException, ClassNotFoundException {
        File folder = new File(".chunks/"+sha256sum+"/");
        if(!folder.exists()) {
            throw new IllegalArgumentException("No such file");
        }

        FileOutputStream out = new FileOutputStream(destinationPath);

        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles, Comparator.comparing(file -> Integer.valueOf(file.getName())));

        for(File chunkFile : listOfFiles) {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(chunkFile));

            Chunk chunk = (Chunk) in.readObject();

            out.write(chunk.getData());
            in.close();
        }
        out.close();
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File f = new File(".chunks/240725e94554e735000486d62ce0e5e707e866482d75285666eac3c7b203797c");
        f.mkdir();
        new FileMerger().mergeChunks("240725e94554e735000486d62ce0e5e707e866482d75285666eac3c7b203797c", "/home/kyubu/a.a");
        
    }
}
