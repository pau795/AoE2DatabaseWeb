package com.aoedb.data;

import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Civilization extends BaseEntity {

    String civStyle;

    List<Integer> uniqueUnitList;
    HashMap<Integer, Integer> uniqueUnitElite;

    List<Integer> uniqueBuildingList;

    HashMap<Integer, String> statRelation;

    public Civilization(HashMap<Integer, String> ecoList, String language) {
        super(language);
        uniqueUnitList = new ArrayList<>();
        uniqueBuildingList = new ArrayList<>();
        uniqueUnitElite = new HashMap<>();
        statRelation = ecoList;
    }

    public Civilization(Civilization civ){
        super(civ);
        this.civStyle = civ.getCivStyle();
        this.uniqueUnitList = civ.getUniqueUnitList();
        this.uniqueUnitElite = civ.uniqueUnitElite;
        this.uniqueBuildingList = civ.getUniqueBuildingList();
        this.statRelation = civ.statRelation;
    }


    public String getCivStyle() {
        return civStyle;
    }

    public String getCivThemeString() {
        return getNameElement().getMedia();
    }

    public int getTeamBonusId(){
        return bonusContainer.getTeamBonusList().get(0);
    }

    public List<Integer> getBonusList() {
        return bonusContainer.getBonusList();
    }

    public List<Integer> getUniqueUnitList() {
        return uniqueUnitList;
    }

    public List<Integer> getUniqueTechList() {
        return bonusContainer.getUniqueTechList();
    }

    public List<Integer> getUniqueBuildingList() {
        return uniqueBuildingList;
    }

    public int getUniqueUnit(){
        return uniqueUnitList.get(0);
    }

    public int getEliteUniqueUnit(int unitID) {
        return uniqueUnitElite.get(unitID);
    }



    public void setCivStyle(String civStyle) {
        this.civStyle = civStyle;
    }


    public void setEliteUniqueUnit(int unitID, int eliteUnitID){
        uniqueUnitElite.put(unitID, eliteUnitID);
    }

    public void addUniqueUnit(int id){
        uniqueUnitList.add(id);
    }


    public void addUniqueBuilding(int buildingID){
        uniqueBuildingList.add(buildingID);
    }

    //STATS

    @Override
    protected void calculateStatsPostFilter(String category, int age, Effect e){
        if (category.equals("eco")) { //eco stat
            int bStat = Integer.parseInt(e.getStat());
            String operator = e.getOperator();
            double value = e.getValue(age);
            double stat = getCalculatedStat(statRelation.get(bStat));
            setCalculatedStat(statRelation.get(bStat), Utils.calculate(stat, value, operator));
        }
    }

    public void setUpgradesIds(List<Integer> list) {
        upgradesIds = list;
    }

    public void setUpgradesIds() {
    }

    public String writeCivBonuses(){
        StringBuilder text = new StringBuilder();
        text.append("<ul style=\"list-style-type: square\">");
        for (int i : getBonusList()){
            Bonus b = Database.getBonus(i, language);
            String desc = b.getTechTreeDescription();
            text.append("<li>").append(desc).append("</li>");
        }
        Bonus b = Database.getBonus(getTeamBonusId(), language);
        String desc = b.getTechTreeDescription();
        text.append("</ul><b>").append(Database.getString("civilization_team_bonus", language)).append(": </b>").append(desc);
        return text.toString();
    }

    public String writeTechTreeInfo(){
        StringBuilder text = new StringBuilder();
        text.append("<p><b>").append(civStyle).append("</b></p><ul>");
        for (int i : getBonusList()){
            Bonus b = Database.getBonus(i, language);
            String desc = b.getTechTreeDescription();
            text.append("<li>").append(desc).append("</li>");
        }
        text.append("<br></ul><p><b>").append(Database.getString("civilization_unique_units", language)).append(":</b></p><ul>");
        for (int i : uniqueUnitList){
            String name = Database.getUnit(i, language).getName();
            String desc = Database.getUnit(i, language).getDescriptor().getQuickDescription();
            text.append("<li>").append(name).append(" (").append(desc).append(").</li>");
        }
        text.append("<br></ul><p><b>").append(Database.getString("civilization_unique_technologies", language)).append(":</b></p><ul>");
        for (int i : getUniqueTechList()){
            String name = Database.getTechnology(i, language).getName();
            String desc = Database.getTechnology(i, language).getDescriptor().getQuickDescription();
            text.append("<li>").append(name).append(" (").append(desc).append(").</li>");
        }
        text.append("<br></ul><p><b>").append(Database.getString("civilization_team_bonus", language)).append(":</b></p><ul>");
        Bonus b = Database.getBonus(getTeamBonusId(), language);
        String desc = b.getTechTreeDescription();
        text.append("<li>").append(desc).append("</li>");
        return text.toString();
    }

}