package controller;

import enumeration.ChunkStatus;
import model.Manifest;
import model.Sourcefile;

import java.io.*;
import java.util.*;

public class SourcefileController {
    private static final String SOURCEFILE_LIST_PATH = ".sourcefiles";

    private Manifest manifest = new Manifest();

    public void loadSourcefiles() throws IOException {
        File sourcefileList = new File(SOURCEFILE_LIST_PATH);
        if(!sourcefileList.exists()) {
            sourcefileList.createNewFile();
        }

        Scanner in = new Scanner(sourcefileList);
        while (in.hasNextLine()) {
            String filePath = in.nextLine();
            addSourcefileFromFile(filePath);
        }
        in.close();
    }

    public void addSourcefileFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        if(file.exists() && file.isFile()) {
            Sourcefile newSourcefile = new Sourcefile(file);
            this.manifest.addSourcefile(newSourcefile);
        }
    }

    public void addSourcefileFromNet(Sourcefile sourcefile) {
        sourcefile.populateChunkStatus(ChunkStatus.VOID);
        this.manifest.addSourcefile(sourcefile);
    }

    public void saveSourcefiles() throws IOException {
        File sourcefileList = new File(SOURCEFILE_LIST_PATH);
        PrintWriter out = new PrintWriter(sourcefileList);
        for(Sourcefile sourcefile : this.getAll()) {
            out.println(sourcefile.getFile().getCanonicalPath());
        }
    }


    public Sourcefile getSourcefile(String sha256sum) {
        return this.manifest.getSourcefile(sha256sum);
    }

    public List<Sourcefile> getAll() {
        return this.manifest.getAll();
    }
}
