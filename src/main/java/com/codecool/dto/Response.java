package com.codecool.dto;

import com.codecool.enumeration.ResponseType;
import com.codecool.model.Chunk;
import com.codecool.model.Manifest;
import com.codecool.service.ByteArraySerializer;

import java.io.IOException;
import java.io.Serializable;

public class Response implements Serializable {
    private ResponseType responseType;
    private byte[] data;

    public Response(String errorMessage) throws IOException {
        this.responseType = ResponseType.ERROR;
        this.data = ByteArraySerializer.serializeToByteArray(errorMessage);
    }

    public Response(Chunk chunk) throws IOException {
        this.responseType = ResponseType.CHUNK;
        this.data = ByteArraySerializer.serializeToByteArray(chunk);
    }

    public Response(Manifest manifest) throws IOException {
        this.responseType = ResponseType.MANIFEST;
        this.data = ByteArraySerializer.serializeToByteArray(manifest);
    }

    public String getErrorMessage() throws IOException, ClassNotFoundException {
        Object deserializedObject = ByteArraySerializer.deserializeData(this.data);
        if(deserializedObject instanceof String) {
            return (String) deserializedObject;
        } else {
            throw new IllegalStateException("Data is not manifest!");
        }
    }

    public Chunk getChunk() throws IOException, ClassNotFoundException {
        Object deserializedObject = ByteArraySerializer.deserializeData(this.data);
        if(deserializedObject instanceof Chunk) {
            return (Chunk) deserializedObject;
        } else {
            throw new IllegalStateException("Data is not chunk!");
        }
    }

    public Manifest getManifest() throws IOException, ClassNotFoundException {
        Object deserializedObject = ByteArraySerializer.deserializeData(this.data);
        if(deserializedObject instanceof Manifest) {
            return (Manifest) deserializedObject;
        } else {
            throw new IllegalStateException("Data is not manifest!");
        }
    }
}
