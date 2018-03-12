package controller;

import model.Sourcefile;

import java.io.*;
import java.util.*;

public class SourcefileController {
    private static final String SOURCEFILE_LIST_PATH = ".sourcefiles";

    private Map<String, Sourcefile> sourcefiles = new Hashtable<>();

    public void loadSourcefiles() throws IOException {
        File sourcefileList = new File(SOURCEFILE_LIST_PATH);
        if(!sourcefileList.exists()) {
            sourcefileList.createNewFile();
        }

        Scanner in = new Scanner(sourcefileList);
        while (in.hasNextLine()) {
            String filePath = in.nextLine();
            File file = new File(filePath);
            if(file.exists()) {
                Sourcefile newSourcefile = new Sourcefile(file);
                this.sourcefiles.put(newSourcefile.getSha256(), newSourcefile);
            }
        }
        in.close();
    }

    public void saveSourcefiles() throws IOException {
        File sourcefileList = new File(SOURCEFILE_LIST_PATH);
        PrintWriter out = new PrintWriter(sourcefileList);
        for(Sourcefile sourcefile : this.sourcefiles.values()) {
            out.println(sourcefile.getFile().getCanonicalPath());
        }
    }


    public Sourcefile getSourcefile(String sha256sum) {
        return this.sourcefiles.get(sha256sum);
    }

    public List<Sourcefile> getAll() {
        return new ArrayList<>(this.sourcefiles.values());
    }
}
