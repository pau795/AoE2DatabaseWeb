package com.aoedb.views.components;

import com.aoedb.data.Item;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.views.database.EntityView;
import com.aoedb.views.database.UnitView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.HashMap;
import java.util.Map;


public class StatsPopup extends Div {

    Tabs tabs;
    Map<Tab, Component> tabMap;
    String language;
    Unit u;
    StatsDisplay statsDisplay;

    public StatsPopup(Unit u, String language){

        this.language = language;
        this.u = u;
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
        switch (i) {
            case 0:
                return getStats();
            case 1:
                return getAttackValues();
            default:
                return getArmorValues();
        }
    }

    private Div getStats(){
        statsDisplay =  new StatsDisplay(u, UnitView.statList(), language);
        Div statsLayout = new Div();
        statsLayout.addClassNames("section");
        statsLayout.addClassNames("margin-section");
        Label name = new Label(u.getName());
        name.addClassNames("title");
        Image icon = new Image();
        icon.setSrc(u.getNameElement().getImage());
        icon.addClassNames("popup-icon");
        statsLayout.add(name, icon);
        String[] titles = UnitView.sectionTitles(language);
        Label descTitle = new Label(titles[0]);
        descTitle.addClassNames("title");
        statsLayout.add(descTitle, new Hr());
        Label descLabel = new Label(u.getDescriptor().getLongDescription());
        descLabel.addClassNames("plain-text");
        statsLayout.add(descLabel);
        Label statsTitle = new Label(titles[1]);
        statsTitle.addClassNames("title");
        statsLayout.add(statsTitle, new Hr(), statsDisplay.getStatsLayout());
        Label costTitle = new Label(titles[2]);
        costTitle.addClassNames("title");
        statsLayout.add(costTitle, new Hr(), statsDisplay.getCostLayout(), new Hr());
        statsDisplay.updateStats();
        return statsLayout;
    }

    private Div getAttackValues(){
        return new ExpandableTypeList(u.getAttackValues(), language);
    }

    private Div getArmorValues(){
        return new ExpandableTypeList(u.getArmorValues(), language);
    }


    private void selectTab(){
        tabMap.values().forEach(page -> page.setVisible(false));
        Component selectedPage = tabMap.get(tabs.getSelectedTab());
        selectedPage.setVisible(true);
    }


    public String[] getTabTitles(){
        return new String[]{
            Database.getString("dc_show_stats", language),
            Database.getString("attack_values", language),
            Database.getString("armor_values", language)
        };
    }


}
