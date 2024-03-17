package com.aoedb.database;

import com.aoedb.data.*;
import java.util.*;

public class Database {

    //DATABASE CONSTANTS
    public final static String APP_VERSION = "v2.8";
    public final static int PATCH_VERSION = 107882;
    public final static String BASE_DIR = "META-INF/resources/";


    //LANGUAGES
    public final static String SPANISH = "es";
    public final static String ENGLISH = "en";
    public final static String DEFAULT_LANGUAGE = ENGLISH;

    public final static String ENGLISH_FLAG = "\uD83C\uDDEC\uD83C\uDDE7 English";
    public final static String SPANISH_FLAG = "\uD83C\uDDEA\uD83C\uDDF8 Español";
    public final static String DEFAULT_FLAG = ENGLISH_FLAG;

    //TYPES
    public final static String ENTITY = "entity";
    public final static String UNIT = "unit";
    public final static String BUILDING = "building";
    public final static String TECH = "tech";
    public final static String CIV = "civilization";
    public final static String CLASS = "class";
    public final static String TYPE = "type";
    public final static String PERFORMANCE = "performance";
    public final static String HISTORY = "history";
    public final static String EMPTY = "";
    public final static String NONE = "none";

    //RESOURCES
    public final static String WOOD = "Wood";
    public final static String FOOD = "Food";
    public final static String GOLD = "Gold";
    public final static String STONE = "Stone";

    //FILES
    public final static String BONUS_EFFECT = "bonus_effect";
    public final static String BONUS_LIST = "bonus_list";
    public final static String BUILDING_ARMOR = "building_armor";
    public final static String BUILDING_ATTACK = "building_attack";
    public final static String BUILDING_AVAILABILITY = "building_availability";
    public final static String BUILDING_BONUS = "building_bonus";
    public final static String BUILDING_GROUPS = "building_groups";
    public final static String BUILDING_LIST = "building_list";
    public final static String BUILDING_STATS = "building_stats";
    public final static String BUILDING_TRAINABLE = "building_trainable";
    public final static String BUILDING_UPGRADES = "building_upgrades";
    public final static String CIVILIZATION_GROUPS = "civilization_groups";
    public final static String CIVILIZATION_INFO = "civilization_info";
    public final static String CIVILIZATION_LIST = "civilization_list";
    public final static String CLASS_LIST = "class_list";
    public final static String ECO_LIST = "eco_list";
    public final static String ECO_UPGRADES = "eco_upgrades";
    public final static String ENTITY_LIST = "entity_list";
    public final static String GATHERING_RATES = "gathering_rates";
    public final static String HIDDEN_BONUS = "hidden_bonus";
    public final static String HIDDEN_BONUS_EFFECT = "hidden_bonus_effect";
    public final static String HISTORY_GROUPS = "history_groups";
    public final static String HISTORY_LIST = "history_list";
    public final static String HISTORY_TEXT = "history_text";
    public final static String PERFORMANCE_GROUPS = "performance_groups";
    public final static String PERFORMANCE_LIST = "performance_list";
    public final static String STAT_LIST = "stat_list";
    public final static String TAUNT_LIST = "taunt_list";
    public final static String TECH_AVAILABILITY = "tech_availability";
    public final static String TECH_BONUS = "tech_bonus";
    public final static String TECH_EFFECT = "tech_effect";
    public final static String TECH_GROUPS = "tech_groups";
    public final static String TECH_LIST = "tech_list";
    public final static String TECH_STATS = "tech_stats";

    public final static String TECH_TREE = "tech_tree";
    public final static String TECH_TREE_QUIZ = "tech_tree_quiz";
    public final static String TECH_UPGRADES = "tech_upgrades";
    public final static String TYPE_LIST = "type_list";
    public final static String UNIT_ARMOR = "unit_armor";
    public final static String UNIT_ATTACK = "unit_attack";
    public final static String UNIT_AVAILABILITY = "unit_availability";
    public final static String UNIT_BONUS = "unit_bonus";
    public final static String UNIT_GROUPS = "unit_groups";
    public final static String UNIT_LIST = "unit_list";
    public final static String UNIT_PERFORMANCE = "unit_performance";
    public final static String UNIT_STATS = "unit_stats";
    public final static String UNIT_UPGRADES = "unit_upgrades";
    public final static String STRINGS = "strings";


    //STATS
    public final static String HP = "Hit Points";
    public final static String ATTACK = "Attack";
    public final static String MELEE_ARMOR = "Melee Armor";
    public final static String PIERCE_ARMOR = "Pierce Armor";
    public final static String RANGE = "Range";
    public final static String MINIMUM_RANGE = "Minimum Range";
    public final static String LOS = "Line Of Sight";
    public final static String RELOAD_TIME = "Reload Time";
    public final static String SPEED = "Speed";
    public final static String BLAST_RADIUS = "Blast Radius";
    public final static String ACCURACY = "Accuracy";
    public final static String ATTACK_DELAY = "Attack Delay";
    public final static String NUMBER_PROJECTILES = "Number Projectiles";
    public final static String PROJECTILE_SPEED = "Projectile Speed";
    public final static String GARRISON_CAPACITY = "Garrison Capacity";
    public final static String POPULATION_TAKEN = "Population Taken";
    public final static String TRAINING_TIME = "Training Time";
    public final static String WORK_RATE = "Work Rate";
    public final static String HEAL_RATE = "Heal Rate";
    public final static String HILL_BONUS = "Hill Bonus";
    public final static String HILL_REDUCTION = "Hill Reduction";
    public final static String BONUS_REDUCTION = "Bonus Reduction";
    public final static String CHARGE_ATTACK = "Charge Attack";
    public final static String CHARGE_RELOAD = "Charge Reload";
    public final static String RELICS = "Relics";
    public final static String IGNORE_ARMOR = "Ignore Armor";
    public final static String RESIST_ARMOR_IGNORE = "Resist Armor Ignore";
    public final static String UP_HILL_RESIST = "Up Hill Resist";
    public final static String DOWN_HILL_RESIST = "Down Hill Resist";

    //ECO STATS

    public final static String LUMBERJACK = "Lumberjack";
    public final static String SHEPHERD = "Shepherd";
    public final static String FORAGER = "Forager";
    public final static String HUNTER = "Hunter";
    public final static String FISHERMAN = "Fisherman";
    public final static String FARMER = "Farmer";
    public final static String BUILDER = "Builder";
    public final static String REPAIRER = "Repairer";
    public final static String GOLD_MINER = "Gold Miner";
    public final static String STONE_MINER = "Stone Miner";
    public final static String FISHING_SHIP = "Fishing Ship";
    public final static String RELIC_GOLD = "Relic Gold";
    public final static String TRADE_CART = "Trade Cart";
    public final static String TRADE_COG = "Trade Cog";
    public final static String FEITORIA_WOOD = "Feitoria FWood";
    public final static String FEITORIA_FOOD = "Feitoria Food";
    public final static String FEITORIA_GOLD = "Feitoria Gold";
    public final static String FEITORIA_STONE = "Feitoria Stone";
    public final static String KESHIK = "Keshik";
    public final static String FARMING_GOLD = "Farming Gold";
    public final static String RELIC_FOOD = "Relic Food";
    public final static String GOLD_STONE_MINERS = "Gold Stone Miners";
    public final static String GOLD_LUMBERJACKS = "Gold Lumberjacks";



    //COLORS
    public final static String RED = "red";
    public final static String BLUE = "blue";
    public final static String GREEN = "green";

    private static HashMap<String, HashMap<String, String>> stringMap;
    private static HashMap<String, List<String>> historyTextMap;


    private static Storage storage;


    public static void initDatabase(){
        stringMap = Reader.readStringMap();
        historyTextMap = Reader.readHistoryTextMap();
        storage = new Storage();

    }

    private static Storage getStorage(){
       return storage;
    }


    public static String getString(String key, String language){
        if (stringMap.containsKey(language) && stringMap.get(language).containsKey(key)) return stringMap.get(language).get(key);
        else return stringMap.get(DEFAULT_LANGUAGE).get(key);
    }

    public static String getImage(String imageName){
        if (imageName.startsWith("g_")) return "images/"+imageName+".gif";
        else return "images/"+imageName+".png";
    }


    public static String getSound(String soundName, String language){
        if (soundName.startsWith("t_")) return "sound/"+language+"/" + soundName +".ogg";
        else return "sound/"+Database.DEFAULT_LANGUAGE+"/" + soundName +".ogg";
    }

    public static EntityElement getElement(String file, int row) {
        return getStorage().getElement(file, row);
    }

    public static List<EntityElement> getList(String file) {
        return getStorage().getList(file);
    }

    public static LinkedHashMap<StringKey, List<EntityElement>> getAllLists() {
        return getStorage().getAllLists();
    }

    public static HashMap<Integer, Integer> getOrderMap(String file, int index){
        return getStorage().getOrderMap(file, index);
    }

    public static GroupList getGroupList(String file, int sort){
        return getStorage().getGroupList(file, sort);
    }


    public static List<TauntElement> getTauntList(){
        return getStorage().getTauntList();
    }

    public static HashMap<Integer, String> getStatList(){
        return getStorage().getStatList();
    }

    public static HashMap<String, Boolean> getStatAddition(){
        return getStorage().getStatAddition();
    }

    public static HashMap<Integer, String> getEcoList(){
        return getStorage().getEcoList();
    }

    public static HashMap<String, Double> getEcoValues(){
        return getStorage().getEcoValues();
    }

    public static List<EcoElement> getGatheringRates(){
        return getStorage().getGatheringRates();
    }

    public static List<Integer> getEcoUpgrades(){
        return getStorage().getEcoUpgrades();
    }

    public static LinkedHashMap<String, List<Integer>> getTechTreeQuizQuestions(){
        return getStorage().getTechTreeQuizQuestions();
    }

    public static HashMap<Integer, StringKey> getCivNameMap(){
        return getStorage().getCivNameMap();
    }

    public static HashMap<StringKey, Integer> getReversedCivNameMap(){
        return getStorage().getReversedCivNameMap();
    }

    public static String getHistoryText(int id, String language){
        return historyTextMap.get(language).get(id - 1);
    }

    public static Unit getUnit(int id){
        return getStorage().getUnit(id);
    }

    public static Building getBuilding(int id){
        return getStorage().getBuilding(id);
    }

    public static Technology getTechnology(int id){
        return getStorage().getTechnology(id);
    }

    public static Civilization getCivilization(int id){
        return getStorage().getCivilization(id);
    }

    public static Bonus getBonus(int id) {
        return getStorage().getBonus(id);
    }

    public static Bonus getHiddenBonus(int id){
        return getStorage().getHiddenBonus(id);
    }

    public static TechBonus getTechEffect(int id){
        return getStorage().getTechEffect(id);
    }

    public static HashMap<Integer, LinkedHashMap<StringKey, List<EntityElement>>> getAttackTypesEntities() {
        return getStorage().getAttackTypesEntities();
    }

    public static HashMap<Integer, LinkedHashMap<StringKey, List<EntityElement>>> getArmorTypesEntities() {
        return getStorage().getArmorTypesEntities();
    }

    public static List<UpgradeElement> getUpgradeElementList(List<Integer> list){
        return getStorage().getUpgradeElementList(list);
    }





}
