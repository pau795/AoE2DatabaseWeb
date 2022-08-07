package com.aoedb.data;


import java.util.LinkedHashMap;
import java.util.List;

public class Building extends Item {

    protected LinkedHashMap<StringKey, List<EntityElement>> trainable;

    public Building() {
        super();
    }

    public Building(Building b){
        super(b);
        this.trainable = b.getTrainable();
        resetStats();
    }

    public EntityElement getRequiredBuildingElement(){
        return getEntityElement("required_building");
    }

    public EntityElement getCreatorElement(){
        return getEntityElement("builder_unit");
    }

    public LinkedHashMap<StringKey, List<EntityElement>> getTrainable(){
        return trainable;
    }

    public void setTrainable(LinkedHashMap<StringKey, List<EntityElement>> map) {
        trainable = map;
    }

}
