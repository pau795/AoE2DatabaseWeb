package com.aoedb.data;


import java.util.LinkedHashMap;
import java.util.List;



public class Technology extends Entity {

    protected LinkedHashMap<StringKey, List<EntityElement>> applications;

    public Technology() {
        super();
    }

    public Technology(Technology t){
        super(t);
        this.applications = getApplications();
        resetStats();
    }


    public EntityElement getCreatorElement(){
        return getEntityElement("research_building");
    }
    

    public void setApplications(LinkedHashMap<StringKey, List<EntityElement>> map){
        applications = map;
    }

    public LinkedHashMap<StringKey, List<EntityElement>> getApplications(){
        return applications;
    }

    }
