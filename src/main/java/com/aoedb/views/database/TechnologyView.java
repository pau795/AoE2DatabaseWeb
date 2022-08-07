package com.aoedb.views.database;

import com.aoedb.views.components.EntityButton;
import com.aoedb.views.components.ExpandableList;
import com.aoedb.data.Entity;
import com.aoedb.data.Technology;
import com.aoedb.database.Database;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.ArrayList;
import java.util.List;

@RoutePrefix("tech")
@Route(value = ":entityID?/:civID?/:language?", layout = MainLayout.class)
public class TechnologyView extends EntityView {

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    protected Component getComponent(int i) {
        switch (i){
            case 0: return getBasicInfoView();
            case 1: return applicationsView();
            case 2: return availabilityView();
            case 3: return bonusesView();
            default: return new Div();
        }
    }

    @Override
    protected void resetViews(){
    }

    protected Component applicationsView() {
        ExpandableList list = new ExpandableList(((Technology)e).getApplications(), civID, language);
        list.setEntityView(this);
        return list;
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{
                Database.getString("basic_info", language),
                Database.getString("applications", language),
                Database.getString("availability", language),
                Database.getString("bonuses", language)};
    }

    @Override
    protected void setEntityButtons(Div component){
        EntityButton creatorButton = new EntityButton(this, e,"research_building" , true, language);
        EntityButton ageButton = new EntityButton(this, e, "entity_age", true, language);
        EntityButton requiredTechnologyButton = new EntityButton(this, e,"required_technology" , true, language);
        EntityButton nextUpgradeButton = new EntityButton(this, e, "next_upgrade" ,true, language);
        component.add(creatorButton, ageButton, requiredTechnologyButton, nextUpgradeButton);
    }

    @Override
    protected String[] getSectionTitles() {
        return new String[]{
                Database.getString("technology_description", language),
                "",
                Database.getString("technology_cost", language),
        };
    }


    @Override
    protected List<String> getStatsList() {
        return statList();
    }

    protected static List<String> statList() {
        return new ArrayList<>();
    }

    @Override
    protected Entity getEntity(){
        return Database.getTechnology(entityID);
    }
}
