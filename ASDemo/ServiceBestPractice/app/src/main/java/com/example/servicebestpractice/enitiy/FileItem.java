package com.example.servicebestpractice.enitiy;

// FileItem.java
public class FileItem {
    private String name;
//    private String extension;
    private double size;

    public FileItem(String name,double size) {
        this.name = name;
//        this.extension = extension;
        this.size = size;
    }

    public String getName() {
        return name;
    }

//    public String getExtension() {
//        return extension;
//    }

    public double getSize() {
        return size;
    }
}
