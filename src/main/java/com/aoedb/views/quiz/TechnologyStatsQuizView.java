package com.aoedb.views.quiz;

import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RoutePrefix("technology_stats_quiz")
@Route(value = ":language?", layout = MainLayout.class)
public class TechnologyStatsQuizView extends EntityStatsQuizView {

    List<Integer> techList;


    protected void obtainQuestion(){
        Random r = new Random();
        int n = r.nextInt(techList.size());
        entity = Database.getTechnology(techList.get(n));
    }

    @Override
    protected void getEntitiesInfo(){
        techList = new ArrayList<>();
        List<EntityElement> u = Database.getList(Database.TECH_LIST);
        for (EntityElement e:u) if (notForbidden(e.getId(), Database.TECH)) techList.add(e.getId());
    }


    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_technology_stats_quiz", language);
    }
}
