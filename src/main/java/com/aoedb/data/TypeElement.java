package com.aoedb.data;

public class TypeElement {

    private final String name;
    private final int id;
    private final String image;
    private final String value;

    public TypeElement(EntityElement e, String value){
        this.id = e.getId();
        this.name = e.getName();
        this.image = e.getImage();
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getValue() {
        return value;
    }

    public int getID() {
        return id;
    }
}
