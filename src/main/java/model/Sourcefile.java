package model;

import service.FileDivider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Sourcefile {
    private File file;
    private String sha256;

    public Sourcefile(File file) throws IOException {
        this.file = file;
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

    public static void main(String[] args) {
        try {
            Sourcefile sourcefile = new Sourcefile(new File("/home/kyubu/DSC08863.JPG"));
            Chunk chunk = new FileDivider(sourcefile).createChunk(0, 1024);
            System.out.println(chunk.isValid());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
