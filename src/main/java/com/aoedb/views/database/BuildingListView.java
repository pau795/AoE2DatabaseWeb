package com.aoedb.views.database;

import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.views.ListView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.LinkedHashMap;
import java.util.List;

@RoutePrefix("buildings")
@Route(value = ":language", layout = MainLayout.class)
public class BuildingListView extends ListView {

    public BuildingListView() {
        super();
    }

    @Override
    protected String[] getSortOptions() {
        return new String[]{
                Database.getString("sort_age", language),
                Database.getString("sort_military_eco", language),
                Database.getString("sort_alphabetically", language),
        };
    }


    @Override
    protected LinkedHashMap<String, List<EntityElement>> getData() {
        return Database.getGroupList(Database.BUILDING_GROUPS, sort, language);
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_activity_building", language);
    }
}
