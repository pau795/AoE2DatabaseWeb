package com.aoedb.views.components;

import com.aoedb.data.TypeElement;
import com.aoedb.database.Database;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.HashMap;
import java.util.Map;


public class TypesPopup extends Div {

    Tabs tabs;
    Map<Tab, Component> tabMap;
    String language;
    TypeElement e;

    public TypesPopup(TypeElement e, String language){

        this.language = language;
        this.e = e;
        Div tabContainer = new Div();
        tabContainer.addClassNames("entity-tab-container");

        tabs = new Tabs();
        tabs.addClassNames("tab_layout");
        tabMap = new HashMap<>();
        Div pages = new Div();
        pages.addClassNames("entity-div");
        int i = 0;
        for(String s: getTabTitles()){
            Tab tab = new Tab(s);
            tab.setClassName("tab");
            tabs.add(tab);
            Component c = getComponent(i);
            pages.add(c);
            tabMap.put(tab, c);
            ++i;
        }
        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.addSelectedChangeListener(event -> selectTab());
        selectTab();
        Hr divider1 = new Hr();
        divider1.addClassNames("entity-tab-divider");
        tabContainer.add(divider1, tabs, pages);
        addClassNames("stats-popup-container");
        add(tabContainer);

    }

    private Component getComponent(int i){
        if (i == 0) return getAttackValues();
        return getArmorValues();
    }


    private Div getAttackValues(){
        return new ExpandableList(Database.getAttackTypesEntities().get(e.getID()), -1, language);
    }

    private Div getArmorValues(){
        return new ExpandableList(Database.getArmorTypesEntities().get(e.getID()), -1, language);
    }


    private void selectTab(){
        tabMap.values().forEach(page -> page.setVisible(false));
        Component selectedPage = tabMap.get(tabs.getSelectedTab());
        selectedPage.setVisible(true);
    }


    public String[] getTabTitles(){
        return new String[]{
            Database.getString("attack_values", language),
            Database.getString("armor_values", language)
        };
    }


}
