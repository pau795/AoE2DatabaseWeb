package com.aoedb.database;

import com.aoedb.data.*;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Storage {

    private List<EntityElement> unitList;
    private List<EntityElement> buildingList;
    private List<EntityElement> techList;
    private List<EntityElement> civilizationList;
    private List<EntityElement> classList;
    private List<EntityElement> performanceList;
    private List<EntityElement> typeList;
    private List<EntityElement> historyList;
    private List<EntityElement> entityList;

    private List<TauntElement> tauntList;
    private HashMap<Integer, String> statList;
    private HashMap<String, Boolean> statAddition;
    private HashMap<Integer, String> ecoList;
    private HashMap<String, Double> ecoValues;
    private List<EcoElement> gatheringRates;
    private List<Integer> ecoUpgrades;
    private LinkedHashMap<String, List<Integer>> techTreeQuizQuestions;

    private List<HashMap<Integer, Integer>> unitOrder;
    private List<HashMap<Integer, Integer>> buildingOrder;
    private List<HashMap<Integer, Integer>> techOrder;
    private List<HashMap<Integer, Integer>> civilizationOrder;
    private List<HashMap<Integer, Integer>> performanceOrder;

    private List<LinkedHashMap<String, List<EntityElement>>> unitGroups;
    private List<LinkedHashMap<String, List<EntityElement>>> buildingGroups;
    private List<LinkedHashMap<String, List<EntityElement>>> techGroups;
    private List<LinkedHashMap<String, List<EntityElement>>> civilizationGroups;
    private List<LinkedHashMap<String, List<EntityElement>>> performanceGroups;
    private List<LinkedHashMap<String, List<EntityElement>>> historyGroups;

    private HashMap<Integer, String> civNameMap;
    private HashMap<String, Integer> reversedCivNameMap;

    private HashMap<Integer, LinkedHashMap<String, List<EntityElement>>> attackTypesEntities;
    private HashMap<Integer, LinkedHashMap<String, List<EntityElement>>> armorTypesEntities;

    private List<String> historyText;

    private List<Unit> unitMap;
    private List<Building> buildingMap;
    private List<Technology> techMap;
    private List<Civilization> civilizationMap;

    private List<Bonus> bonusMap;
    private List<Bonus> hiddenBonusMap;
    private List<TechBonus> techBonusMap;


    private final String language;

    public Storage(String language){
        this.language = language;
        loadStorage(language);
    }

    public void loadStorage(String language){

        Reader r = new Reader(this, language);

        unitList = r.readList(Database.UNIT_LIST);
        buildingList = r.readList(Database.BUILDING_LIST);
        techList = r.readList(Database.TECH_LIST);
        civilizationList = r.readList(Database.CIVILIZATION_LIST);
        classList = r.readList(Database.CLASS_LIST);
        performanceList = r.readList(Database.PERFORMANCE_LIST);
        typeList = r.readList(Database.TYPE_LIST);
        historyList = r.readList(Database.HISTORY_LIST);
        entityList = mergeEntityLists();

        tauntList = r.readTaunts();
        statList = r.readStatsNames(Database.STAT_LIST);
        statAddition = r.readStatsAddition();
        ecoList = r.readStatsNames(Database.ECO_LIST);
        ecoValues = r.readEcoValues();
        gatheringRates = r.readGatheringRates();
        ecoUpgrades = r.readEcoUpgrades();
        techTreeQuizQuestions = r.readTechTreeQuestions();
        civNameMap = r.makeCivMap();
        reversedCivNameMap = r.makeReversedCivMap();

        unitOrder = r.sortIndexMap(Database.UNIT_GROUPS);
        buildingOrder = r.sortIndexMap(Database.BUILDING_GROUPS);
        techOrder = r.sortIndexMap(Database.TECH_GROUPS);
        civilizationOrder = r.sortIndexMap(Database.CIVILIZATION_GROUPS);
        performanceOrder = r.sortIndexMap(Database.PERFORMANCE_GROUPS);

        unitGroups = r.readGroupLists(Database.UNIT_GROUPS, unitList);
        buildingGroups = r.readGroupLists(Database.BUILDING_GROUPS, buildingList);
        techGroups = r.readGroupLists(Database.TECH_GROUPS, techList);
        civilizationGroups = r.readGroupLists(Database.CIVILIZATION_GROUPS, civilizationList);
        performanceGroups = r.readGroupLists(Database.PERFORMANCE_GROUPS, performanceList);
        historyGroups = r.readGroupLists(Database.HISTORY_GROUPS, historyList);

        historyText = r.readHistoryText();

        bonusMap = r.readBonuses(Database.BONUS_LIST);
        hiddenBonusMap = r.readBonuses(Database.HIDDEN_BONUS);

        unitMap = r.readUnits();
        buildingMap = r.readBuildings();
        techMap = r.readTechnologies();
        r.readTechApplications();
        attackTypesEntities = r.readTypeEntity(true);
        armorTypesEntities = r.readTypeEntity(false);
        civilizationMap = r.readCivilizations();
        techBonusMap = r.readTechEffects();

    }

    public void processPostInit(){
        for (int i = 0; i < unitList.size(); ++i){
            getUnit(i + 1).getBonusContainer().sortBonuses();
        }

        for (int i = 0; i < buildingList.size(); ++i) {
            getBuilding(i + 1).getBonusContainer().sortBonuses();
        }

        for (int i = 0; i < techList.size(); ++i) {
            getTechnology(i + 1).getBonusContainer().sortBonuses();
        }
    }

    public String getLanguage() {
        return language;
    }

    
    public EntityElement getElement(String file, int row) {
        if (row == 0) return new EntityElement(0, Database.getString("none", language), Database.getImage("t_white"), "", "");
        else if (row == -1 && file.equals(Database.TECH_LIST)) return new EntityElement(0, Database.getString("dark_age", language), Database.getImage("t_dark_age"), "", Database.TECH);
        else switch (file){
                case Database.UNIT_LIST: return unitList.get(row - 1);
                case Database.BUILDING_LIST: return buildingList.get(row - 1);
                case Database.TECH_LIST: return techList.get(row - 1);
                case Database.CIVILIZATION_LIST: return civilizationList.get(row - 1);
                case Database.CLASS_LIST: return classList.get(row - 1);
                case Database.TYPE_LIST: return typeList.get(row - 1);
                case Database.PERFORMANCE_LIST: return performanceList.get(row - 1);
                case Database.HISTORY_LIST: return historyList.get(row - 1);
                case Database.ENTITY_LIST: return entityList.get(row - 1);
                default: return new EntityElement(0, Database.getString("none", language), Database.getImage("t_white"), "", "");
            }
    }

    public List<EntityElement> getList(String file) {
        switch (file){
            case Database.UNIT_LIST: return unitList;
            case Database.BUILDING_LIST: return buildingList;
            case Database.TECH_LIST: return techList;
            case Database.CIVILIZATION_LIST: return civilizationList;
            case Database.CLASS_LIST: return classList;
            case Database.TYPE_LIST: return typeList;
            case Database.PERFORMANCE_LIST: return performanceList;
            case Database.HISTORY_LIST: return historyList;
            case Database.ENTITY_LIST: return entityList;
            default: return new ArrayList<>();
        }
    }

    private List<EntityElement> mergeEntityLists(){
        ArrayList<EntityElement> list = new ArrayList<>();
        list.addAll(unitList);
        list.addAll(buildingList);
        list.addAll(techList);
        list.addAll(civilizationList);
        list.sort(EntityElement.getAlphabeticalComparator());
        return list;
    }


    public LinkedHashMap<String, List<EntityElement>> getAllLists() {
        LinkedHashMap<String, List<EntityElement>> b = new LinkedHashMap<>();
        List<EntityElement> units = getList(Database.UNIT_LIST);
        List<EntityElement> buildings = getList(Database.BUILDING_LIST);
        List<EntityElement> techs = getList(Database.TECH_LIST);
        List<EntityElement> civs = getList(Database.CIVILIZATION_LIST);
        b.put(Database.getString("tt_units", language), units);
        b.put(Database.getString("tt_buildings", language), buildings);
        b.put(Database.getString("tt_techs", language), techs);
        b.put(Database.getString("tt_civilizations", language), civs);
        return b;
    }

    public HashMap<Integer, Integer> getOrderMap(String file, int index){
        switch (file){
            case Database.UNIT_GROUPS: return unitOrder.get(index);
            case Database.BUILDING_GROUPS: return buildingOrder.get(index);
            case Database.TECH_GROUPS: return techOrder.get(index);
            case Database.CIVILIZATION_GROUPS: return civilizationOrder.get(index);
            case Database.PERFORMANCE_GROUPS: return performanceOrder.get(index);
            default: return new HashMap<>();
        }
    }

    public HashMap<Integer, LinkedHashMap<String, List<EntityElement>>> getAttackTypesEntities() {
        return attackTypesEntities;
    }

    public HashMap<Integer, LinkedHashMap<String, List<EntityElement>>> getArmorTypesEntities() {
        return armorTypesEntities;
    }

    public LinkedHashMap<String, List<EntityElement>> getGroupList(String file, int sort){
        switch (file){
            case Database.UNIT_GROUPS: return unitGroups.get(sort);
            case Database.BUILDING_GROUPS: return buildingGroups.get(sort);
            case Database.TECH_GROUPS: return techGroups.get(sort);
            case Database.CIVILIZATION_GROUPS: return civilizationGroups.get(sort);
            case Database.PERFORMANCE_GROUPS: return performanceGroups.get(sort);
            case Database.HISTORY_GROUPS: return historyGroups.get(sort);
            default: return new LinkedHashMap<>();
        }
    }


    public List<TauntElement> getTauntList(){
        return tauntList;
    }

    public HashMap<Integer, String> getStatList(){
        return statList;
    }

    public HashMap<String, Boolean> getStatAddition(){
        return statAddition;
    }

    public HashMap<Integer, String> getEcoList(){
        return ecoList;
    }

    public HashMap<String, Double> getEcoValues(){
        return ecoValues;
    }

    public List<EcoElement> getGatheringRates(){
        return gatheringRates;
    }

    public List<Integer> getEcoUpgrades(){
        return ecoUpgrades;
    }

    public LinkedHashMap<String, List<Integer>> getTechTreeQuizQuestions(){
        return techTreeQuizQuestions;
    }

    public HashMap<Integer, String> getCivNameMap(){
        return civNameMap;
    }

    public HashMap<String, Integer> getReversedCivNameMap(){
        return reversedCivNameMap;
    }

    public String getHistoryText(int id){
        return historyText.get(id - 1);
    }

    public Unit getUnit(int id){
        return unitMap.get(id - 1);
    }

    public Building getBuilding(int id){
        return buildingMap.get(id - 1);
    }

    public Technology getTechnology(int id){
        return techMap.get(id - 1);
    }

    public Civilization getCivilization(int id){
        return civilizationMap.get(id - 1);
    }

    public Bonus getBonus(int id) {
        return bonusMap.get(id - 1);
    }

    public Bonus getHiddenBonus(int id){
        return hiddenBonusMap.get(id - 1);
    }

    public TechBonus getTechEffect(int id){
        return techBonusMap.get(id - 1);
    }


    public List<UpgradeElement> getUpgradeElementList(List<Integer> list){
        List<UpgradeElement> upgradeList = new ArrayList<>(list.size());
        for(int i: list) upgradeList.add(new UpgradeElement(Database.getElement(Database.TECH_LIST, i, language)));
        upgradeList.sort(UpgradeElement.getListElementComparator(Database.TECH_GROUPS, 1, language));
        return upgradeList;
    }

}
