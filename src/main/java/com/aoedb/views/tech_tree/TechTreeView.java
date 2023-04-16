package com.aoedb.views.tech_tree;

import com.aoedb.views.BaseView;
import com.aoedb.views.MainLayout;
import com.aoedb.views.components.TechTreeBox;
import com.aoedb.views.components.TechTreeConnector;
import com.aoedb.views.components.TechTreeEmptyBox;
import com.aoedb.views.components.TechTreeSlotLayout;
import com.aoedb.data.Civilization;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RoutePrefix;

import java.util.*;

@RoutePrefix("tech_tree")
@Route(value = ":civID?/:language", layout = MainLayout.class)
@CssImport("./themes/aoe2database/tech-tree-view.css")
public class TechTreeView extends BaseView {


    static int boxWidth = 135;
    static int boxHeight = 110;

    static int columnWidth = boxWidth;
    static int sectionHeight = boxHeight * 2;
    static int archeryRangeWidth = boxWidth * 7;
    static int stableWidth = boxWidth * 7;
    static int siegeWorkshopWidth = boxWidth * 5;
    static int blacksmithWidth = boxWidth * 5;
    static int universityWidth = boxWidth * 7;
    static int castleWidth = boxWidth * 8;
    static int donjonWidth = boxWidth * 2;

    static int monasteryWidth = boxWidth * 8;
    static int miningCampWidth = boxWidth * 2;
    static int marketWidth = boxWidth * 2;


    ArrayList<EntityElement> civNames;
    Civilization civ;
    int civID;
    Label civName;
    Span civInfoSpan;

    Component leftShortcut;
    Component archeryRangeShortcut;
    Component barracksShortcut;
    Component stableShortcut;
    Component siegeWorkshopShortcut;
    Component blacksmithShortcut;
    Component dockShortcut;
    Component universityShortcut;
    Component towersShortcut;
    Component castleShortcut;
    Component monasteryShortcut;
    Component townCenterShortcut;
    Component lumberCampShortcut;
    Component rightShortcut;


    Set<TechTreeBox> boxSet;
    Select<EntityElement> civSelector;
    TechTreeBox uniqueUnit, eliteUniqueUnit, castleUniqueTech, imperialUniqueTech, hussar, mill;

    @Override
    public void initView() {
        removeClassNames("view-main-layout");
        boxSet = new HashSet<>();
        Div topBar = getTopBar();
        Div techTreeCivInfo = getCivInfoLayout();

        Div darkContainer = darkAgeLayout();
        Div feudalContainer = feudalAgeLayout();
        Div castleContainer = castleAgeLayout();
        Div imperialContainer = imperialAgeLayout();
        Div techTreeFigureContainer = new Div();
        techTreeFigureContainer.addClassNames("tech-tree-figure-container");
        techTreeFigureContainer.add(darkContainer, feudalContainer, castleContainer, imperialContainer);

        Div techTreeContainer = new Div();
        techTreeContainer.addClassNames("tech-tree-container");
        techTreeContainer.add(techTreeCivInfo, techTreeFigureContainer);
        Div mainContainer = new Div();
        mainContainer.add(topBar, techTreeContainer);
        mainContainer.addClassNames("tech-tree-main-container");
        addClassNames("tech-tree-view");
        add(mainContainer);
        setupSelector();
    }

    private Div getCivInfoLayout() {
        Div civInfoLayout = new Div();
        civInfoLayout.addClassNames("tech-tree-civ-info-layout", "tech-tree-feudal-background");
        civInfoSpan = new Span();
        civInfoSpan.addClassNames("tech-tree-civ-info-text");
        civName = new Label();
        leftShortcut = civName;
        civName.addClassNames("tech-tree-civ-name");
        Div legend = new Div();
        legend.addClassNames("tech-tree-legend-layout");
        legend.add( getLegendRow(Database.getString("tt_buildings", language), "tech-tree-box-red"),
                getLegendRow(Database.getString("tt_units", language), "tech-tree-box-teal"),
                getLegendRow(Database.getString("tt_techs", language), "tech-tree-box-green"),
                getLegendRow(Database.getString("tt_unique_units", language), "tech-tree-box-purple"));
        civInfoLayout.add(civName, civInfoSpan, legend);
        return civInfoLayout;
    }

    private Div getLegendRow(String name, String classColor){
        Div row = new Div();
        row.addClassNames("tech-tree-legend-row");
        Div color = new Div();
        color.addClassNames("tech-tree-legend-box", classColor);
        Label label = new Label(name);
        label.addClassNames("tech-tree-legend-text");
        row.add(color, label);
        return row;

    }

    private Div getTopBar(){
        Div bar = new Div();
        bar.addClassNames("tech-tree-top-bar");
        Div civSelectorLayout = new Div();
        civSelectorLayout.addClassNames("tech-tree-top-bar-selector-layout");
        civSelector = new Select<>();
        civSelector.addClassNames("tech-tree-civ-selector");
        civSelectorLayout.add(civSelector);
        Div shortcutLayout = shortCutLayout();
        bar.add(civSelectorLayout, shortcutLayout);
        return bar;
    }

    private Div shortCutLayout(){
        Div shortcutLayout = new Div();
        Label shortcutLabel = new Label(Database.getString("shortcut_name", language));
        Image left = new Image();
        left.addClickListener(event -> leftShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", leftShortcut.getElement()));
        left.setSrc("images/left.png");
        left.addClassNames("tech-tree-shortcut-image");
        Image archeryRange = new Image();
        archeryRange.addClickListener(event -> archeryRangeShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", archeryRangeShortcut.getElement()));
        archeryRange.setSrc("images/b_archery_range.png");
        archeryRange.addClassNames("tech-tree-shortcut-image");
        Image barracks = new Image();
        barracks.addClickListener(event -> barracksShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", barracksShortcut.getElement()));
        barracks.setSrc("images/b_barracks.png");
        barracks.addClassNames("tech-tree-shortcut-image");
        Image stable = new Image();
        stable.addClickListener(event -> stableShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", stableShortcut.getElement()));
        stable.setSrc("images/b_stable.png");
        stable.addClassNames("tech-tree-shortcut-image");
        Image siegeWorkshop = new Image();
        siegeWorkshop.addClickListener(event -> siegeWorkshopShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", siegeWorkshopShortcut.getElement()));
        siegeWorkshop.setSrc("images/b_siege_workshop.png");
        siegeWorkshop.addClassNames("tech-tree-shortcut-image");
        Image blacksmith = new Image();
        blacksmith.addClickListener(event -> blacksmithShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", blacksmithShortcut.getElement()));
        blacksmith.setSrc("images/b_blacksmith.png");
        blacksmith.addClassNames("tech-tree-shortcut-image");
        Image dock = new Image();
        dock.addClickListener(event -> dockShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", dockShortcut.getElement()));
        dock.setSrc("images/b_dock.png");
        dock.addClassNames("tech-tree-shortcut-image");
        Image university = new Image();
        university.addClickListener(event -> universityShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", universityShortcut.getElement()));
        university.setSrc("images/b_university.png");
        university.addClassNames("tech-tree-shortcut-image");
        Image towers = new Image();
        towers.addClickListener(event -> towersShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", towersShortcut.getElement()));
        towers.setSrc("images/b_tower.png");
        towers.addClassNames("tech-tree-shortcut-image");
        Image castle = new Image();
        castle.addClickListener(event -> castleShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", castleShortcut.getElement()));
        castle.setSrc("images/b_castle.png");
        castle.addClassNames("tech-tree-shortcut-image");
        Image monastery = new Image();
        monastery.addClickListener(event -> monasteryShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", monasteryShortcut.getElement()));
        monastery.setSrc("images/b_monastery.png");
        monastery.addClassNames("tech-tree-shortcut-image");
        Image townCenter = new Image();
        townCenter.addClickListener(event -> townCenterShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", townCenterShortcut.getElement()));
        townCenter.setSrc("images/b_town_center.png");
        townCenter.addClassNames("tech-tree-shortcut-image");
        Image lumberCamp = new Image();
        lumberCamp.addClickListener(event -> lumberCampShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", lumberCampShortcut.getElement()));
        lumberCamp.setSrc("images/b_lumber_camp.png");
        lumberCamp.addClassNames("tech-tree-shortcut-image");
        Image right = new Image();
        right.addClickListener(event -> rightShortcut.getElement().executeJs("$0.scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"center\"});", rightShortcut.getElement()));
        right.setSrc("images/right.png");
        right.addClassNames("tech-tree-shortcut-image");

        shortcutLayout.add(shortcutLabel, left, archeryRange, barracks, stable, siegeWorkshop, blacksmith, dock, university, towers,
                castle, monastery, townCenter, lumberCamp, right);

        shortcutLayout.addClassNames("tech-tree-top-bar-shortcut-layout");
        return shortcutLayout;
    }

    private void setupSelector(){
        civSelector.setRenderer(new ComponentRenderer<>(item-> Utils.getEntityItemRow(item, false, language)));
        civSelector.setItemLabelGenerator(entityElement -> entityElement.getName().getTranslatedString(language));
        civNames = new ArrayList<>(Database.getList(Database.CIVILIZATION_LIST));
        civNames.sort(EntityElement.getAlphabeticalComparator(language));
        civSelector.setItems(civNames);
        civSelector.setValue(Database.getElement(Database.CIVILIZATION_LIST, civID));
        civSelector.addValueChangeListener(event->{
            civID = event.getValue().getId();
            loadCiv();
        });
        loadCiv();

    }

    private void loadCiv(){

        civ = Database.getCivilization(civID);
        civName.setText(civ.getName().getTranslatedString(language));
        civInfoSpan.getElement().setProperty("innerHTML", civ.writeTechTreeInfo(language));
        uniqueUnit.setEntity(Database.getUnit(civ.getUniqueUnitList().get(0)));
        eliteUniqueUnit.setEntity(Database.getUnit(civ.getEliteUniqueUnit(civ.getUniqueUnitList().get(0))));
        castleUniqueTech.setEntity(Database.getTechnology(civ.getUniqueTechList().get(0)));
        imperialUniqueTech.setEntity(Database.getTechnology(civ.getUniqueTechList().get(1)));

        if (civID == 39) mill.setEntity(Database.getBuilding(34));
        else mill.setEntity(Database.getBuilding(4));
        if (civID == 34 || civID == 39) hussar.setEntity(Database.getUnit(154));
        else hussar.setEntity(Database.getUnit(90));


        for (TechTreeBox box: boxSet) {
            box.setAvailable(box.getEntity().isAvailableTo(civID));
            box.setCivID(civID);
        }

        Map<String, String> params = new HashMap<>();
        params.put("language", language);
        params.put("civID", String.valueOf(civID));
        getUI().ifPresent(ui -> ui.navigate(this.getClass(), new RouteParameters(params)));
    }

    private Div shieldDiv(int age){
        String image, name;
        switch (age){
            case 0:
                image = Database.getImage("dark_age_shield");
                name = Database.getString("tt_dark_age", language);
                break;
            case 1:
                image = Database.getImage("feudal_age_shield");
                name = Database.getString("tt_feudal_age", language);
                break;
            case 2:
                image = Database.getImage("castle_age_shield");
                name = Database.getString("tt_castle_age", language);
                break;
            case 3:
                image = Database.getImage("imperial_age_shield");
                name = Database.getString("tt_imperial_age", language);
                break;
            default:
                image = "";
                name = "";
        }

        Image shield = new Image();
        shield.setSrc(image);
        shield.addClassNames("tech-tree-shield-image");
        Label ageName = new Label(name);
        ageName.addClassNames("tech-tree-shield-name");
        Div shieldLayout = new Div(shield, ageName);
        shieldLayout.addClassNames("tech-tree-shield-layout");
        return shieldLayout;
    }


    private Div darkAgeLayout(){
        //ARCHERY RANGE
        Div archeryRangeLayout = new TechTreeConnector(archeryRangeWidth, true);

        //BARRACKS
        TechTreeBox barracks = new TechTreeBox(Database.getBuilding(8), false, language);
        barracks.removeTopLine();

        barracksShortcut = barracks;
        TechTreeBox militia = new TechTreeBox(Database.getUnit(4), false, language);
        militia.setTopRightLine();
        militia.setTopLeftLine();
        TechTreeEmptyBox barracksLine1 = new TechTreeEmptyBox(true);
        barracksLine1.setTopLeftLine();
        barracksLine1.setTopRightLine();
        TechTreeEmptyBox barracksLine2 = new TechTreeEmptyBox(true);
        barracksLine2.setTopLeftLine();
        barracksLine2.setTopRightLine();
        TechTreeEmptyBox barracksLine3 = new TechTreeEmptyBox(true);
        barracksLine3.setTopLeftLine();
        barracksLine3.setTopRightLine();
        TechTreeEmptyBox barracksLine4 = new TechTreeEmptyBox(true);
        barracksLine4.setTopLeftLine();
        barracksLine4.setTopRightLine();
        TechTreeEmptyBox barracksLine5 = new TechTreeEmptyBox(true);
        barracksLine5.setTopLeftLine();
        barracksLine5.setTopRightLine();

        TechTreeSlotLayout barracksLayout = new TechTreeSlotLayout(boxSet);
        barracksLayout.addFistRow(barracks);
        barracksLayout.addSecondRow(militia, barracksLine1, barracksLine2, barracksLine3, barracksLine4, barracksLine5);

        //STABLE
        Div stableLayout = new TechTreeConnector(stableWidth, false);

        //SIEGE WORKSHOP

        Div siegeWorkshopLayout = new Div();
        siegeWorkshopLayout.setWidth(siegeWorkshopWidth + "px");
        siegeWorkshopLayout.setHeight(sectionHeight + "px");

        //BLACKSMITH
        Div blacksmithLayout = new Div();
        blacksmithLayout.setWidth(blacksmithWidth + "px");
        blacksmithLayout.setHeight(sectionHeight + "px");

        //DOCK
        TechTreeBox dock = new TechTreeBox(Database.getBuilding(6), false, language);
        dockShortcut = dock;
        dock.removeTopLine();
        TechTreeBox fishingShip = new TechTreeBox(Database.getUnit(2), false, language);
        fishingShip.setTopRightLine();
        fishingShip.removeBottomLine();
        TechTreeBox transportShip = new TechTreeBox(Database.getUnit(3), false, language);
        transportShip.setTopLeftLine();
        transportShip.setTopRightLine();
        transportShip.removeBottomLine();
        TechTreeEmptyBox dockLine1 = new TechTreeEmptyBox(true);
        dockLine1.setTopLeftLine();
        dockLine1.setTopRightLine();
        TechTreeEmptyBox dockLine2 = new TechTreeEmptyBox(true);
        dockLine2.setTopLeftLine();
        dockLine2.setTopRightLine();
        TechTreeEmptyBox dockLine3 = new TechTreeEmptyBox(true);
        dockLine3.setTopLeftLine();
        dockLine3.setTopRightLine();
        TechTreeEmptyBox dockLine4 = new TechTreeEmptyBox(true);
        dockLine4.setTopLeftLine();
        dockLine4.setTopRightLine();
        TechTreeEmptyBox dockLine5 = new TechTreeEmptyBox(true);
        dockLine5.setTopLeftLine();
        dockLine5.setTopRightLine();
        TechTreeEmptyBox dockLine6 = new TechTreeEmptyBox(true);
        dockLine6.setTopLeftLine();
        dockLine6.setTopRightLine();
        TechTreeEmptyBox dockLine7 = new TechTreeEmptyBox(true);
        dockLine7.setTopLeftLine();
        dockLine7.setTopRightLine();
        TechTreeEmptyBox dockLine8 = new TechTreeEmptyBox(true);
        dockLine8.setTopLeftLine();

        TechTreeSlotLayout dockLayout = new TechTreeSlotLayout(boxSet);
        dockLayout.addFistRow(dock);
        dockLayout.addSecondRow(fishingShip, transportShip, dockLine1, dockLine2, dockLine3, dockLine4, dockLine5, dockLine6, dockLine7, dockLine8);

        //UNIVERSITY
        Div universityLayout = new Div();
        universityLayout.setWidth(universityWidth + "px");
        universityLayout.setHeight(sectionHeight + "px");

        //TOWERS
        TechTreeBox outpost = new TechTreeBox(Database.getBuilding(9), false, language);
        outpost.removeBottomLine();
        outpost.removeTopLine();
        TechTreeBox palisadeWall = new TechTreeBox(Database.getBuilding(10), false, language);
        palisadeWall.removeBottomLine();
        palisadeWall.removeTopLine();
        TechTreeEmptyBox towerLine1 = new TechTreeEmptyBox(false);
        TechTreeBox palisadeGate = new TechTreeBox(Database.getBuilding(11), false, language);
        palisadeGate.removeBottomLine();
        palisadeGate.removeTopLine();
        TechTreeSlotLayout towersLayout = new TechTreeSlotLayout(boxSet);
        towersLayout.addFistRow(outpost, palisadeWall);
        towersLayout.addSecondRow(towerLine1, palisadeGate);

        //CASTLE
        Div castleLayout = new Div();
        castleLayout.setWidth(castleWidth + "px");
        castleLayout.setHeight(sectionHeight + "px");

        //MONASTERY
        Div monasteryLayout = new Div();
        monasteryLayout.setWidth(monasteryWidth + "px");
        monasteryLayout.setHeight(sectionHeight + "px");

        //KREPOST
        Div krepostLayout = new Div();
        krepostLayout.setWidth(columnWidth + "px");
        krepostLayout.setHeight(sectionHeight + "px");

        //DONJON
        Div donjonLayout = new Div();
        donjonLayout.setWidth(donjonWidth + "px");
        donjonLayout.setHeight(donjonWidth + "px");

        //HOUSE WONDER
        TechTreeBox house = new TechTreeBox(Database.getBuilding(2), false, language);
        house.removeBottomLine();
        house.removeTopLine();
        TechTreeEmptyBox houseLine1 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout houseLayout = new TechTreeSlotLayout(boxSet);
        houseLayout.addFistRow(house);
        houseLayout.addSecondRow(houseLine1);

        //TOWN CENTER
        TechTreeBox townCenter = new TechTreeBox(Database.getBuilding(1), false, language);
        townCenterShortcut = townCenter;
        townCenter.removeTopLine();
        TechTreeBox villager = new TechTreeBox(Database.getUnit(1), false, language);
        villager.removeBottomLine();
        villager.setTopRightLine();
        TechTreeBox feudalAge = new TechTreeBox(Database.getTechnology(2), false, language);
        feudalAge.setTopRightLine();
        feudalAge.setTopLeftLine();
        TechTreeBox loom = new TechTreeBox(Database.getTechnology(1), false, language);
        loom.setTopLeftLine();
        loom.removeBottomLine();

        TechTreeSlotLayout townCenterLayout = new TechTreeSlotLayout(boxSet);
        townCenterLayout.addFistRow(townCenter);
        townCenterLayout.addSecondRow(villager, feudalAge, loom);

        //FEITORIA
        Div feitoriaLayout = new Div();
        feitoriaLayout.setWidth(columnWidth + "px");
        feitoriaLayout.setHeight(sectionHeight + "px");

        //CARAVANSERAI
        Div caravanseraiLayout = new Div();
        caravanseraiLayout.setWidth(columnWidth + "px");
        caravanseraiLayout.setHeight(sectionHeight + "px");

        //MINING CAMP
        TechTreeBox miningCamp = new TechTreeBox(Database.getBuilding(5), false, language);
        miningCamp.removeTopLine();
        TechTreeEmptyBox miningCampLine1 = new TechTreeEmptyBox(true);
        miningCampLine1.setTopRightLine();
        TechTreeEmptyBox miningCampLine2 = new TechTreeEmptyBox(true);
        miningCampLine2.setTopLeftLine();
        TechTreeSlotLayout miningCampLayout = new TechTreeSlotLayout(boxSet);
        miningCampLayout.addFistRow(miningCamp);
        miningCampLayout.addSecondRow(miningCampLine1, miningCampLine2);

        //LUMBER CAMP
        TechTreeBox lumberCamp = new TechTreeBox(Database.getBuilding(3), false, language);
        lumberCampShortcut = lumberCamp;
        lumberCamp.removeTopLine();
        TechTreeEmptyBox lumberCampLine1 = new TechTreeEmptyBox(true);
        TechTreeSlotLayout lumberCampLayout = new TechTreeSlotLayout(boxSet);
        lumberCampLayout.addFistRow(lumberCamp);
        lumberCampLayout.addSecondRow(lumberCampLine1);

        //MARKET
        Div marketLayout = new TechTreeConnector(marketWidth, true);

        //MILL
        mill = new TechTreeBox(Database.getBuilding(4), false, language);
        mill.removeTopLine();
        TechTreeEmptyBox millLine1 = new TechTreeEmptyBox(true);
        millLine1.setTopLeftLine();
        millLine1.setTopRightLine();
        TechTreeSlotLayout millLayout = new TechTreeSlotLayout(boxSet);
        millLayout.addFistRow(mill);
        millLayout.addSecondRow(millLine1);

        //FARM
        TechTreeEmptyBox farmLine1 = new TechTreeEmptyBox(false);
        TechTreeBox farm = new TechTreeBox(Database.getBuilding(7), false, language);
        farm.removeBottomLine();
        farm.setTopLeftLine();
        TechTreeSlotLayout farmLayout = new TechTreeSlotLayout(boxSet);
        farmLayout.addFistRow(farmLine1);
        farmLayout.addSecondRow(farm);

        Div shieldLayoutInit = shieldDiv(0), shieldLayoutEnd = shieldDiv(0);
        rightShortcut = shieldLayoutEnd;
        Div container = new Div(shieldLayoutInit, archeryRangeLayout, barracksLayout, stableLayout,
                siegeWorkshopLayout, blacksmithLayout, dockLayout, universityLayout, towersLayout,
                castleLayout, monasteryLayout, krepostLayout, donjonLayout, houseLayout,
                townCenterLayout, feitoriaLayout, caravanseraiLayout, miningCampLayout, lumberCampLayout,
                marketLayout, millLayout, farmLayout, shieldLayoutEnd);
        container.addClassNames("tech-tree-age-container", "tech-tree-dark-background");

        return container;
    }

    private Div feudalAgeLayout(){

        //ARCHERY RANGE
        TechTreeBox archeryRange = new TechTreeBox(Database.getBuilding(14), false, language);
        this.archeryRangeShortcut = archeryRange;
        TechTreeBox archer = new TechTreeBox(Database.getUnit(13), false, language);
        archer.setTopRightLine();
        TechTreeBox skirmisher = new TechTreeBox(Database.getUnit(14), false, language);
        skirmisher.setTopLeftLine();
        skirmisher.setTopRightLine();
        TechTreeEmptyBox rangeLine1 = new TechTreeEmptyBox(true);
        rangeLine1.setTopLeftLine();
        rangeLine1.setTopRightLine();
        TechTreeEmptyBox rangeLine2 = new TechTreeEmptyBox(true);
        rangeLine2.setTopLeftLine();
        rangeLine2.setTopRightLine();
        TechTreeEmptyBox rangeLine3 = new TechTreeEmptyBox(true);
        rangeLine3.setTopLeftLine();
        rangeLine3.setTopRightLine();
        TechTreeEmptyBox rangeLine4 = new TechTreeEmptyBox(true);
        rangeLine4.setTopLeftLine();
        rangeLine4.setTopRightLine();
        TechTreeEmptyBox rangeLine5 = new TechTreeEmptyBox(true);
        rangeLine5.setTopLeftLine();

        TechTreeSlotLayout archeryRangeLayout = new TechTreeSlotLayout(boxSet);
        archeryRangeLayout.addFistRow(archeryRange);
        archeryRangeLayout.addSecondRow(archer, skirmisher, rangeLine1, rangeLine2, rangeLine3, rangeLine4, rangeLine5);

        //BARRACKS
        TechTreeBox manAtArms = new TechTreeBox(Database.getUnit(10), false, language);
        TechTreeBox spearman = new TechTreeBox(Database.getUnit(11), false, language);
        TechTreeBox eagleScout = new TechTreeBox(Database.getUnit(12), false, language);
        TechTreeBox supplies = new TechTreeBox(Database.getTechnology(17), false, language);
        TechTreeEmptyBox barracksLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine2 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine3 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine4 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine5 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine6 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine7 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine8 = new TechTreeEmptyBox(true);

        TechTreeSlotLayout barracksLayout = new TechTreeSlotLayout(boxSet);
        barracksLayout.addFistRow(manAtArms, spearman, eagleScout, supplies, barracksLine1, barracksLine2);
        barracksLayout.addSecondRow(barracksLine3, barracksLine4, barracksLine5, barracksLine6, barracksLine7, barracksLine8);

        //STABLE
        TechTreeBox stable = new TechTreeBox(Database.getBuilding(15), false, language);
        stableShortcut = stable;
        TechTreeBox scout = new TechTreeBox(Database.getUnit(15), false, language);
        scout.setTopRightLine();
        TechTreeBox bloodlines = new TechTreeBox(Database.getTechnology(18), false, language);
        bloodlines.setTopLeftLine();
        bloodlines.setTopRightLine();
        bloodlines.removeBottomLine();
        TechTreeBox camelScout = new TechTreeBox(Database.getUnit(160), true, language);
        camelScout.setTopLeftLine();
        camelScout.setTopRightLine();
        TechTreeEmptyBox stableLine2 = new TechTreeEmptyBox(true);
        stableLine2.setTopLeftLine();
        stableLine2.setTopRightLine();
        TechTreeEmptyBox stableLine3 = new TechTreeEmptyBox(true);
        stableLine3.setTopLeftLine();
        stableLine3.setTopRightLine();
        TechTreeEmptyBox stableLine4 = new TechTreeEmptyBox(true);
        stableLine4.setTopLeftLine();
        stableLine4.setTopRightLine();
        TechTreeEmptyBox stableLine5 = new TechTreeEmptyBox(true);
        stableLine5.setTopLeftLine();

        TechTreeSlotLayout stableLayout = new TechTreeSlotLayout(boxSet);
        stableLayout.addFistRow(stable);
        stableLayout.addSecondRow(scout, bloodlines, camelScout, stableLine2, stableLine3, stableLine4, stableLine5);

        //SIEGE WORKSHOP
        Div siegeWorkshopLayout = new TechTreeConnector(siegeWorkshopWidth, true);

        //BLACKSMITH
        TechTreeBox blacksmith = new TechTreeBox(Database.getBuilding(12), false, language);
        blacksmithShortcut = blacksmith;
        blacksmith.removeTopLine();
        TechTreeBox paddedArcherArmor = new TechTreeBox(Database.getTechnology(15), false, language);
        paddedArcherArmor.setTopRightLine();
        paddedArcherArmor.setTopLeftLine();
        TechTreeBox fletching = new TechTreeBox(Database.getTechnology(14), false, language);
        fletching.setTopRightLine();
        fletching.setTopLeftLine();
        TechTreeBox forging = new TechTreeBox(Database.getTechnology(11), false, language);
        forging.setTopRightLine();
        forging.setTopLeftLine();
        TechTreeBox scaleBardingArmor = new TechTreeBox(Database.getTechnology(13), false, language);
        scaleBardingArmor.setTopRightLine();
        scaleBardingArmor.setTopLeftLine();
        TechTreeBox scaleMailArmor = new TechTreeBox(Database.getTechnology(12), false, language);
        scaleMailArmor.setTopLeftLine();
        TechTreeSlotLayout blacksmithLayout = new TechTreeSlotLayout(boxSet);
        blacksmithLayout.addFistRow(blacksmith);
        blacksmithLayout.addSecondRow(paddedArcherArmor, fletching, forging, scaleBardingArmor, scaleMailArmor);

        //DOCK
        TechTreeBox fireGalley = new TechTreeBox(Database.getUnit(7), false, language);
        fireGalley.removeTopLine();
        TechTreeBox demoRaft = new TechTreeBox(Database.getUnit(8), false, language);
        demoRaft.removeTopLine();
        TechTreeBox galley = new TechTreeBox(Database.getUnit(6), false, language);
        TechTreeBox tradeCog = new TechTreeBox(Database.getUnit(5), false, language);
        tradeCog.removeBottomLine();
        TechTreeEmptyBox dockLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine2 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine3 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine3_1 = new TechTreeEmptyBox(true);
        TechTreeBox fishTrap = new TechTreeBox(Database.getBuilding(19), false, language);
        fishTrap.removeBottomLine();
        TechTreeEmptyBox dockLine4 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine5 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine6 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine7 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine8 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine9 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine10 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine11 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine11_1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine12 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine13 = new TechTreeEmptyBox(true);

        TechTreeSlotLayout dockLayout = new TechTreeSlotLayout(boxSet);
        dockLayout.addFistRow(fireGalley, demoRaft, galley, tradeCog, dockLine1, dockLine2, dockLine3, dockLine3_1, fishTrap, dockLine4);
        dockLayout.addSecondRow(dockLine5, dockLine6, dockLine7, dockLine8, dockLine9, dockLine10, dockLine11, dockLine11_1,dockLine12, dockLine13);

        //UNIVERSITY
        Div universityLayout = new Div();
        universityLayout.setWidth(universityWidth + "px");
        universityLayout.setHeight(sectionHeight + "px");

        //TOWERS
        TechTreeBox watchTower = new TechTreeBox(Database.getBuilding(18), false, language);
        towersShortcut = watchTower;
        watchTower.removeTopLine();
        TechTreeBox stoneGate = new TechTreeBox(Database.getBuilding(17), false, language);
        stoneGate.removeBottomLine();
        stoneGate.removeTopLine();
        TechTreeEmptyBox towerLine1 = new TechTreeEmptyBox(true);
        TechTreeBox stoneWall = new TechTreeBox(Database.getBuilding(16), false, language);
        stoneWall.removeTopLine();
        TechTreeSlotLayout towersLayout = new TechTreeSlotLayout(boxSet);
        towersLayout.addFistRow(watchTower, stoneGate);
        towersLayout.addSecondRow(towerLine1, stoneWall);

        //CASTLE
        Div castleLayout = new Div();
        castleLayout.setWidth(castleWidth + "px");
        castleLayout.setHeight(sectionHeight + "px");

        //MONASTERY
        Div monasteryLayout = new Div();
        monasteryLayout.setWidth(monasteryWidth + "px");
        monasteryLayout.setHeight(sectionHeight + "px");

        //KREPOST
        Div krepostLayout = new Div();
        krepostLayout.setWidth(columnWidth + "px");
        krepostLayout.setHeight(sectionHeight + "px");

        //DONJON
        TechTreeBox donjon = new TechTreeBox(Database.getBuilding(33), false, language);
        donjon.removeTopLine();
        TechTreeBox serjeant = new TechTreeBox(Database.getUnit(150), true, language);
        serjeant.setTopRightLine();
        TechTreeSlotLayout donjonLayout = new TechTreeSlotLayout(boxSet);
        TechTreeBox spearman1 = new TechTreeBox(Database.getUnit(11), false, language);
        spearman1.setTopLeftLine();
        donjonLayout.addFistRow(donjon);
        donjonLayout.addSecondRow(serjeant, spearman1);

        //HOUSE WONDER
        Div houseLayout = new Div();
        houseLayout.setWidth(columnWidth + "px");
        houseLayout.setHeight(sectionHeight + "px");

        //TOWN CENTER
        TechTreeBox townWatch = new TechTreeBox(Database.getTechnology(4), false, language);
        townWatch.removeTopLine();
        TechTreeBox castleAge = new TechTreeBox(Database.getTechnology(19), false, language);
        TechTreeBox wheelbarrow = new TechTreeBox(Database.getTechnology(3), false, language);
        wheelbarrow.removeTopLine();
        TechTreeEmptyBox townCenterLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox townCenterLine2 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox townCenterLine3 = new TechTreeEmptyBox(true);

        TechTreeSlotLayout townCenterLayout = new TechTreeSlotLayout(boxSet);
        townCenterLayout.addFistRow(townWatch, castleAge, wheelbarrow);
        townCenterLayout.addSecondRow(townCenterLine1, townCenterLine2, townCenterLine3);

        //FEITORIA
        Div feitoriaLayout = new Div();
        feitoriaLayout.setWidth(columnWidth + "px");
        feitoriaLayout.setHeight(sectionHeight + "px");

        //CARAVANSERAI
        Div caravanseraiLayout = new Div();
        caravanseraiLayout.setWidth(columnWidth + "px");
        caravanseraiLayout.setHeight(sectionHeight + "px");

        //MINING CAMP
        TechTreeBox goldMining = new TechTreeBox(Database.getTechnology(7), false, language);
        TechTreeBox stoneMining = new TechTreeBox(Database.getTechnology(8), false, language);
        TechTreeEmptyBox miningCampLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox miningCampLine2 = new TechTreeEmptyBox(true);
        TechTreeSlotLayout miningCampLayout = new TechTreeSlotLayout(boxSet);
        miningCampLayout.addFistRow(goldMining, stoneMining);
        miningCampLayout.addSecondRow(miningCampLine1, miningCampLine2);

        //LUMBER CAMP
        TechTreeBox doubleBitAxe = new TechTreeBox(Database.getTechnology(5), false, language);
        TechTreeEmptyBox lumberCampLine1 = new TechTreeEmptyBox(true);
        TechTreeSlotLayout lumberCampLayout = new TechTreeSlotLayout(boxSet);
        lumberCampLayout.addFistRow(doubleBitAxe);
        lumberCampLayout.addSecondRow(lumberCampLine1);

        //MARKET
        TechTreeBox market = new TechTreeBox(Database.getBuilding(13), false, language);
        TechTreeBox tradeCart = new TechTreeBox(Database.getUnit(9), false, language);
        tradeCart.removeBottomLine();
        tradeCart.setTopRightLine();
        TechTreeEmptyBox marketLine1 = new TechTreeEmptyBox(true);
        marketLine1.setTopLeftLine();
        TechTreeSlotLayout marketLayout = new TechTreeSlotLayout(boxSet);
        marketLayout.addFistRow(market);
        marketLayout.addSecondRow(tradeCart, marketLine1);

        //MILL
        TechTreeBox horseCollar = new TechTreeBox(Database.getTechnology(6), false, language);
        TechTreeEmptyBox millLine1 = new TechTreeEmptyBox(true);
        TechTreeSlotLayout millLayout = new TechTreeSlotLayout(boxSet);
        millLayout.addFistRow(horseCollar);
        millLayout.addSecondRow(millLine1);

        //FARM
        Div farmLayout = new Div();
        farmLayout.setWidth(columnWidth + "px");
        farmLayout.setHeight(sectionHeight + "px");

        Div shieldLayoutInit = shieldDiv(1), shieldLayoutEnd = shieldDiv(1);
        Div container = new Div(shieldLayoutInit, archeryRangeLayout, barracksLayout, stableLayout,
                siegeWorkshopLayout, blacksmithLayout, dockLayout, universityLayout, towersLayout,
                castleLayout, monasteryLayout, krepostLayout, donjonLayout, houseLayout,
                townCenterLayout, feitoriaLayout, caravanseraiLayout, miningCampLayout, lumberCampLayout,
                marketLayout, millLayout, farmLayout, shieldLayoutEnd);
        container.addClassNames("tech-tree-age-container", "tech-tree-feudal-background");

        return container;
    }

    private Div castleAgeLayout(){

        //ARCHERY RANGE
        TechTreeBox crossbowman = new TechTreeBox(Database.getUnit(27), false, language);
        TechTreeBox eliteSkirmisher = new TechTreeBox(Database.getUnit(28), false, language);
        TechTreeBox cavArcher = new TechTreeBox(Database.getUnit(29), false, language);
        TechTreeBox elephantArcher = new TechTreeBox(Database.getUnit(161), false, language);
        TechTreeBox slinger = new TechTreeBox(Database.getUnit(31), true, language);
        slinger.removeBottomLine();
        TechTreeBox genitour = new TechTreeBox(Database.getUnit(30), true, language);
        TechTreeBox thumbRing = new TechTreeBox(Database.getTechnology(56), false, language);
        thumbRing.removeBottomLine();
        TechTreeEmptyBox rangeLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox rangeLine2 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox rangeLine3 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox rangeLine3_1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox rangeLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox rangeLine5 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox rangeLine6 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout archeryRangeLayout = new TechTreeSlotLayout(boxSet);
        archeryRangeLayout.addFistRow(crossbowman, eliteSkirmisher, cavArcher, elephantArcher, slinger, genitour, thumbRing);
        archeryRangeLayout.addSecondRow(rangeLine1, rangeLine2, rangeLine3, rangeLine3_1, rangeLine4, rangeLine5, rangeLine6);

        //BARRACKS

        TechTreeBox longSwordsman = new TechTreeBox(Database.getUnit(24), false, language);
        TechTreeBox pikeman = new TechTreeBox(Database.getUnit(25), false, language);
        TechTreeBox eagleWarrior = new TechTreeBox(Database.getUnit(26), false, language);
        TechTreeBox gambesons = new TechTreeBox(Database.getTechnology(244), false, language);
        gambesons.removeBottomLine();
        TechTreeBox squires = new TechTreeBox(Database.getTechnology(52), false, language);
        squires.removeBottomLine();
        TechTreeBox arson = new TechTreeBox(Database.getTechnology(53), false, language);
        arson.removeBottomLine();
        TechTreeEmptyBox barracksLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine2 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine3 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox barracksLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox barracksLine5 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox barracksLine6 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout barracksLayout = new TechTreeSlotLayout(boxSet);
        barracksLayout.addFistRow(longSwordsman, pikeman, eagleWarrior, gambesons, squires, arson);
        barracksLayout.addSecondRow(barracksLine1, barracksLine2, barracksLine3, barracksLine4, barracksLine5, barracksLine6);

        //STABLE

        TechTreeBox lightCav = new TechTreeBox(Database.getUnit(32), false, language);
        TechTreeBox knight = new TechTreeBox(Database.getUnit(33), false, language);
        knight.removeTopLine();
        TechTreeBox camel = new TechTreeBox(Database.getUnit(34), false, language);
        TechTreeBox battleElephant = new TechTreeBox(Database.getUnit(35), false, language);
        TechTreeBox steppeLancer = new TechTreeBox(Database.getUnit(135), false, language);
        TechTreeBox shrivamshaRider = new TechTreeBox(Database.getUnit(162), true, language);
        TechTreeBox husbandry = new TechTreeBox(Database.getTechnology(58), false, language);
        husbandry.removeBottomLine();
        TechTreeEmptyBox stableLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox stableLine2 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox stableLine3 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox stableLine4 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox stableLine5 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox stableLine5_1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox stableLine6 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout stableLayout = new TechTreeSlotLayout(boxSet);
        stableLayout.addFistRow(lightCav, knight, camel, battleElephant, steppeLancer, shrivamshaRider, husbandry);
        stableLayout.addSecondRow(stableLine1, stableLine2, stableLine3, stableLine4, stableLine5, stableLine5_1, stableLine6);

        //SIEGE WORKSHOP
        TechTreeBox siegeWorkshop = new TechTreeBox(Database.getBuilding(22), false, language);
        siegeWorkshopShortcut = siegeWorkshop;
        TechTreeBox batteringRam = new TechTreeBox(Database.getUnit(36), false, language);
        batteringRam.setTopRightLine();
        TechTreeBox armoredElephant = new TechTreeBox(Database.getUnit(163), false, language);
        armoredElephant.setTopLeftLine();
        armoredElephant.setTopRightLine();
        TechTreeBox mangonel = new TechTreeBox(Database.getUnit(37), false, language);
        mangonel.setTopLeftLine();
        mangonel.setTopRightLine();
        TechTreeBox scorpion = new TechTreeBox(Database.getUnit(38), false, language);
        scorpion.setTopLeftLine();
        scorpion.setTopRightLine();
        TechTreeBox siegeTower = new TechTreeBox(Database.getUnit(39), false, language);
        siegeTower.setTopLeftLine();
        siegeTower.removeBottomLine();

        TechTreeSlotLayout siegeWorkshopLayout = new TechTreeSlotLayout(boxSet);
        siegeWorkshopLayout.addFistRow(siegeWorkshop);
        siegeWorkshopLayout.addSecondRow(batteringRam, armoredElephant, mangonel, scorpion, siegeTower);

        //BLACKSMITH
        TechTreeBox leatherArcherArmor = new TechTreeBox(Database.getTechnology(35), false, language);
        TechTreeBox bodkinArrow = new TechTreeBox(Database.getTechnology(34), false, language);
        TechTreeBox ironCasting = new TechTreeBox(Database.getTechnology(31), false, language);
        TechTreeBox chainBardingArmor = new TechTreeBox(Database.getTechnology(33), false, language);
        TechTreeBox chainMailArmor = new TechTreeBox(Database.getTechnology(32), false, language);
        TechTreeEmptyBox blacksmithLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox blacksmithLine2 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox blacksmithLine3= new TechTreeEmptyBox(true);
        TechTreeEmptyBox blacksmithLine4 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox blacksmithLine5 = new TechTreeEmptyBox(true);

        TechTreeSlotLayout blacksmithLayout = new TechTreeSlotLayout(boxSet);
        blacksmithLayout.addFistRow(leatherArcherArmor, bodkinArrow, ironCasting, chainBardingArmor, chainMailArmor);
        blacksmithLayout.addSecondRow(blacksmithLine1, blacksmithLine2, blacksmithLine3, blacksmithLine4, blacksmithLine5);

        //DOCK
        TechTreeBox fireShip = new TechTreeBox(Database.getUnit(17), false, language);
        TechTreeBox demoShip = new TechTreeBox(Database.getUnit(18), false, language);
        TechTreeBox warGalley = new TechTreeBox(Database.getUnit(16), false, language);
        TechTreeEmptyBox dockLine1 = new TechTreeEmptyBox(false);
        TechTreeBox longboat = new TechTreeBox(Database.getUnit(19), true, language);
        TechTreeBox turtleShip = new TechTreeBox(Database.getUnit(20), true, language);
        TechTreeBox caravel = new TechTreeBox(Database.getUnit(21), true, language);
        TechTreeEmptyBox dockLine1_1 = new TechTreeEmptyBox(true);
        TechTreeBox gillnets = new TechTreeBox(Database.getTechnology(26), false, language);
        gillnets.removeBottomLine();
        gillnets.removeTopLine();
        TechTreeBox careening = new TechTreeBox(Database.getTechnology(27), false, language);
        TechTreeEmptyBox dockLine2 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine3 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine4 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine5 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine6 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine7 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine8 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine8_1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox dockLine9 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine10 = new TechTreeEmptyBox(true);

        TechTreeSlotLayout dockLayout = new TechTreeSlotLayout(boxSet);
        dockLayout.addFistRow(fireShip, demoShip, warGalley, dockLine1, longboat, turtleShip, caravel, dockLine1_1, gillnets, careening);
        dockLayout.addSecondRow(dockLine2, dockLine3, dockLine4, dockLine5, dockLine6, dockLine7, dockLine8, dockLine8_1, dockLine9, dockLine10);

        //UNIVERSITY
        TechTreeBox university = new TechTreeBox(Database.getBuilding(21), false, language);
        universityShortcut = university;
        university.removeTopLine();
        TechTreeBox masonry = new TechTreeBox(Database.getTechnology(42), false, language);
        masonry.setTopRightLine();
        TechTreeBox fortifiedWall = new TechTreeBox(Database.getTechnology(48), false, language);
        fortifiedWall.removeBottomLine();
        fortifiedWall.setTopRightLine();
        fortifiedWall.setTopLeftLine();
        TechTreeBox ballistics = new TechTreeBox(Database.getTechnology(45), false, language);
        ballistics.removeBottomLine();
        ballistics.setTopRightLine();
        ballistics.setTopLeftLine();
        TechTreeBox guardTower = new TechTreeBox(Database.getTechnology(47), false, language);
        guardTower.setTopRightLine();
        guardTower.setTopLeftLine();
        TechTreeBox headedShot = new TechTreeBox(Database.getTechnology(44), false, language);
        headedShot.removeBottomLine();
        headedShot.setTopRightLine();
        headedShot.setTopLeftLine();
        TechTreeBox murderHoles = new TechTreeBox(Database.getTechnology(46), false, language);
        murderHoles.removeBottomLine();
        murderHoles.setTopRightLine();
        murderHoles.setTopLeftLine();
        TechTreeBox treadmillCrane = new TechTreeBox(Database.getTechnology(43), false, language);
        treadmillCrane.removeBottomLine();
        treadmillCrane.setTopLeftLine();

        TechTreeSlotLayout universityLayout = new TechTreeSlotLayout(boxSet);
        universityLayout.addFistRow(university);
        universityLayout.addSecondRow(masonry, fortifiedWall, ballistics, guardTower, headedShot, murderHoles, treadmillCrane);

        //TOWERS
        TechTreeBox guardTowerB = new TechTreeBox(Database.getBuilding(25), false, language);
        TechTreeBox fortifiedWallB = new TechTreeBox(Database.getBuilding(23), false, language);
        fortifiedWallB.removeBottomLine();
        TechTreeEmptyBox towerLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox towerLine2 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout towersLayout = new TechTreeSlotLayout(boxSet);
        towersLayout.addFistRow(guardTowerB, fortifiedWallB);
        towersLayout.addSecondRow(towerLine1, towerLine2);

        //CASTLE
        TechTreeBox castle = new TechTreeBox(Database.getBuilding(26), false, language);
        castleShortcut = castle;
        castle.removeTopLine();
        TechTreeBox uniqueUnit = new TechTreeBox(Database.getUnit(41), true, language);
        this.uniqueUnit = uniqueUnit;
        uniqueUnit.setTopRightLine();
        TechTreeBox petard = new TechTreeBox(Database.getUnit(40), false, language);
        petard.removeBottomLine();
        petard.setTopRightLine();
        petard.setTopLeftLine();
        TechTreeEmptyBox castleLine1 = new TechTreeEmptyBox(true);
        castleLine1.setTopRightLine();
        castleLine1.setTopLeftLine();
        TechTreeBox castleUniqueTech = new TechTreeBox(Database.getTechnology(59), false, language);
        this.castleUniqueTech = castleUniqueTech;
        castleUniqueTech.setTopRightLine();
        castleUniqueTech.setTopLeftLine();
        castleUniqueTech.removeBottomLine();
        TechTreeEmptyBox castleLine2 = new TechTreeEmptyBox(true);
        castleLine2.setTopRightLine();
        castleLine2.setTopLeftLine();
        TechTreeEmptyBox castleLine3 = new TechTreeEmptyBox(true);
        castleLine3.setTopRightLine();
        castleLine3.setTopLeftLine();
        TechTreeEmptyBox castleLine4 = new TechTreeEmptyBox(true);
        castleLine4.setTopRightLine();
        castleLine4.setTopLeftLine();
        TechTreeEmptyBox castleLine5 = new TechTreeEmptyBox(true);
        castleLine5.setTopLeftLine();

        TechTreeSlotLayout castleLayout = new TechTreeSlotLayout(boxSet);
        castleLayout.addFistRow(castle);
        castleLayout.addSecondRow(uniqueUnit, petard, castleLine1, castleUniqueTech, castleLine2, castleLine3, castleLine4, castleLine5);

        //MONASTERY
        TechTreeBox monastery = new TechTreeBox(Database.getBuilding(20), false, language);
        monasteryShortcut = monastery;
        monastery.removeTopLine();
        TechTreeBox monk = new TechTreeBox(Database.getUnit(22), false, language);
        monk.removeBottomLine();
        monk.setTopRightLine();
        TechTreeBox missionary = new TechTreeBox(Database.getUnit(23), true, language);
        missionary.removeBottomLine();
        missionary.setTopRightLine();
        missionary.setTopLeftLine();
        TechTreeBox redemption = new TechTreeBox(Database.getTechnology(36), false, language);
        redemption.removeBottomLine();
        redemption.setTopRightLine();
        redemption.setTopLeftLine();
        TechTreeBox atonement = new TechTreeBox(Database.getTechnology(37), false, language);
        atonement.removeBottomLine();
        atonement.setTopRightLine();
        atonement.setTopLeftLine();
        TechTreeBox herbalMedicine = new TechTreeBox(Database.getTechnology(41), false, language);
        herbalMedicine.removeBottomLine();
        herbalMedicine.setTopRightLine();
        herbalMedicine.setTopLeftLine();
        TechTreeBox heresy = new TechTreeBox(Database.getTechnology(40), false, language);
        heresy.removeBottomLine();
        heresy.setTopRightLine();
        heresy.setTopLeftLine();
        TechTreeBox sanctity = new TechTreeBox(Database.getTechnology(39), false, language);
        sanctity.removeBottomLine();
        sanctity.setTopRightLine();
        sanctity.setTopLeftLine();
        TechTreeBox fervor = new TechTreeBox(Database.getTechnology(38), false, language);
        fervor.removeBottomLine();
        fervor.setTopLeftLine();

        TechTreeSlotLayout monasteryLayout = new TechTreeSlotLayout(boxSet);
        monasteryLayout.addFistRow(monastery);
        monasteryLayout.addSecondRow(monk, missionary, redemption, atonement, herbalMedicine, heresy, sanctity, fervor);

        //KREPOST
        TechTreeBox krepost = new TechTreeBox(Database.getBuilding(32), false, language);
        krepost.removeTopLine();
        TechTreeBox konnik = new TechTreeBox(Database.getUnit(136), true, language);
        TechTreeSlotLayout krepostLayout = new TechTreeSlotLayout(boxSet);
        krepostLayout.addFistRow(krepost);
        krepostLayout.addSecondRow(konnik);

        //DONJON
        TechTreeEmptyBox donjonLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox donjonLine2 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox donjonLine3 = new TechTreeEmptyBox(true);
        TechTreeBox pikeman1 = new TechTreeBox(Database.getUnit(25), false, language);
        TechTreeSlotLayout donjonLayout = new TechTreeSlotLayout(boxSet);
        donjonLayout.addFistRow(donjonLine1, pikeman1);
        donjonLayout.addSecondRow(donjonLine2, donjonLine3);

        //HOUSE WONDER
        Div houseLayout = new Div();
        houseLayout.setWidth(columnWidth + "px");
        houseLayout.setHeight(sectionHeight + "px");

        //TOWN CENTER
        TechTreeBox townPatrol = new TechTreeBox(Database.getTechnology(21), false, language);
        townPatrol.removeBottomLine();
        TechTreeBox imperialAge = new TechTreeBox(Database.getTechnology(19), false, language);
        imperialAge.removeBottomLine();
        TechTreeBox handCart = new TechTreeBox(Database.getTechnology(20), false, language);
        handCart.removeBottomLine();
        TechTreeEmptyBox townCenterLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox townCenterLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox townCenterLine3 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout townCenterLayout = new TechTreeSlotLayout(boxSet);
        townCenterLayout.addFistRow(townPatrol, imperialAge, handCart);
        townCenterLayout.addSecondRow(townCenterLine1, townCenterLine2, townCenterLine3);

        //FEITORIA
        TechTreeBox townCenter2 = new TechTreeBox(Database.getBuilding(1), false, language);
        townCenter2.removeTopLine();
        townCenter2.removeBottomLine();
        TechTreeEmptyBox feitoriaLine1 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout feitoriaLayout = new TechTreeSlotLayout(boxSet);
        feitoriaLayout.addFistRow(townCenter2);
        feitoriaLayout.addSecondRow(feitoriaLine1);

        //CARAVANSERAI
        Div caravanseraiLayout = new Div();
        caravanseraiLayout.setWidth(columnWidth + "px");
        caravanseraiLayout.setHeight(sectionHeight + "px");

        //MINING CAMP
        TechTreeBox goldShaftMining = new TechTreeBox(Database.getTechnology(24), false, language);
        goldShaftMining.removeBottomLine();
        TechTreeBox stoneShaftMining = new TechTreeBox(Database.getTechnology(25), false, language);
        stoneShaftMining.removeBottomLine();
        TechTreeEmptyBox miningCampLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox miningCampLine2 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout miningCampLayout = new TechTreeSlotLayout(boxSet);
        miningCampLayout.addFistRow(goldShaftMining, stoneShaftMining);
        miningCampLayout.addSecondRow(miningCampLine1, miningCampLine2);

        //LUMBER CAMP
        TechTreeBox bowSaw = new TechTreeBox(Database.getTechnology(22), false, language);
        TechTreeEmptyBox lumberCampLine1 = new TechTreeEmptyBox(true);
        TechTreeSlotLayout lumberCampLayout = new TechTreeSlotLayout(boxSet);
        lumberCampLayout.addFistRow(bowSaw);
        lumberCampLayout.addSecondRow(lumberCampLine1);

        //MARKET
        TechTreeBox coinage = new TechTreeBox(Database.getTechnology(10), false, language);
        coinage.removeTopLine();
        TechTreeBox caravan = new TechTreeBox(Database.getTechnology(29), false, language);
        caravan.removeBottomLine();
        TechTreeEmptyBox marketLine1 = new TechTreeEmptyBox(true);
        TechTreeEmptyBox marketLine2 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout marketLayout = new TechTreeSlotLayout(boxSet);
        marketLayout.addFistRow(coinage, caravan);
        marketLayout.addSecondRow(marketLine1, marketLine2);

        //MILL
        TechTreeBox heavyPlow = new TechTreeBox(Database.getTechnology(23), false, language);
        TechTreeEmptyBox millLine1 = new TechTreeEmptyBox(true);
        TechTreeSlotLayout millLayout = new TechTreeSlotLayout(boxSet);
        millLayout.addFistRow(heavyPlow);
        millLayout.addSecondRow(millLine1);

        //FARM
        Div farmLayout = new Div();
        farmLayout.setWidth(columnWidth + "px");
        farmLayout.setHeight(sectionHeight + "px");

        Div shieldLayoutInit = shieldDiv(2), shieldLayoutEnd = shieldDiv(2);
        Div container = new Div(shieldLayoutInit, archeryRangeLayout, barracksLayout, stableLayout,
                siegeWorkshopLayout, blacksmithLayout, dockLayout, universityLayout, towersLayout,
                castleLayout, monasteryLayout, krepostLayout, donjonLayout, houseLayout,
                townCenterLayout, feitoriaLayout, caravanseraiLayout, miningCampLayout, lumberCampLayout,
                marketLayout, millLayout, farmLayout, shieldLayoutEnd);
        container.addClassNames("tech-tree-age-container", "tech-tree-castle-background");

        return container;
    }

    private Div imperialAgeLayout(){

        //ARCHERY RANGE
        TechTreeBox arbalester = new TechTreeBox(Database.getUnit(85), false, language);
        arbalester.removeBottomLine();
        TechTreeBox imperialSkirmisher = new TechTreeBox(Database.getUnit(86), false, language);
        imperialSkirmisher.removeBottomLine();
        TechTreeBox heavyCavArcher = new TechTreeBox(Database.getUnit(87), false, language);
        heavyCavArcher.removeBottomLine();
        TechTreeBox eliteElefantArcher = new TechTreeBox(Database.getUnit(169), false, language);
        eliteElefantArcher.removeBottomLine();
        TechTreeBox handCannoneer = new TechTreeBox(Database.getUnit(88), false, language);
        handCannoneer.removeTopLine();
        handCannoneer.removeBottomLine();
        TechTreeBox eliteGenitour = new TechTreeBox(Database.getUnit(89), true, language);
        eliteGenitour.removeBottomLine();
        TechTreeBox parthianTactics = new TechTreeBox(Database.getTechnology(127), false, language);
        parthianTactics.removeTopLine();
        parthianTactics.removeBottomLine();

        TechTreeEmptyBox rangeLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox rangeLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox rangeLine3 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox rangeLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox rangeLine5 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox rangeLine6 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox rangeLine7 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout archeryRangeLayout = new TechTreeSlotLayout(boxSet);
        archeryRangeLayout.addFistRow(arbalester, imperialSkirmisher, heavyCavArcher, eliteElefantArcher, handCannoneer, eliteGenitour, parthianTactics);
        archeryRangeLayout.addSecondRow(rangeLine1, rangeLine2, rangeLine3, rangeLine4, rangeLine5, rangeLine6, rangeLine7);

        //BARRACKS

        TechTreeBox twoHandedSwordsman = new TechTreeBox(Database.getUnit(80), false, language);
        TechTreeBox halberdier = new TechTreeBox(Database.getUnit(82), false, language);
        halberdier.removeBottomLine();
        TechTreeBox eliteEagleWarrior = new TechTreeBox(Database.getUnit(83), false, language);
        eliteEagleWarrior.removeBottomLine();
        TechTreeBox condottiero = new TechTreeBox(Database.getUnit(84), true, language);
        condottiero.removeTopLine();
        condottiero.removeBottomLine();
        TechTreeBox champion = new TechTreeBox(Database.getUnit(81), false, language);
        champion.removeBottomLine();
        TechTreeBox flemishMilitia = new TechTreeBox(Database.getUnit(153), true, language);
        flemishMilitia.removeBottomLine();
        flemishMilitia.removeTopLine();
        TechTreeEmptyBox barracksLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox barracksLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox barracksLine3 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox barracksLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox barracksLine5 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox barracksLine6 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout barracksLayout = new TechTreeSlotLayout(boxSet);
        barracksLayout.addFistRow(twoHandedSwordsman, halberdier, eliteEagleWarrior, condottiero, flemishMilitia, barracksLine1);
        barracksLayout.addSecondRow(champion, barracksLine2, barracksLine3, barracksLine4, barracksLine5, barracksLine6);

        //STABLE
        hussar = new TechTreeBox(Database.getUnit(90), false, language);
        hussar.removeBottomLine();
        TechTreeBox cavalier = new TechTreeBox(Database.getUnit(91), false, language);
        TechTreeBox heavyCamel = new TechTreeBox(Database.getUnit(93), false, language);
        TechTreeBox eliteBattleElephant = new TechTreeBox(Database.getUnit(95), false, language);
        eliteBattleElephant.removeBottomLine();
        TechTreeBox eliteSteppeLancer = new TechTreeBox(Database.getUnit(141), false, language);
        eliteSteppeLancer.removeBottomLine();
        TechTreeBox eliteShrivamshaRider = new TechTreeBox(Database.getUnit(170), false, language);
        eliteShrivamshaRider.removeBottomLine();
        TechTreeBox paladin = new TechTreeBox(Database.getUnit(92), false, language);
        paladin.removeBottomLine();
        TechTreeBox imperialCamel = new TechTreeBox(Database.getUnit(94), true, language);
        imperialCamel.removeBottomLine();
        TechTreeEmptyBox stableLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox stableLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox stableLine3 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox stableLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox stableLine5 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox stableLine6 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout stableLayout = new TechTreeSlotLayout(boxSet);
        stableLayout.addFistRow(hussar, cavalier, heavyCamel, eliteBattleElephant, eliteSteppeLancer,  eliteShrivamshaRider, stableLine1);
        stableLayout.addSecondRow(stableLine2, paladin, imperialCamel, stableLine3, stableLine4, stableLine5, stableLine6);

        //SIEGE WORKSHOP
        TechTreeBox cappedRam = new TechTreeBox(Database.getUnit(96), false, language);
        TechTreeBox siegeElephant = new TechTreeBox(Database.getUnit(171), false, language);
        siegeElephant.removeBottomLine();
        TechTreeBox onager = new TechTreeBox(Database.getUnit(98), false, language);
        TechTreeBox heavyScorpion = new TechTreeBox(Database.getUnit(100), false, language);
        heavyScorpion.removeBottomLine();
        TechTreeBox bombardCannon = new TechTreeBox(Database.getUnit(101), false, language);
        bombardCannon.removeTopLine();
        TechTreeBox siegeRam = new TechTreeBox(Database.getUnit(97), false, language);
        siegeRam.removeBottomLine();
        TechTreeEmptyBox siegeWorkshopLine0 = new TechTreeEmptyBox(false);
        TechTreeBox siegeOnager = new TechTreeBox(Database.getUnit(99), false, language);
        siegeOnager.removeBottomLine();
        TechTreeEmptyBox siegeWorkshopLine1 = new TechTreeEmptyBox(false);
        TechTreeBox houfnice = new TechTreeBox(Database.getUnit(159), true, language);
        houfnice.removeBottomLine();

        TechTreeSlotLayout siegeWorkshopLayout = new TechTreeSlotLayout(boxSet);
        siegeWorkshopLayout.addFistRow(cappedRam, siegeElephant, onager, heavyScorpion, bombardCannon);
        siegeWorkshopLayout.addSecondRow(siegeRam,siegeWorkshopLine0, siegeOnager, siegeWorkshopLine1, houfnice);

        //BLACKSMITH
        TechTreeBox ringArcherArmor = new TechTreeBox(Database.getTechnology(108), false, language);
        ringArcherArmor.removeBottomLine();
        TechTreeBox bracer = new TechTreeBox(Database.getTechnology(107), false, language);
        bracer.removeBottomLine();
        TechTreeBox blastFurnace = new TechTreeBox(Database.getTechnology(104), false, language);
        blastFurnace.removeBottomLine();
        TechTreeBox plateBardingArmor = new TechTreeBox(Database.getTechnology(106), false, language);
        plateBardingArmor.removeBottomLine();
        TechTreeBox plateMailArmor = new TechTreeBox(Database.getTechnology(105), false, language);
        plateMailArmor.removeBottomLine();
        TechTreeEmptyBox blacksmithLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox blacksmithLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox blacksmithLine3= new TechTreeEmptyBox(false);
        TechTreeEmptyBox blacksmithLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox blacksmithLine5 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout blacksmithLayout = new TechTreeSlotLayout(boxSet);
        blacksmithLayout.addFistRow(ringArcherArmor, bracer, blastFurnace, plateBardingArmor, plateMailArmor);
        blacksmithLayout.addSecondRow(blacksmithLine1, blacksmithLine2, blacksmithLine3, blacksmithLine4, blacksmithLine5);

        //DOCK
        TechTreeBox fastFireShip = new TechTreeBox(Database.getUnit(73), false, language);
        fastFireShip.removeBottomLine();
        TechTreeBox heavyDemoShip = new TechTreeBox(Database.getUnit(74), false, language);
        heavyDemoShip.removeBottomLine();
        TechTreeBox galleon = new TechTreeBox(Database.getUnit(72), false, language);
        galleon.removeBottomLine();
        TechTreeBox cannonGalleon = new TechTreeBox(Database.getUnit(75), false, language);
        cannonGalleon.removeTopLine();
        TechTreeBox eliteLongboat = new TechTreeBox(Database.getUnit(77), true, language);
        eliteLongboat.removeBottomLine();
        TechTreeBox eliteTurtleShip = new TechTreeBox(Database.getUnit(78), true, language);
        eliteTurtleShip.removeBottomLine();
        TechTreeBox eliteCaravel = new TechTreeBox(Database.getUnit(79), true, language);
        eliteCaravel.removeBottomLine();
        TechTreeBox thiridasai = new TechTreeBox(Database.getUnit(168), true, language);
        thiridasai.removeBottomLine();
        TechTreeBox shipwright = new TechTreeBox(Database.getTechnology(94), false, language);
        shipwright.removeBottomLine();
        shipwright.removeTopLine();
        TechTreeBox dryDock = new TechTreeBox(Database.getTechnology(93), false, language);
        dryDock.removeBottomLine();
        TechTreeEmptyBox dockLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine3 = new TechTreeEmptyBox(false);
        TechTreeBox eliteCannonGalleon = new TechTreeBox(Database.getUnit(76), false, language);
        eliteCannonGalleon.removeBottomLine();
        TechTreeEmptyBox dockLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine5 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine6 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine7 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine8 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox dockLine9 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout dockLayout = new TechTreeSlotLayout(boxSet);
        dockLayout.addFistRow(fastFireShip, heavyDemoShip, galleon, cannonGalleon, eliteLongboat, eliteTurtleShip, eliteCaravel, thiridasai, shipwright, dryDock);
        dockLayout.addSecondRow(dockLine1, dockLine2, dockLine3, eliteCannonGalleon, dockLine4, dockLine5, dockLine6, dockLine7, dockLine8, dockLine9);

        TechTreeBox architecture = new TechTreeBox(Database.getTechnology(113), false, language);
        architecture.removeBottomLine();
        TechTreeBox chemistry = new TechTreeBox(Database.getTechnology(114), false, language);
        chemistry.removeTopLine();
        TechTreeBox siegeEngineers = new TechTreeBox(Database.getTechnology(115), false, language);
        siegeEngineers.removeTopLine();
        siegeEngineers.removeBottomLine();

        TechTreeBox keep = new TechTreeBox(Database.getTechnology(117), false, language);
        keep.removeBottomLine();
        TechTreeBox arrowslits = new TechTreeBox(Database.getTechnology(116), false, language);
        arrowslits.removeBottomLine();
        arrowslits.removeTopLine();
        TechTreeEmptyBox universityLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox universityLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox universityLine3 = new TechTreeEmptyBox(false);
        TechTreeBox bombardTower = new TechTreeBox(Database.getTechnology(118), false, language);
        bombardTower.removeBottomLine();
        TechTreeEmptyBox universityLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox universityLine5 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox universityLine6 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox universityLine7 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox universityLine8 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout universityLayout = new TechTreeSlotLayout(boxSet);
        universityLayout.addFistRow(architecture, chemistry, siegeEngineers, keep, arrowslits, universityLine1, universityLine2);
        universityLayout.addSecondRow(universityLine3, bombardTower, universityLine4, universityLine5, universityLine6, universityLine7, universityLine8);

        //TOWERS
        TechTreeBox keepB = new TechTreeBox(Database.getBuilding(30), false, language);
        keepB.removeBottomLine();
        TechTreeEmptyBox towerLine1 = new TechTreeEmptyBox(false);
        TechTreeBox bombardTowerB = new TechTreeBox(Database.getBuilding(31), false, language);
        bombardTowerB.removeBottomLine();
        bombardTowerB.removeTopLine();
        TechTreeEmptyBox towerLine2 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout towersLayout = new TechTreeSlotLayout(boxSet);
        towersLayout.addFistRow(keepB, towerLine1);
        towersLayout.addSecondRow(bombardTowerB, towerLine2);

        //CASTLE
        TechTreeBox eliteUniqueUnit = new TechTreeBox(Database.getUnit(104), true, language);
        this.eliteUniqueUnit = eliteUniqueUnit;
        eliteUniqueUnit.removeBottomLine();
        TechTreeBox trebuchet = new TechTreeBox(Database.getUnit(102), false, language);
        trebuchet.removeBottomLine();
        trebuchet.removeTopLine();
        TechTreeBox flamingCamel = new TechTreeBox(Database.getUnit(148), true, language);
        flamingCamel.removeBottomLine();
        TechTreeBox imperialUniqueTech = new TechTreeBox(Database.getTechnology(174), false, language);
        this.imperialUniqueTech = imperialUniqueTech;
        imperialUniqueTech.removeBottomLine();
        imperialUniqueTech.removeTopLine();
        TechTreeBox hoardings = new TechTreeBox(Database.getTechnology(139), false, language);
        hoardings.removeBottomLine();
        TechTreeBox sappers = new TechTreeBox(Database.getTechnology(140), false, language);
        sappers.removeBottomLine();
        TechTreeBox conscription = new TechTreeBox(Database.getTechnology(141), false, language);
        conscription.removeBottomLine();
        TechTreeBox spies = new TechTreeBox(Database.getTechnology(142), false, language);
        spies.removeBottomLine();
        TechTreeEmptyBox castleLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox castleLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox castleLine3 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox castleLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox castleLine5 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox castleLine6 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox castleLine7 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox castleLine8 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout castleLayout = new TechTreeSlotLayout(boxSet);
        castleLayout.addFistRow(eliteUniqueUnit, trebuchet, flamingCamel, imperialUniqueTech, hoardings, sappers, conscription, spies);
        castleLayout.addSecondRow(castleLine1, castleLine2, castleLine3, castleLine4, castleLine5, castleLine6, castleLine7, castleLine8);

        //MONASTERY
        TechTreeBox faith = new TechTreeBox(Database.getTechnology(109), false, language);
        faith.removeBottomLine();
        faith.removeTopLine();

        TechTreeBox illumination = new TechTreeBox(Database.getTechnology(110), false, language);
        illumination.removeBottomLine();
        illumination.removeTopLine();

        TechTreeBox blockPrinting = new TechTreeBox(Database.getTechnology(111), false, language);
        blockPrinting.removeBottomLine();
        blockPrinting.removeTopLine();

        TechTreeBox theocracy = new TechTreeBox(Database.getTechnology(112), false, language);
        theocracy.removeBottomLine();
        theocracy.removeTopLine();
        TechTreeEmptyBox monasteryLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox monasteryLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox monasteryLine3 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox monasteryLine4 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout monasteryLayout = new TechTreeSlotLayout(boxSet);
        monasteryLayout.addFistRow(faith, illumination, blockPrinting, theocracy, monasteryLine1, monasteryLine2, monasteryLine3, monasteryLine4);

        //KREPOST
        TechTreeBox eliteKonnik = new TechTreeBox(Database.getUnit(142), true, language);
        eliteKonnik.removeBottomLine();
        TechTreeEmptyBox krepostLine1 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout krepostLayout = new TechTreeSlotLayout(boxSet);
        krepostLayout.addFistRow(eliteKonnik);
        krepostLayout.addSecondRow(krepostLine1);

        //DONJON
        TechTreeBox eliteSerjeant = new TechTreeBox(Database.getUnit(152), true, language);
        eliteSerjeant.removeBottomLine();
        TechTreeBox halberdier1 = new TechTreeBox(Database.getUnit(82), false, language);
        halberdier1.removeBottomLine();
        TechTreeEmptyBox donjonLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox donjonLine3 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout donjonLayout = new TechTreeSlotLayout(boxSet);
        donjonLayout.addFistRow(eliteSerjeant, halberdier1);
        donjonLayout.addSecondRow(donjonLine2, donjonLine3);

        //HOUSE WONDER
        TechTreeBox wonder = new TechTreeBox(Database.getBuilding(28), false, language);
        wonder.removeBottomLine();
        wonder.removeTopLine();
        TechTreeEmptyBox houseLine1 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout houseLayout = new TechTreeSlotLayout(boxSet);
        houseLayout.addFistRow(wonder);
        houseLayout.addSecondRow(houseLine1);

        //TOWN CENTER

        TechTreeEmptyBox townCenterLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox townCenterLine2 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox townCenterLine3 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox townCenterLine4 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox townCenterLine5 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox townCenterLine6 = new TechTreeEmptyBox(false);

        TechTreeSlotLayout townCenterLayout = new TechTreeSlotLayout(boxSet);
        townCenterLayout.addFistRow(townCenterLine1, townCenterLine2, townCenterLine3);
        townCenterLayout.addSecondRow(townCenterLine4, townCenterLine5, townCenterLine6);

        //FEITORIA
        TechTreeBox feitoria = new TechTreeBox(Database.getBuilding(29), false, language);
        feitoria.removeTopLine();
        feitoria.removeBottomLine();
        TechTreeEmptyBox feitoriaLine1 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout feitoriaLayout = new TechTreeSlotLayout(boxSet);
        feitoriaLayout.addFistRow(feitoria);
        feitoriaLayout.addSecondRow(feitoriaLine1);

        //CARAVANSERAI
        TechTreeBox caravanserai = new TechTreeBox(Database.getBuilding(35), false, language);
        caravanserai.removeTopLine();
        caravanserai.removeBottomLine();
        TechTreeEmptyBox caravanseraiLine1 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout caravanseraiLayout = new TechTreeSlotLayout(boxSet);
        caravanseraiLayout.addFistRow(caravanserai);
        caravanseraiLayout.addSecondRow(caravanseraiLine1);

        //MINING CAMP
        Div miningCampLayout = new Div();
        miningCampLayout.setWidth(miningCampWidth + "px");
        miningCampLayout.setHeight(sectionHeight + "px");

        //LUMBER CAMP
        TechTreeBox twoManSaw = new TechTreeBox(Database.getTechnology(91), false, language);
        twoManSaw.removeBottomLine();
        TechTreeEmptyBox lumberCampLine1 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout lumberCampLayout = new TechTreeSlotLayout(boxSet);
        lumberCampLayout.addFistRow(twoManSaw);
        lumberCampLayout.addSecondRow(lumberCampLine1);

        //MARKET
        TechTreeBox banking = new TechTreeBox(Database.getTechnology(30), false, language);
        banking.removeBottomLine();
        TechTreeBox guilds = new TechTreeBox(Database.getTechnology(103), false, language);
        guilds.removeBottomLine();
        guilds.removeTopLine();
        TechTreeEmptyBox marketLine1 = new TechTreeEmptyBox(false);
        TechTreeEmptyBox marketLine2 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout marketLayout = new TechTreeSlotLayout(boxSet);
        marketLayout.addFistRow(banking, guilds);
        marketLayout.addSecondRow(marketLine1, marketLine2);

        //LUMBER CAMP
        TechTreeBox cropRotation = new TechTreeBox(Database.getTechnology(92), false, language);
        cropRotation.removeBottomLine();
        TechTreeEmptyBox millLine1 = new TechTreeEmptyBox(false);
        TechTreeSlotLayout millLayout = new TechTreeSlotLayout(boxSet);
        millLayout.addFistRow(cropRotation);
        millLayout.addSecondRow(millLine1);

        //FARM
        Div farmLayout = new Div();
        farmLayout.setWidth(columnWidth + "px");
        farmLayout.setHeight(sectionHeight + "px");

        Div shieldLayoutInit = shieldDiv(3), shieldLayoutEnd = shieldDiv(3);
        Div container = new Div(shieldLayoutInit, archeryRangeLayout, barracksLayout, stableLayout,
                siegeWorkshopLayout, blacksmithLayout, dockLayout, universityLayout, towersLayout,
                castleLayout, monasteryLayout, krepostLayout, donjonLayout, houseLayout,
                townCenterLayout, feitoriaLayout, caravanseraiLayout, miningCampLayout, lumberCampLayout,
                marketLayout, millLayout, farmLayout, shieldLayoutEnd);
        container.addClassNames("tech-tree-age-container", "tech-tree-imperial-background");

        return container;
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_activity_tech_tree", language);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        parameters = beforeEnterEvent.getRouteParameters();
        civID = Integer.parseInt(getParameters().get("civID").orElse("1"));
        language = Utils.checkLanguage(parameters.get("language").orElse(Database.DEFAULT_LANGUAGE));

        if (!init){
            initView();
            init = true;
        }
    }
}
