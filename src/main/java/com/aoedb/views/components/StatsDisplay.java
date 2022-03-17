package com.aoedb.views.components;

import com.aoedb.data.Entity;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;

import java.util.HashMap;
import java.util.List;

@CssImport("./themes/aoe2database/components/stats-display.css")
public class StatsDisplay {

    String language;
    Entity e;
    HashMap<String, Label> stats;

    Div statsLayout;
    HashMap<String, Label> costs;
    Label trainingTimeValue;
    Div costsLayout;



    public StatsDisplay(Entity e, List<String> statsList, String language){
        this.language = language;
        this.e = e;
        stats = new HashMap<>();
        statsLayout = new Div();
        statsLayout.addClassNames("stat-container");
        int counter = 0;
        Div rowLayout = new Div();
        rowLayout.addClassNames("stat-row-layout");
        for(String s : statsList) {
            Label statTitleLabel = new Label(Database.getString(Utils.getStatTitle(s), language));
            statTitleLabel.addClassNames("title-text");
            Label statValueLabel = new Label();
            statValueLabel.addClassNames("value-text");
            Div statLayout = new Div(statTitleLabel, statValueLabel);
            statLayout.addClassNames("stat-layout");
            rowLayout.add(statLayout);
            stats.put(s, statValueLabel);
            ++counter;
            if (counter % 2 == 0) {
                statsLayout.add(rowLayout);
                rowLayout = new Div();
                rowLayout.addClassNames("stat-row-layout");
            }
        }
        costs = new HashMap<>();
        costsLayout = new Div();
        costsLayout.addClassNames("cost-layout");
    }

    public Div getStatsLayout(){
        return statsLayout;
    }

    public Div getCostLayout(){
        return costsLayout;
    }



    public void updateStats(){
        for(String s: stats.keySet()) stats.get(s).setText(e.getStatString(s));
        HashMap<String, Integer> cost = e.getCalculatedCost();
        costsLayout.removeAll();
        for(String res: cost.keySet()){
            Image resIcon = new Image();
            String iconPath = Utils.getResourceIcon(res);
            resIcon.setSrc(iconPath);
            resIcon.addClassNames("res-icon");
            Label resValue = new Label();
            resValue.addClassNames("res-value");
            Div resLayout = new Div(resIcon, resValue);
            resLayout.addClassNames("res-layout");
            costsLayout.add(resLayout);
            costs.put(res, resValue);
        }
        Div trainingTimeLayout = new Div();
        trainingTimeLayout.addClassNames("res-layout");
        Label trainingTime = new Label(Database.getString(Utils.getStatTitle(Database.TRAINING_TIME), language));
        trainingTime.addClassNames("res-title");
        trainingTimeValue = new Label();
        trainingTimeValue.addClassNames("res-value");
        trainingTimeLayout.add(trainingTime, trainingTimeValue);
        costsLayout.add(trainingTimeLayout);
        for(String s: costs.keySet()) costs.get(s).setText(e.getCostString().get(s));
        trainingTimeValue.setText(e.getStatString(Database.TRAINING_TIME));
    }

}
