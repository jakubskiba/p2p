package com.codecool.service;

import java.io.*;

public class ByteArraySerializer {
    public static byte[] serializeToByteArray(Object object) throws IOException {
        byte[] data;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(object);
        data = baos.toByteArray();

        oos.close();
        baos.close();

        return data;
    }

    public static Object deserializeData(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream oos = new ObjectInputStream(bais);
        Object object = oos.readObject();

        oos.close();

        return object;
    }
}
