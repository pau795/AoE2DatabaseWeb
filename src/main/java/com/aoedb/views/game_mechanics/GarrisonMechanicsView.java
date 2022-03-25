package com.aoedb.views.game_mechanics;

import com.aoedb.database.Database;
import com.aoedb.views.DocumentView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("garrison_mechanics")
@Route(value = ":language?", layout = MainLayout.class)
public class GarrisonMechanicsView extends DocumentView {


    @Override
    public void initView() {
        super.initView();
        addTitle(Database.getString("gm_basics", language));
        addText(Database.getString("garrison_basics", language), false);
        addTitle(Database.getString("gm_unit_garrison_ability", language));
        addText(Database.getString("garrison_ability1", language), false);
        addTable(2, table1Headings(), table1Strings());
        addText(Database.getString("garrison_ability2", language), false);
        addTitle(Database.getString("gm_healing_units_inside_buildings", language));
        addText(Database.getString("garrison_healing", language), false);
        addTitle(Database.getString("gm_buildings_firing_extra_projectiles", language));
        addText(Database.getString("garrison_projectiles1", language), false);
        addTable(3, table2Headings(), table2Strings());
        addText(Database.getString("garrison_projectiles2", language), false);
        addImage(Database.getImage("garrison_formula"));
        addText(getFormulaString(), true);
        addText(Database.getString("garrison_projectiles3", language), false);
    }

    private String getFormulaString() {
        String text = Database.getString("garrison_formula_text", language);
        text += "<ul>";
        text += "<li>" + Database.getString("garrison_formula_text1", language) + "</li>";
        text += "<li>" + Database.getString("garrison_formula_text2", language) + "</li>";
        text += "<li>" + Database.getString("garrison_formula_text3", language) + "</li>";
        text += "<li>" + Database.getString("garrison_formula_text4", language) + "</li>";
        text += "<li>" + Database.getString("garrison_formula_text5", language) + "</li>";
        text += "<li>" + Database.getString("garrison_formula_text6", language) + "</li>";
        text += "<li>" + Database.getString("garrison_formula_text7", language) + "</li>";
        text += "<li>" + Database.getString("garrison_formula_text8", language) + "</li>";
        text += "<li>" + Database.getString("garrison_formula_text9", language) + "</li>";
        text += "<li>" + Database.getString("garrison_formula_text10", language) + "</li>";

        text += "</ul>";
        return text;
    }


    private String[] table1Headings(){
        return new String[]{
                Database.getString("tt_units", language),
                Database.getString("gm_unit_garrison_ability", language)
        };
    }

    private String[] table2Headings(){
        return new String[]{
                Database.getString("gm_garrison_table_building", language),
                Database.getString("gm_garrison_table_number_of_projectiles_empty", language),
                Database.getString("gm_garrison_table_maximum_number_of_projectiles", language)
        };
    }

    private String[] table1Strings(){
        return new String[]{
                Database.getString("gm_garrison_table_villagers", language), Database.getString("gm_garrison_table_villagers_value", language),
                Database.getString("gm_garrison_table_infantry", language), Database.getString("gm_garrison_table_infantry_value", language),
                Database.getString("gm_garrison_table_archers", language), Database.getString("gm_garrison_table_archers_value", language),
                Database.getString("gm_garrison_table_monks", language), Database.getString("gm_garrison_table_monks_value", language),
                Database.getString("gm_garrison_table_mounted_units", language), Database.getString("gm_garrison_table_mounted_units_value", language),
                Database.getString("gm_garrison_table_ships", language), Database.getString("none", language),
                Database.getString("gm_garrison_table_remaining", language), Database.getString("gm_garrison_table_remaining_values", language)
        };
    }

    private String[] table2Strings(){
        return new String[]{
                Database.getString("gm_garrison_table_town_center", language), "0", "10",
                Database.getString("gm_garrison_table_malian_town_center_with_tigui", language), "5", "15",
                Database.getString("gm_garrison_table_teutonic_town_center", language), "0", "15",
                Database.getString("gm_garrison_table_watch_tower_guard_tower_and_keep", language), "1", "5",
                Database.getString("gm_garrison_table_teutonic_watch_tower_guard_tower_and_keep", language), "1", "9",
                Database.getString("gm_garrison_table_japanese_watch_tower_guard_tower_and_keep_with_yasama", language), "3", "7",
                Database.getString("gm_garrison_table_castle", language), "5", "21",
                Database.getString("gm_garrison_table_krepost", language), "5", "21",
                Database.getString("gm_garrison_table_donjon_feudal", language), "1", "5",
                Database.getString("gm_garrison_table_donjon_castle", language), "2", "9",
                Database.getString("gm_garrison_table_donjon_imp", language), "3", "13",
                Database.getString("gm_garrison_table_bombard_tower", language), "1", "1",
                Database.getString("gm_garrison_table_teutonic_bombard_tower", language), "1", "4",
        };
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_garrison_mechanics", language);
    }
}