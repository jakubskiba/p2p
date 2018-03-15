package com.codecool.controller;

import com.codecool.enumeration.ChunkStatus;
import com.codecool.model.Manifest;
import com.codecool.model.Peer;
import com.codecool.model.Sourcefile;
import com.codecool.network.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PeerController {
    private SourcefileController sourcefileController;
    private Set<Peer> peers = new HashSet<>();

    public PeerController(SourcefileController sourcefileController) {
        this.sourcefileController = sourcefileController;
    }

    private void addManifest(InetSocketAddress host, Manifest manifest) {
        this.peers.add(new Peer(host, manifest));
    }

    public void updatePeer(InetSocketAddress host) throws IOException, ClassNotFoundException {
        Client client = new Client(host.getHostName(), host.getPort(), this, sourcefileController);
        Manifest manifest = client.getManifest();
        addManifest(host, manifest);
    }

    public void updateAll() throws IOException, ClassNotFoundException {
        List<InetSocketAddress> addresses = this.peers.stream()
                                                .map(Peer::getAddress)
                                                .collect(Collectors.toList());
        for(InetSocketAddress host : addresses) {
            updatePeer(host);
        }
    }

    public List<Sourcefile> getAllSourcefiles() {
        List<Manifest> manifests = this.peers.stream()
                                            .map(Peer::getManifest)
                                            .collect(Collectors.toList());
        List<Sourcefile> sourcefiles = new ArrayList<>();
        for(Manifest manifest : manifests) {
            sourcefiles.addAll(manifest.getAll());
        }

        return sourcefiles;
    }

    public List<Peer> getOwners(String sha256sum, Integer chunkId) {
        List<Peer> owners = new ArrayList<>();
        for(Peer peer : this.peers) {
            Manifest manifest = peer.getManifest();
            for(Sourcefile sourcefile : manifest.getAll()) {
                if(sourcefile.getSha256().equals(sha256sum)
                        && sourcefile.getChunkStatus(chunkId) == ChunkStatus.COMPLETE) {
                    owners.add(peer);
                }
            }
        }

        return owners;
    }
}
