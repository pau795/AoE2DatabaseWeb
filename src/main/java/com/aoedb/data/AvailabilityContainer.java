package com.aoedb.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AvailabilityContainer {

    LinkedHashMap<StringKey, List<EntityElement>> availabilityList;
    List<Integer> availableIDs;
    HashMap<Integer, Boolean> availabilityMap;

    public AvailabilityContainer(LinkedHashMap<StringKey, List<EntityElement>> list, List<Integer> ids, HashMap<Integer, Boolean> map){
        this.availabilityList = list;
        this.availableIDs = ids;
        this.availabilityMap = map;
    }

    public LinkedHashMap<StringKey, List<EntityElement>> getAvailabilityList() {
        return availabilityList;
    }

    public List<Integer> getAvailableIDs() {
        return availableIDs;
    }

    public HashMap<Integer, Boolean> getAvailabilityMap() {
        return availabilityMap;
    }
}
