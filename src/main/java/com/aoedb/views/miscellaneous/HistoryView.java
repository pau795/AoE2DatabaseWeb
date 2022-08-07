package com.aoedb.views.miscellaneous;

import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.aoedb.views.DocumentView;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

@RoutePrefix("history")
@Route(value = ":entityID?/:language?", layout = MainLayout.class)
public class HistoryView extends DocumentView {

    int entityID;
    EntityElement e;

    @Override
    public void initView() {
        super.initView();
        e = Database.getElement(Database.HISTORY_LIST, entityID);
        String history = Database.getHistoryText(entityID, language);
        addText(history, false);
    }



    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        parameters = beforeEnterEvent.getRouteParameters();
        language = Utils.checkLanguage(parameters.get("language").orElse(Database.DEFAULT_LANGUAGE));
        entityID = Integer.parseInt(getParameters().get("entityID").orElse("1"));
        if (!init){
            initView();
            init = true;
        }
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + e.getName();
    }

}
