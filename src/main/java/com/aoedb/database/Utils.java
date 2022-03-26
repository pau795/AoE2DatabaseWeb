package com.aoedb.database;


import com.aoedb.data.Entity;
import com.aoedb.data.EntityElement;
import com.aoedb.data.TypeElement;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.RouteParameters;

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
        String e1Name = e1.getAgeElement().getName(), e2Name = e2.getAgeElement().getName();
        if (e1.getEntityID() == 12 || e1.getEntityID() == 15) e1Name = Database.getElement(Database.TECH_LIST, -1, language).getName();
        if (e2.getEntityID() == 12 || e2.getEntityID() == 15) e2Name = Database.getElement(Database.TECH_LIST, -1, language).getName();

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
            case Database.ACCURACY: return "stat_name_11";
            case Database.ATTACK_DELAY: return "stat_name_12";
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
       return Database.getElement(Database.CIVILIZATION_LIST, Database.getTechnology(id, language).getAvailableCivIds().get(0), language).getName();
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
        if (Database.SPANISH.equals(lang)) {
            return Database.SPANISH_FLAG;
        }
        return Database.DEFAULT_FLAG;
    }

    public static String getLanguageFromFlag(String flagLang){
        if (Database.SPANISH_FLAG.equals(flagLang)) {
            return Database.SPANISH;
        }
        return Database.DEFAULT_LANGUAGE;
    }

    public static String checkLanguage(String language) {
        if (Database.SPANISH.equals(language)) {
            return language;
        }
        return Database.DEFAULT_LANGUAGE;
    }

    public static RouteParameters changeRouteParameter(RouteParameters r, String name, String value){
        Map<String, String> map = new HashMap<>();
        for (String s : r.getParameterNames()) map.put(s, r.get(s).orElse(""));
        map.put(name, value);
        return new RouteParameters(map);
    }

    public static Component getEntityItemRow(EntityElement entityElement, boolean border) {
        Label name = new Label(entityElement.getName());
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
        Label name = new Label(entityElement.getName());
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

    /*
    public static void showPopupIcon(View parent, Context c, String name, int imageID, boolean transparency, String backgroundColor){
            if (imageID != R.drawable.t_white) {
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.icon_zoom, null);
                TextView nameText = view.findViewById(R.id.entity_name);
                nameText.setText(name);
                View layout = view.findViewById(R.id.popup_layout);
                ImageView icon = view.findViewById(R.id.entity_icon);
                icon.setImageResource(imageID);
                switch (backgroundColor){
                    case Database.BLUE:
                        layout.setBackgroundColor(c.getColor(R.color.tt_back4));
                        icon.setBackgroundColor(c.getColor(R.color.blue_border));
                        break;
                    case Database.RED:
                        layout.setBackgroundColor(c.getColor(R.color.tt_back3));
                        icon.setBackgroundColor(c.getColor(R.color.red_border));
                        break;
                    case Database.GREEN:
                        layout.setBackgroundColor(c.getColor(R.color.tt_back1));
                        icon.setBackgroundColor(c.getColor(R.color.green_border));
                        break;
                }

                if (transparency) icon.setBackground(null);
                final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pw.setOutsideTouchable(true);
                pw.setTouchInterceptor(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            pw.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
                View container = pw.getContentView().getRootView();
                Context context = pw.getContentView().getContext();
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
                p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                p.dimAmount = 0.5f;
                wm.updateViewLayout(container, p);
            }
    }

     */

    /*
    public static void setTypeValues(LinkedHashMap<String, List<TypeElement>> values, ExpandableListView elv, Context c){
        ArrayList<String> groupList = new ArrayList<>(values.keySet());
        TypeAdapter adapter = new TypeAdapter(c, groupList, values);
        elv.setAdapter(adapter);
        for(int i=0; i < adapter.getGroupCount(); i++) elv.expandGroup(i);
    }
     */



    /*
    public static void printOrderedLists(){
        List<EntityElement> u = Database.getList(Database.UNIT_LIST);
        List<EntityElement> b = Database.getList(Database.BUILDING_LIST);
        List<EntityElement> t = Database.getList(Database.TECH_LIST);
        Comparator<EntityElement> comparator = new Comparator<EntityElement>() {
            @Override
            public int compare(EntityElement o1, EntityElement o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Collections.sort(u, comparator);
        Collections.sort(b, comparator);
        Collections.sort(t, comparator);
        for(EntityElement e: u) System.out.print(e.getId() +" ");
        System.out.println();
        System.out.println("-----------");
        for(EntityElement e: b) System.out.print(e.getId() +" ");
        System.out.println();
        System.out.println("-----------");
        for(EntityElement e: t) System.out.print(e.getId() +" ");
        System.out.println();
        System.out.println("-----------");
    }


    public static void xml(Context c){
        try{
            LinkedHashMap<Integer, String> history =  new LinkedHashMap<>();
            List<ListElement> list = readList(Database.HISTORY_LIST, c);
            for (ListElement l : list) history.put(l.getId(), readHistory(l.getId(), c));
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag("","list");
            for (int i : history.keySet()){
                String text = history.get(i);
                serializer.startTag("", "item");
                serializer.attribute("", "id", String.valueOf(i));
                serializer.text(text);
                serializer.endTag("", "item");
            }
            serializer.endTag("","list");
            serializer.endDocument();
            FileOutputStream fileos= c.openFileOutput("history_text.xml", Context.MODE_PRIVATE);
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    */
}


