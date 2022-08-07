package com.aoedb.views.database;

import com.aoedb.views.components.EntityButton;
import com.aoedb.views.components.TwoColumnList;
import com.aoedb.data.Entity;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.views.MainLayout;
import com.vaadin.flow.component.Component;

import com.vaadin.flow.component.html.Div;

import com.vaadin.flow.router.*;

import java.util.Arrays;
import java.util.List;

@RoutePrefix("unit")
@Route(value = ":entityID?/:civID?/:language?", layout = MainLayout.class)
public class UnitView extends EntityItemView {



    @Override
    public void initView() {
        super.initView();
    }

    @Override
    protected Component getComponent(int i) {
        switch (i){
            case 0: return getBasicInfoView();
            case 1: return attackView();
            case 2: return armorView();
            case 3: return upgradesView();
            case 4: return performanceView();
            case 5: return availabilityView();
            case 6: return bonusesView();
            default: return new Div();
        }
    }

    protected Component performanceView() {

        return new TwoColumnList(((Unit)e).getPerformance(), civID, language);

    }

    @Override
    protected void setEntityButtons(Div component){
        EntityButton creatorButton = new EntityButton(this, e, "unit_training_building", true, language);
        EntityButton ageButton = new EntityButton(this, e, "entity_age", true, language);
        EntityButton classButton = new EntityButton(this, e, "entity_class",false, language);
        EntityButton requiredTechnologyButton = new EntityButton(this, e,"required_technology", true, language);
        EntityButton upgradedFromButton = new EntityButton(this, e, "upgraded_from", true, language);
        EntityButton nextUpgradeButton = new EntityButton(this, e, "next_upgrade" ,true, language);
        component.add(creatorButton, ageButton, classButton, requiredTechnologyButton, upgradedFromButton, nextUpgradeButton);
    }

    @Override
    protected List<String> getStatsList() {
        return statList();
    }

    public static List<String> statList(){
        return Arrays.asList(
                Database.HP, Database.ATTACK,
                Database.MELEE_ARMOR, Database.PIERCE_ARMOR,
                Database.RANGE, Database.MINIMUM_RANGE,
                Database.LOS, Database.RELOAD_TIME,
                Database.SPEED, Database.BLAST_RADIUS,
                Database.ATTACK_DELAY, Database.ACCURACY,
                Database.NUMBER_PROJECTILES, Database.PROJECTILE_SPEED,
                Database.GARRISON_CAPACITY, Database.POPULATION_TAKEN,
                Database.WORK_RATE, Database.HEAL_RATE);
    }

    @Override
    protected String[] getTabTitles() {
        return new String[]{
                Database.getString("basic_info", language),
                Database.getString("attack_values", language),
                Database.getString("armor_values", language),
                Database.getString("upgrades", language),
                Database.getString("performance", language),
                Database.getString("availability", language),
                Database.getString("bonuses", language)};
    }


    @Override
    protected String[] getSectionTitles() {
        return sectionTitles(language);
    }

    public static String[] sectionTitles(String language) {
        return new String[]{
                Database.getString("unit_description", language),
                Database.getString("unit_stats", language),
                Database.getString("unit_cost", language),
        };
    }

    @Override
    protected Entity getEntity(){
        return Database.getUnit(entityID);
    }
}
