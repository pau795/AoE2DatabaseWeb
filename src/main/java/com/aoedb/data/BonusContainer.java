package com.aoedb.data;


import com.aoedb.database.Database;
import com.aoedb.database.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BonusContainer {
    private List<Integer> bonusList;
    private List<Integer> teamBonusList;
    private List<Integer> uniqueTechList;
    private List<Integer> hiddenBonusList;
    private final HashMap<Integer, String> civMap;
    private String language;


    private String type;

    public BonusContainer(HashMap<Integer, String> civMap, String language) {
        bonusList = new ArrayList<>();
        teamBonusList = new ArrayList<>();
        uniqueTechList = new ArrayList<>();
        hiddenBonusList = new ArrayList<>();
        this.civMap = civMap;
        this.language = language;
    }

    public List<Integer> getBonusList() {
        return bonusList;
    }

    public List<Integer> getTeamBonusList() {
        return teamBonusList;
    }

    public List<Integer> getUniqueTechList() {
        return uniqueTechList;
    }

    public List<Integer> getHiddenBonusList(){
        return hiddenBonusList;
    }

    public void setType(String type){
        this.type =  type;
    }

    Comparator<Integer> bonusComp = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            Bonus b1 = Database.getBonus(o1, language), b2 = Database.getBonus(o2, language);
            return civMap.get(b1.getCivilization()).compareTo(civMap.get(b2.getCivilization()));
        }
    };

    Comparator<Integer> teamBonusComp = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            Bonus b1 = Database.getBonus(o1, language), b2 = Database.getBonus(o2, language);
            return civMap.get(b1.getCivilization()).compareTo(civMap.get(b2.getCivilization()));
        }
    };

    Comparator<Integer> uniqueTechComp = (Integer o1, Integer o2) -> {
        String civName1 = Utils.getUTCiv(o1, language), civName2 = Utils.getUTCiv(o2, language);
        return civName1.compareTo(civName2);
    };

    public void setList(List<Integer> list, int i){
        switch (i){
            case 0:
                bonusList = list;
                break;
            case 1:
                teamBonusList = list;
                break;
            case 2:
                uniqueTechList = list;
                break;
            case 3:
                hiddenBonusList = list;
                break;
        }
    }

    public void sortBonuses(){
        bonusList.sort(bonusComp);
        teamBonusList.sort(teamBonusComp);
        uniqueTechList.sort(uniqueTechComp);
    }


    public List<String> writeBonuses(String name){
        List<String> bonusTextList = new ArrayList<>();

        StringBuilder bonusText = new StringBuilder();
        bonusText.append("<ul>");
        for (int id : bonusList) {
            Bonus bonus = Database.getBonus(id, language);
            String desc = Utils.getProperDescription(bonus.getItemDescription(), type);
            desc = desc.replace("@", name);
            bonusText.append("<li><b>").append(civMap.get(bonus.getCivilization())).append(":</b> ").append(desc).append("</li>");
        }
        bonusText.append("</ul>");
        if (!bonusText.toString().equals("<ul></ul>")) bonusTextList.add(bonusText.toString());
        else bonusTextList.add("");
        StringBuilder teamBonusText = new StringBuilder();
        teamBonusText.append("<ul>");
        for (int id : teamBonusList) {
            Bonus bonus = Database.getBonus(id, language);
            String desc = Utils.getProperDescription(bonus.getItemDescription(), type);
            desc = desc.replace("@", name);
            teamBonusText.append("<li><b>").append(civMap.get(bonus.getCivilization())).append(":</b> ").append(desc).append("</li>");
        }
        teamBonusText.append("</ul>");
        if (!teamBonusText.toString().equals("<ul></ul>")) bonusTextList.add(teamBonusText.toString());
        else bonusTextList.add("");
        StringBuilder uniqueTechText = new StringBuilder();
        uniqueTechText.append("<ul>");
        for (int id : uniqueTechList) {
            Descriptor d = Database.getTechnology(id, language).getDescriptor();
            String civName = Utils.getUTCiv(id, language);
            uniqueTechText.append("<li><b>").append(civName).append(", ").append(d.getNominative()).append(":</b> ").append(d.getExtraDescription()).append("</li>");
        }
        uniqueTechText.append("</ul>");
        if (!uniqueTechText.toString().equals("<ul></ul>")) bonusTextList.add(uniqueTechText.toString());
        else bonusTextList.add("");
        return bonusTextList;

    }
}
