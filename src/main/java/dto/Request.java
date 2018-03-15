package dto;

import enumeration.RequestType;
import model.Manifest;
import service.ByteArraySerializer;

import java.io.*;

public class Request implements Serializable {
    private static final long serialVersionUID = 3251874664011502924L;
    private RequestType type;
    private byte[] data;

    public Request(ChunkRequest chunkRequest) throws IOException {
        this.type = RequestType.CHUNK_REQUEST;
        this.data = ByteArraySerializer.serializeToByteArray(chunkRequest);
    }

    public Request(Manifest manifest) throws IOException {
        this.type = RequestType.MANIFEST_REQUEST;
        this.data = ByteArraySerializer.serializeToByteArray(manifest);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Manifest getManifest() throws IOException, ClassNotFoundException {
        Object deserializedObject = ByteArraySerializer.deserializeData(this.data);
        if(deserializedObject instanceof Manifest) {
            return (Manifest) deserializedObject;
        } else {
            throw new IllegalStateException("Data is not manifest!");
        }
    }

    public ChunkRequest getChunkRequest() throws IOException, ClassNotFoundException {
        Object deserializedObject = ByteArraySerializer.deserializeData(this.data);
        if(deserializedObject instanceof ChunkRequest) {
            return (ChunkRequest) deserializedObject;
        } else {
            throw new IllegalStateException("Data is not chunk request!");
        }
    }
}
