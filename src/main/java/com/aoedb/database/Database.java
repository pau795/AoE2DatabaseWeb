package com.aoedb.database;

import com.aoedb.data.Bonus;
import com.aoedb.data.Building;
import com.aoedb.data.Civilization;
import com.aoedb.data.EcoElement;
import com.aoedb.data.EntityElement;
import com.aoedb.data.ScoreList;
import com.aoedb.data.TauntElement;
import com.aoedb.data.TechBonus;
import com.aoedb.data.Technology;
import com.aoedb.data.Unit;
import com.aoedb.data.UpgradeElement;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.io.*;
import java.util.*;

public class Database {

    //DATABASE CONSTANTS
    public final static String APP_VERSION = "v1.7";
    public final static int PATCH_VERSION = 59165;
    public final static String BASE_DIR = "META-INF/resources/";


    //LANGUAGES
    public final static String SPANISH = "es";
    public final static String SPANISH_FLAG = "\uD83C\uDDEA\uD83C\uDDF8 Español";
    public final static String ENGLISH = "en";
    public final static String ENGLISH_FLAG = "\uD83C\uDDEC\uD83C\uDDE7 English";
    public final static String DEUTSCH = "de";
    public final static String DEUTSCH_FLAG = "\uD83C\uDDE9\uD83C\uDDEA Deutsch";
    public final static String DEFAULT_LANGUAGE = ENGLISH;
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
    public final static String BUILDING_DESCRIPTION = "building_description";
    public final static String BUILDING_GROUPS = "building_groups";
    public final static String BUILDING_LIST = "building_list";
    public final static String BUILDING_STATS = "building_stats";
    public final static String BUILDING_TRAINABLE = "building_trainable";
    public final static String BUILDING_UPGRADES = "building_upgrades";
    public final static String CIVILIZATION_GROUPS = "civilization_groups";
    public final static String CIVILIZATION_INFO = "civilization_info";
    public final static String CIVILIZATION_LIST = "civilization_list";
    public final static String CIVILIZATION_STYLE = "civilization_style";
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
    public final static String TECH_DESCRIPTION = "tech_description";
    public final static String TECH_EFFECT = "tech_effect";
    public final static String TECH_GROUPS = "tech_groups";
    public final static String TECH_LIST = "tech_list";
    public final static String TECH_STATS = "tech_stats";
    public final static String TECH_TREE_QUIZ = "tech_tree_quiz";
    public final static String TECH_UPGRADES = "tech_upgrades";
    public final static String TYPE_LIST = "type_list";
    public final static String UNIT_ARMOR = "unit_armor";
    public final static String UNIT_ATTACK = "unit_attack";
    public final static String UNIT_AVAILABILITY = "unit_availability";
    public final static String UNIT_BONUS = "unit_bonus";
    public final static String UNIT_DESCRIPTION = "unit_description";
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

    //ECO STATS
    public final static String LUMBERJACK = "Lumberjack";
    public final static String FARMER = "Farmer";
    public final static String GOLD_MINER = "Gold Miner";


    //COLORS
    public final static String RED = "red";
    public final static String BLUE = "blue";
    public final static String GREEN = "green";

    private static HashMap<String, HashMap<String, String>> stringMap;

    private static Storage englishStorage;
    private static Storage spanishStorage;


    public static void initDatabase(){
        stringMap = Reader.readStringMap();
        englishStorage = new Storage(Database.ENGLISH);
        spanishStorage = new Storage(Database.SPANISH);
        englishStorage.processPostInit();
        spanishStorage.processPostInit();
    }

    private static Storage getStorage(String language){
        if(SPANISH.equals(language)) return spanishStorage;
        else return englishStorage;
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

    public static EntityElement getElement(String file, int row, String language) {
        return getStorage(language).getElement(file, row);
    }

    public static List<EntityElement> getList(String file, String language) {
        return getStorage(language).getList(file);
    }

    public static LinkedHashMap<String, List<EntityElement>> getAllLists(String language) {
        return getStorage(language).getAllLists();
    }

    public static HashMap<Integer, Integer> getOrderMap(String file, int index, String language){
        return getStorage(language).getOrderMap(file, index);
    }

    public static LinkedHashMap<String, List<EntityElement>> getGroupList(String file, int sort, String language){
        return getStorage(language).getGroupList(file, sort);
    }


    public static List<TauntElement> getTauntList(String language){
        return getStorage(language).getTauntList();
    }

    public static HashMap<Integer, String> getStatList(String language){
        return getStorage(language).getStatList();
    }

    public static HashMap<String, Boolean> getStatAddition(String language){
        return getStorage(language).getStatAddition();
    }

    public static HashMap<Integer, String> getEcoList(String language){
        return getStorage(language).getEcoList();
    }

    public static HashMap<String, Double> getEcoValues(String language){
        return getStorage(language).getEcoValues();
    }

    public static List<EcoElement> getGatheringRates(String language){
        return getStorage(language).getGatheringRates();
    }

    public static List<Integer> getEcoUpgrades(String language){
        return getStorage(language).getEcoUpgrades();
    }

    public static LinkedHashMap<String, List<Integer>> getTechTreeQuizQuestions(String language){
        return getStorage(language).getTechTreeQuizQuestions();
    }

    public static HashMap<Integer, String> getCivNameMap(String language){
        return getStorage(language).getCivNameMap();
    }

    public static HashMap<String, Integer> getReversedCivNameMap(String language){
        return getStorage(language).getReversedCivNameMap();
    }

    public static String getHistoryText(int id, String language){
        return getStorage(language).getHistoryText(id);
    }

    public static Unit getUnit(int id, String language){
        return getStorage(language).getUnit(id);
    }

    public static Building getBuilding(int id, String language){
        return getStorage(language).getBuilding(id);
    }

    public static Technology getTechnology(int id, String language){
        return getStorage(language).getTechnology(id);
    }

    public static Civilization getCivilization(int id, String language){
        return getStorage(language).getCivilization(id);
    }

    public static Bonus getBonus(int id, String language) {
        return getStorage(language).getBonus(id);
    }

    public static Bonus getHiddenBonus(int id, String language){
        return getStorage(language).getHiddenBonus(id);
    }

    public static TechBonus getTechEffect(int id, String language){
        return getStorage(language).getTechEffect(id);
    }

    public static HashMap<Integer, LinkedHashMap<String, List<EntityElement>>> getAttackTypesEntities(String language) {
        return getStorage(language).getAttackTypesEntities();
    }

    public static HashMap<Integer, LinkedHashMap<String, List<EntityElement>>> getArmorTypesEntities(String language) {
        return getStorage(language).getArmorTypesEntities();
    }

    public static List<UpgradeElement> getUpgradeElementList(List<Integer> list, String language){
        return getStorage(language).getUpgradeElementList(list);
    }





}
