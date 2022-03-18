package com.aoedb.data;

import com.aoedb.database.Database;

import java.util.LinkedHashMap;
import java.util.List;

public class Building extends Item {

    protected LinkedHashMap<String, List<EntityElement>> trainable;

    public Building(String language) {
        super(language);
    }

    public Building(Building b){
        super(b);
        this.trainable = b.getTrainable();
        resetStats();
    }

    public EntityElement getRequiredBuildingElement(){
        return getEntityElement(Database.getString("required_building", language));
    }

    public EntityElement getCreatorElement(){
        return getEntityElement(Database.getString("builder_unit", language));
    }

    public LinkedHashMap<String, List<EntityElement>> getTrainable(){
        return trainable;
    }

    public void setTrainable(LinkedHashMap<String, List<EntityElement>> map) {
        trainable = map;
    }

}
