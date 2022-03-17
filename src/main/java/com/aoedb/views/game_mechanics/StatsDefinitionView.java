package com.aoedb.views.game_mechanics;

import com.aoedb.database.Database;
import com.aoedb.views.DocumentView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("stats_definition")
@Route(value = ":language?", layout = MainLayout.class)
public class StatsDefinitionView extends DocumentView {


    @Override
    public void initView() {
        super.initView();
        addTitle(Database.getString("gm_introduction", language));
        addText(Database.getString("stats_introduction", language), false);
        addTitle(Database.getString("gm_stats", language));
        addText(Database.getString("stats_definition1", language), false);
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_stats_definition", language);
    }
}