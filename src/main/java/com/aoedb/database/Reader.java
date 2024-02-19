package com.aoedb.database;


import com.aoedb.data.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Reader {
    Storage storage;
    
    public Reader(Storage storage){
        this.storage = storage;
    }

    public InputStream getData(String fileName) throws IOException {
        String path = Database.BASE_DIR +"data/" + fileName +".xml";
        PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
        return scanner.getResource(path).getInputStream();
    }

    public static HashMap<String, HashMap<String, String>> readStringMap(){
        HashMap<String, HashMap<String, String>> b = new HashMap<>();
        HashMap<String, String> enMap = readLangMap(Database.ENGLISH);
        HashMap<String, String> esMap = readLangMap(Database.SPANISH);
        b.put(Database.ENGLISH, enMap);
        b.put(Database.SPANISH, esMap);
        return b;
    }

    public static HashMap<String, List<String>> readHistoryTextMap(){
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> enHistory = readHistoryText(Database.ENGLISH);
        List<String> esHistory = readHistoryText(Database.SPANISH);
        map.put(Database.ENGLISH, enHistory);
        map.put(Database.SPANISH, esHistory);
        return map;
    }


    public static List<String> readHistoryText(String language){
        try {
            List<String> b = new ArrayList<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();

            String path = Database.BASE_DIR +"data/"+ language + "/" + Database.HISTORY_TEXT +".xml";
            PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
            Document doc = docBuilder.parse(scanner.getResource(path).getInputStream());
            NodeList list = doc.getElementsByTagName("item");
            for (int i = 0; i < list.getLength(); ++i){
                Element item = (Element) list.item(i);
                b.add(item.getTextContent());
            }
            return b;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static HashMap<String, String> readLangMap(String language){
        try {
            HashMap<String, String> b = new HashMap<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            String path = Database.BASE_DIR + "data/" + language + "/strings.xml";
            PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
            Resource resource = scanner.getResource(path);
            Document doc = docBuilder.parse(resource.getInputStream());
            NodeList list = doc.getElementsByTagName("string");
            for (int i = 0; i< list.getLength(); ++i) {
                Element element = (Element) list.item(i);
                String key = element.getAttribute("name");
                String content = element.getTextContent().replace("\\n", "\n").replace("\\'", "'").replace("\\@", "@");
                b.put(key, content);
            }
            return b;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public List<EntityElement> readList(String file){
        try{
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            NodeList list = doc.getElementsByTagName("item");
            List<EntityElement> b = new ArrayList<>();
            for (int i = 0; i< list.getLength(); ++i) {
                Element element = (Element) list.item(i);
                String type = Utils.getEntityTypeString(file);
                int id = Integer.parseInt(element.getAttribute("id"));
                String name = type +"_name_"+id;
                String img = element.getAttribute("image");

                String mediaPath;
                if (file.equals(Database.CIVILIZATION_LIST)) {
                    mediaPath = "s_" + img.substring(2);
                }
                else {
                    if (id == 23 && file.equals(Database.BUILDING_LIST)) mediaPath = "g_fortified_wall";
                    else if (id == 18 && file.equals(Database.BUILDING_LIST)) mediaPath = "g_watch_tower";
                    else if (id == 24 && file.equals(Database.BUILDING_LIST)) mediaPath = "g_fortified_gate";
                    else if (id == 25 && file.equals(Database.BUILDING_LIST)) mediaPath = "g_guard_tower";
                    else if (id == 30 && file.equals(Database.BUILDING_LIST)) mediaPath = "g_keep";
                    else mediaPath = "g_" + img.substring(2);
                    mediaPath = Database.getImage(mediaPath);
                }
                String imgPath = Database.getImage(img);
                EntityElement u = new EntityElement(id, name, imgPath, mediaPath, type);
                b.add(u);
            }
            return b;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<GroupList> readGroupLists(String file, List<EntityElement> set) {
        try {
            List<GroupList> b = new ArrayList<>();

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            NodeList list = doc.getElementsByTagName("group");
            for (int sort = 0; sort < list.getLength(); ++sort) {
                LinkedHashMap<StringKey, List<EntityElement>> map = new LinkedHashMap<>();
                Element group = (Element) list.item(sort);
                boolean alphabeticalOrder = Boolean.parseBoolean(group.getAttribute("alphabeticalOrder"));
                NodeList categoryList = group.getElementsByTagName("category");
                GroupList groupList = new GroupList();
                for (int i = 0; i < categoryList.getLength(); ++i) {
                    Element category = (Element) categoryList.item(i);
                    String[] sids = category.getAttribute("ids").split(" ");
                    ArrayList<EntityElement> a = new ArrayList<>();
                    for (String sid : sids) a.add(set.get(Integer.parseInt(sid) - 1));
                    StringKey groupTitle = new StringKey(category.getAttribute("name"));
                    map.put(groupTitle, a);
                    groupList.setEntityMap(map);
                    groupList.setAlphabeticalOrder(alphabeticalOrder);
                }
                b.add(groupList);
            }
            return b;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<HashMap<Integer, Integer>> sortIndexMap(String file){
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            List<HashMap<Integer, Integer>> listMap = new ArrayList<>();
            NodeList list = doc.getElementsByTagName("group");
            for(int z = 0; z < list.getLength(); ++z) {
                HashMap<Integer, Integer> b = new HashMap<>();
                Element group = (Element) list.item(z);
                NodeList categoryList = group.getElementsByTagName("category");
                int counter = 0;
                for (int i = 0; i < categoryList.getLength(); ++i) {
                    Element category = (Element) categoryList.item(i);
                    String[] sids = category.getAttribute("ids").split(" ");
                    for (String sid : sids) {
                        int id = Integer.parseInt(sid);
                        b.put(id, counter);
                        ++counter;
                    }
                }
                listMap.add(b);
            }
            return listMap;
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Descriptor> readDescriptors(String type){
        List<Descriptor> map = new ArrayList<>();
        List<EntityElement> list = storage.getList(type +"_list");
        for (EntityElement element: list) {
            Descriptor d = new Descriptor();
            d.setNominative(type + "_description_nominative_" + element.getId());
            d.setQuickDescription(type + "_description_quick_" + element.getId());
            d.setBriefDescription(type + "_description_brief_" + element.getId());
            d.setLongDescription(type + "_description_long_" + element.getId());
            d.setExtraDescription(type + "_description_extra_" + element.getId());
            if (d.getNominative().getKey().isEmpty()) d.setNominative(element.getName().getKey());
            if (d.getBriefDescription().getKey().isEmpty()) d.setBriefDescription(d.getLongDescription().getKey());
            map.add(d);
        }
        return map;
    }

    public List<LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>>> readTypeValues(String file) {
        try {
            List<LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>>> map = new ArrayList<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            NodeList list = doc.getElementsByTagName("item");
            for (int z = 0; z < list.getLength(); ++z) {
                Node item = list.item(z);
                NodeList entryList = item.getChildNodes();
                LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> b = new LinkedHashMap<>();
                for (int i = 0; i < entryList.getLength(); ++i) {
                    Node entryNode = entryList.item(i);
                    if (entryNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element entry = (Element) entryNode;
                        String groupName = entry.getAttribute("nameID");
                        NodeList valList = entry.getChildNodes();
                        LinkedHashMap<Integer, Double> valuesMap = new LinkedHashMap<>();
                        for (int j = 0; j < valList.getLength(); ++j) {
                            Node valueNode = valList.item(j);
                            if (valueNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element value = (Element) valList.item(j);
                                int typeID = Integer.parseInt(value.getAttribute("type"));
                                double val = Double.parseDouble(value.getTextContent());
                                valuesMap.put(typeID, val);
                            }
                        }
                        b.put(new StringKey(groupName), valuesMap);
                    }
                }
                map.add(b);
            }
            return map;
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public List<LinkedHashMap<StringKey, List<EntityElement>>> readUpgrades(String file) {
        try {
            List<LinkedHashMap<StringKey, List<EntityElement>>> map = new ArrayList<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            List<EntityElement> buildingList = storage.getList(Database.BUILDING_LIST);
            List<EntityElement> techList = storage.getList(Database.TECH_LIST);
            NodeList list = doc.getElementsByTagName("item");
            for (int z = 0; z < list.getLength(); ++z) {
                Node n = list.item(z);
                LinkedHashMap<StringKey, List<EntityElement>> b = new LinkedHashMap<>();
                NodeList bList = n.getChildNodes();
                for (int i = 0; i < bList.getLength(); ++i) {
                    Node n1 = bList.item(i);
                    if (n1.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) n1;
                        int buildingID = Integer.parseInt(e.getAttribute("id"));
                        StringKey buildingName = buildingList.get(buildingID - 1).getName();
                        String[] upgrades = e.getAttribute("upgrades").split(" ");
                        List<EntityElement> upgradeList = new ArrayList<>();
                        for (String u : upgrades) {
                            int uid = Integer.parseInt(u);
                            EntityElement l = techList.get(uid - 1);
                            upgradeList.add(l);
                        }
                        b.put(buildingName, upgradeList);
                    }
                }
                if (b.isEmpty()) b.put(new StringKey("none"), new ArrayList<>());
                map.add(b);
            }

            return map;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public List<LinkedHashMap<StringKey, List<EntityElement>>> readPerformance() {
        try {
            List<LinkedHashMap<StringKey, List<EntityElement>>> map = new ArrayList<>();
            List<EntityElement> performanceList = storage.getList(Database.PERFORMANCE_LIST);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.UNIT_PERFORMANCE));
            NodeList list = doc.getElementsByTagName("item");
            for (int z = 0; z < list.getLength(); ++z) {
                Element n = (Element) list.item(z);
                LinkedHashMap<StringKey, List<EntityElement>> b = new LinkedHashMap<>();
                Element strong = (Element) n.getElementsByTagName("strong").item(0);
                ArrayList<EntityElement> strongList = new ArrayList<>();
                if (strong.hasAttribute("ids")) {
                    String[] strongIDs = strong.getAttribute("ids").split(" ");
                    for (String s : strongIDs) {
                        int id = Integer.parseInt(s);
                        EntityElement l = performanceList.get(id - 1);
                        strongList.add(l);
                    }
                }
                Element weak = (Element) n.getElementsByTagName("weak").item(0);
                ArrayList<EntityElement> weakList = new ArrayList<>();
                if (weak.hasAttribute("ids")) {
                    String[] weakIDs = weak.getAttribute("ids").split(" ");
                    for (String s : weakIDs) {
                        int id = Integer.parseInt(s);
                        EntityElement l = performanceList.get(id - 1);
                        weakList.add(l);
                    }
                }
                Comparator<EntityElement> comp = EntityElement.getListElementComparator(storage.getOrderMap(Database.PERFORMANCE_GROUPS, 0));
                strongList.sort(comp);
                weakList.sort(comp);
                b.put(new StringKey("performance_strong"), strongList);
                b.put(new StringKey("performance_weak"), weakList);
                map.add(b);
            }
            return map;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public List<LinkedHashMap<StringKey, List<EntityElement>>> readTrainable() {
        try {
            List<LinkedHashMap<StringKey, List<EntityElement>>> map = new ArrayList<>();
            List<EntityElement> unitList = storage.getList(Database.UNIT_LIST);
            List<EntityElement> techList = storage.getList(Database.TECH_LIST);
            List<EntityElement> buildingList = storage.getList(Database.BUILDING_LIST);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.BUILDING_TRAINABLE));
            NodeList list = doc.getElementsByTagName("item");
            for (int z = 0; z < list.getLength(); ++z) {
                Node item = list.item(z);
                NodeList applicationList = item.getChildNodes();
                LinkedHashMap<StringKey, List<EntityElement>> b = new LinkedHashMap<>();
                for (int i = 0; i < applicationList.getLength(); ++i) {
                    Node entryNode = applicationList.item(i);
                    if (entryNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element app = (Element) entryNode;
                        String tag = app.getTagName();
                        EntityElement l;
                        String groupName = "", sortFile = "";
                        String[] idList = app.getTextContent().split(" ");
                        ArrayList<EntityElement> elementList = new ArrayList<>();
                        for (String sid : idList) {
                            int entityID = Integer.parseInt(sid);
                            switch (tag) {
                                case "units":
                                    l = unitList.get(entityID - 1);
                                    groupName = "tt_units";
                                    sortFile = Database.UNIT_GROUPS;
                                    break;
                                case "techs":
                                    l = techList.get(entityID - 1);
                                    groupName = "tt_techs";
                                    sortFile = Database.TECH_GROUPS;
                                    break;
                                case "buildings":
                                    l = buildingList.get(entityID - 1);
                                    groupName = "tt_buildings";
                                    sortFile = Database.BUILDING_GROUPS;
                                    break;
                                default:
                                    groupName = "none";
                                    l = new EntityElement(-1, groupName, Database.getImage("t_white"), "", "");
                                    break;
                            }
                            elementList.add(l);
                        }
                        elementList.sort(EntityElement.  getListElementComparator(storage.getOrderMap(sortFile, 0)));
                        b.put(new StringKey(groupName), elementList);

                    }
                }
                if (b.isEmpty()) b.put(new StringKey("none"), new ArrayList<>());
                map.add(b);
            }
            return map;
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void readTechApplications(){

        List<EntityElement> units = storage.getList(Database.UNIT_LIST);
        List<EntityElement> buildings = storage.getList(Database.BUILDING_LIST);
        List<EntityElement> techs = storage.getList(Database.TECH_LIST);
        List<LinkedHashMap<StringKey, List<EntityElement>>> applications = new ArrayList<>(techs.size());
        for(int i = 0; i< techs.size(); ++i) applications.add(new LinkedHashMap<>());
        for(EntityElement e: units) {
           List<Integer> upgrades = storage.getUnit(e.getId()).getUpgradesIds();
           for(int i : upgrades){
               LinkedHashMap<StringKey, List<EntityElement>> map =  applications.get(i - 1);
               if(map.containsKey(new StringKey("tt_units"))){
                   List<EntityElement> list = map.get(new StringKey("tt_units"));
                   if (!list.contains(e)) list.add(e);
               }
               else {
                   List<EntityElement> list =  new ArrayList<>();
                   list.add(e);
                   map.put(new StringKey("tt_units"), list);
               }
           }
        }
        for(EntityElement e: buildings) {
            List<Integer> upgrades = storage.getBuilding(e.getId()).getUpgradesIds();
            for(int i : upgrades){
                LinkedHashMap<StringKey, List<EntityElement>> map =  applications.get(i - 1);
                if(map.containsKey(new StringKey("tt_buildings"))){
                    List<EntityElement> list = map.get(new StringKey("tt_buildings"));
                    if (!list.contains(e)) list.add(e);
                }
                else {
                    List<EntityElement> list =  new ArrayList<>();
                    list.add(e);
                    map.put(new StringKey("tt_buildings"), list);
                }
            }
        }
        for(EntityElement e: techs) {
            List<Integer> upgrades = storage.getTechnology(e.getId()).getUpgradesIds();
            for(int i : upgrades){
                LinkedHashMap<StringKey, List<EntityElement>> map =  applications.get(i - 1);
                if(map.containsKey(new StringKey("tt_techs"))){
                    List<EntityElement> list = map.get(new StringKey("tt_techs"));
                    if (!list.contains(e)) list.add(e);
                }
                else {
                    List<EntityElement> list =  new ArrayList<>();
                    list.add(e);
                    map.put(new StringKey("tt_techs"), list);
                }
            }
        }
        for (HashMap<StringKey, List<EntityElement>> map : applications){
            for(StringKey s : map.keySet()){
                List<EntityElement> list =  map.get(s);
                String sortFile;
                if (new StringKey("tt_units").equals(s)) sortFile = Database.UNIT_GROUPS;
                else if (new StringKey("tt_buildings").equals(s)) sortFile = Database.BUILDING_GROUPS;
                else if (new StringKey("tt_techs").equals(s)) sortFile = Database.TECH_GROUPS;
                else sortFile = "";
                list.sort(EntityElement.getListElementComparator(storage.getOrderMap(sortFile, 0)));
            }
            if (map.isEmpty()) map.put(new StringKey("none"), new ArrayList<>());
        }

        for (EntityElement i: techs) storage.getTechnology(i.getId()).setApplications(applications.get(i.getId() - 1));


    }

    public HashMap<Integer, LinkedHashMap<StringKey, List<EntityElement>>> readTypeEntity(boolean isAttackType){
        HashMap<Integer, LinkedHashMap<StringKey, List<EntityElement>>> b = new HashMap<>();
        List<EntityElement> types = storage.getList(Database.TYPE_LIST);

        for (EntityElement element : types){
            LinkedHashMap<StringKey, List<EntityElement>> map = new LinkedHashMap<>();
            List<EntityElement> unitList = new ArrayList<>();
            List<EntityElement> buildingList = new ArrayList<>();
            for (EntityElement unit : storage.getList(Database.UNIT_LIST)){
                Unit u = storage.getUnit(unit.getId());
                if (itemContainsType(u, element.getId(), isAttackType)) unitList.add(unit);
            }
            for (EntityElement building : storage.getList(Database.BUILDING_LIST)){
                Building bl = storage.getBuilding(building.getId());
                if (itemContainsType(bl, element.getId(), isAttackType)) buildingList.add(building);
            }
            if (isAttackType){
                if (!unitList.isEmpty()) map.put(new StringKey("type_entities_unit_attack", element.getName()), unitList);
                if (!buildingList.isEmpty()) map.put(new StringKey("type_entities_building_attack", element.getName()), buildingList);
            }
            else {
                if (!unitList.isEmpty()) map.put(new StringKey("type_entities_unit_armor", element.getName()), unitList);
                if (!buildingList.isEmpty()) map.put(new StringKey("type_entities_building_armor", element.getName()), buildingList);
            }
            b.put(element.getId(), map);
        }
        return b;
    }

    public boolean itemContainsType(Item i, int typeID, boolean isAttack){
        LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>> typeList;
        if (isAttack) typeList = i.getBaseAttackValues();
        else typeList = i.getBaseArmorValues();
        for (StringKey s : typeList.keySet()) {
            LinkedHashMap<Integer, Double> map = typeList.get(s);
            if (map.containsKey(typeID)) return true;
        }
        return false;
    }

    public List<AvailabilityContainer> readAvailability(String file) {
        try {
            List<AvailabilityContainer> map = new ArrayList<>();
            List<EntityElement> civs = storage.getList(Database.CIVILIZATION_LIST);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            NodeList list = doc.getElementsByTagName("item");
            for (int z = 0; z < list.getLength(); ++z) {
                Element item = (Element) list.item(z);
                LinkedHashMap<StringKey, List<EntityElement>> availabilityList = new LinkedHashMap<>();
                List<EntityElement> available = new ArrayList<>();
                List<EntityElement> unavailable = new ArrayList<>(civs);
                HashMap<Integer, Boolean> civMap = new HashMap<>();
                for (int j = 1; j <= civs.size(); ++j) civMap.put(j, false);
                String[] civIDs = item.getAttribute("civs").split(" ");
                for (String s : civIDs) {
                    EntityElement e = civs.get(Integer.parseInt(s) - 1);
                    civMap.put(Integer.valueOf(s), true);
                    available.add(e);
                    unavailable.remove(e);
                }
                List<Integer> availableIDs = new ArrayList<>();
                int realRow;
                if (file.equals(Database.UNIT_AVAILABILITY) && (z == 29 || z == 83 || z == 85 || z == 88 || z == 143)) {
                    if (z == 85) realRow = 27; //imp skirmisher -> elite skirmisher (no turks)
                    else realRow = 0; //villager -> all civs
                }
                else if (file.equals(Database.TECH_AVAILABILITY) && (z == 123 || z == 125)) {
                    if (z == 123) realRow = 54; //imp skirmisher -> elite skirmisher (no turks)
                    else realRow = 0; //villager -> all civs
                }
                else realRow = z;
                Element item1 = (Element) list.item(realRow);
                String[] civIDs1 = item1.getAttribute("civs").split(" ");
                for (String s : civIDs1) availableIDs.add(Integer.parseInt(s));
                availabilityList.put(new StringKey("civ_available"), available);
                availabilityList.put(new StringKey("civ_unavailable"), unavailable);
                map.add(new AvailabilityContainer(availabilityList, availableIDs, civMap));
            }
            return map;

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public List<Unit> readUnits(){
        try {
            List<Unit> unitMap = new ArrayList<>();

            List<Descriptor> descriptors = readDescriptors(Database.UNIT);
            List<LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>>> attackValues = readTypeValues(Database.UNIT_ATTACK);
            List<LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>>> armorValues = readTypeValues(Database.UNIT_ARMOR);
            List<LinkedHashMap<StringKey, List<EntityElement>>> upgrades = readUpgrades(Database.UNIT_UPGRADES);
            List<LinkedHashMap<StringKey, List<EntityElement>>> performance = readPerformance();
            List<AvailabilityContainer> availability = readAvailability(Database.UNIT_AVAILABILITY);
            List<BonusContainer> bonuses = readBonusContainers(Database.UNIT_BONUS);

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.UNIT_STATS));
            NodeList list = doc.getElementsByTagName("item");
            for (int i = 0; i < list.getLength(); ++i) {
                Element unitItem = (Element) list.item(i);
                Unit u = new Unit();

                //UNIT INFO
                Element baseInfo = (Element) unitItem.getElementsByTagName("baseInfo").item(0);
                EntityElement le1 = storage.getElement(Database.UNIT_LIST, i + 1);
                u.addEntityElement("entity_name", le1);

                int buildingID = Integer.parseInt(baseInfo.getAttribute("trainingBuildingID"));
                EntityElement le2 = storage.getElement(Database.BUILDING_LIST, buildingID);
                u.addEntityElement("unit_training_building", le2);

                int ageID = Integer.parseInt(baseInfo.getAttribute("ageID"));
                EntityElement le3 = storage.getElement(Database.TECH_LIST, ageID);
                u.addEntityElement("entity_age", le3);

                int classID = Integer.parseInt(baseInfo.getAttribute("classID"));
                EntityElement le4 = storage.getElement(Database.CLASS_LIST, classID);
                u.addEntityElement("entity_class", le4);

                //UNIT STATS
                Element stats = (Element) unitItem.getElementsByTagName("stats").item(0);
                NamedNodeMap statsList = stats.getAttributes();
                for (int j = 0; j < statsList.getLength(); ++j) {
                    Attr attr = (Attr) statsList.item(j);
                    String statName = Utils.unCamelCase(attr.getName());
                    String valueStr = attr.getValue();
                    double value;
                    if (valueStr.equals("-")) value = Double.NaN;
                    else value = Double.parseDouble(valueStr);
                    u.setBaseStat(statName, value);
                }

                //UNIT COST
                Element cost = (Element) unitItem.getElementsByTagName("cost").item(0);
                NodeList resourceList = cost.getElementsByTagName("resource");
                for (int j = 0; j < resourceList.getLength(); ++j) {
                    Element res = (Element) resourceList.item(j);
                    String resName = res.getAttribute("name");
                    int resValue = Integer.parseInt(res.getTextContent());
                    u.setBaseCost(resName, resValue);
                }

                //UNIT DEVELOPMENT
                Element development = (Element) unitItem.getElementsByTagName("development").item(0);
                int rTechID = Integer.parseInt(development.getAttribute("requiredTechID"));
                EntityElement le5 = storage.getElement(Database.TECH_LIST, rTechID);
                u.addEntityElement("required_technology", le5);

                int pUpgradeID = Integer.parseInt(development.getAttribute("previousUpgradeID"));
                EntityElement le6 = storage.getElement(Database.UNIT_LIST, pUpgradeID);
                u.addEntityElement("upgraded_from", le6);

                int nUpgradeID = Integer.parseInt(development.getAttribute("nextUpgradeID"));
                EntityElement le7 = storage.getElement(Database.UNIT_LIST, nUpgradeID);
                u.addEntityElement("next_upgrade", le7);

                u.setDescriptor(descriptors.get(i));
                u.setUpgrades(upgrades.get(i));
                u.setUpgradesIds();
                u.setAvailability(availability.get(i));
                u.setBonuses(bonuses.get(i));
                u.setAttackValues(attackValues.get(i));
                u.setArmorValues(armorValues.get(i));
                u.setPerformance(performance.get(i));
                u.resetStats();

                unitMap.add(u);
            }
            return unitMap;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Building> readBuildings(){
        try {
            List<Building> buildingMap = new ArrayList<>();

            List<Descriptor> descriptors = readDescriptors(Database.BUILDING);
            List<LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>>> attackValues = readTypeValues(Database.BUILDING_ATTACK);
            List<LinkedHashMap<StringKey, LinkedHashMap<Integer, Double>>> armorValues = readTypeValues(Database.BUILDING_ARMOR);
            List<LinkedHashMap<StringKey, List<EntityElement>>> upgrades = readUpgrades(Database.BUILDING_UPGRADES);
            List<LinkedHashMap<StringKey, List<EntityElement>>> trainable = readTrainable();
            List<AvailabilityContainer> availability = readAvailability(Database.BUILDING_AVAILABILITY);
            List<BonusContainer> bonuses = readBonusContainers(Database.BUILDING_BONUS);

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.BUILDING_STATS));
            NodeList list = doc.getElementsByTagName("item");
            for(int i = 0; i < list.getLength(); ++i) {
                Element buildingItem = (Element) list.item(i);
                Building b = new Building();

                //BUILDING INFO
                Element baseInfo = (Element) buildingItem.getElementsByTagName("baseInfo").item(0);
                EntityElement le1 = storage.getElement(Database.BUILDING_LIST, i + 1);
                b.addEntityElement("entity_name", le1);

                int builderUnitID = Integer.parseInt(baseInfo.getAttribute("builderUnitID"));
                EntityElement le2 = storage.getElement(Database.UNIT_LIST, builderUnitID);
                b.addEntityElement("builder_unit", le2);

                int ageID = Integer.parseInt(baseInfo.getAttribute("ageID"));
                EntityElement le3 = storage.getElement(Database.TECH_LIST, ageID);
                b.addEntityElement("entity_age", le3);

                int classID = Integer.parseInt(baseInfo.getAttribute("classID"));
                EntityElement le4 = storage.getElement(Database.CLASS_LIST, classID);
                b.addEntityElement("entity_class", le4);

                //BUILDING STATS
                Element stats = (Element) buildingItem.getElementsByTagName("stats").item(0);
                NamedNodeMap statsList = stats.getAttributes();
                for (int j = 0; j < statsList.getLength(); ++j) {
                    Attr attr = (Attr) statsList.item(j);
                    String statName = Utils.unCamelCase(attr.getName());
                    String valueStr = attr.getValue();
                    double value;
                    if (valueStr.equals("-")) value = Double.NaN;
                    else value = Double.parseDouble(valueStr);
                    b.setBaseStat(statName, value);
                }

                //UNIT COST
                Element cost = (Element) buildingItem.getElementsByTagName("cost").item(0);
                NodeList resourceList = cost.getElementsByTagName("resource");
                for (int j = 0; j < resourceList.getLength(); ++j) {
                    Element res = (Element) resourceList.item(j);
                    String resName = res.getAttribute("name");
                    int resValue = Integer.parseInt(res.getTextContent());
                    b.setBaseCost(resName, resValue);
                }

                //BUILDING DEVELOPMENT
                Element development = (Element) buildingItem.getElementsByTagName("development").item(0);

                int rBuildingID = Integer.parseInt(development.getAttribute("requiredBuildingID"));
                EntityElement le5 = storage.getElement(Database.BUILDING_LIST, rBuildingID);
                b.addEntityElement("required_building", le5);

                int rTechID = Integer.parseInt(development.getAttribute("requiredTechID"));
                EntityElement le6 = storage.getElement(Database.TECH_LIST, rTechID);
                b.addEntityElement("required_technology", le6);

                int pUpgradeID = Integer.parseInt(development.getAttribute("previousUpgradeID"));
                EntityElement le7 = storage.getElement(Database.BUILDING_LIST, pUpgradeID);
                b.addEntityElement("upgraded_from", le7);

                int nUpgradeID = Integer.parseInt(development.getAttribute("nextUpgradeID"));
                EntityElement le8 = storage.getElement(Database.BUILDING_LIST, nUpgradeID);
                b.addEntityElement("next_upgrade", le8);

                b.setDescriptor(descriptors.get(i));
                b.setUpgrades(upgrades.get(i));
                b.setUpgradesIds();
                b.setAvailability(availability.get(i));
                b.setBonuses(bonuses.get(i));
                b.setAttackValues(attackValues.get(i));
                b.setArmorValues(armorValues.get(i));
                b.setTrainable(trainable.get(i));
                b.resetStats();

                buildingMap.add(b);
            }
            return buildingMap;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Technology> readTechnologies(){
        try {
            List<Technology> techMap = new ArrayList<>();

            List<Descriptor> descriptors = readDescriptors(Database.TECH);
            List<LinkedHashMap<StringKey, List<EntityElement>>> upgrades = readUpgrades(Database.TECH_UPGRADES);
            List<AvailabilityContainer> availability = readAvailability(Database.TECH_AVAILABILITY);
            List<BonusContainer> bonuses = readBonusContainers(Database.TECH_BONUS);

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.TECH_STATS));
            NodeList list = doc.getElementsByTagName("item");
            for(int i = 0; i < list.getLength(); ++i) {
                Element techItem = (Element) list.item(i);
                Technology t = new Technology();

                //TECH INFO
                Element baseInfo = (Element) techItem.getElementsByTagName("baseInfo").item(0);
                EntityElement le1 = storage.getElement(Database.TECH_LIST, i + 1);
                t.addEntityElement("entity_name", le1);

                int buildingID = Integer.parseInt(baseInfo.getAttribute("researchBuildingID"));
                EntityElement le2 = storage.getElement(Database.BUILDING_LIST, buildingID);
                t.addEntityElement("research_building", le2);

                int ageID = Integer.parseInt(baseInfo.getAttribute("ageID"));
                EntityElement le3 = storage.getElement(Database.TECH_LIST, ageID);
                t.addEntityElement("entity_age", le3);

                //TECH STATS
                Element stats = (Element) techItem.getElementsByTagName("stats").item(0);
                NamedNodeMap statsList = stats.getAttributes();
                for (int j = 0; j < statsList.getLength(); ++j) {
                    Attr attr = (Attr) statsList.item(j);
                    String statName = Utils.unCamelCase(attr.getName());
                    String valueStr = attr.getValue();
                    double value;
                    if (valueStr.equals("-")) value = Double.NaN;
                    else value = Double.parseDouble(valueStr);
                    t.setBaseStat(statName, value);
                }


                //TECH COST
                Element cost = (Element) techItem.getElementsByTagName("cost").item(0);
                NodeList resourceList = cost.getElementsByTagName("resource");
                for (int j = 0; j < resourceList.getLength(); ++j) {
                    Element res = (Element) resourceList.item(j);
                    String resName = res.getAttribute("name");
                    int resValue = Integer.parseInt(res.getTextContent());
                    t.setBaseCost(resName, resValue);
                }

                //TECH DEPENDENCIES
                Element development = (Element) techItem.getElementsByTagName("development").item(0);
                int rTechID = Integer.parseInt(development.getAttribute("requiredTechID"));
                EntityElement le4 = storage.getElement(Database.TECH_LIST, rTechID);
                t.addEntityElement("required_technology", le4);

                int nUpgradeID = Integer.parseInt(development.getAttribute("nextUpgradeID"));
                EntityElement le5 = storage.getElement(Database.TECH_LIST, nUpgradeID);
                t.addEntityElement("next_upgrade", le5);


                t.setDescriptor(descriptors.get(i));
                t.setUpgrades(upgrades.get(i));
                t.setUpgradesIds();
                t.setAvailability(availability.get(i));
                t.setBonuses(bonuses.get(i));
                t.resetStats();
                techMap.add(t);
            }
            return techMap;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Civilization> readCivilizations(){
        try {
            List<Civilization> civMap =  new ArrayList<>();
            List<StringKey> styles = readCivStyles();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.CIVILIZATION_INFO));
            NodeList list = doc.getElementsByTagName("item");
            for (int i = 0; i < list.getLength(); ++i) {
                Element item = (Element) list.item(i);
                Civilization civ = new Civilization(storage.getEcoList());
                EntityElement lel = storage.getElement(Database.CIVILIZATION_LIST, i + 1);
                civ.addEntityElement("entity_name", lel);

                //CIV STYLE
                civ.setCivStyle(styles.get(i));

                //CIV BONUS
                BonusContainer bc = new BonusContainer();
                Element bonus = (Element) item.getElementsByTagName("bonus").item(0);
                String[] bList = bonus.getAttribute("bonusList").split(" ");
                List<Integer> bonusList = new ArrayList<>();
                for (String sid : bList) {
                    int bID = Integer.parseInt(sid);
                    bonusList.add(bID);
                }
                bc.setList(bonusList, 0);

                //TEAM BONUS
                List<Integer> teamBonusList = new ArrayList<>();
                teamBonusList.add(Integer.parseInt(bonus.getAttribute("teamBonus")));
                bc.setList(teamBonusList, 1);

                //UNIQUE UNITS
                Element unitSection = (Element) item.getElementsByTagName("uniqueUnits").item(0);
                NodeList units = unitSection.getElementsByTagName(Database.UNIT);
                for (int j = 0; j < units.getLength(); ++j) {
                    Element unit = (Element) units.item(j);
                    int unitID = Integer.parseInt(unit.getAttribute("unitID"));
                    if (unit.hasAttribute("eliteUnitID")) {
                        int eliteUnitID = Integer.parseInt(unit.getAttribute("eliteUnitID"));
                        civ.setEliteUniqueUnit(unitID, eliteUnitID);
                    }
                    civ.addUniqueUnit(unitID);
                }

                //UNIQUE TECHS
                Element techSection = (Element) item.getElementsByTagName("uniqueTechs").item(0);
                NamedNodeMap techList = techSection.getAttributes();
                ArrayList<Integer> uniqueTechList =  new ArrayList<>();
                for (int j = 0; j < techList.getLength(); ++j) {
                    Attr attr = (Attr) techList.item(j);
                    int techID = Integer.parseInt(attr.getValue());
                    uniqueTechList.add(techID);
                }
                bc.setList(uniqueTechList, 2);

                //UNIQUE BUILDINGS
                Element buildingSection = (Element) item.getElementsByTagName("uniqueBuildings").item(0);
                NamedNodeMap uniqueBuildingsAttributes = buildingSection.getAttributes();
                Node buildingIDs = uniqueBuildingsAttributes.getNamedItem("buildingIDs");
                if (buildingIDs != null){
                    String[] ids = ((Attr) buildingIDs).getValue().split(" ");
                    for (String id: ids){
                        civ.addUniqueBuilding(Integer.parseInt(id));
                    }
                }
                civ.setBonuses(bc);

                //STATS
                HashMap<String, Double> ecoStats = storage.getEcoValues();
                for (String s : ecoStats.keySet()) civ.setBaseStat(s, ecoStats.get(s));

                civ.setUpgradesIds(storage.getEcoUpgrades());

                civMap.add(civ);
            }
            return civMap;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }



    public HashMap<Integer, StringKey> makeCivMap() {
        try {
            LinkedHashMap<Integer, StringKey> m = new LinkedHashMap<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.CIVILIZATION_LIST));
            NodeList list = doc.getElementsByTagName("item");
            for (int i = 0; i<list.getLength(); ++i) {
                Element e = (Element) list.item(i);
                int id = Integer.parseInt(e.getAttribute("id"));
                String name ="civilization_name_" + (i + 1);
                m.put(id, new StringKey(name));
            }
            return m;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public HashMap<StringKey, Integer> makeReversedCivMap() {
        try {
            LinkedHashMap<StringKey, Integer> m = new LinkedHashMap<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.CIVILIZATION_LIST));
            NodeList list = doc.getElementsByTagName("item");
            for (int i = 0; i<list.getLength(); ++i) {
                Element e = (Element) list.item(i);
                int id = Integer.parseInt(e.getAttribute("id"));
                String name = "civilization_name_" + (i + 1);
                m.put(new StringKey(name), id);
            }
            return m;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public List<StringKey> readCivStyles(){
        List<StringKey> map =  new ArrayList<>();
        List<EntityElement> list = storage.getList(Database.CIVILIZATION_LIST);
        for(EntityElement element: list){
            String style = "civilization_style_" + element.getId();
            map.add(new StringKey(style));
        }
        return map;
    }

    public List<BonusContainer> readBonusContainers(String file){
        try {
            List<BonusContainer> map = new ArrayList<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            NodeList list = doc.getElementsByTagName("item");
            for (int z = 0; z < list.getLength(); ++z) {
                Element item = (Element) list.item(z);
                BonusContainer bc = new BonusContainer();
                Element bonusElement = (Element) item.getElementsByTagName("bonusList").item(0);
                Element teamBonusElement = (Element) item.getElementsByTagName("teamBonusList").item(0);
                Element uniqueTechElement = (Element) item.getElementsByTagName("uniqueTechList").item(0);
                Element hiddenBonusElement = (Element) item.getElementsByTagName("hiddenBonusList").item(0);
                List<Integer> bonusIds = new ArrayList<>();
                List<Integer> teamBonusIds = new ArrayList<>();
                List<Integer> uniqueTechsIds = new ArrayList<>();
                List<Integer> hiddenBonusIds = new ArrayList<>();
                if (bonusElement.hasAttribute("ids")) {
                    String[] ids = bonusElement.getAttribute("ids").split(" ");
                    for (String sid : ids) bonusIds.add(Integer.parseInt(sid));
                }
                if (teamBonusElement.hasAttribute("ids")) {
                    String[] ids = teamBonusElement.getAttribute("ids").split(" ");
                    for (String sid : ids) teamBonusIds.add(Integer.parseInt(sid));
                }
                if (uniqueTechElement.hasAttribute("ids")) {
                    String[] ids = uniqueTechElement.getAttribute("ids").split(" ");
                    for (String sid : ids) uniqueTechsIds.add(Integer.parseInt(sid));
                }
                if (hiddenBonusElement.hasAttribute("ids")) {
                    String[] ids = hiddenBonusElement.getAttribute("ids").split(" ");
                    for (String sid : ids) hiddenBonusIds.add(Integer.parseInt(sid));
                }
                bc.setList(bonusIds, 0);
                bc.setList(teamBonusIds, 1);
                bc.setList(uniqueTechsIds, 2);
                bc.setList(hiddenBonusIds, 3);
                map.add(bc);
            }
            return map;
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Bonus> readBonuses(String file){
        try{
            List<Bonus> map = new ArrayList<>();
            String effectFile = Utils.getEffectFileName(file);
            List<EffectContainer> effects = readAllEffects(effectFile);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            NodeList list = doc.getElementsByTagName("item");
            for (int z = 0; z < list.getLength(); ++z) {
                Bonus b = new Bonus();
                switch (file) {
                    case Database.TECH_LIST:
                    case Database.HIDDEN_BONUS:
                        b.setTechTreeDescription("");
                        b.setItemDescription("");
                        break;
                    default:
                        String techTreeDescription = "bonus_tech_tree_description_" + (z + 1);
                        String itemDescription = "bonus_item_description_" + (z + 1);
                        b.setTechTreeDescription(techTreeDescription);
                        b.setItemDescription(itemDescription);
                        break;
                }
                b.setEffectContainer(effects.get(z));
                map.add(b);
            }
            return map;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<TechBonus> readTechEffects(){
        List<TechBonus> map = new ArrayList<>();
        List<Bonus> techBonus = readBonuses(Database.TECH_LIST);
        for(int z = 0; z < techBonus.size(); ++z){
            Technology t = storage.getTechnology(z + 1);
            TechBonus techEffect = new TechBonus(+ 1);
            techEffect.setAge(Utils.mapAgeID(t.getAgeElement().getId()));
            techEffect.setAvailableCivs(t.getAvailableCivIds());
            techEffect.setBonus(techBonus.get(z));
            map.add(techEffect);
        }
        return map;
    }

    public List<EffectContainer> readAllEffects(String file){
        try {
            List<EffectContainer> map = new ArrayList<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            NodeList list = doc.getElementsByTagName("item");
            for (int z = 0; z < list.getLength(); ++z) {
                Element item = (Element) list.item(z);
                EffectContainer container = new EffectContainer(z + 1);
                Element effectsInfo = (Element) item.getElementsByTagName("info").item(0);
                boolean staggered;
                switch (file){
                    case Database.TECH_EFFECT:
                        container.setCivID(0);
                        staggered = false;
                        container.setTeamBonus(false);
                        container.setGlobalFilter(Database.NONE);
                        break;
                    case Database.HIDDEN_BONUS_EFFECT:
                        container.setCivID(0);
                        staggered = true;
                        container.setTeamBonus(false);
                        container.setGlobalFilter(Database.NONE);
                        break;
                    default:
                        container.setCivID(Integer.parseInt(effectsInfo.getAttribute("civ")));
                        staggered = Boolean.parseBoolean(effectsInfo.getAttribute("staggered"));
                        container.setTeamBonus(Boolean.parseBoolean(effectsInfo.getAttribute("teamBonus")));
                        container.setGlobalFilter(effectsInfo.getAttribute("globalFilter"));
                        break;
                }
                container.setStaggered(staggered);
                Element effectsData = (Element) item.getElementsByTagName("effects").item(0);
                Element statEffect = (Element) effectsData.getElementsByTagName("statEffect").item(0);
                Element costEffect = (Element) effectsData.getElementsByTagName("costEffect").item(0);
                Element ecoEffect = (Element) effectsData.getElementsByTagName("ecoEffect").item(0);
                Element attackValueEffect = (Element) effectsData.getElementsByTagName("attackValueEffect").item(0);
                Element armorValueEffect = (Element) effectsData.getElementsByTagName("armorValueEffect").item(0);
                ArrayList<Effect> sEa = processBonusEffect(staggered, statEffect);
                ArrayList<Effect> cEa = processBonusEffect(staggered, costEffect);
                ArrayList<Effect> eEa = processBonusEffect(staggered, ecoEffect);
                ArrayList<Effect> attVEa = processBonusEffect(staggered, attackValueEffect);
                ArrayList<Effect> armVEa = processBonusEffect(staggered, armorValueEffect);
                container.addEffectList("stat", sEa);
                container.addEffectList("cost", cEa);
                container.addEffectList("eco", eEa);
                container.addEffectList("attack", attVEa);
                container.addEffectList("armor", armVEa);
                map.add(container);
            }
            return map;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    private ArrayList<Effect> processBonusEffect(boolean staggered, Element item){
        ArrayList<Effect> eff = new ArrayList<>();
        NodeList effectList = item.getElementsByTagName("effectItem");
        for(int i = 0; i < effectList.getLength(); ++i){
            Effect effect = new Effect(staggered);
            Element effectItem = (Element) effectList.item(i);
            Element filter = (Element) effectItem.getElementsByTagName("filter").item(0);
            Element operation = (Element) effectItem.getElementsByTagName("operation").item(0);
            if (filter.hasAttributes()){
                effect.setFilterClass(filter.getAttribute("class"));
                if (filter.hasAttribute("affectsSecondaryProjectile"))
                    effect.setPlus(Boolean.parseBoolean(filter.getAttribute("affectsSecondaryProjectile")));
                else effect.setPlus(false);
                if (filter.hasAttribute("requiredTech"))
                    effect.setRequiredTechID(Integer.parseInt(filter.getAttribute("requiredTech")));
                else effect.setRequiredTechID(-1);
                ArrayList<Integer> filterIds = new ArrayList<>();
                String[] fIDs = filter.getAttribute("ids").split(" ");
                for (String s : fIDs){
                    if (s.equals("@")) filterIds.add(-1);
                    else filterIds.add(Integer.parseInt(s));
                }
                effect.setFilterIds(filterIds);
            }
            else effect.setFilterClass(Database.NONE);
            effect.setStat(operation.getAttribute("stat"));
            effect.setOperator(operation.getAttribute("operator"));
            effect.setValue(operation.getAttribute("value"));
            eff.add(effect);
        }
        return eff;
    }


    public List<Integer> readEcoUpgrades() {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.ECO_UPGRADES));
            Element item = (Element) doc.getElementsByTagName("item").item(0);
            String[] ids = item.getTextContent().split(" ");
            ArrayList<Integer> numList = new ArrayList<>();
            for (String s : ids) {
                numList.add(Integer.valueOf(s));
            }
            return numList;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<EcoElement> readGatheringRates(){
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.GATHERING_RATES));
            NodeList list = doc.getElementsByTagName("item");
            List<EcoElement> ecoList = new ArrayList<>();
            for(int i = 0;  i < list.getLength(); ++i){
                Element ecoElement = (Element) list.item(i);
                EcoElement element = new EcoElement();
                element.setStat(Integer.parseInt(ecoElement.getAttribute("ecoID")));
                element.setGatheringRate(1.0f);
                element.setStatName("gathering_rates_" + (i + 1));
                String sIcon = ecoElement.getAttribute("statIcon");
                String rIcon = ecoElement.getAttribute("resourceIcon");
                element.setResourceIcon(Database.getImage(rIcon));
                element.setStatIcon(Database.getImage(sIcon));
                ecoList.add(element);
            }
            return ecoList;

        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public HashMap<Integer, String> readStatsNames(String file){
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(file));
            NodeList list = doc.getElementsByTagName("item");
            HashMap<Integer, String> b = new HashMap<>();
            for (int i = 0; i < list.getLength(); ++i){
                Element stat = (Element) list.item(i);
                int id = Integer.parseInt(stat.getAttribute("id"));
                String name;
                if (file.equals(Database.ECO_LIST)) name = Utils.getEcoString(id);
                else name = Utils.getStatString(id);
                b.put(id, name);
            }
            return b;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public HashMap<String, Boolean> readStatsAddition(){
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.STAT_LIST));
            NodeList list = doc.getElementsByTagName("item");
            HashMap<String, Boolean> b = new HashMap<>();
            for (int i = 0; i < list.getLength(); ++i){
                Element stat = (Element) list.item(i);
                int id = Integer.parseInt(stat.getAttribute("id"));
                String name = Utils.getStatString(id);
                Boolean addition = Boolean.parseBoolean(stat.getAttribute("addition"));
                b.put(name, addition);
            }
            return b;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public HashMap<String, Double> readEcoValues(){
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.ECO_LIST));
            NodeList list = doc.getElementsByTagName("item");
            HashMap<String, Double> b = new HashMap<>();
            for (int i = 0; i < list.getLength(); ++i){
                Element stat = (Element) list.item(i);
                int id = Integer.parseInt(stat.getAttribute("id"));
                String name = Utils.getEcoString(id);
                String valueString = stat.getAttribute("value");
                double value;
                if (valueString.equals("-")) value = Double.NaN;
                else value = Double.parseDouble(valueString);
                b.put(name, value);
            }
            return b;
        }
        catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }


    public TechTree readTechTree(){
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.TECH_TREE));
            Element root = (Element) doc.getElementsByTagName("techTree").item(0);
            return getRecursiveTechTree(root);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new TechTree();
    }

    public TechTree getRecursiveTechTree(Element root){
        TechTree techTree = new TechTree();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    if (elem.getNodeName().equals("entity")) {
                        int civID = Integer.parseInt(elem.getAttribute("civ"));
                        String entityType = elem.getAttribute("type");
                        int entityID = Integer.parseInt(elem.getAttribute("id"));
                        int ageID = Integer.parseInt(elem.getAttribute("age"));
                        techTree.addCivNode(civID, new TechTree.TechTreeNode(entityID, entityType, ageID));
                    }
                    if (elem.getNodeName().equals("techTree")) {
                        techTree.addChild(getRecursiveTechTree(elem));
                    }
                }
        }
        return techTree;
    }

    public LinkedHashMap<String, List<Integer>> readTechTreeQuestions() {
        try {
            LinkedHashMap<String, List<Integer>> b = new LinkedHashMap<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.TECH_TREE_QUIZ));
            Element units = (Element) doc.getElementsByTagName("units").item(0);
            Element techs = (Element) doc.getElementsByTagName("techs").item(0);
            String[] unitIDs = units.getAttribute("ids").split(" ");
            String[] techIDs = techs.getAttribute("ids").split(" ");
            List<Integer> unitList = new ArrayList<>();
            List<Integer> techList = new ArrayList<>();
            for (String s: unitIDs) unitList.add(Integer.parseInt(s));
            for (String s: techIDs) techList.add(Integer.parseInt(s));
            b.put("Units", unitList);
            b.put("Techs", techList);
            return b;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new LinkedHashMap<>();
    }

    public List<TauntElement> readTaunts(){
        try {
            List<TauntElement> b = new ArrayList<>();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(getData(Database.TAUNT_LIST));
            NodeList list = doc.getElementsByTagName("item");
            for (int i = 0; i < list.getLength(); ++i){
                Element elem = (Element) list.item(i);
                String id = elem.getAttribute("id");
                String text = "taunt_name_" + (i + 1);
                String soundPath = ("t_" + id);
                TauntElement l3 = new TauntElement(Integer.parseInt(id), text, soundPath);
                b.add(l3);
            }
            return b;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
