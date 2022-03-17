package com.aoedb.views.components;

import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@CssImport("./themes/aoe2database/components/stat-comparator.css")
public class StatComparator extends Div {
    String language;
    HashMap<String, Label> unit1Stats;
    HashMap<String, Label> unit2Stats;
    Div cost1Layout, cost2Layout;


    public StatComparator(String language){
        this.language = language;
        unit1Stats = new HashMap<>();
        unit2Stats = new HashMap<>();
        Div statsGrid = new Div();
        statsGrid.addClassNames("stat-comparator-grid");
        for (String s : statList()){
            Label unit1Stat = new Label();
            unit1Stat.addClassNames("stat-comparator-left-stat");
            Label unit2Stat = new Label();
            unit2Stat.addClassNames("stat-comparator-right-stat");
            Label statName = new Label(Database.getString(Utils.getStatTitle(s), language));
            statName.addClassNames("stat-comparator-stat-name");
            unit1Stats.put(s, unit1Stat);
            unit2Stats.put(s, unit2Stat);
            statsGrid.add(unit1Stat, statName, unit2Stat);
        }
        Div costsLayout = new Div();
        costsLayout.addClassNames("stat-comparator-cost-layout");
        cost1Layout = new Div();
        cost1Layout.addClassNames("stat-comparator-unit-cost-layout");
        cost2Layout = new Div();
        cost2Layout.addClassNames("stat-comparator-unit-cost-layout");
        Label costName = new Label(Database.getString("stat_cost", language));
        costName.addClassNames("stat-comparator-stat-name");
        costsLayout.add(cost1Layout, costName, cost2Layout);
        Div statsContainer = new Div(new Hr(), statsGrid, new Hr(), costsLayout, new Hr());
        statsContainer.addClassNames("stat-comparator-container");
        add(statsContainer);

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
                Database.WORK_RATE, Database.HEAL_RATE, Database.TRAINING_TIME);
    }

    public void updateStats(Unit u1, Unit u2){
        for (String s: statList()){
            unit1Stats.get(s).setText(u1.getStatString(s));
            unit2Stats.get(s).setText(u2.getStatString(s));
        }
        HashMap<String, Integer> cost1 = u1.getCalculatedCost();
        HashMap<String, Integer> cost2 = u2.getCalculatedCost();
        cost1Layout.removeAll();
        cost2Layout.removeAll();
        for(String res: cost1.keySet()) cost1Layout.add(getCostDiv(res, u1));
        for(String res: cost2.keySet()) cost2Layout.add(getCostDiv(res, u2));

    }

    private Div getCostDiv(String res, Unit u){
        Image resIcon = new Image();
        String iconPath = Utils.getResourceIcon(res);
        resIcon.setSrc(iconPath);
        resIcon.addClassNames("stat-comparator-res-icon");
        Label resValue = new Label();
        resValue.addClassNames("stat-comparator-res-value");
        resValue.setText(u.getCostString().get(res));
        Div resLayout = new Div(resIcon, resValue);
        resLayout.addClassNames("stat-comparator-res-layout");
        return resLayout;
    }


}
