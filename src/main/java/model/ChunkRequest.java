package model;

import java.io.Serializable;

public class ChunkRequest implements Serializable{
    private int id;
    private Sourcefile sourcefile;

    public ChunkRequest(Sourcefile sourcefile, int id) {
        this.sourcefile = sourcefile;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sourcefile getSourcefile() {
        return sourcefile;
    }

    public void setSourcefile(Sourcefile sourcefile) {
        this.sourcefile = sourcefile;
    }
}
