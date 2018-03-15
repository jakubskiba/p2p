package com.codecool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Manifest implements Serializable {
    private static final long serialVersionUID = 8708366516871711422L;

    private Map<String, Sourcefile> sourcefiles = new Hashtable<>();

    public void addSourcefile(Sourcefile sourcefile) {
        this.sourcefiles.put(sourcefile.getSha256(), sourcefile);
    }

    public Sourcefile getSourcefile(String sha256sum) {
        return this.sourcefiles.get(sha256sum);
    }

    public boolean contains(String sha256sum) {
        return this.sourcefiles.containsKey(sha256sum);
    }

    public List<Sourcefile> getAll() {
        return new ArrayList<>(sourcefiles.values());
    }
}
