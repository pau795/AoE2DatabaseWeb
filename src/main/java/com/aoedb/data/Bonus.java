package com.aoedb.data;

import java.util.List;

public class Bonus {

    private String techTreeDescription, itemDescription;

	private EffectContainer effectContainer;

    public Bonus(){}

    public String getTechTreeDescription() {
        return techTreeDescription;
    }

    public void setTechTreeDescription(String techTreeDescription) {
        this.techTreeDescription = techTreeDescription;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setEffectContainer(EffectContainer effectContainer) {
        this.effectContainer = effectContainer;
    }

    public int getCivilization(){
        return effectContainer.getCivID();
    }


    public String getGlobalFilter(){
        return effectContainer.getGlobalFilter();
    }


    public List<Effect> getEffects(String category){
        return effectContainer.getEffectList(category);
    }



}
