package com.aoedb.views.game_mechanics;

import com.aoedb.database.Database;
import com.aoedb.views.DocumentView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("repairing_mechanics")
@Route(value = ":language?", layout = MainLayout.class)
public class RepairingMechanicsView extends DocumentView {

    @Override
    public void initView() {
        super.initView();
        addTitle(Database.getString("gm_basics", language));
        addText(Database.getString("repair_mechanics_basics", language), false);
        addTitle(Database.getString("gm_repairing_speed", language));
        addText(Database.getString("repair_mechanics_speed1", language), false);
        addTable(3, getTableHeadings(), getTableStrings());
        addText(Database.getString("repair_mechanics_speed2", language), false);
        addTitle(Database.getString("gm_repairing_cost", language));
        addText(Database.getString("repair_mechanics_cost", language), false);
    }


    private String[] getTableHeadings(){
        return new String[]{
                Database.getString("gm_repair_table_unit_building", language),
                Database.getString("gm_repair_table_villager", language),
                Database.getString("gm_repair_table_repairing_rates", language)
        };
    }

    private String[] getTableStrings(){
        return new String[]{
                Database.getString("gm_repair_table_buildings", language), Database.getString("gm_repair_table_first_villager", language), "750HP/min",
                Database.getString("gm_repair_table_buildings", language), Database.getString("gm_repair_table_extra_villagers", language), "375HP/min",
                Database.getString("gm_repair_table_siege_ships", language), Database.getString("gm_repair_table_first_villager", language), "187.5HP/min",
                Database.getString("gm_repair_table_siege_ships", language), Database.getString("gm_repair_table_extra_villagers", language), "93.75HP/min",
        };
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_repairing_mechanics", language);
    }
}
