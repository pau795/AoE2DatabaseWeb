package com.aoedb.views;


import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;


public abstract class BaseView extends Div implements HasDynamicTitle, BeforeEnterObserver {

    protected String language;
    protected RouteParameters parameters;
    protected boolean init;

    public BaseView() {
        addClassNames("view-main-layout");
        init = false;
    }

    public RouteParameters getParameters(){
        return parameters;
    }

    public String getLanguage(){
        return language;
    }

    public abstract void initView();

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        parameters = beforeEnterEvent.getRouteParameters();
        language = Utils.checkLanguage(parameters.get("language").orElse(Database.DEFAULT_LANGUAGE));
        if (!init){
            initView();
            init = true;
        }
    }
}
