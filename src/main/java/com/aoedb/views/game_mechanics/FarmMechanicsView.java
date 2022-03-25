package com.aoedb.views.game_mechanics;

import com.aoedb.database.Database;
import com.aoedb.views.DocumentView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("farm_mechanics")
@Route(value = ":language?", layout = MainLayout.class)
public class FarmMechanicsView extends DocumentView {



    @Override
    public void initView() {
        super.initView();
        addTitle(Database.getString("gm_basics", language));
        addText(Database.getString("farm_mechanics_basics", language), false);
        addTable(3, getTableHeadings(), getTableStrings());
        addTitle(Database.getString("gm_farm_harvesting", language));
        addText(Database.getString("farm_mechanics_harvesting1", language), false);
        addText(getStringList(), false);
        addImage(Database.getImage("farm_mechanics2"));
        addText(Database.getString("farm_mechanics_img", language), true);
        addTitle(Database.getString("gm_farm_gathering_rates", language));
        addText(Database.getString("farm_mechanics_rates", language), false);
    }

    private String getStringList(){
        String text ="<ol>";
        text += "<li>"+ Database.getString("farm_mechanics_harvesting2", language) + "</li>";
        text += "<li>"+ Database.getString("farm_mechanics_harvesting3", language) + "</li>";
        text += "</ol>";
        return text;
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_farm_mechanics", language);
    }

    private String[] getTableHeadings(){
        return new String[]{
                Database.getString("gm_farm", language),
                Database.getString("gm_food_provided", language),
                Database.getString("gm_food_generated_per_wood", language)
        };
    }

    private String[] getTableStrings(){
        return new String[]{
                Database.getString("gm_farm_table_standard", language),"175","2.92",
                Database.getString("gm_farm_table_horse_collar", language),"250","4.17",
                Database.getString("gm_farm_table_heavy_plow", language),"375","6.25",
                Database.getString("gm_farm_table_crop_rotation", language),"550","9.17",
                Database.getString("gm_farm_table_teutons_standard", language),"175","4.86",
                Database.getString("gm_farm_table_teutons_horse_collar", language),"250","6.94",
                Database.getString("gm_farm_table_teutons_heavy_plow", language),"375","10.41",
                Database.getString("gm_farm_table_teutons_crop_rotation", language),"550","15.28",
                Database.getString("gm_farm_table_chinese_standard", language),"193","3.21",
                Database.getString("gm_farm_table_chinese_horse_collar", language),"275","4.58",
                Database.getString("gm_farm_table_chinese_heavy_plow", language),"413","6.88",
                Database.getString("gm_farm_table_teutons_chinese_ally_crop_rotation", language),"605","16.81",
                Database.getString("gm_farm_table_sicilians_horse_collar", language),"325","5.41",
                Database.getString("gm_farm_table_sicilians_heavy_plow", language),"575","9.58",
                Database.getString("gm_farm_table_sicilians_crop_rotation", language),"925","15.41",
                Database.getString("gm_farm_table_sicilians_crop_rotation_chinese_ally", language),"1018","16.96",
        };
    }
}
