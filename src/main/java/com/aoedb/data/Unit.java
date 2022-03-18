package com.aoedb.data;

import com.aoedb.database.Database;

import java.util.LinkedHashMap;
import java.util.List;


public class Unit extends Item {

    protected LinkedHashMap<String, List<EntityElement>> performance;

    public Unit(String language){
        super(language);
    }

    public Unit(Unit u){
        super(u);
        this.performance = u.getPerformance();
        resetStats();
    }

    public EntityElement getCreatorElement(){
        return getEntityElement(Database.getString("unit_training_building", language));
    }

    public void setPerformance(LinkedHashMap<String, List<EntityElement>> map) {

        performance = map;
    }

    public LinkedHashMap<String, List<EntityElement>> getPerformance() {
        return performance;
    }

}
