package model;

import java.net.InetSocketAddress;
import java.util.Objects;

public class Peer {
    private InetSocketAddress address;
    private Manifest manifest;
    private Boolean isConnected;

    public Peer(InetSocketAddress address, Manifest manifest) {
        this.address = address;
        this.manifest = manifest;
        this.isConnected = false;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Peer && this.address.equals( ((Peer) o).address);
    }
}
