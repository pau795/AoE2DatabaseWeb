package com.aoedb.views.miscellaneous;

import com.aoedb.database.Database;
import com.aoedb.views.MainLayout;
import com.aoedb.views.DocumentView;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("cheat_codes")
@Route(value = ":language?", layout = MainLayout.class)
public class CheatCodesView extends DocumentView {

    @Override
    public void initView() {
        super.initView();
        addTitle(Database.getString("ct_resources", language));
        addText(Database.getString("cheat_codes_resources", language), false);
        addTitle(Database.getString("ct_units", language));
        addText(Database.getString("cheat_codes_units", language), false);
        addTitle(Database.getString("ct_various", language));
        addText(Database.getString("cheat_codes_various", language), false);
        addTitle(Database.getString("ct_non_cheat_codes", language));
        addText(Database.getString("non_cheat_codes", language), false);
        addTitle(Database.getString("ct_event_cheat_codes", language));
        addText(Database.getString("event_cheat_codes", language), false);

    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_cheat_codes", language);
    }
}
