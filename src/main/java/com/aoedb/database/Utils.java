package com.aoedb.database;


import com.aoedb.data.Entity;
import com.aoedb.data.EntityElement;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.RouteParameters;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class Utils {

    public static String getProperDescription(String itemDescription, String type){
        String[] d = itemDescription.split("\\|");
        if (d.length > 1){
            switch (type){
                case Database.UNIT: return d[0];
                case Database.BUILDING: return d[1];
                case Database.TECH: return d[2];
            }
        }
        return itemDescription;
    }

    public static String getStatString(double bStat, double cStat, boolean addition, boolean accuracy){
        if (Double.isNaN(bStat)) return "-"; //stat has no value
        else if( Double.compare(bStat, cStat) == 0){ //showing base stat
            BigDecimal b= new BigDecimal(cStat);
            b = b.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
            if (b.compareTo(BigDecimal.ZERO) == 0) return "0";
            else return  b.toPlainString();
        }
        else { // showing base stat + calculated stat (addition changes the format)
            BigDecimal base, calculated;
            String baseString, calculatedString;

            base = new BigDecimal(bStat);
            baseString = getDecimalString(base, 2);

            if (addition) calculated = new BigDecimal(cStat - bStat);
            else calculated = new BigDecimal(cStat);
            calculatedString = getDecimalString(calculated, 2);

            if (accuracy){
                baseString += "%";
                calculatedString += "%";
            }

            if (addition) return baseString + " (+" + calculatedString + ")";
            else return baseString+ " ("+ calculatedString+")";
        }
    }


    public static String getDecimalString(double number, int numDecimals){
        if (Double.isNaN(number)) return "-";
        else if (Double.isInfinite(number)) return "∞";
        else {
            BigDecimal b = new BigDecimal(number);
            return getDecimalString(b, numDecimals);
        }
    }

    private static String getDecimalString(BigDecimal b, int decimals){
        b = b.setScale(decimals, RoundingMode.HALF_UP).stripTrailingZeros();
        if (b.compareTo(BigDecimal.ZERO) == 0) return "0";
        else return b.toPlainString();
    }

    public static String getResourceIcon(String resource){
        switch (resource){
            case Database.WOOD: return Database.getImage("r_wood");
            case Database.FOOD: return Database.getImage("r_food");
            case Database.GOLD: return Database.getImage("r_gold");
            case Database.STONE: return Database.getImage("r_stone");
            default: return "";
        }
    }

    public static String getResourceString(String resource, String language){
        switch (resource){
            case Database.WOOD: return Database.getString("res_wood", language);
            case Database.FOOD: return Database.getString("res_food", language);
            case Database.GOLD: return Database.getString("res_gold", language);
            case Database.STONE: return Database.getString("res_stone", language);
            default: return "";
        }
    }



    public static double calculate(double stat, double value, String operator){
        switch (operator){
            case "+": return stat + value;
            case "-": return stat - value;
            case "*": return stat * value;
            case "/": return stat / value;
            case "@": return value;
            default: return stat;
        }
    }

    public static String getMaxAge(Entity e1, Entity e2, String language){
        String e1Name = e1.getAgeElement().getName().getTranslatedString(language), e2Name = e2.getAgeElement().getName().getTranslatedString(language);
        if (e1.getEntityID() == 12 || e1.getEntityID() == 15) e1Name = Database.getElement(Database.TECH_LIST, -1).getName().getTranslatedString(language);
        if (e2.getEntityID() == 12 || e2.getEntityID() == 15) e2Name = Database.getElement(Database.TECH_LIST, -1).getName().getTranslatedString(language);

        String darkAge = Database.getString("dark_age", language);
        String feudalAge = Database.getString("feudal_age", language);
        String castleAge = Database.getString("castle_age", language);
        String imperialAge = Database.getString("imperial_age", language);
        
        if (e1Name.equals(imperialAge) || e2Name.equals(imperialAge)) return imperialAge;
        else if (e1Name.equals(castleAge) || e2Name.equals(castleAge)) return castleAge;
        else if (e1Name.equals(feudalAge) || e2Name.equals(feudalAge)) return feudalAge;
        else return darkAge;
    }


    public static String getStatTitle(String stat){
        switch (stat){
            case Database.HP: return "stat_name_1";
            case Database.ATTACK: return "stat_name_2";
            case Database.MELEE_ARMOR: return "stat_name_3";
            case Database.PIERCE_ARMOR: return "stat_name_4";
            case Database.RANGE: return "stat_name_5";
            case Database.MINIMUM_RANGE: return "stat_name_6";
            case Database.LOS: return "stat_name_7";
            case Database.RELOAD_TIME: return "stat_name_8";
            case Database.SPEED: return "stat_name_9";
            case Database.BLAST_RADIUS: return "stat_name_10";
            case Database.ATTACK_DELAY: return "stat_name_11";
            case Database.ACCURACY: return "stat_name_12";
            case Database.NUMBER_PROJECTILES: return "stat_name_13";
            case Database.PROJECTILE_SPEED: return "stat_name_14";
            case Database.GARRISON_CAPACITY: return "stat_name_15";
            case Database.POPULATION_TAKEN: return "stat_name_16";
            case Database.TRAINING_TIME: return "stat_name_17";
            case Database.WORK_RATE: return "stat_name_18";
            case Database.HEAL_RATE: return "stat_name_19";
            case Database.HILL_BONUS: return "stat_name_20";
            case Database.HILL_REDUCTION: return "stat_name_21";
            case Database.BONUS_REDUCTION: return "stat_name_22";
            case Database.CHARGE_ATTACK: return "stat_name_23";
            case Database.CHARGE_RELOAD: return "stat_name_24";
            case Database.RELICS: return "stat_name_25";
            case Database.IGNORE_ARMOR: return "stat_name_26";
            case Database.RESIST_ARMOR_IGNORE: return "stat_name_27";
            default: return "";
        }
    }

    public static String getStatString(int id){
        switch (id){
            case 1: return Database.HP;
            case 2: return Database.ATTACK;
            case 3: return Database.MELEE_ARMOR;
            case 4: return Database.PIERCE_ARMOR;
            case 5: return Database.RANGE;
            case 6: return Database.MINIMUM_RANGE;
            case 7: return Database.LOS;
            case 8: return Database.RELOAD_TIME;
            case 9: return Database.SPEED;
            case 10: return Database.BLAST_RADIUS;
            case 11: return Database.ATTACK_DELAY;
            case 12: return Database.ACCURACY;
            case 13: return Database.NUMBER_PROJECTILES;
            case 14: return Database.PROJECTILE_SPEED;
            case 15: return Database.GARRISON_CAPACITY;
            case 16: return Database.POPULATION_TAKEN;
            case 17: return Database.TRAINING_TIME;
            case 18: return Database.WORK_RATE;
            case 19: return Database.HEAL_RATE;
            case 20: return Database.HILL_BONUS;
            case 21: return Database.HILL_REDUCTION;
            case 22: return Database.BONUS_REDUCTION;
            case 23: return Database.CHARGE_ATTACK;
            case 24: return Database.CHARGE_RELOAD;
            case 25: return Database.RELICS;
            case 26: return Database.IGNORE_ARMOR;
            case 27: return Database.RESIST_ARMOR_IGNORE;
            default: return "";
        }
    }

    public static String getEcoString(int id){
        switch (id){
            case 1: return Database.LUMBERJACK;
            case 2: return Database.SHEPHERD;
            case 3: return Database.FORAGER;
            case 4: return Database.HUNTER;
            case 5: return Database.FISHERMAN;
            case 6: return Database.FARMER;
            case 7: return Database.BUILDER;
            case 8: return Database.REPAIRER;
            case 9: return Database.GOLD_MINER;
            case 10: return Database.STONE_MINER;
            case 11: return Database.FISHING_SHIP;
            case 12: return Database.RELIC_GOLD;
            case 13: return Database.TRADE_CART;
            case 14: return Database.TRADE_COG;
            case 15: return Database.FEITORIA_WOOD;
            case 16: return Database.FEITORIA_FOOD;
            case 17: return Database.FEITORIA_GOLD;
            case 18: return Database.FEITORIA_STONE;
            case 19: return Database.KESHIK;
            case 20: return Database.FARMING_GOLD;
            case 21: return Database.RELIC_FOOD;
            case 22: return Database.GOLD_STONE_MINERS;
            case 23: return Database.GOLD_LUMBERJACKS;
            default: return "";
        }
    }

    public static String unCamelCase(String s){
        String aux = s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ), " ");
        return  aux.substring(0, 1).toUpperCase() + aux.substring(1);
    }


    public static int convertAge(String name, String language){
            if (name.equals(Database.getString("dark_age", language))) return 0;
            else if (name.equals(Database.getString("feudal_age", language))) return 1;
            else if (name.equals(Database.getString("castle_age", language))) return 2;
            else if (name.equals(Database.getString("imperial_age", language))) return 3;
            else return -1;
    }

    public static String getUTCiv(int id, String language){
       return Database.getElement(Database.CIVILIZATION_LIST, Database.getTechnology(id).getAvailableCivIds().get(0)).getName().getTranslatedString(language);
    }



    public static String getEntityTypeString(String listString){
        switch (listString){
            case Database.UNIT_LIST: return Database.UNIT;
            case Database.BUILDING_LIST: return Database.BUILDING;
            case Database.TECH_LIST: return Database.TECH;
            case Database.CIVILIZATION_LIST: return Database.CIV;
            case Database.CLASS_LIST: return Database.CLASS;
            case Database.TYPE_LIST: return Database.TYPE;
            case Database.PERFORMANCE_LIST: return Database.PERFORMANCE;
            case Database.HISTORY_LIST: return Database.HISTORY;
            default: return Database.EMPTY;
        }
    }

    public static String getEntityTypeName(String type, String language){
        switch (type){
            case Database.UNIT: return Database.getString("entity_unit", language);
            case Database.BUILDING: return Database.getString("entity_building", language);
            case Database.TECH: return Database.getString("entity_technology", language);
            case Database.CIV: return Database.getString("entity_civilization", language);
            default: return Database.EMPTY;
        }
    }


    public static int mapAgeID(int id){
        switch (id){
            case -1: return 0;
            case 2: return 1;
            case 19: return 2;
            case 90: return 3;
            default: return -1;
        }
    }

    public static String getEffectFileName(String file){
        switch (file){
            case Database.TECH_LIST: return Database.TECH_EFFECT;
            case Database.HIDDEN_BONUS: return Database.HIDDEN_BONUS_EFFECT;
            default: return Database.BONUS_EFFECT;
        }
    }

    public static String getLanguageFlagString(String lang){
        switch (lang){
            case Database.SPANISH: return Database.SPANISH_FLAG;
            case Database.DEUTSCH: return Database.DEUTSCH_FLAG;
            case Database.ENGLISH: return Database.ENGLISH_FLAG;
            default: return Database.DEFAULT_FLAG;
        }
    }

    public static String getLanguageFromFlag(String flagLang){
        switch (flagLang){
            case Database.SPANISH_FLAG: return Database.SPANISH;
            case Database.DEUTSCH_FLAG: return Database.DEUTSCH;
            case Database.ENGLISH_FLAG: return Database.ENGLISH;
            default: return Database.DEFAULT_LANGUAGE;
        }
    }

    public static String checkLanguage(String language) {
        switch (language) {
            case Database.SPANISH:
            case Database.DEUTSCH:
            case Database.ENGLISH:
                return language;
            default:
                return Database.DEFAULT_LANGUAGE;
        }

    }

    public static RouteParameters changeRouteParameter(RouteParameters r, String name, String value){
        Map<String, String> map = new HashMap<>();
        for (String s : r.getParameterNames()) map.put(s, r.get(s).orElse(""));
        map.put(name, value);
        return new RouteParameters(map);
    }

    public static ComboBox.ItemFilter<EntityElement> getEntityElementComboBoxFilter(String language){
         return (entityElement, filterString) -> StringUtils.stripAccents(entityElement.getName().getTranslatedString(language).toLowerCase()).contains(StringUtils.stripAccents(filterString.toLowerCase()));
    }
    public static Component getEntityItemRow(EntityElement entityElement, boolean border, String language) {
        Label name = new Label(entityElement.getName().getTranslatedString(language));
        name.getStyle().set("flex-grow", "1");
        name.getStyle().set("font-weight", "bold");
        Image image = new Image();
        image.setSrc(entityElement.getImage());
        image.getStyle().set("height", "50px");
        image.getStyle().set("width", "50px");
        if (border) image.getStyle().set("border", "3px solid var(--lumo-primary-color)");
        Div container = new Div(name, image);
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "row");
        container.getStyle().set("align-items", "center");
        return container;
    }

    public static Component getSearchEntityItemRow(EntityElement entityElement, String language) {
        Label name = new Label(entityElement.getName().getTranslatedString(language));
        name.getStyle().set("flex-grow", "1");
        name.getStyle().set("font-weight", "bold");

        Label type = new Label(Utils.getEntityTypeName(entityElement.getType(), language));
        type.getStyle().set("font-weight", "bold");
        Image image = new Image();
        image.setSrc(entityElement.getImage());
        image.getStyle().set("height", "50px");
        image.getStyle().set("width", "50px");
        if (!entityElement.getType().equals(Database.CIV)) image.getStyle().set("border", "3px solid var(--lumo-primary-color)");
        Div container = new Div(name, type, image);
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "row");
        container.getStyle().set("gap", "10px");
        container.getStyle().set("align-items", "center");
        return container;
    }



}


