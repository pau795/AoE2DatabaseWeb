package com.aoedb.data;

import java.util.HashMap;
import java.util.List;

public class EffectContainer {
    private int id;
    private int civID;
    private String globalFilter;
    private boolean staggered, teamBonus;
    private final HashMap<String, List<Effect>> effectMap;

    public EffectContainer(int id){
        this.id = id;
        effectMap = new HashMap<>();
    }

    private static final String[] categories = {"stat", "cost", "eco", "attack", "armor"};

    public static String[] getCategories(){
        return categories;
    }

    public int getCivID() {
        return civID;
    }

    public void setCivID(int civID) {
        this.civID = civID;
    }

    public String getGlobalFilter() {
        return globalFilter;
    }

    public void setGlobalFilter(String globalFilter) {
        this.globalFilter = globalFilter;
    }

    public void setStaggered(boolean staggered) {
        this.staggered = staggered;
    }

    public void setTeamBonus(boolean teamBonus) {
        this.teamBonus = teamBonus;
    }

    public void addEffectList(String s, List<Effect> list){
        effectMap.put(s, list);
    }

    public List<Effect> getEffectList(String s){
        return effectMap.get(s);
    }


}
