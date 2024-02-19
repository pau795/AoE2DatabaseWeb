package com.aoedb.database;

import com.aoedb.data.*;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Storage {

    private final List<EntityElement> unitList;
    private final List<EntityElement> buildingList;
    private final List<EntityElement> techList;
    private final List<EntityElement> civilizationList;
    private final List<EntityElement> classList;
    private final List<EntityElement> performanceList;
    private final List<EntityElement> typeList;
    private final List<EntityElement> historyList;
    private final List<EntityElement> entityList;

    private final List<TauntElement> tauntList;
    private final HashMap<Integer, String> statList;
    private final HashMap<String, Boolean> statAddition;
    private final HashMap<Integer, String> ecoList;
    private final HashMap<String, Double> ecoValues;
    private final List<EcoElement> gatheringRates;
    private final List<Integer> ecoUpgrades;

    private final TechTree techTree;
    private final LinkedHashMap<String, List<Integer>> techTreeQuizQuestions;

    private final List<HashMap<Integer, Integer>> unitOrder;
    private final List<HashMap<Integer, Integer>> buildingOrder;
    private final List<HashMap<Integer, Integer>> techOrder;
    private final List<HashMap<Integer, Integer>> civilizationOrder;
    private final List<HashMap<Integer, Integer>> performanceOrder;
    private final List<HashMap<Integer, Integer>> historyOrder;

    private final List<GroupList> unitGroups;
    private final List<GroupList> buildingGroups;
    private final List<GroupList> techGroups;
    private final List<GroupList> civilizationGroups;
    private final List<GroupList> performanceGroups;
    private final List<GroupList> historyGroups;

    private final HashMap<Integer, StringKey> civNameMap;
    private final HashMap<StringKey, Integer> reversedCivNameMap;

    private final HashMap<Integer, LinkedHashMap<StringKey, List<EntityElement>>> attackTypesEntities;
    private final HashMap<Integer, LinkedHashMap<StringKey, List<EntityElement>>> armorTypesEntities;


    private final List<Unit> unitMap;
    private final List<Building> buildingMap;
    private final List<Technology> techMap;
    private final List<Civilization> civilizationMap;

    private final List<Bonus> bonusMap;
    private final List<Bonus> hiddenBonusMap;
    private final List<TechBonus> techBonusMap;


    public Storage(){

        Reader r = new Reader(this);

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
        historyOrder = r.sortIndexMap(Database.HISTORY_GROUPS);

        unitGroups = r.readGroupLists(Database.UNIT_GROUPS, unitList);
        buildingGroups = r.readGroupLists(Database.BUILDING_GROUPS, buildingList);
        techGroups = r.readGroupLists(Database.TECH_GROUPS, techList);
        civilizationGroups = r.readGroupLists(Database.CIVILIZATION_GROUPS, civilizationList);
        performanceGroups = r.readGroupLists(Database.PERFORMANCE_GROUPS, performanceList);
        historyGroups = r.readGroupLists(Database.HISTORY_GROUPS, historyList);

        techTree = r.readTechTree();

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


    
    public EntityElement getElement(String file, int row) {
        if (row == 0) return new EntityElement(0, "none", Database.getImage("t_white"), "", "");
        else if (row == -1 && file.equals(Database.TECH_LIST)) return new EntityElement(0, "dark_age", Database.getImage("t_dark_age"), "", Database.TECH);
        else switch (file){
                case Database.UNIT_LIST: return unitList.get(row - 1);
                case Database.BUILDING_LIST: return buildingList.get(row - 1);
                case Database.TECH_LIST: return techList.get(row - 1);
                case Database.CIVILIZATION_LIST: return civilizationList.get(row - 1);
                case Database.CLASS_LIST: return classList.get(row - 1);
                case Database.TYPE_LIST: return typeList.get(row - 1);
                case Database.PERFORMANCE_LIST: return performanceList.get(row - 1);
                case Database.HISTORY_LIST: return historyList.get(row - 1);
                default: return new EntityElement(0, "none", Database.getImage("t_white"), "", "");
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
        return list;
    }


    public LinkedHashMap<StringKey, List<EntityElement>> getAllLists() {
        LinkedHashMap<StringKey, List<EntityElement>> b = new LinkedHashMap<>();
        List<EntityElement> units = getList(Database.UNIT_LIST);
        List<EntityElement> buildings = getList(Database.BUILDING_LIST);
        List<EntityElement> techs = getList(Database.TECH_LIST);
        List<EntityElement> civs = getList(Database.CIVILIZATION_LIST);
        b.put(new StringKey("tt_units"), units);
        b.put(new StringKey("tt_buildings"), buildings);
        b.put(new StringKey("tt_techs"), techs);
        b.put(new StringKey("tt_civilizations"), civs);
        return b;
    }

    public HashMap<Integer, Integer> getOrderMap(String file, int index){
        switch (file){
            case Database.UNIT_GROUPS: return unitOrder.get(index);
            case Database.BUILDING_GROUPS: return buildingOrder.get(index);
            case Database.TECH_GROUPS: return techOrder.get(index);
            case Database.CIVILIZATION_GROUPS: return civilizationOrder.get(index);
            case Database.PERFORMANCE_GROUPS: return performanceOrder.get(index);
            case Database.HISTORY_GROUPS: return historyOrder.get(index);
            default: return new HashMap<>();
        }
    }

    public HashMap<Integer, LinkedHashMap<StringKey, List<EntityElement>>> getAttackTypesEntities() {
        return attackTypesEntities;
    }

    public HashMap<Integer, LinkedHashMap<StringKey, List<EntityElement>>> getArmorTypesEntities() {
        return armorTypesEntities;
    }

    public GroupList getGroupList(String file, int sort){
        switch (file){
            case Database.UNIT_GROUPS: return unitGroups.get(sort);
            case Database.BUILDING_GROUPS: return buildingGroups.get(sort);
            case Database.TECH_GROUPS: return techGroups.get(sort);
            case Database.CIVILIZATION_GROUPS: return civilizationGroups.get(sort);
            case Database.PERFORMANCE_GROUPS: return performanceGroups.get(sort);
            case Database.HISTORY_GROUPS: return historyGroups.get(sort);
            default: return new GroupList();
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

    public HashMap<Integer, StringKey> getCivNameMap(){
        return civNameMap;
    }

    public HashMap<StringKey, Integer> getReversedCivNameMap(){
        return reversedCivNameMap;
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
        for(int i: list) upgradeList.add(new UpgradeElement(Database.getElement(Database.TECH_LIST, i)));
        upgradeList.sort(UpgradeElement.getListElementComparator(Database.TECH_GROUPS, 1));
        return upgradeList;
    }

}
