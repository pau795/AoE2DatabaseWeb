package com.aoedb.views.database;

import com.aoedb.views.components.EntityButton;
import com.aoedb.views.components.ExpandableList;
import com.aoedb.data.Building;
import com.aoedb.data.Entity;
import com.aoedb.database.Database;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.Arrays;
import java.util.List;

@RoutePrefix("building")
@Route(value = ":entityID?/:civID?/:language?", layout = MainLayout.class)
public class BuildingView extends EntityItemView {


    @Override
    protected Component getComponent(int i) {
        switch (i){
            case 0: return getBasicInfoView();
            case 1: return attackView();
            case 2: return armorView();
            case 3: return upgradesView();
            case 4: return trainableView();
            case 5: return availabilityView();
            case 6: return bonusesView();
            default: return new Div();
        }
    }

    protected Component trainableView() {
        ExpandableList list = new ExpandableList(((Building)e).getTrainable(), civID, language);
        list.setEntityView(this);
        return list;
    }

    @Override
    protected void setEntityButtons(Div component){
        EntityButton creatorButton = new EntityButton(this, e,Database.getString("builder_unit", language) , true, language);
        EntityButton ageButton = new EntityButton(this, e, Database.getString("entity_age", language), true, language);
        EntityButton classButton = new EntityButton(this, e, Database.getString("entity_class", language) ,false, language);
        EntityButton requiredBuildingButton = new EntityButton(this, e, Database.getString("required_building", language) ,true, language);
        EntityButton requiredTechnologyButton = new EntityButton(this, e,Database.getString("required_technology", language) , true, language);
        EntityButton upgradedFromButton = new EntityButton(this, e, Database.getString("upgraded_from", language), true, language);
        EntityButton nextUpgradeButton = new EntityButton(this, e, Database.getString("next_upgrade", language) ,true, language);
        component.add(creatorButton, ageButton, classButton, requiredBuildingButton, requiredTechnologyButton, upgradedFromButton, nextUpgradeButton);
    }


    @Override
    protected String[] getTabTitles() {
        return new String[]{
                Database.getString("basic_info", language),
                Database.getString("attack_values", language),
                Database.getString("armor_values", language),
                Database.getString("upgrades", language),
                Database.getString("units_and_research", language),
                Database.getString("availability", language),
                Database.getString("bonuses", language)};
    }

    @Override
    protected String[] getSectionTitles() {
        return new String[]{
                Database.getString("building_description", language),
                Database.getString("building_stats", language),
                Database.getString("building_cost", language),
        };
    }

    @Override
    protected List<String> getStatsList() {
        return statList();
    }

    protected static List<String> statList() {
        return Arrays.asList(
                Database.HP, Database.ATTACK,
                Database.MELEE_ARMOR, Database.PIERCE_ARMOR,
                Database.RANGE, Database.MINIMUM_RANGE,
                Database.LOS, Database.RELOAD_TIME,
                Database.ATTACK_DELAY, Database.ACCURACY,
                Database.NUMBER_PROJECTILES, Database.PROJECTILE_SPEED,
                Database.GARRISON_CAPACITY, Database.POPULATION_TAKEN,
                Database.WORK_RATE, Database.HEAL_RATE);
    }



    @Override
    protected Entity getEntity(){
        return Database.getBuilding(entityID, language);
    }
}
