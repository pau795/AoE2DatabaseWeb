package com.aoedb.views.game_mechanics;

import com.aoedb.database.Database;
import com.aoedb.views.DocumentView;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("conversion_mechanics")
@Route(value = ":language?", layout = MainLayout.class)


public class ConversionMechanicsView extends DocumentView {



    @Override
    public void initView() {
        super.initView();
        addTitle(Database.getString("gm_basics", language));
        addText(Database.getString("conversion_basics1", language), false);
        addText(Database.getString("conversion_basics2", language), false);
        addText(Database.getString("conversion_basics4", language), false);
        addTitle(Database.getString("gm_performing_a_conversion", language));
        addText(Database.getString("conversion_perform1", language), false);
        addTable(3, getTableHeadings(), getTableStrings());
        addText(Database.getString("conversion_perform2", language), false);
        addTitle(Database.getString("gm_conversion_resistance", language));
        addText(Database.getString("conversion_resistance", language), false);
    }


    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_conversion_mechanics", language);
    }

    private String[] getTableHeadings(){
        return new String[]{
                Database.getString("gm_conversion_table_interval", language),
                Database.getString("gm_conversion_table_time", language),
                Database.getString("gm_conversion_table_chance", language)
        };
    }

    private String[] getTableStrings(){
        return new String[]{
                "1","1.40s","0%",
                "2","2.62s","0%",
                "3","3.84s","0%",
                "4","5.06s","26.9%",
                "5","6.28s","48.1%",
                "6","7.50s","63.3%",
                "7","8.72s","74.2%",
                "8","9.94s","81.4%",
                "9","11.16s","86.1%",
                "10","12.38s","90.8%",
                "11","13.60s","94.7%",
                "12","14.60s","100%",
        };
    }

}
