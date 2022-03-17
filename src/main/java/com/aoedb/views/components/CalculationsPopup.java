package com.aoedb.views.components;

import com.aoedb.data.DamageCalculator;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import org.springframework.lang.NonNull;


@CssImport("./themes/aoe2database/components/calculations-popup.css")
public class CalculationsPopup extends Div {

    String language;

    public CalculationsPopup(DamageCalculator.UnitStats stats, String language){
        addClassNames("calculations-main-container");
        this.language = language;

        Image unit1icon = new Image();
        unit1icon.addClassNames("calculations-header-icon");
        unit1icon.setSrc(stats.uIcon);
        Image unit2icon = new Image();
        unit2icon.setSrc(stats.rIcon);
        unit2icon.addClassNames("calculations-header-icon");
        Label headerText = new Label(String.format(Database.getString("dc_calc_unit_name", language), stats.uName, stats.rName));
        headerText.addClassNames("calculations-header-text");
        Div initHeader = new Div(unit1icon, headerText, unit2icon);
        initHeader.addClassNames("calculations-header-div");
        add(initHeader);


        if (stats.attackValuesContent.containsKey("attack")) {
            Div unitDamageLayout = new Div();
            unitDamageLayout.addClassNames("calculations-attack-layout");
            Label unitDamageLabel = new Label(Database.getString("dc_calc_main_unit", language));
            unitDamageLabel.addClassNames("calculations-attack-title");
            unitDamageLayout.add(unitDamageLabel, new Hr());
            for (DamageCalculator.UnitStats.AttackValues at : stats.attackValuesContent.get("attack")) {
                EntityElement e = Database.getElement(Database.TYPE_LIST, at.type, language);
                Div damageView = setDamageLayout(e.getName(), e.getImage(), at.attackValue, at.armorValue, at.multiplier, at.result, 0);
                unitDamageLayout.add(damageView);
            }
            unitDamageLayout.add(new Hr());
            Div sumView = calculationRow(createSpecialString(Database.getString("dc_calc_damage_sum", language)), Utils.getDecimalString(stats.damageSum, 2));
            unitDamageLayout.add(sumView);

            Div hillTitle = getTitleDiv(Database.getString("dc_calc_hill_bonus", language));
            Div damageSumAnnotation = new AnnotationText(Utils.getDecimalString(stats.damageSum, 2), Database.getString("dc_calc_damage_sum", language), "red");
            Div hillMultiplierAnnotation = new AnnotationText(Utils.getDecimalString(stats.hillMultiplier, 2), Database.getString("dc_calc_multiplier", language), "purple");
            Div hillCalculation = createSpecialString("@@ × @@", damageSumAnnotation, hillMultiplierAnnotation);
            Div hillContent = calculationRow(hillCalculation, Utils.getDecimalString(stats.hillDamage, 2));
            Div hillLayout = twoRowsLayout(hillTitle, hillContent);
            unitDamageLayout.add(hillLayout);

            if (stats.finalDamage != stats.hillDamage) {
                Div minView = calculationRow(createSpecialString(Database.getString("dc_calc_min_damage", language)), Utils.getDecimalString(stats.finalDamage, 2));
                unitDamageLayout.add(minView);
            }
            Div totalDamageView = calculationRow(createSpecialString(Database.getString("dc_calc_unit_damage", language)), Utils.getDecimalString(stats.finalDamage, 2));
            unitDamageLayout.add(totalDamageView);
            add(unitDamageLayout);

            //UNIT PROJECTILES
            if (stats.hasExtraProjectiles) {
                Div projectileDamageLayout = new Div();
                projectileDamageLayout.addClassNames("calculations-attack-layout");
                Label projectileDamageLabel = new Label(Database.getString("dc_calc_projectile_section", language));
                projectileDamageLabel.addClassNames("calculations-attack-title");
                projectileDamageLayout.add(projectileDamageLabel, new Hr());
                for (DamageCalculator.UnitStats.AttackValues at : stats.attackValuesContent.get("projectile")) {
                    EntityElement e = Database.getElement(Database.TYPE_LIST, at.type, language);
                    Div damageView = setDamageLayout(e.getName(), e.getImage(), at.attackValue, at.armorValue, at.multiplier, at.result, 0);
                    projectileDamageLayout.add(damageView);
                }
                projectileDamageLayout.add(new Hr());
                Div projectileSumView = calculationRow(createSpecialString(Database.getString("dc_calc_damage_sum", language)), Utils.getDecimalString(stats.projectileSum, 2));
                projectileDamageLayout.add(projectileSumView);
                if (stats.projectileSum != stats.projectileDamage) {
                    Div minView = calculationRow(createSpecialString(Database.getString("dc_calc_min_damage", language)), Utils.getDecimalString(stats.projectileDamage, 2));
                    projectileDamageLayout.add(minView);
                }

                Div projectileTitle = getTitleDiv(Database.getString("dc_calc_projectiles_damage", language));
                Div projectileDamageAnnotation = new AnnotationText(Utils.getDecimalString(stats.projectileDamage, 2), Database.getString("dc_calc_damage_sum", language), "red");
                Div numProjectilesAnnotation = new AnnotationText(Utils.getDecimalString(stats.numProjectiles, 2), Database.getString("dc_calc_projectile", language), "purple");
                Div projectileCalculation = createSpecialString("@@ × @@", projectileDamageAnnotation, numProjectilesAnnotation);
                Div projectileContent = calculationRow(projectileCalculation, Utils.getDecimalString(stats.finalProjectileDamage, 2));
                Div projectileLayout = twoRowsLayout(projectileTitle, projectileContent);
                projectileDamageLayout.add(projectileLayout);

                add(projectileDamageLayout);
            }
        }
        //UNIT CHARGE
        if (stats.hasCharge){
            Div chargeDamageLayout = new Div();
            chargeDamageLayout.addClassNames("calculations-attack-layout");
            Label chargeDamageLabel = new Label(Database.getString("dc_calc_charge_attack", language));
            chargeDamageLabel.addClassNames("calculations-attack-title");
            chargeDamageLayout.add(chargeDamageLabel, new Hr());
            for (DamageCalculator.UnitStats.AttackValues at : stats.attackValuesContent.get("charge")){
                EntityElement e = Database.getElement(Database.TYPE_LIST, at.type, language);
                Div damageView;
                if (at.type == 1) damageView = setDamageLayout(e.getName(), e.getImage(), at.attackValue, at.armorValue, at.multiplier, at.result, stats.chargeAttack);
                else damageView = setDamageLayout(e.getName(), e.getImage(), at.attackValue, at.armorValue, at.multiplier, at.result, 0);
                chargeDamageLayout.add(damageView);
            }
            chargeDamageLayout.add(new Hr());
            Div chargeSumView = calculationRow(createSpecialString(Database.getString("dc_calc_damage_sum", language)), Utils.getDecimalString(stats.chargeSum, 2));
            chargeDamageLayout.add(chargeSumView);

            Div hillTitle = getTitleDiv(Database.getString("dc_calc_hill_bonus", language));
            Div damageSumAnnotation = new AnnotationText(Utils.getDecimalString(stats.chargeSum, 2), Database.getString("dc_calc_damage_sum", language), "red");
            Div hillMultiplierAnnotation = new AnnotationText(Utils.getDecimalString(stats.hillMultiplier, 2), Database.getString("dc_calc_multiplier", language), "purple");
            Div hillCalculation = createSpecialString("@@ × @@", damageSumAnnotation, hillMultiplierAnnotation);
            Div hillContent = calculationRow(hillCalculation, Utils.getDecimalString(stats.chargeHillDamage, 2));
            Div hillLayout = twoRowsLayout(hillTitle, hillContent);
            chargeDamageLayout.add(hillLayout);

            if (stats.finalChargeDamage != stats.chargeHillDamage) {
                Div minView = calculationRow(createSpecialString(Database.getString("dc_calc_min_damage", language)), Utils.getDecimalString(stats.finalChargeDamage, 2));
                chargeDamageLayout.add(minView);
            }
            Div totalChargeDamageView = calculationRow(createSpecialString(Database.getString("dc_calc_charge_attack", language)), Utils.getDecimalString(stats.finalChargeDamage, 2));
            chargeDamageLayout.add(totalChargeDamageView);
            add(chargeDamageLayout);
        }

        //RESULTS
        Div resultsLayout = new Div();
        resultsLayout.addClassNames("calculations-attack-layout");
        Label chargeDamageLabel = new Label(Database.getString("dc_calc_results", language));
        chargeDamageLabel.addClassNames("calculations-attack-title");
        resultsLayout.add(chargeDamageLabel, new Hr());
        Div damageOneHit = calculationRow(createSpecialString(Database.getString("dc_calc_damage_one_hit", language)), Utils.getDecimalString(stats.totalDamage, 2));

        Div timeTitle = getTitleDiv(Database.getString("dc_calc_time_kill", language));
        Div delayAnnotation = new AnnotationText(Utils.getDecimalString(stats.delay, 2), Database.getString("dc_calc_delay", language), "purple");
        Div hitsAnnotation = new AnnotationText(Utils.getDecimalString(stats.hits -1, 2), Database.getString("dc_calc_hits1", language), "red");
        Div reloadAnnotation = new AnnotationText(Utils.getDecimalString(stats.reload, 2), Database.getString("dc_calc_rof", language), "green");
        Div timeCalculation = createSpecialString("@@ +  (@@ × @@)", delayAnnotation, hitsAnnotation, reloadAnnotation);
        Div timeContent = calculationRow(timeCalculation, Utils.getDecimalString(stats.time, 2));
        Div timeToKill = twoRowsLayout(timeTitle, timeContent);

        Div hpLeft = calculationRow(createSpecialString(Database.getString("dc_calc_remaining_hp", language)), Utils.getDecimalString(stats.hpLeft, 2));

        Div hitsPerformed = calculationRow(createSpecialString(Database.getString("dc_calc_hits_performed", language)), Utils.getDecimalString(stats.hitsPerformed, 2));

        Div costEfTitle = getTitleDiv(Database.getString("dc_calc_cost_ef", language));
        Div time1Annotation = new AnnotationText(Utils.getDecimalString(stats.rTime, 2), Database.getString("dc_calc_time", language), "red");
        Div cost1Annotation = new AnnotationText(Utils.getDecimalString(stats.rCost, 2), Database.getString("dc_calc_cost", language), "green");
        Div time2Annotation = new AnnotationText(Utils.getDecimalString(stats.time, 2), Database.getString("dc_calc_time", language), "red");
        Div cost2Annotation = new AnnotationText(Utils.getDecimalString(stats.cost, 2), Database.getString("dc_calc_cost", language), "green");
        Div costEfCalculation = createSpecialString("(@@ × @@)  /  (@@ × @@)", time1Annotation, cost1Annotation, time2Annotation, cost2Annotation);
        Div costEfContent = calculationRow(costEfCalculation, Utils.getDecimalString(stats.costEfficiency, 2));
        Div costEf = twoRowsLayout(costEfTitle, costEfContent);

        Div popEfTitle = getTitleDiv(Database.getString("dc_calc_cost_ef", language));
        Div time11Annotation = new AnnotationText(Utils.getDecimalString(stats.rTime, 2), Database.getString("dc_calc_time", language), "red");
        Div pop11Annotation = new AnnotationText(Utils.getDecimalString(stats.rPopulation, 2), Database.getString("dc_calc_pop", language), "green");
        Div time22Annotation = new AnnotationText(Utils.getDecimalString(stats.time, 2), Database.getString("dc_calc_time", language), "red");
        Div pop22Annotation = new AnnotationText(Utils.getDecimalString(stats.population, 2), Database.getString("dc_calc_pop", language), "green");
        Div popEfCalculation = createSpecialString("(@@ × @@)  /  (@@ × @@)", time11Annotation, pop11Annotation, time22Annotation, pop22Annotation);
        Div popEfContent = calculationRow(popEfCalculation, Utils.getDecimalString(stats.popEfficiency, 2));
        Div popEf = twoRowsLayout(popEfTitle, popEfContent);

        Div hitsToKill, dps;
        resultsLayout.add(damageOneHit);
        if (stats.hasCharge){
            Div hitsTitle = getTitleDiv(Database.getString("dc_calc_hits_kill", language));
            Div chargeHitsAnnotation = new AnnotationText(Utils.getDecimalString(stats.chargeHits, 2), Database.getString("dc_calc_charge_hits", language), "red");
            Div normalHitsAnnotation = new AnnotationText(Utils.getDecimalString(stats.normalHits, 2), Database.getString("dc_calc_normal_hits", language), "blue");
            Div hitsCalculation = createSpecialString("@@ × @@", chargeHitsAnnotation, normalHitsAnnotation);
            Div hitsContent = calculationRow(hitsCalculation, Utils.getDecimalString(stats.hits, 2));
            hitsToKill = twoRowsLayout(hitsTitle, hitsContent);

            dps = calculationRow(createSpecialString(Database.getString("dc_calc_dps", language)), Utils.getDecimalString(stats.dps, 2));

            Div chargeDamageOneHit = calculationRow(createSpecialString(Database.getString("dc_calc_charge_damage_one_hit", language)), Utils.getDecimalString(stats.totalChargeDamage, 2));
            resultsLayout.add(chargeDamageOneHit);

        }
        else {
            Div hitsTitle = getTitleDiv(Database.getString("dc_calc_hits_kill", language));
            Div hpAnnotation = new AnnotationText(Utils.getDecimalString(stats.rHp, 2), Database.getString("dc_calc_hp", language), "green");
            Div damageAnnotation = new AnnotationText(Utils.getDecimalString(stats.totalDamage, 2), Database.getString("dc_calc_damage", language), "red");
            Div hitsCalculation = createSpecialString("@@ / @@", hpAnnotation, damageAnnotation);
            Div hitsContent = calculationRow(hitsCalculation, Utils.getDecimalString(stats.hits, 2));
            hitsToKill = twoRowsLayout(hitsTitle, hitsContent);

            Div dpsTitle = getTitleDiv(Database.getString("dc_calc_hits_kill", language));
            Div totalDamageAnnotation = new AnnotationText(Utils.getDecimalString(stats.totalDamage, 2), Database.getString("dc_calc_damage", language), "red");
            Div reload1Annotation = new AnnotationText(Utils.getDecimalString(stats.reload, 2), Database.getString("dc_calc_rof", language), "green");
            Div dpsCalculation = createSpecialString("@@ / @@", totalDamageAnnotation, reload1Annotation);
            Div dpsContent = calculationRow(dpsCalculation, Utils.getDecimalString(stats.dps, 2));
            dps = twoRowsLayout(dpsTitle, dpsContent);
        }
        resultsLayout.add(hitsToKill);
        resultsLayout.add(timeToKill);
        resultsLayout.add(hitsPerformed);
        resultsLayout.add(hpLeft);
        resultsLayout.add(dps);
        resultsLayout.add(costEf);
        resultsLayout.add(popEf);
        add(resultsLayout);

    }


    private Div setDamageLayout(String title, String icon, double value1, double value2, double multiplier, double result, double chargeValue){
        Div firstRow = new Div();
        firstRow.addClassNames("calculator-attack-row");
        Image attackIcon = new Image();
        attackIcon.setSrc(icon);
        attackIcon.addClassNames("calculator-attack-row-icon");
        if(!icon.contains("t_white")) attackIcon.addClassNames("calculator-attack-row-icon-border");
        Label attackLabel = new Label(title);
        attackLabel.addClassNames("calculator-attack-row-name");
        firstRow.add(attackIcon, attackLabel);
        Div formulaDiv;
        if(!Double.isNaN(chargeValue) && chargeValue != 0){
            Div attack = new AnnotationText(Utils.getDecimalString(value1, 2), Database.getString("dc_calc_attack", language), "red");
            Div armor = new AnnotationText(Utils.getDecimalString(value2, 2), Database.getString("dc_calc_armor", language), "green");
            Div charge = new AnnotationText(Utils.getDecimalString(chargeValue, 2), Database.getString("dc_calc_charge", language), "blue");
            formulaDiv = createSpecialString("@@ + @@ - @@", attack, charge, armor);
        }
        else if (multiplier != 1.0){
            Div attack = new AnnotationText(Utils.getDecimalString(value1, 2), Database.getString("dc_calc_attack", language), "red");
            Div armor = new AnnotationText(Utils.getDecimalString(value2, 2), Database.getString("dc_calc_armor", language), "green");
            Div mult = new AnnotationText(Utils.getDecimalString(multiplier, 2), Database.getString("dc_calc_multiplier", language), "purple");
            formulaDiv = createSpecialString("(@@ × @@)  - @@", attack, mult, armor);
        }
        else{
            Div attack = new AnnotationText(Utils.getDecimalString(value1, 2), Database.getString("dc_calc_attack", language), "red");
            Div armor = new AnnotationText(Utils.getDecimalString(value2, 2), Database.getString("dc_calc_armor", language), "green");
            formulaDiv = createSpecialString("@@ - @@", attack, armor);
        }
        Div secondRow = calculationRow(formulaDiv, Utils.getDecimalString(result, 2));
        return twoRowsLayout(firstRow, secondRow);
    }

    private Div twoRowsLayout(Div first, Div second){
        Div container = new Div(first, second);
        container.addClassNames("calculator-two-rows");
        return container;
    }

    private Div getTitleDiv(String title){
        Span titleText = new Span(title);
        titleText.addClassNames("calculator-title-row-text");
        Div container = new Div(titleText);
        titleText.addClassNames("calculator-title-row-div");
        return container;
    }


    private Div calculationRow(Component content, String result){
        Div container = new Div();
        container.addClassNames("calculator-formula-container");
        Span arrow = new Span("➞");
        arrow.addClassNames("calculator-formula-arrow");
        Span resultText = new Span(result);
        resultText.addClassNames("calculator-formula-result");
        container.add(content, arrow, resultText);
        return container;
    }


    public static Div createSpecialString(@NonNull String s, Div... replacements){
        String[] list = s.split("@@");
        Div container = new Div();
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "row");
        container.getStyle().set("flex-grow", "1");
        container.getStyle().set("justify-content", "center");
        container.getStyle().set("gap", "10px");

        for(int i = 0; i < list.length; ++i){
            Span span = new Span(list[i]);
            span.getStyle().set("font-weight", "600");
            container.add(span);
            if (i < replacements.length) container.add(replacements[i]);
        }
        return container;
    }



    public static class AnnotationText extends Div{

        public AnnotationText(String text, String annotation, String color){
            getStyle().set("display", "flex");
            getStyle().set("flex-direction", "column");
            getStyle().set("justify-content", "center");
            getStyle().set("align-items", "center");
            Label textLabel = new Label(text);
            textLabel.getStyle().set("border-bottom", "1px solid " + color);
            textLabel.getStyle().set("text-align", "center");
            textLabel.getStyle().set("font-weight", "600");

            Label annotationText = new Label(annotation);
            annotationText.getStyle().set("font-size", "10px");
            annotationText.getStyle().set("color", color);
            annotationText.getStyle().set("text-align", "center");
            annotationText.getStyle().set("white-space", "pre-wrap");
            add(textLabel, annotationText);
        }
    }
}
