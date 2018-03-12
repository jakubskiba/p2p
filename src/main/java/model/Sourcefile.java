package model;

import service.FileDivider;

import java.io.*;

public class Sourcefile implements Serializable {
    private static final int CHUNK_SIZE = 1024;

    private File file;
    private int chunkSize;
    private int chunkAmount;
    private String sha256;

    public Sourcefile(File file) throws IOException {
        this.file = file;
        this.chunkSize = CHUNK_SIZE;
        this.chunkAmount = (int) Math.ceil(file.length() / chunkSize);
        this.sha256 = computeSHA();
    }

    public Sourcefile(String fileName, String sha256) {
        this.file = new File(fileName);
        this.sha256 = sha256;
    }

    @Override
    public String toString() {
        return file.toString() + " " + sha256;
    }

    public String computeSHA() throws IOException {
        Process p = Runtime.getRuntime().exec("sha256sum "+this.file.getCanonicalPath());
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));
        return stdInput.readLine().split(" ")[0];
    }

    public File getFile() {
        return file;
    }

    public String getSha256() {
        return sha256;
    }

    public int getChunkAmount() {
        return chunkAmount;
    }

    public int getChunkSize() {
        return chunkSize;
    }
}
