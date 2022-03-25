package com.aoedb.views.game_mechanics;

import com.aoedb.database.Database;
import com.aoedb.views.DocumentView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("damage_formula")
@Route(value = ":language?", layout = MainLayout.class)
public class DamageFormulaView extends DocumentView {

    @Override
    public void initView() {
        super.initView();
        addTitle(Database.getString("gm_basics", language));
        addText(Database.getString("damage_formula_basics", language), false);
        addTitle(Database.getString("gm_unit_stats", language));
        addText(Database.getString("damage_formula_unit_stats", language), false);
        addTitle(Database.getString("gm_hill_and_cliff_multipliers", language));
        addText(Database.getString("damage_formula_hills", language), false);
        addTitle(Database.getString("gm_accuracy_multiplier", language));
        addText(Database.getString("damage_formula_accuracy1", language), false);
        addImage(Database.getImage("damage_formula1"));
        addText(getFormula1String(), true);
        addText(Database.getString("damage_formula_accuracy2", language), false);
        addTitle(Database.getString("gm_secondary_projectiles", language));
        addText(Database.getString("damage_formula_projectiles", language), false);
        addTitle(Database.getString("gm_minimum_damage", language));
        addText(Database.getString("damage_formula_min_damage", language), false);
        addTitle(Database.getString("gm_damage_formula", language));
        addText(Database.getString("damage_formula_formula1", language), false);
        addImage(Database.getImage("damage_formula"));
        addText(getFormula2String(), true);
        addText(Database.getString("damage_formula_formula2", language), false);
        addTitle(Database.getString("gm_practical_example", language));
        addText(Database.getString("damage_formula_example1", language), false);
        addTable(2, getTable1Headings(), getTable1Strings());
        addText(Database.getString("damage_formula_example2", language), false);
        addImage(Database.getImage("damage_formula2"));
        addText(Database.getString("damage_formula_example3", language), false);
        addImage(Database.getImage("damage_formula3"));
        addText(Database.getString("damage_formula_example4", language), false);
        addTable(2, getTable2Headings(), getTable2Strings());
        addText(Database.getString("damage_formula_example5", language), false);
        addImage(Database.getImage("damage_formula4"));
        addText(Database.getString("damage_formula_example6", language), false);
        addImage(Database.getImage("damage_formula5"));

    }

    private String[] getTable1Headings(){
        return new String[]{
                Database.getString("gm_damage_table1_cataphract_attack_values", language),
                Database.getString("gm_damage_table1_halberdier_armor_values", language),
        };
    }

    private String[] getTable2Headings(){
        return new String[]{
                Database.getString("gm_damage_table2_halberdier_attack_values", language),
                Database.getString("gm_damage_table2_cataphract_armor_values", language),
        };
    }

    private String[] getTable1Strings(){
        return new String[]{
                Database.getString("gm_damage_table1_cata1", language), Database.getString("gm_damage_table1_halb1", language),
                Database.getString("gm_damage_table1_cata2", language), Database.getString("gm_damage_table1_halb2", language),
                Database.getString("gm_damage_table1_cata3", language), Database.getString("gm_damage_table1_halb3", language),
                Database.getString("gm_damage_table1_cata4", language), Database.getString("gm_damage_table1_halb4", language),
                Database.getString("gm_damage_table1_cata5", language), "",
                Database.getString("gm_damage_table1_cata6", language), "",
            };
    }

    private String[] getTable2Strings(){
        return new String[]{
                Database.getString("gm_damage_table2_halb1", language),Database.getString("gm_damage_table2_cata1", language),
                Database.getString("gm_damage_table2_halb2", language),Database.getString("gm_damage_table2_cata2", language),
                Database.getString("gm_damage_table2_halb3", language),Database.getString("gm_damage_table2_cata3", language),
                Database.getString("gm_damage_table2_halb4", language),Database.getString("gm_damage_table2_cata4", language),
                Database.getString("gm_damage_table2_halb5", language), "",
                Database.getString("gm_damage_table2_halb6", language), "",
                Database.getString("gm_damage_table2_halb7", language), "",
                Database.getString("gm_damage_table2_halb8", language), "",
                Database.getString("gm_damage_table2_halb9", language), "",
        };
    }


    private String getFormula1String() {
        String text = Database.getString("damage_formula_formula1_text", language);
        text += "<ul>";
        text += "<li>" + Database.getString("damage_formula_formula1_text1", language) + "</li>";
        text += "<li>" + Database.getString("damage_formula_formula1_text2", language) + "</li>";
        text += "<li>" + Database.getString("damage_formula_formula1_text3", language) + "</li>";
        text += "</ul>";
        return text;
    }

    private String getFormula2String() {
        String text = Database.getString("damage_formula_formula2_text", language);
        text += "<ul>";
        text += "<li>" + Database.getString("damage_formula_formula2_text1", language) + "</li>";
        text += "<li>" + Database.getString("damage_formula_formula2_text2", language) + "</li>";
        text += "<li>" + Database.getString("damage_formula_formula2_text3", language) + "</li>";
        text += "<li>" + Database.getString("damage_formula_formula2_text4", language) + "</li>";
        text += "<li>" + Database.getString("damage_formula_formula2_text5", language) + "</li>";
        text += "<li>" + Database.getString("damage_formula_formula2_text6", language) + "</li>";
        text += "<li>" + Database.getString("damage_formula_formula2_text7", language) + "</li>";
        text += "<li>" + Database.getString("damage_formula_formula2_text8", language) + "</li>";
        text += "</ul>";
        return text;
    }


    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_damage_formula", language);
    }
}