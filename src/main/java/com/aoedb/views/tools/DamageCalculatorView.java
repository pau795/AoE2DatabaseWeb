package com.aoedb.views.tools;

import com.aoedb.views.components.CalculationsPopup;
import com.aoedb.views.components.DoubleAgeCivSelector;
import com.aoedb.data.DamageCalculator;
import com.aoedb.data.EntityElement;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.aoedb.views.OneColumnView;
import com.aoedb.views.components.StatsPopup;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@CssImport("./themes/aoe2database/damage-calculator.css")
@RoutePrefix("damage_calculator")
@Route(value = ":entityID1?/:entityID2?/:language", layout = MainLayout.class)
public class DamageCalculatorView extends OneColumnView {

    int civID1, civID2, initUnit1, initUnit2;
    Unit unit1, unit2;
    Image unit1Image, unit2Image;
    ComboBox<EntityElement> unit1Selector, unit2Selector;
    List<Integer> upgradesID1, upgradesID2;
    int ageID;

    DamageCalculator calculator;
    DoubleAgeCivSelector selector;

    Label unit1ResultText;
    Label unit2ResultText;
    Label unit1DamageText;
    Label unit2DamageText;
    Label unit1HitsText;
    Label unit2HitsText;
    Label unit1TimeText;
    Label unit1HitsPerformedText;
    Label unit2HitsPerformedText;
    Label unit2TimeText;
    Label unit1HPLeftText;
    Label unit2HPLeftText;
    Label unit1DPSText;
    Label unit2DPSText;
    Label unit1CostEfText;
    Label unit2CostEfText;
    Label unit1PopEfText;
    Label unit2PopEfText;

    Button unit1Info, unit2Info, unit1Calculations, unit2Calculations;
    Checkbox unit1Hill, unit2Hill;
    IntegerField unit1Relics, unit2Relics;

    Dialog statsPopup1, statsPopup2, calculationsPopup1, calculationsPopup2;
    StatsPopup stats1, stats2;
    CalculationsPopup calculations1, calculations2;


    @Override
    protected Div getColumn() {
        unit1 =  new Unit(Database.getUnit(1, language));
        unit2 =  new Unit(Database.getUnit(1, language));
        selector = new DoubleAgeCivSelector(language);
        selector.setOnAgeChangeListener(age -> {
            ageID = age;
            calculateStats();
        });
        civID1 = -1;
        civID2 = -1;

        ArrayList<EntityElement> items = new ArrayList<>(Database.getList(Database.UNIT_LIST, language));
        items.sort(EntityElement.getAlphabeticalComparator());
        upgradesID1 = new ArrayList<>();
        upgradesID2 = new ArrayList<>();

        initLabels();
        initButtons();
        Div content = new Div();
        content.addClassNames("damage-calculator-content");
        Div entitiesLayout = new Div();
        entitiesLayout.addClassNames("damage-calculator-images-layout");

        //LEFT
        unit1Image = new Image();
        unit1Image.addClassNames("damage-calculator-gif");
        unit1Selector = new ComboBox<>();
        unit1Selector.addClassNames("damage-calculator-selector");
        unit1Selector.setItemLabelGenerator(EntityElement::getName);
        unit1Selector.setRenderer(new ComponentRenderer<>(element -> Utils.getEntityItemRow(element, true)));

        unit1Selector.setItems(Utils.getEntityElementComboBoxFilter(),items);
        unit1Selector.addValueChangeListener(event-> {
            if (event.getValue() !=  null) setUnit1(event.getValue().getId());
        });
        unit1Selector.getElement().getStyle().set("--vaadin-combo-box-overlay-width","300px");
        unit1Selector.addFocusListener(event -> unit1Selector.setValue(null));
        Div leftUnitDiv = new Div();
        leftUnitDiv.addClassNames("damage-calculator-unit-layout");
        leftUnitDiv.add(unit1Selector, unit1Image);
        selector.setOnEntity1ChangeListener(new DoubleAgeCivSelector.OnEntityChangeListener() {
            @Override
            public void onCivChanged(int civ) {
                civID1 = civ;
                updateURL();
                calculateStats();
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                upgradesID1 = list;
                calculateStats();
            }
        });
        setUnit1(initUnit1);
        unit1Hill.addValueChangeListener(event ->{
            if (unit1Hill.getValue() && unit2Hill.getValue()) unit2Hill.setValue(false);
            calculateStats();
        });
        unit1Relics.addValueChangeListener(event ->{
            if (unit1Relics.getValue() < unit1Relics.getMin()) unit1Relics.setValue(0);
            if (unit1Relics.getValue() > unit1Relics.getMax()) unit1Relics.setValue(4);
            calculateStats();
        });


        //RIGHT
        unit2Image = new Image();
        unit2Image.addClassNames("damage-calculator-gif");
        unit2Selector = new ComboBox<>();
        unit2Selector.addClassNames("damage-calculator-selector");
        unit2Selector.setItemLabelGenerator(EntityElement::getName);
        unit2Selector.setRenderer(new ComponentRenderer<>(element -> Utils.getEntityItemRow(element, true)));
        unit2Selector.setItems(Utils.getEntityElementComboBoxFilter(), items);
        unit2Selector.addValueChangeListener(event -> {
            if (event.getValue() != null) setUnit2(event.getValue().getId());
        });
        unit2Selector.getElement().getStyle().set("--vaadin-combo-box-overlay-width","300px");
        unit2Selector.addFocusListener(event -> unit2Selector.setValue(null));
        selector.setOnEntity2ChangeListener(new DoubleAgeCivSelector.OnEntityChangeListener() {
            @Override
            public void onCivChanged(int civ) {
                civID2 = civ;
                updateURL();
                calculateStats();
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                upgradesID2 = list;
                calculateStats();
            }
        });
        setUnit2(initUnit2);
        unit2Hill.addValueChangeListener(event ->{
            if (unit2Hill.getValue() && unit1Hill.getValue()) unit1Hill.setValue(false);
            calculateStats();
        });
        unit2Relics.addValueChangeListener(event ->{
            if (unit2Relics.getValue() < unit2Relics.getMin()) unit2Relics.setValue(0);
            if (unit2Relics.getValue() > unit2Relics.getMax()) unit2Relics.setValue(4);
            calculateStats();
        });

        Div rightUnitDiv = new Div();
        rightUnitDiv.addClassNames("damage-calculator-unit-layout");
        rightUnitDiv.add(unit2Selector, unit2Image);
        entitiesLayout.add(leftUnitDiv, rightUnitDiv);


        Div buttonsLayout = getButtonsLayout();
        Div resultsLayout = getResultsLayout();
        content.add(entitiesLayout, selector, buttonsLayout, statsPopup1, statsPopup2, new Hr(), resultsLayout);

        return content;
    }

    private void setUnit1(int id){
        unit1 = new Unit(Database.getUnit(id, language));
        unit1Image.setSrc(unit1.getNameElement().getMedia());
        unit1Selector.setValue(unit1.getNameElement());
        List<Integer> availableCivs = unit1.getAvailableCivIds();
        List<EntityElement> civList = new ArrayList<>(availableCivs.size());
        ageID = Utils.convertAge(Utils.getMaxAge(unit1, unit2, language), language);
        for(int i : availableCivs) civList.add(Database.getElement(Database.CIVILIZATION_LIST, i, language));
        civList.sort(EntityElement.getAlphabeticalComparator());
        if (civID1 == -1) civID1 = availableCivs.get(0);
        upgradesID1 = unit1.getUpgradesIds();
        selector.setupUpgrade1Selector(upgradesID1);
        selector.setUnit1Civ(Database.getElement(Database.CIVILIZATION_LIST, civID1, language), civList);
        updateURL();
        setupAgeLayout();
    }

    private void setUnit2(int id){
        unit2 = new Unit(Database.getUnit(id, language));
        unit2Image.setSrc(unit2.getNameElement().getMedia());
        unit2Selector.setValue(unit2.getNameElement());
        List<Integer> availableCivs = unit2.getAvailableCivIds();
        List<EntityElement> civList = new ArrayList<>(availableCivs.size());
        ageID = Utils.convertAge(Utils.getMaxAge(unit1, unit2, language), language);
        for(int i : availableCivs) civList.add(Database.getElement(Database.CIVILIZATION_LIST, i, language));
        civList.sort(EntityElement.getAlphabeticalComparator());
        if (civID2 == -1) civID2 = availableCivs.get(0);
        upgradesID2 = unit2.getUpgradesIds();
        selector.setupUpgrade2Selector(upgradesID2);
        selector.setUnit2Civ(Database.getElement(Database.CIVILIZATION_LIST, civID2, language), civList);
        updateURL();
        setupAgeLayout();
    }

    private void updateURL(){
        parameters = Utils.changeRouteParameter(parameters, "entityID1", String.valueOf(unit1.getEntityID()));
        parameters = Utils.changeRouteParameter(parameters, "entityID2", String.valueOf(unit2.getEntityID()));
        getUI().ifPresent(ui -> ui.navigate(this.getClass(), parameters));
    }

    private void calculateStats() {

        calculator = new DamageCalculator(unit1, unit2, language);

        calculator.setUnit1Hill(unit1Hill.getValue());
        calculator.setUnit2Hill(unit2Hill.getValue());
        calculator.setUnit1Relics(unit1Relics.getValue());
        calculator.setUnit2Relics(unit2Relics.getValue());

        calculator.calculateStats(ageID, civID1, civID2, upgradesID1, upgradesID2);

        DamageCalculator.UnitStats u1 = calculator.getUnit1Stats();
        DamageCalculator.UnitStats u2 = calculator.getUnit2Stats();

        if (u1.time == u2.time) {
            unit1ResultText.getStyle().set("color", "orange");
            unit1ResultText.setText(Database.getString("dc_tie", language));
            unit2ResultText.getStyle().set("color", "orange");
            unit2ResultText.setText(Database.getString("dc_tie", language));
        }
        else if ((u1.time < u2.time && u1.totalDamage != 0 || u2.totalDamage == 0)) {
            unit1ResultText.getStyle().set("color", "green");
            unit1ResultText.setText(Database.getString("dc_win", language));
            unit2ResultText.getStyle().set("color", "red");
            unit2ResultText.setText(Database.getString("dc_lose", language));
        }
        else{
            unit1ResultText.getStyle().set("color", "red");
            unit1ResultText.setText(Database.getString("dc_lose", language));
            unit2ResultText.getStyle().set("color", "green");
            unit2ResultText.setText(Database.getString("dc_win", language));
        }
        if (u1.hasCharge){
            unit1DamageText.setText(String.format(Database.getString("dc_damage_dealt", language), Utils.getDecimalString(u1.totalDamage, 2)) + "\n" +
                String.format(Database.getString("dc_damage_dealt_charge", language), Utils.getDecimalString(u1.totalChargeDamage, 2)));
        }
        else unit1DamageText.setText(String.format(Database.getString("dc_damage_dealt", language), Utils.getDecimalString(u1.totalDamage, 2)));
        if (u2.hasCharge){
            unit2DamageText.setText(String.format(Database.getString("dc_damage_dealt", language), Utils.getDecimalString(u2.totalDamage, 2)) + "\n" +
                String.format(Database.getString("dc_damage_dealt_charge", language), Utils.getDecimalString(u2.totalChargeDamage, 2)));
        }
        else unit2DamageText.setText(String.format(Database.getString("dc_damage_dealt", language), Utils.getDecimalString(u2.totalDamage, 2)));
        unit1HitsText.setText(String.format(Database.getString("dc_hits_dealt", language), u1.hits));
        unit2HitsText.setText(String.format(Database.getString("dc_hits_dealt", language), u2.hits));
        unit1HitsPerformedText.setText(String.format(Database.getString("dc_hits_dealt", language), u1.hitsPerformed));
        unit2HitsPerformedText.setText(String.format(Database.getString("dc_hits_dealt", language), u2.hitsPerformed));
        unit1TimeText.setText(String.format(Database.getString("dc_time_needed", language), Utils.getDecimalString(u1.time, 2)));
        unit2TimeText.setText(String.format(Database.getString("dc_time_needed", language), Utils.getDecimalString(u2.time, 2)));
        unit1HPLeftText.setText(String.format(Database.getString("dc_hp_left", language), Utils.getDecimalString(u1.hpLeft, 1), Utils.getDecimalString(u1.hp, 1)));
        unit2HPLeftText.setText(String.format(Database.getString("dc_hp_left", language), Utils.getDecimalString(u2.hpLeft, 1), Utils.getDecimalString(u2.hp, 1)));
        unit1DPSText.setText(String.format(Database.getString("dc_dps_dealt", language), Utils.getDecimalString(u1.dps, 2)));
        unit2DPSText.setText(String.format(Database.getString("dc_dps_dealt", language), Utils.getDecimalString(u2.dps, 2)));
        unit1CostEfText.setText(Utils.getDecimalString(u1.costEfficiency, 2));
        unit2CostEfText.setText(Utils.getDecimalString(u2.costEfficiency, 2));
        unit1PopEfText.setText(Utils.getDecimalString(u1.popEfficiency, 2));
        unit2PopEfText.setText(Utils.getDecimalString(u2.popEfficiency, 2));
    }

    private void initLabels(){
        unit1ResultText = new Label();
        unit1ResultText.addClassNames("damage-calculator-results-left-column", "damage-calculator-results-label");
        unit2ResultText = new Label();
        unit2ResultText.addClassNames("damage-calculator-results-right-column", "damage-calculator-results-label");
        unit1DamageText = new Label();
        unit1DamageText.addClassNames("damage-calculator-results-left-column");
        unit2DamageText = new Label();
        unit2DamageText.addClassNames("damage-calculator-results-right-column");
        unit1HitsText = new Label();
        unit1HitsText.addClassNames("damage-calculator-results-left-column");
        unit2HitsText = new Label();
        unit2HitsText.addClassNames("damage-calculator-results-right-column");
        unit1TimeText = new Label();
        unit1TimeText.addClassNames("damage-calculator-results-left-column");
        unit2TimeText = new Label();
        unit2TimeText.addClassNames("damage-calculator-results-right-column");
        unit1HitsPerformedText = new Label();
        unit1HitsPerformedText.addClassNames("damage-calculator-results-left-column");
        unit2HitsPerformedText = new Label();
        unit2HitsPerformedText.addClassNames("damage-calculator-results-right-column");
        unit1HPLeftText = new Label();
        unit1HPLeftText.addClassNames("damage-calculator-results-left-column");
        unit2HPLeftText = new Label();
        unit2HPLeftText.addClassNames("damage-calculator-results-right-column");
        unit1DPSText = new Label();
        unit1DPSText.addClassNames("damage-calculator-results-left-column");
        unit2DPSText = new Label();
        unit2DPSText.addClassNames("damage-calculator-results-right-column");
        unit1CostEfText = new Label();
        unit1CostEfText.addClassNames("damage-calculator-results-left-column");
        unit2CostEfText = new Label();
        unit2CostEfText.addClassNames("damage-calculator-results-right-column");
        unit1PopEfText = new Label();
        unit1PopEfText.addClassNames("damage-calculator-results-left-column");
        unit2PopEfText = new Label();
        unit2PopEfText.addClassNames("damage-calculator-results-right-column");
    }

    private void initButtons(){
        unit1Info = new Button();
        unit1Info = new Button(Database.getString("dc_show_stats", language).toUpperCase(Locale.ROOT));
        unit1Info.setId("stats1");
        unit1Info.addClassNames("damage-calculator-button");
        unit1Info.getElement().setProperty("title", Database.getString("dc_show_stats", language));

        statsPopup1 = new Dialog();
        statsPopup1.setCloseOnOutsideClick(true);
        statsPopup1.setCloseOnEsc(true);
        stats1 = new StatsPopup(unit1, language);
        statsPopup1.add(stats1);
        unit1Info.addClickListener(event -> {
            stats1 = new StatsPopup(unit1, language);
            statsPopup1.removeAll();
            statsPopup1.add(stats1);
            statsPopup1.open();
        });


        unit2Info = new Button();
        unit2Info = new Button(Database.getString("dc_show_stats", language).toUpperCase(Locale.ROOT));
        unit2Info.setId("stats2");
        unit2Info.addClassNames("damage-calculator-button");
        unit2Info.getElement().setProperty("title", Database.getString("dc_show_stats", language));

        statsPopup2 = new Dialog();
        statsPopup2.setCloseOnOutsideClick(true);
        statsPopup2.setCloseOnEsc(true);
        stats2 = new StatsPopup(unit2, language);
        statsPopup2.add(stats2);
        unit2Info.addClickListener(event -> {
            stats2 = new StatsPopup(unit2, language);
            statsPopup2.removeAll();
            statsPopup2.add(stats2);
            statsPopup2.open();
        });

        unit1Calculations = new Button();
        unit1Calculations = new Button(Database.getString("dc_unit_calculations", language).toUpperCase(Locale.ROOT));
        unit1Calculations.setId("calculations1");
        unit1Calculations.addClassNames("damage-calculator-button");
        unit1Calculations.getElement().setProperty("title", Database.getString("dc_unit_calculations", language));

        calculationsPopup1 = new Dialog();
        calculationsPopup1.setCloseOnOutsideClick(true);
        calculationsPopup1.setCloseOnEsc(true);
        unit1Calculations.addClickListener(event -> {
            calculations1 = new CalculationsPopup(calculator.getUnit1Stats(), language);
            calculationsPopup1.removeAll();
            calculationsPopup1.add(calculations1);
            calculationsPopup1.open();
        });

        unit2Calculations = new Button();
        unit2Calculations = new Button(Database.getString("dc_unit_calculations", language).toUpperCase(Locale.ROOT));
        unit2Calculations.setId("calculations2");
        unit2Calculations.addClassNames("damage-calculator-button");
        unit2Calculations.getElement().setProperty("title", Database.getString("dc_unit_calculations", language));

        calculationsPopup2 = new Dialog();
        calculationsPopup2.setCloseOnOutsideClick(true);
        calculationsPopup2.setCloseOnEsc(true);
        unit2Calculations.addClickListener(event -> {
            calculations2 = new CalculationsPopup(calculator.getUnit2Stats(), language);
            calculationsPopup2.removeAll();
            calculationsPopup2.add(calculations2);
            calculationsPopup2.open();
        });

        unit1Hill = new Checkbox();
        unit1Hill.addClassNames("damage-calculator-hill-checkbox");
        unit2Hill = new Checkbox();
        unit2Hill.addClassNames("damage-calculator-hill-checkbox");

        unit1Relics = new IntegerField();
        unit1Relics.addClassNames("damage-calculator-relics");
        unit1Relics.setValue(0);
        unit1Relics.setHasControls(true);
        unit1Relics.setMin(0);
        unit1Relics.setMax(4);

        unit2Relics = new IntegerField();
        unit2Relics.addClassNames("damage-calculator-relics");
        unit2Relics.setValue(0);
        unit2Relics.setHasControls(true);
        unit2Relics.setMin(0);
        unit2Relics.setMax(4);
    }

    private Div getButtonsLayout(){
        Div buttonGrid1 = new Div();
        buttonGrid1.addClassNames("damage-calculator-buttons-grid1");
        Div buttonGrid2 = new Div();
        buttonGrid2.addClassNames("damage-calculator-buttons-grid2");
        Label hillBonusText = new Label(Database.getString("dc_hill_bonus", language));
        hillBonusText.addClassNames("damage-calculator-buttons-label");
        Label relicsText = new Label(Database.getString("dc_relics", language));
        relicsText.addClassNames("damage-calculator-buttons-label");

        buttonGrid1.add(unit1Info, unit2Info);
        buttonGrid1.add(unit1Calculations, unit2Calculations);
        buttonGrid2.add(unit1Hill, hillBonusText ,unit2Hill);
        buttonGrid2.add(unit1Relics, relicsText, unit2Relics);

        Div buttonContainer = new Div();
        buttonContainer.add(buttonGrid1, buttonGrid2);
        buttonContainer.addClassNames("damage-calculator-buttons-container");
        return buttonContainer;
    }

    private Div getResultsLayout(){
        Div container = new Div();
        container.addClassNames("damage-calculator-results-grid");
        Label results = new Label(Database.getString("dc_results", language));
        results.addClassNames("damage-calculator-results-center-column");
        Label damageOneHit = new Label(Database.getString("dc_damage", language));
        damageOneHit.addClassNames("damage-calculator-results-center-column");
        Label hitsToKill = new Label(Database.getString("dc_hits", language));
        hitsToKill.addClassNames("damage-calculator-results-center-column");
        Label timeToKill = new Label(Database.getString("dc_time", language));
        timeToKill.addClassNames("damage-calculator-results-center-column");
        Label hitsPerformed = new Label(Database.getString("dc_hits_performed", language));
        hitsPerformed.addClassNames("damage-calculator-results-center-column");
        Label remainingHP = new Label(Database.getString("dc_remaining_hp", language));
        remainingHP.addClassNames("damage-calculator-results-center-column");
        Label dps = new Label(Database.getString("dc_dps", language));
        dps.addClassNames("damage-calculator-results-center-column");
        Label costEfficiency = new Label(Database.getString("dc_cost_efficiency", language));
        costEfficiency.addClassNames("damage-calculator-results-center-column");
        Label popEfficiency = new Label(Database.getString("dc_pop_efficiency", language));
        popEfficiency.addClassNames("damage-calculator-results-center-column");

        Div resultsDiv = new Div(unit1ResultText, results, unit2ResultText);
        resultsDiv.addClassNames("damage-calculator-grid-row");

        Div damageDiv = new Div(unit1DamageText, damageOneHit, unit2DamageText);
        damageDiv.addClassNames("damage-calculator-grid-row");

        Div hitsDiv = new Div(unit1HitsText, hitsToKill, unit2HitsText);
        hitsDiv.addClassNames("damage-calculator-grid-row");

        Div timeDiv = new Div(unit1TimeText, timeToKill, unit2TimeText);
        timeDiv.addClassNames("damage-calculator-grid-row");

        Div hitsPerformedDiv = new Div(unit1HitsPerformedText, hitsPerformed, unit2HitsPerformedText);
        hitsPerformedDiv.addClassNames("damage-calculator-grid-row");

        Div hpLeftDiv = new Div(unit1HPLeftText, remainingHP, unit2HPLeftText);
        hpLeftDiv.addClassNames("damage-calculator-grid-row");

        Div dpsDiv = new Div(unit1DPSText, dps, unit2DPSText);
        dpsDiv.addClassNames("damage-calculator-grid-row");

        Div costEffDiv = new Div(unit1CostEfText, costEfficiency, unit2CostEfText);
        costEffDiv.addClassNames("damage-calculator-grid-row");

        Div popEffDiv = new Div(unit1PopEfText, popEfficiency, unit2PopEfText);
        popEffDiv.addClassNames("damage-calculator-grid-row");

        container.add(resultsDiv, damageDiv, hitsDiv, timeDiv, hitsPerformedDiv, hpLeftDiv, dpsDiv, costEffDiv, popEffDiv);
        return container;
    }

    private void setupAgeLayout(){
        String ageString = Utils.getMaxAge(unit1, unit2, language);
        if (ageString.equals(Database.getString("dark_age", language))) selector.showDarkAge();
        else if (ageString.equals(Database.getString("feudal_age", language))) selector.showFeudalAge();
        else if (ageString.equals(Database.getString("castle_age", language))) selector.showCastleAge();
        else if (ageString.equals(Database.getString("imperial_age", language))) selector.showImperialAge();
        selector.selectInitialAge(Utils.convertAge(ageString, language));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        parameters = beforeEnterEvent.getRouteParameters();

        initUnit1 = Integer.parseInt(parameters.get("entityID1").orElse("1"));
        initUnit2 = Integer.parseInt(parameters.get("entityID2").orElse("1"));
        language = Utils.checkLanguage(parameters.get("language").orElse(Database.DEFAULT_LANGUAGE));
        if (!init){
            initView();
            init = true;
        }
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_damage_calculator_activity", language);
    }

}
