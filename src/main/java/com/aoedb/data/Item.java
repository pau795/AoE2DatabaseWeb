package com.aoedb.data;

import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public abstract class Item extends Entity {

    protected LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> baseAttackValues;
    protected LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> baseArmorValues;

    protected LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> calculatedAttackValues;
    protected LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> calculatedArmorValues;

    protected LinkedHashMap<StringKey, List<TypeElement>> attackValues;
    protected LinkedHashMap<StringKey, List<TypeElement>> armorValues;



    public Item(){
        super();
        baseAttackValues = new LinkedHashMap<>();
        baseArmorValues = new LinkedHashMap<>();
    }

    public Item(Item it){
        super(it);
        this.baseAttackValues = it.getBaseAttackValues();
        this.baseArmorValues = it.getBaseArmorValues();
        this.calculatedAttackValues = new LinkedHashMap<>();
        this.calculatedArmorValues = new LinkedHashMap<>();
        this.attackValues = new LinkedHashMap<>();
        this.armorValues = new LinkedHashMap<>();
        for (StringKey s: baseAttackValues.keySet()){
            LinkedHashMap<Integer, Double> l = baseAttackValues.get(s);
            LinkedHashMap<Integer, Double> n = new LinkedHashMap<>();
            for (int i : l.keySet()) n.put(i, l.get(i));
            calculatedAttackValues.put(s, n);
        }
        for (StringKey s: baseArmorValues.keySet()){
            LinkedHashMap<Integer, Double> l = baseArmorValues.get(s);
            LinkedHashMap<Integer, Double> n = new LinkedHashMap<>();
            for (int i : l.keySet()) n.put(i, l.get(i));
            calculatedArmorValues.put(s, n);
        }
    }



    //PREVIEW
    public EntityElement getClassElement(){
        return getEntityElement("entity_class");
    }

    public EntityElement getPreviousUpgradeElement(){
        return getEntityElement("upgraded_from");
    }

    @Override
    protected boolean filterPassed(Effect e, List<Integer> selectedUpgrades){
        if (e.getFilterClass().equals(Database.CLASS)) {
            if (e.getRequiredTechID() != -1 && !selectedUpgrades.contains(e.getRequiredTechID())) return false; // filter not passed if doesn't contain the required upgrade
            return e.getFilterIds().contains(getClassElement().getId());
        }
        return super.filterPassed(e, selectedUpgrades);
    }


    @Override
    public void processPostBonus() {
        if (getCalculatedStat(Database.LOS) > 20) setCalculatedStat(Database.LOS, 20);
        if(calculatedStats.containsKey(Database.RELICS)) {
            double relics = getCalculatedStat(Database.RELICS);
            if (!Double.isNaN(relics)) {
                double subtract = 4 - relics;
                setCalculatedStat(Database.ATTACK, getCalculatedStat(Database.ATTACK) - subtract);
                HashMap<Integer, Double> l1 = calculatedAttackValues.entrySet().iterator().next().getValue();
                if (l1.containsKey(1)) {
                    double s = l1.get(1);
                    double r = Utils.calculate(s, subtract, "-");
                    setCalculatedStat(Database.ATTACK, r);
                    l1.put(1, r);
                }
            }
        }
        super.processPostBonus();
        attackValues = processTypeValues(baseAttackValues, calculatedAttackValues);
        armorValues = processTypeValues(baseArmorValues, calculatedArmorValues);
    }

    private LinkedHashMap<StringKey, List<TypeElement>> processTypeValues(LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> m1,
                                                                       LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> m2){
        LinkedHashMap<StringKey, List<TypeElement>> r = new LinkedHashMap<>();
        for(StringKey s: m1.keySet()){
            HashMap<Integer, Double> l1 = m1.get(s);
            HashMap<Integer, Double> l2 = m2.get(s);
            List<TypeElement> list = new ArrayList<>();
            for(int i : l1.keySet()){
                double d1 = l1.get(i);
                double d2 = l2.get(i);
                String result = Utils.getStatString(d1, d2, true, false);
                TypeElement element = new TypeElement(Database.getElement(Database.TYPE_LIST, i), result);
                list.add(element);
            }
            r.put(s, list);
        }
        if (r.isEmpty()) r.put( new StringKey("none"), new ArrayList<>());
        return r;
    }

    @Override
    public void resetStats(){
        super.resetStats();
        for(StringKey s:baseAttackValues.keySet()){
            LinkedHashMap<Integer, Double> l1 = baseAttackValues.get(s);
            LinkedHashMap<Integer, Double> l2 = calculatedAttackValues.get(s);
            for (int i : l1.keySet()) l2.put(i, l1.get(i));
        }
        for(StringKey s:baseArmorValues.keySet()){
            LinkedHashMap<Integer, Double> l1 = baseArmorValues.get(s);
            LinkedHashMap<Integer, Double> l2 = calculatedArmorValues.get(s);
            for (int i : l1.keySet()) l2.put(i, l1.get(i));
        }
    }

    @Override
    protected void calculateStatsPostFilter(String category, int age, Effect e){
        switch (category){
            case "attack":
                if (e.isPlus()){ //affects main entity and its projectiles
                    for(StringKey s1: calculatedAttackValues.keySet()){
                        HashMap<Integer, Double> l1 = calculatedAttackValues.get(s1);
                        statsPostFilterAux(l1, age, e);
                    }
                }
                else{ //only affects main entity
                    HashMap<Integer, Double> l1 = calculatedAttackValues.entrySet().iterator().next().getValue();
                    statsPostFilterAux(l1, age, e);
                }
                break;
            case "armor":
                HashMap<Integer, Double> l1 = calculatedArmorValues.entrySet().iterator().next().getValue();
                statsPostFilterAux(l1, age, e);
                break;
            default:
                super.calculateStatsPostFilter(category, age, e);
                break;
        }
    }

    private void statsPostFilterAux(HashMap<Integer, Double> list, int age, Effect e){
        int stat = Integer.parseInt(e.getStat());
        if (list.containsKey(stat)){
            double s = list.get(stat);
            double value = e.getValue(age);
            String operator = e.getOperator();
            double r = Utils.calculate(s, value, operator);
            list.put(stat, r);
        }
    }

    //ATTACK AND ARMOR VALUES

    public void setAttackValues(LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> map){
        baseAttackValues = map;
        calculatedAttackValues = new LinkedHashMap<>();
        for (StringKey s: baseAttackValues.keySet()){
            LinkedHashMap<Integer, Double> list = baseAttackValues.get(s);
            LinkedHashMap<Integer, Double> list1 = new LinkedHashMap<>();
            for (int i : list.keySet()) list1.put(i, list.get(i));
            calculatedAttackValues.put(s, list1);
        }
    }

    public void setArmorValues(LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> map){
        baseArmorValues = map;
        calculatedArmorValues = new LinkedHashMap<>();
        for (StringKey s: baseArmorValues.keySet()){
            LinkedHashMap<Integer, Double> list = baseArmorValues.get(s);
            LinkedHashMap<Integer, Double> list1 = new LinkedHashMap<>();
            for (int i : list.keySet()) list1.put(i, list.get(i));
            calculatedArmorValues.put(s, list1);
        }
    }

    public LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> getBaseAttackValues(){
        return baseAttackValues;
    }
    public LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> getBaseArmorValues(){
        return baseArmorValues;
    }

    public LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> getCalculatedAttackValues(){
        return calculatedAttackValues;
    }
    public LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> getCalculatedArmorValues(){
        return calculatedArmorValues;
    }

    public LinkedHashMap<StringKey, List<TypeElement>> getAttackValues() {
        return attackValues;
    }

    public LinkedHashMap<StringKey, List<TypeElement>> getArmorValues() {
        return armorValues;
    }
}
