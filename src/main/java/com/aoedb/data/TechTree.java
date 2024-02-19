package com.aoedb.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class TechTree {

    private final HashMap<Integer, TechTreeNode> civToNode;

    private final List<TechTree> childList;

    public TechTree(){
        this.civToNode = new HashMap<>();
        this.childList = new ArrayList<>();
    }

    public void addCivNode(int civID, TechTreeNode node){
        this.civToNode.put(civID, node);
    }

    public void addChild(TechTree child){
        this.childList.add(child);
    }

    public Set<Integer> getCivNodes(){
        return this.civToNode.keySet();
    }

    public TechTreeNode getTechTreeNode(int civID){
        return this.civToNode.get(civID);
    }

    public List<TechTree> getChildren(){
        return this.childList;
    }

    public static class TechTreeNode{
        private int entityID;
        private String entityType;
        private int ageID;

        public TechTreeNode(int entityID, String entityType, int ageID) {
            this.entityID = entityID;
            this.entityType = entityType;
            this.ageID = ageID;
        }

        public int getEntityID() {
            return entityID;
        }

        public void setEntityID(int entityID) {
            this.entityID = entityID;
        }

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
        }

        public int getAgeID() {
            return ageID;
        }

        public void setAgeID(int ageID) {
            this.ageID = ageID;
        }
    }
}

