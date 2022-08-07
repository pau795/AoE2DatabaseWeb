package com.aoedb.data;


import com.aoedb.database.Database;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;

public class EntityElement implements Serializable{
    private final int id;
    private final StringKey name;
    private final String image;
    private final String media;
    private final String type;

    public EntityElement(int id, String name, String image, String gif, String type) {
        this.id = id;
        this.name = new StringKey(name);
        this.image = image;
        this.media = gif;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public StringKey getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getMedia() {
        return media;
    }

    public String getType() {
        return type;
    }


    public static Comparator<EntityElement> getListElementComparator(HashMap<Integer, Integer> orderMap){

        return Comparator.comparing(o -> orderMap.get(o.getId()));
    }

    public static Comparator<EntityElement> getAlphabeticalComparator(String language){
        return Comparator.comparing(entityElement -> entityElement.getName().getTranslatedString(language));
    }
}
