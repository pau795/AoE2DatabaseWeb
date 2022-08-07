package com.aoedb.data;

public class TauntElement {

    private final StringKey name;
    private final int id;
    private final String filePath;

    public TauntElement(int id, String name, String filePath) {
        this.id = id;
        this.name = new StringKey(name);
        this.filePath = filePath;
    }

    public int getID() {
        return id;
    }

    public StringKey getName() {
        return name;
    }


    public String getFilePath() {
        return filePath;
    }
}
