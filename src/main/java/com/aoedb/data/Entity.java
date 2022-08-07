package com.aoedb.data;


import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static java.lang.Math.round;

public abstract class Entity extends BaseEntity {

    protected Descriptor descriptor;
    protected LinkedHashMap<String, Integer> baseCost;
    protected LinkedHashMap<String, Integer> calculatedCost;
    protected LinkedHashMap<String, String> stringCost;
    protected AvailabilityContainer availabilityContainer;
    protected LinkedHashMap<StringKey, List<EntityElement>> upgrades;

    public Entity(){
        super();
        this.baseCost = new LinkedHashMap<>();
        this.calculatedCost = new LinkedHashMap<>();
        this.stringCost = new LinkedHashMap<>();
    }

    public Entity(Entity e){
        super(e);

        this.descriptor = e.getDescriptor();

        this.baseCost = e.getBaseCost();
        this.calculatedCost = new LinkedHashMap<>();
        this.stringCost = new LinkedHashMap<>();

        this.upgrades = e.getUpgrades();
        this.availabilityContainer = e.getAvailabilityContainer();
    }


    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor d) {
        descriptor = d;
    }

    public abstract EntityElement getCreatorElement();

    public EntityElement getAgeElement(){
        return getEntityElement("entity_age");
    }

    public EntityElement getRequiredTechElement(){
        return getEntityElement("required_technology");
    }

    public EntityElement getNextUpgradeElement(){
        return getEntityElement("next_upgrade");
    }

    public void resetStats() {
        super.resetStats();
        for(String resource:baseCost.keySet()) {
            if (baseCost.get(resource) == 0) {
                baseCost.remove(resource);
                calculatedCost.remove(resource);
            }
            else calculatedCost.put(resource, baseCost.get(resource));
        }
        stringCost = new LinkedHashMap<>();
    }

    //COST

    public LinkedHashMap<String, Integer> getBaseCost(){
        return baseCost;
    }

    public LinkedHashMap<String, Integer> getCalculatedCost(){
        return calculatedCost;
    }

    public LinkedHashMap<String, String> getCostString(){
        return stringCost;
    }




    public void setBaseCost(String cost, int value){
        baseCost.put(cost, value);
    }

    protected void calculateCost(String res, double value, String operator){
        if (res.equals("All")){
            for(String r:calculatedCost.keySet()){
                int stat = calculatedCost.get(r);
                calculatedCost.put(r,(int) round(Utils.calculate(stat, value, operator)));
            }
        }

        else if (calculatedCost.containsKey(res)){
            int stat = calculatedCost.get(res);
            calculatedCost.put(res,(int) round(Utils.calculate(stat, value, operator)));
        }
		
		else if (operator.equals("@")){

			baseCost.put(res, 0);
			calculatedCost.put(res, (int) round(Utils.calculate(0, value, operator)));
		}
    }

    //UPGRADES

    public void setUpgradesIds(){
        upgradesIds =  new ArrayList<>();
        for(StringKey s: upgrades.keySet())
            for(EntityElement l: upgrades.get(s)) upgradesIds.add(l.getId());
    }


    public void setUpgrades(LinkedHashMap<StringKey, List<EntityElement>> map) {
        upgrades = map;
    }

    public LinkedHashMap<StringKey, List<EntityElement>> getUpgrades(){
        return upgrades;
    }


    //AVAILABILITY


    public void setAvailability(AvailabilityContainer container){
        availabilityContainer = container;
    }

    public LinkedHashMap<StringKey, List<EntityElement>> getCivAvailability(){
        return availabilityContainer.getAvailabilityList();
    }

    public List<Integer> getAvailableCivIds(){
        return availabilityContainer.getAvailableIDs();
    }

    public boolean isAvailableTo(int civID){
        return availabilityContainer.getAvailabilityMap().get(civID);
    }

    private AvailabilityContainer getAvailabilityContainer(){
        return availabilityContainer;
    }

    //BONUSES
    protected void calculateStatsPostFilter(String category, int age, Effect e){
        switch (category) {
            case "stat":
                HashMap<Integer, String> statRelation = Database.getStatList();
                int bStat = Integer.parseInt(e.getStat());
                String operator = e.getOperator();
                double value = e.getValue(age);
                double stat = getCalculatedStat(statRelation.get(bStat));
                setCalculatedStat(statRelation.get(bStat), Utils.calculate(stat, value, operator));
                break;
            case "cost":
                String res = e.getStat();
                operator = e.getOperator();
                value = e.getValue(age);
                calculateCost(res, value, operator);
                break;
        }
    }

    public void processPostBonus(){
        super.processPostBonus();
        for (String s : baseStats.keySet()){
            boolean addition = Database.getStatAddition().get(s);
            boolean accuracy = s.equals(Database.ACCURACY);
            String result = Utils.getStatString(baseStats.get(s), calculatedStats.get(s), addition, accuracy);
            stringStats.put(s, result);
        }
        for (String s : baseCost.keySet()){
            String result = Utils.getStatString(baseCost.get(s), calculatedCost.get(s), false, false);
            stringCost.put(s, result);
        }
    }

    public List<String> writeBonuses(String language){
        return bonusContainer.writeBonuses(descriptor.getNominative(), language);
    }
}
