package dto;

import enumeration.RequestType;
import model.Manifest;

import java.io.*;

public class Request implements Serializable {
    private static final long serialVersionUID = -6378619089115000184L;

    private RequestType type;
    private byte[] data;

    public Request(ChunkRequest chunkRequest) throws IOException {
        this.type = RequestType.CHUNK_REQUEST;
        this.data = serializeToByteArray(chunkRequest);
    }

    public Request(Manifest manifest) throws IOException {
        this.type = RequestType.MANIFEST_REQUEST;
        this.data = serializeToByteArray(manifest);
    }

    private byte[] serializeToByteArray(Object object) throws IOException {
        byte[] data;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(object);
        data = baos.toByteArray();

        oos.close();
        baos.close();

        return data;
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

    private Object deserializeData() throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(this.data);
        ObjectInputStream oos = new ObjectInputStream(bais);
        return oos.readObject();
    }

    public Manifest getManifest() throws IOException, ClassNotFoundException {
        Object deserializedObject = deserializeData();
        if(deserializedObject instanceof Manifest) {
            return (Manifest) deserializedObject;
        } else {
            throw new IllegalStateException("Data is not manifest!");
        }
    }

    public ChunkRequest getChunkRequest() throws IOException, ClassNotFoundException {
        Object deserializedObject = deserializeData();
        if(deserializedObject instanceof ChunkRequest) {
            return (ChunkRequest) deserializedObject;
        } else {
            throw new IllegalStateException("Data is not manifest!");
        }
    }
}
