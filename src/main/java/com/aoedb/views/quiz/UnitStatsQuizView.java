package com.aoedb.views.quiz;

import com.aoedb.data.*;
import com.aoedb.database.Database;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.*;

@RoutePrefix("unit_stats_quiz")
@Route(value = ":language?", layout = MainLayout.class)
public class UnitStatsQuizView extends EntityStatsQuizView {

    List<Integer> unitList, buildingList;


    protected void obtainQuestion(){
        Random r = new Random();
        int n = r.nextInt(buildingList.size()+unitList.size());
        if (n < buildingList.size()) entity = Database.getBuilding(buildingList.get(n));
        else entity = Database.getUnit(unitList.get(r.nextInt(unitList.size())));
    }


    @Override
    protected void getEntitiesInfo(){
        unitList = new ArrayList<>();
        buildingList = new ArrayList<>();
        List<EntityElement> u = Database.getList(Database.UNIT_LIST);
        for (EntityElement e:u) if (notForbidden(e.getId(), Database.UNIT)) unitList.add(e.getId());
        List<EntityElement> b = Database.getList(Database.BUILDING_LIST);
        for (EntityElement e:b)  if (notForbidden(e.getId(), Database.BUILDING)) buildingList.add(e.getId());
    }
    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_unit_building_stats_quiz", language);
    }
}
