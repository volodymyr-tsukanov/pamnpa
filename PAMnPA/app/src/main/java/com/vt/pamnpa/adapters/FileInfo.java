package com.vt.pamnpa.adapters;

public class FileInfo {
    int size;
    String type;

    public FileInfo(int size, String type){
        this.size = size;
        this.type = type;
    }

    public int getSize() {
        return size;
    }
    public String getType() {
        return type;
    }
}
