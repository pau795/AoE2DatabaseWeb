package com.aoedb.data;

import java.util.List;

public class Bonus {

    private StringKey techTreeDescription, itemDescription;

	private EffectContainer effectContainer;

    public Bonus(){}

    public StringKey getTechTreeDescription() {
        return techTreeDescription;
    }

    public void setTechTreeDescription(String techTreeDescription) {
        this.techTreeDescription = new StringKey(techTreeDescription);
    }

    public StringKey getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = new StringKey(itemDescription);
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
