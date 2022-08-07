package com.aoedb.data;


import java.util.LinkedHashMap;
import java.util.List;


public class Unit extends Item {

    protected LinkedHashMap<StringKey, List<EntityElement>> performance;

    public Unit(){
        super();
    }

    public Unit(Unit u){
        super(u);
        this.performance = u.getPerformance();
        resetStats();
    }

    public EntityElement getCreatorElement(){
        return getEntityElement("unit_training_building");
    }

    public void setPerformance(LinkedHashMap<StringKey, List<EntityElement>> map) {
        performance = map;
    }

    public LinkedHashMap<StringKey, List<EntityElement>> getPerformance() {
        return performance;
    }

}
