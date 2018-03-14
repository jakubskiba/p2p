package model;

import enumeration.ChunkStatus;
import enumeration.Form;

import java.io.*;
import java.util.stream.Stream;

public class Sourcefile implements Serializable {
    private static final int CHUNK_SIZE = 1024;
    private static final long serialVersionUID = -1626632555858035641L;

    private File file;
    private int chunkSize;
    private int chunkAmount;
    private String sha256;
    private Form form;
    private ChunkStatus[] chunkStatus;

    public Sourcefile(File file) throws IOException {
        this.file = file;
        this.chunkSize = CHUNK_SIZE;
        this.chunkAmount = (int) Math.ceil(file.length() / chunkSize);
        this.sha256 = computeSHA();
        this.chunkStatus = populateChunkStatus(ChunkStatus.COMPLETE);
        this.form = Form.FILE;
    }

    public ChunkStatus[] populateChunkStatus(ChunkStatus status) {
        ChunkStatus[] list = new ChunkStatus[this.chunkAmount];

        for(int i = 0; i < chunkAmount; i++) {
            list[i] = status;
        }

        return list;
    }

    public boolean isComplete() {
        return Stream.of(this.chunkStatus)
                .filter(status -> status != ChunkStatus.COMPLETE)
                .findAny()
                .isPresent();
    }

    @Override
    public String toString() {
        return file.toString() + " " + sha256;
    }

    public String computeSHA() throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("No file!");
        }
        System.out.println("computing sha of: " + this.file.getName());
        Process p = Runtime.getRuntime().exec("sha256sum "+this.file.getCanonicalPath());
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));
        return stdInput.readLine().split(" ")[0];
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public ChunkStatus getChunkStatus(Integer chunkNumber) {
        checkChunkExistence(chunkNumber);
        return this.chunkStatus[chunkNumber];
    }

    public void setChunkStatus(Integer chunkNumber, ChunkStatus chunkStatus) {
        checkChunkExistence(chunkNumber);
        this.chunkStatus[chunkNumber] = chunkStatus;
    }

    private void checkChunkExistence(Integer chunkNumber) {
        if(chunkNumber > this.chunkAmount || chunkNumber < 0) {
            throw new IllegalArgumentException("No such chunk");
        }
    }
}
