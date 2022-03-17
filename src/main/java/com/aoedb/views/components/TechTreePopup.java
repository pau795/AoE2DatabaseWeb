package com.aoedb.views.components;

import com.aoedb.data.Entity;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;

import java.util.HashMap;

@CssImport("./themes/aoe2database/components/tech-tree-popup.css")
public class TechTreePopup extends Div {
    Entity e;
    int civID;
    String language;
    OnButtonClickedListener listener;

    public TechTreePopup(Entity e, int civID, String language){
        this.e = e;
        this.civID = civID;
        this.language = language;
        Label name = new Label(e.getName());
        name.addClassNames("tech-tree-popup-name");
        Image image = new Image();
        image.setSrc(e.getNameElement().getImage());
        image.addClassNames("tech-tree-popup-image");
        Div costLayout = getCostLayout();
        Label description = new Label(e.getDescriptor().getBriefDescription());
        description.addClassNames("tech-tree-popup-description");
        Div statsLayout = getStatsLayout();
        Div upgradeCost = getUpgradeCostLayout();
        Button moreInfoButton = new Button(Database.getString("tt_popup_more_info", this.language));
        moreInfoButton.addClassNames("tech-tree-popup-button");
        moreInfoButton.addClickListener(buttonClickEvent -> {
            if (listener != null) listener.onButtonClicked();
        });
        addClassNames("tech-tree-popup-view");
        add(name, image, costLayout, description, statsLayout, upgradeCost, moreInfoButton);
    }

    private Div getCostLayout(){
        Div costLayout = new Div();
        costLayout.addClassNames("tech-tree-popup-row-layout");
        HashMap<String, Integer> cost = e.getBaseCost();
        Label costLabel = new Label(Database.getString("tt_box_cost", language));
        costLayout.add(costLabel);
        costLabel.addClassNames("tech-tree-popup-semibold-text");
        for(String res: cost.keySet()){
            Image resIcon = new Image();
            String iconPath = Utils.getResourceIcon(res);
            resIcon.setSrc(iconPath);
            resIcon.addClassNames("tech-tree-popup-icon");
            Label statValue = new Label(String.valueOf(e.getBaseCost().get(res)));
            statValue.addClassNames("tech-tree-popup-res-value");
            Div resLayout = new Div(resIcon, statValue);
            resLayout.addClassNames("tech-tree-popup-res-layout");
            costLayout.add(resLayout);
        }

        return costLayout;
    }

    private Div statLayout(double stat, String iconPath){
        Image statIcon = new Image();
        statIcon.setSrc(iconPath);
        statIcon.addClassNames("tech-tree-popup-icon");
        Label resValue = new Label(Utils.getDecimalString(stat, 0));
        resValue.addClassNames("tech-tree-popup-semibold-text");
        Div resLayout = new Div(statIcon, resValue);
        resLayout.addClassNames("tech-tree-popup-res-layout");
        return resLayout;
    }

    private Div getStatsLayout(){
        Div statsLayout = new Div();
        statsLayout.addClassNames("tech-tree-popup-row-layout");
        if(e.getType().equals(Database.UNIT) || e.getType().equals(Database.BUILDING)){
            double hp = e.getBaseStat(Database.HP);
            if (!Double.isNaN(hp)) {
                Div statLayout = statLayout(hp, "images/hp_icon.png");
                statsLayout.add(statLayout);
            }
            double attack = e.getBaseStat(Database.ATTACK);
            if (!Double.isNaN(attack)) {
                Div statLayout = statLayout(attack, "images/attack_icon.png");
                statsLayout.add(statLayout);
            }
            double range = e.getBaseStat(Database.RANGE);
            if (!Double.isNaN(range)) {
                Div statLayout = statLayout(range, "images/range_icon.png");
                statsLayout.add(statLayout);
            }
        }
        if(e.getType().equals(Database.UNIT)){
            double meleeArmor = e.getBaseStat(Database.MELEE_ARMOR);
            if (!Double.isNaN(meleeArmor)) {
                Div statLayout = statLayout(meleeArmor, "images/melee_armor_icon.png");
                statsLayout.add(statLayout);
            }
            double pierceArmor = e.getBaseStat(Database.PIERCE_ARMOR);
            if (!Double.isNaN(pierceArmor)) {
                Div statLayout = statLayout(pierceArmor, "images/pierce_armor_icon.png");
                statsLayout.add(statLayout);
            }
        }
        double trainingTime = e.getBaseStat(Database.TRAINING_TIME);
        if (!Double.isNaN(trainingTime)) {
            Div statLayout = statLayout(trainingTime, "images/hourglass_black.png");
            statsLayout.add(statLayout);
        }
        return statsLayout;
    }

    private Div getUpgradeCostLayout(){
        Div upgradeCostLayout = new Div();
        upgradeCostLayout.addClassNames("tech-tree-popup-row-layout");
        int techID = e.getRequiredTechElement().getId();
        if (!e.getType().equals(Database.TECH) && techID != 0) {
            HashMap<String, Integer> cost = Database.getTechnology(techID, language).getBaseCost();
            Label costLabel = new Label(Database.getString("tt_box_upgrade_cost", language));
            costLabel.addClassNames("tech-tree-popup-semibold-text");
            upgradeCostLayout.add(costLabel);
            for(String res: cost.keySet()){
                Image resIcon = new Image();
                String iconPath = Utils.getResourceIcon(res);
                resIcon.setSrc(iconPath);
                resIcon.addClassNames("tech-tree-popup-icon");
                Label resValue = new Label(String.valueOf(Database.getTechnology(techID, language).getBaseCost().get(res)));
                resValue.addClassNames("tech-tree-popup-res-value");
                Div resLayout = new Div(resIcon, resValue);
                resLayout.addClassNames("tech-tree-popup-res-layout");
                upgradeCostLayout.add(resLayout);
            }
        }
        else upgradeCostLayout.setVisible(false);
        return upgradeCostLayout;
    }

    public void setCivID(int civID){
        this.civID = civID;
    }

    public void setOnButtonClickedListener (OnButtonClickedListener listener){
        this.listener = listener;
    }

    interface OnButtonClickedListener{
        void onButtonClicked();
    }

}
