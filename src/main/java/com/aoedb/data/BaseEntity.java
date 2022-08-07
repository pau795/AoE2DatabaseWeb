package com.aoedb.data;

import com.aoedb.database.Database;
import java.util.HashMap;
import java.util.List;

public abstract class BaseEntity {

    protected HashMap<String, EntityElement> entityElements;
    protected HashMap<String, Double> baseStats;
    protected HashMap<String, Double> calculatedStats;
    protected HashMap<String, String> stringStats;
    protected List<Integer> upgradesIds;
    protected BonusContainer bonusContainer;

    public BaseEntity(){
        this.entityElements = new HashMap<>();
        this.baseStats = new HashMap<>();
        this.calculatedStats = new HashMap<>();
        this.stringStats = new HashMap<>();
    }

    public BaseEntity(BaseEntity b){
        this.entityElements = b.getEntityElements();
        this.baseStats = b.getBaseStatMap();
        this.calculatedStats = new HashMap<>();
        this.stringStats = new HashMap<>();
        this.upgradesIds = b.getUpgradesIds();
        this.bonusContainer = b.getBonusContainer();
    }


    public int getEntityID(){
        return getNameElement().getId();
    }

    public StringKey getName(){
        return getNameElement().getName();
    }

    public HashMap<String, EntityElement> getEntityElements(){
        return entityElements;
    }

    public void addEntityElement(String s, EntityElement element){
        entityElements.put(s, element);
    }

    public EntityElement getEntityElement(String s){
        return entityElements.get(s);
    }

    public EntityElement getNameElement(){

        return getEntityElement("entity_name");
    }

    public String getType(){
        return getNameElement().getType();
    }

    //STATS


    public double getBaseStat(String stat){
        return baseStats.get(stat);

    }

    private HashMap<String, Double> getBaseStatMap(){
        return baseStats;
    }

    public double getCalculatedStat(String stat){
        return calculatedStats.get(stat);
    }

    public void setBaseStat(String stat, double value) {
        baseStats.put(stat, value);
    }

    public void setCalculatedStat(String stat, double value) {
        calculatedStats.put(stat, value);
    }

    public String getStatString(String stat){
        return stringStats.get(stat);
    }

    public void resetStats() {
        for(String stat:baseStats.keySet()){
            calculatedStats.put(stat, baseStats.get(stat));
        }
        stringStats = new HashMap<>();
    }

    //UPGRADES


    public abstract void setUpgradesIds();

    public List<Integer> getUpgradesIds(){
        return upgradesIds;
    }

    //BONUS

    public void setBonuses(BonusContainer bc) {
        bonusContainer = bc;
        bonusContainer.setType(getType());
    }

    public BonusContainer getBonusContainer() {
        return bonusContainer;
    }

    protected abstract void calculateStatsPostFilter(String category, int age, Effect e);

    protected boolean filterPassed(Effect e, List<Integer> selectedUpgrades){
        if (e.getFilterClass().equals(Database.NONE)) return true;
        else if(e.getFilterClass().equals("techRequirement")) return selectedUpgrades.contains(e.getRequiredTechID());
        else if (e.getFilterClass().equals(getType())){
            if (e.getRequiredTechID() != -1 && !selectedUpgrades.contains(e.getRequiredTechID())) return false; // filter not passed if doesn't contain the required upgrade
            return e.getFilterIds().contains(getEntityID()) || e.getFilterIds().contains(-1);
        }
        return false;
    }

    public void processPostBonus(){}

    public void calculateStats(int age, int civ, List<Integer> selectedUpgrades){
        resetStats();

        //HIDDEN BONUS
        for (int i: bonusContainer.getHiddenBonusList()){
            Bonus b = Database.getHiddenBonus(i);
            processBonusEffects(b, age, selectedUpgrades);
        }

        //BONUSES
        for (int i:bonusContainer.getBonusList()){
            Bonus b = Database.getBonus(i);
            if (b.getCivilization() == civ && (b.getGlobalFilter().equals(Database.NONE) || b.getGlobalFilter().equals(getType())))
                processBonusEffects(b, age, selectedUpgrades);
        }

        //TEAM BONUS
        for (int i:bonusContainer.getTeamBonusList()){
            Bonus b = Database.getBonus(i);
            if(b.getCivilization() == civ && (b.getGlobalFilter().equals(Database.NONE) || b.getGlobalFilter().equals(getType())))
                processBonusEffects(b, age, selectedUpgrades);
        }

        //TECHS
        for (int i : selectedUpgrades){
            TechBonus t = Database.getTechEffect(i);
            if(t.getAvailableCivs().contains(civ) && t.getAge() <= age)
                processBonusEffects(t.getBonus(), age, selectedUpgrades);
        }

        processPostBonus();
    }

    private void processBonusEffects(Bonus b, int age, List<Integer> selectedUpgrades){
        for(String category : EffectContainer.getCategories())
            for(Effect e: b.getEffects(category))
                if(filterPassed(e,selectedUpgrades)) calculateStatsPostFilter(category, age, e);
    }



}
