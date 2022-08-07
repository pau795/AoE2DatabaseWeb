package com.aoedb.views.miscellaneous;

import com.aoedb.data.EntityElement;
import com.aoedb.data.StringKey;
import com.aoedb.database.Database;
import com.aoedb.views.MainLayout;
import com.aoedb.views.ListView;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.LinkedHashMap;
import java.util.List;

@RoutePrefix("history_list")
@Route(value = ":language", layout = MainLayout.class)
public class HistoryListView extends ListView {

    @Override
    protected String[] getSortOptions() {
        return new String[]{
                Database.getString("sort_topic", language)
        };
    }

    @Override
    protected LinkedHashMap<StringKey, List<EntityElement>> getData() {
        return Database.getGroupList(Database.HISTORY_GROUPS, sort).getGroupMap(Database.HISTORY_GROUPS,language);
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_history", language);
    }
}
