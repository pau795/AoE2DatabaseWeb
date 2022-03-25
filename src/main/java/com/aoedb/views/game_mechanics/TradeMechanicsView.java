package com.aoedb.views.game_mechanics;

import com.aoedb.database.Database;
import com.aoedb.views.DocumentView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("trade_mechanics")
@Route(value = ":language?", layout = MainLayout.class)
public class TradeMechanicsView extends DocumentView {

    @Override
    public void initView() {
        super.initView();
        addTitle(Database.getString("gm_basics", language));
        addText(Database.getString("trade_mechanics_basics", language), false);
        addTitle(Database.getString("gm_gold_profit", language));
        addText(Database.getString("trade_mechanics_profit1", language), false);
        addText(getStringList1(), false);
        addImage(Database.getImage("formula_trade_1"));
        addText(Database.getString("trade_mechanics_quote1", language), true);
        addText(getStringList2(), false);
        addText(Database.getString("trade_mechanics_profit6", language), false);
        addImage(Database.getImage("formula_trade_2"));
        addText(getFormulaString(), true);
        addText(Database.getString("trade_mechanics_profit7", language), false);
        addTable(2, getTable1Headings(), getTable1Strings());
        addTitle(Database.getString("gm_gold_income", language));
        addText(Database.getString("trade_mechanics_income1", language), false);
        addTable(3, getTable2Headings(), getTable2Strings());
        addText(Database.getString("trade_mechanics_income2", language), false);
        addTable(3, getTable3Headings(), getTable3Strings());

    }

    private String getStringList1(){
        String text ="<ol>";
        text += "<li>"+ Database.getString("trade_mechanics_profit2", language) + "</li>";
        text += "</ol>";
        return text;
    }

    private String getStringList2(){
        String text ="<ol start=\"2\">";
        text += "<li>"+ Database.getString("trade_mechanics_profit3", language) + "</li>";
        text += "<li>"+ Database.getString("trade_mechanics_profit4", language) + "</li>";
        text += "<li>"+ Database.getString("trade_mechanics_profit5", language) + "</li>";
        text += "</ol>";
        return text;
    }

    private String getFormulaString(){
        String text = Database.getString("trade_mechanics_formula_text", language);
        text += "<ul>";
        text += "<li>"+ Database.getString("trade_mechanics_formula_text1", language) + "</li>";
        text += "<li>"+ Database.getString("trade_mechanics_formula_text2", language) + "</li>";
        text += "<li>"+ Database.getString("trade_mechanics_formula_text3", language) + "</li>";
        text += "<li>"+ Database.getString("trade_mechanics_formula_text4", language) + "</li>";
        text += "<li>"+ Database.getString("trade_mechanics_formula_text5", language) + "</li>";
        text += "</ul>";
        return text;
    }

    private String[] getTable1Headings(){
        return new String[]{
                Database.getString("gm_map_size", language),
                Database.getString("gm_gold_profit", language),
        };
    }

    private String[] getTable2Headings(){
        return new String[]{
                Database.getString("gm_trade_table_distance_tiles", language),
                Database.getString("gm_gold_profit", language),
                Database.getString("gm_trade_table_trade_cart_gold_min", language),
        };
    }

    private String[] getTable3Headings(){
        return new String[]{
                Database.getString("gm_map_size", language),
                Database.getString("gm_trade_table_trade_cart_gold_min_no_bumping", language),
                Database.getString("gm_trade_table_trade_cart_gold_min_dense_traffic", language),
        };
    }

    private String[] getTable1Strings(){
        return new String[]{
                Database.getString("gm_trade_table_tiny_map_2_players", language), "63",
                Database.getString("gm_trade_table_small_map_3_players", language), "77",
                Database.getString("gm_trade_table_normal_map_4_players", language), "91",
                Database.getString("gm_trade_table_medium_map_6_players", language), "110",
                Database.getString("gm_trade_table_large_map_8_players", language), "122",
                Database.getString("gm_trade_table_giant_map", language), "134",
        };
    }

    private String[] getTable2Strings(){
        return new String[]{
                "0","0","0",
                "30","4","5.73",
                "60","12","9.63",
                "100","30","13.44",
                "140","54","17.34",
                "180","85","21.88",
                "200","122","25.0",
        };
    }

    private String[] getTable3Strings(){
        return new String[]{
                Database.getString("gm_trade_table_tiny_map_2_players", language), "23.46", "19.71",
                Database.getString("gm_trade_table_small_map_3_players", language), "24.02", "20.17",
                Database.getString("gm_trade_table_normal_map_4_players", language), "24.30", "20.41",
                Database.getString("gm_trade_table_medium_map_6_players", language), "24.80", "20.83",
                Database.getString("gm_trade_table_large_map_8_players", language), "25.0", "20.01",
                Database.getString("gm_trade_table_giant_map", language), "25.15", "21.13",
        };
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_trade_mechanics", language);
    }
}