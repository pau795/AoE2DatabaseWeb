package com.aoedb.views.tools;

import com.aoedb.data.EntityElement;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.aoedb.views.OneColumnView;
import com.aoedb.views.components.DoubleAgeCivSelector;
import com.aoedb.views.components.StatComparator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.ArrayList;
import java.util.List;

@RoutePrefix("unit_comparator")
@Route(value = ":entityID1?/:entityID2?/:language", layout = MainLayout.class)
public class UnitComparatorView extends OneColumnView {

    int civID1, civID2, initUnit1, initUnit2;
    Unit unit1, unit2;
    Image unit1Image, unit2Image;
    ComboBox<EntityElement> unit1Selector, unit2Selector;
    List<Integer> upgradesID1, upgradesID2;
    int ageID;

    DoubleAgeCivSelector selector;
    StatComparator comparator;


    @Override
    protected Div getColumn() {
        unit1 =  new Unit(Database.getUnit(1));
        unit2 =  new Unit(Database.getUnit(1));
        selector = new DoubleAgeCivSelector(language);
        selector.setOnAgeChangeListener(age -> {
            ageID = age;
            calculateStats();
        });
        civID1 = -1;
        civID2 = -1;
        comparator = new StatComparator(language);


        ArrayList<EntityElement> items = new ArrayList<>(Database.getList(Database.UNIT_LIST));
        items.sort(EntityElement.getAlphabeticalComparator(language));
        upgradesID1 = new ArrayList<>();
        upgradesID2 = new ArrayList<>();

        Div content = new Div();
        content.addClassNames("damage-calculator-content");
        Div entitiesLayout = new Div();
        entitiesLayout.addClassNames("damage-calculator-images-layout");

        //LEFT
        unit1Image = new Image();
        unit1Image.addClassNames("damage-calculator-gif");
        unit1Selector = new ComboBox<>();
        unit1Selector.addClassNames("damage-calculator-selector");
        unit1Selector.setItemLabelGenerator(entityElement -> entityElement.getName().getTranslatedString(language));
        unit1Selector.setRenderer(new ComponentRenderer<>(element -> Utils.getEntityItemRow(element, true, language)));

        unit1Selector.setItems(Utils.getEntityElementComboBoxFilter(language), items);
        unit1Selector.addValueChangeListener(event -> {
            if (event.getValue() != null) setUnit1(event.getValue().getId());
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


        //RIGHT
        unit2Image = new Image();
        unit2Image.addClassNames("damage-calculator-gif");
        unit2Selector = new ComboBox<>();
        unit2Selector.addClassNames("damage-calculator-selector");
        unit2Selector.setItemLabelGenerator(entityElement -> entityElement.getName().getTranslatedString(language));
        unit2Selector.setRenderer(new ComponentRenderer<>(element -> Utils.getEntityItemRow(element, true, language)));
        unit2Selector.setItems(Utils.getEntityElementComboBoxFilter(language), items);
        unit2Selector.addValueChangeListener(event -> {
            if(event.getValue() != null) setUnit2(event.getValue().getId());
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


        Div rightUnitDiv = new Div();
        rightUnitDiv.addClassNames("damage-calculator-unit-layout");
        rightUnitDiv.add(unit2Selector, unit2Image);
        entitiesLayout.add(leftUnitDiv, rightUnitDiv);


        content.add(entitiesLayout, selector, comparator);

        return content;
    }

    private void setUnit1(int id){
        unit1 = new Unit(Database.getUnit(id));
        unit1Image.setSrc(unit1.getNameElement().getMedia());
        unit1Selector.setValue(unit1.getNameElement());
        List<Integer> availableCivs = unit1.getAvailableCivIds();
        List<EntityElement> civList = new ArrayList<>(availableCivs.size());
        ageID = Utils.convertAge(Utils.getMaxAge(unit1, unit2, language), language);
        for(int i : availableCivs) civList.add(Database.getElement(Database.CIVILIZATION_LIST, i));
        civList.sort(EntityElement.getAlphabeticalComparator(language));
        if (civID1 == -1) civID1 = availableCivs.get(0);
        upgradesID1 = unit1.getUpgradesIds();
        selector.setupUpgrade1Selector(upgradesID1);
        selector.setUnit1Civ(Database.getElement(Database.CIVILIZATION_LIST, civID1), civList);
        updateURL();
        setupAgeLayout();
    }

    private void updateURL(){
        parameters = Utils.changeRouteParameter(parameters, "entityID1", String.valueOf(unit1.getEntityID()));
        parameters = Utils.changeRouteParameter(parameters, "entityID2", String.valueOf(unit2.getEntityID()));
        getUI().ifPresent(ui -> ui.navigate(this.getClass(), parameters));
    }

    private void setUnit2(int id){
        unit2 = new Unit(Database.getUnit(id));
        unit2Image.setSrc(unit2.getNameElement().getMedia());
        unit2Selector.setValue(unit2.getNameElement());
        List<Integer> availableCivs = unit2.getAvailableCivIds();
        List<EntityElement> civList = new ArrayList<>(availableCivs.size());
        ageID = Utils.convertAge(Utils.getMaxAge(unit1, unit2, language), language);
        for(int i : availableCivs) civList.add(Database.getElement(Database.CIVILIZATION_LIST, i));
        civList.sort(EntityElement.getAlphabeticalComparator(language));
        if (civID2 == -1) civID2 = availableCivs.get(0);
        upgradesID2 = unit2.getUpgradesIds();
        selector.setupUpgrade2Selector(upgradesID2);
        selector.setUnit2Civ(Database.getElement(Database.CIVILIZATION_LIST, civID2), civList);
        updateURL();
        setupAgeLayout();
    }

    private void setupAgeLayout(){
        String ageString = Utils.getMaxAge(unit1, unit2, language);
        selector.showDarkAge();
        selector.selectInitialAge(Utils.convertAge(ageString, language));
    }

    private void calculateStats() {
        unit1.calculateStats(ageID, civID1, upgradesID1);
        unit2.calculateStats(ageID, civID2, upgradesID2);
        comparator.updateStats(unit1, unit2);
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
        return Database.getString("app_name", language) + " - " + Database.getString("title_unit_comparator", language);
    }
}