package com.aoedb.views.game_mechanics;

import com.aoedb.database.Database;
import com.aoedb.views.DocumentView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("building_mechanics")
@Route(value = ":language?", layout = MainLayout.class)


public class BuildingMechanicsView extends DocumentView {



    @Override
    public void initView() {
        super.initView();
        addTitle(Database.getString("gm_basics", language));
        addText(Database.getString("building_mechanics_basics1", language), false);
        addImage(Database.getImage("formula_building_1"));
        addText(Database.getString("building_mechanics_formula_text", language), true);
        addText(Database.getString("building_mechanics_basics2", language), false);
        addTable(3, getTableHeadings(), getTableStrings());
        addTitle(Database.getString("gm_building_upgrades_and_bonuses", language));
        addText(Database.getString("building_mechanics_bonuses", language),false);
    }


    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_building_mechanics", language);
    }

    private String[] getTableHeadings(){
        return new String[]{
            Database.getString("gm_building_villagers", language),
            Database.getString("gm_building_time", language),
            Database.getString("gm_speedup", language)
        };
    }

    private String[] getTableStrings(){
        return new String[]{
                "1","200","1.00",
                "2","150","1.33",
                "4","100","2.00",
                "7","66.7","3.00",
                "10","50","4.00",
                "13","40","5.00",
                "16","33.3","6.00",
        };
    }

}
