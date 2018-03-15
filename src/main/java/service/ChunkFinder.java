package service;

import controller.SourcefileController;
import dto.ChunkRequest;
import model.Chunk;
import model.Sourcefile;

import java.io.IOException;
import java.util.Optional;

public class ChunkFinder {
    private SourcefileController sourcefileController;

    public ChunkFinder(SourcefileController sourcefileController) {
        this.sourcefileController = sourcefileController;
    }

    public Optional<Chunk> find(ChunkRequest chunkRequest) throws IOException {
        Integer chunkId = chunkRequest.getId();
        String sha256sum = chunkRequest.getSourcefile().getSha256();
        Optional<Sourcefile> sourcefile = this.sourcefileController.getSourcefile(sha256sum);
        if(sourcefile.isPresent()) {
            return Optional.of(new FileDivider(sourcefile.get()).createChunk(chunkId));
        } else {
            return Optional.empty();
        }
    }
}
