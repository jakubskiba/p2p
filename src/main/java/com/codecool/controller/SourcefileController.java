package com.codecool.controller;

import com.codecool.enumeration.ChunkStatus;
import com.codecool.model.Manifest;
import com.codecool.model.Sourcefile;
import com.codecool.network.Server;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

public class SourcefileController {
    private static final String SOURCEFILE_LIST_PATH = ".sourcefiles";
    private static final Logger logger = Logger.getLogger(Server.class);

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
        logger.info("sourcefiles loaded");
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


    public Optional<Sourcefile> getSourcefile(String sha256sum) {
        return Optional.ofNullable(this.manifest.getSourcefile(sha256sum));
    }

    public List<Sourcefile> getAll() {
        return this.manifest.getAll();
    }

    public Manifest getManifest() {
        return manifest;
    }
}
