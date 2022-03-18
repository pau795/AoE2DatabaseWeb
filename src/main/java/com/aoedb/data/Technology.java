package com.aoedb.data;

import com.aoedb.database.Database;

import java.util.LinkedHashMap;
import java.util.List;



public class Technology extends Entity {

    protected LinkedHashMap<String, List<EntityElement>> applications;

    public Technology(String language) {
        super(language);
    }

    public Technology(Technology t){
        super(t);
        this.applications = getApplications();
        resetStats();
    }


    public EntityElement getCreatorElement(){
        return getEntityElement(Database.getString("research_building", language));
    }
    

    public void setApplications(LinkedHashMap<String, List<EntityElement>> map){
        applications = map;
    }

    public LinkedHashMap<String, List<EntityElement>> getApplications(){
        return applications;
    }

    }
