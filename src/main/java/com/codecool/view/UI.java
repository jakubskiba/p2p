package com.codecool.view;

import com.codecool.model.Manifest;
import com.codecool.model.Sourcefile;

import java.util.List;
import java.util.Scanner;

public class UI {
    private Scanner in;

    public UI(Scanner in) {
        this.in = in;
    }

    public void printManifest(Manifest manifest) {
        for(Sourcefile sourcefile : manifest.getAll()) {
            System.out.println(sourcefile);
        }
    }

    public void printSourcefiles(List<Sourcefile> sourcefileList) {
        for(int i = 0; i<sourcefileList.size(); i++) {
            Sourcefile sourcefile = sourcefileList.get(i);
            System.out.printf("%d, %s%n", i, sourcefile);
        }
    }

    public Integer getFileId() {
        System.out.print("provide id: ");
        return getInteger();
    }

    public void printMenu() {
        String menu = "1. Print manifest\n"+
        "2. Add new peer\n" +
        "3. Download file\n" +
        "0. Exit\n";
        System.out.println(menu);
    }

    public Integer getChoice() {
        return getInteger();
    }

    public String  getHost() {
        System.out.print("provide host: ");
        return in.nextLine();
    }

    public Integer getPort() {
        System.out.print("provide port: ");
        return getInteger();
    }

    private Integer getInteger() {
        String line = in.nextLine();
        return Integer.valueOf(line);
    }

    public String getDestinationPath() {
        System.out.print("provide destination path: ");
        return in.nextLine();
    }
}
