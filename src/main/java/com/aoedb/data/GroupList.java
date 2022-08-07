package com.aoedb.data;

import com.aoedb.database.Database;

import java.util.LinkedHashMap;
import java.util.List;

public class GroupList {
    private LinkedHashMap<StringKey, List<EntityElement>> entityMap;
    private boolean alphabeticalOrder;

    public GroupList(){

    }
    public void setEntityMap(LinkedHashMap<StringKey, List<EntityElement>> entityMap) {
        this.entityMap = entityMap;
    }

    public void setAlphabeticalOrder(boolean alphabeticalOrder) {
        this.alphabeticalOrder = alphabeticalOrder;
    }

    public LinkedHashMap<StringKey, List<EntityElement>> getGroupMap(String sortFile, String language){
        for (StringKey s : entityMap.keySet()){
            List<EntityElement> list = entityMap.get(s);
            if(alphabeticalOrder) list.sort(EntityElement.getAlphabeticalComparator(language));
            else list.sort(EntityElement.getListElementComparator(Database.getOrderMap(sortFile, 0)));
        }
        return entityMap;
    }
}
