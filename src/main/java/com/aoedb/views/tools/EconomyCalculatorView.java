package com.aoedb.views.tools;

import com.aoedb.data.Civilization;
import com.aoedb.data.EntityElement;
import com.aoedb.data.Unit;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.aoedb.views.TwoColumnView;
import com.aoedb.views.components.AgeCivSelector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RoutePrefix("economy_calculator")
@Route(value = ":language?", layout = MainLayout.class)
@CssImport("./themes/aoe2database/economy-calculator.css")
public class EconomyCalculatorView extends TwoColumnView {


    Div unitSelectionLayout, ecoStatsLayout;
    int ageID;
    int civID;
    Civilization civ;
    List<Integer> ecoUpgrades;
    EconomyCalculator calculator;
    Label lumberjackRatio, farmerRatio, goldMinerRatio;
    Label numLumberjacks, numFarmers, numGoldMiners;
    Label totalEcoLumberjackRatio, totalEcoFarmerRatio, totalEcoGoldMinerRatio;
    ArrayList<EntityElement> unitList;
    Button addUnitButton;


    @Override
    protected Div getFirstColumn() {
        ageID = 0;
        civID = 1;
        unitList = new ArrayList<>(Database.getList(Database.UNIT_LIST, language));
        unitList.sort(EntityElement.getAlphabeticalComparator());
        civ = Database.getCivilization(civID, language);
        ecoUpgrades = civ.getUpgradesIds();
        List<EntityElement> civNames = new ArrayList<>(Database.getList(Database.CIVILIZATION_LIST, language));
        civNames.sort(EntityElement.getAlphabeticalComparator());
        calculator = new EconomyCalculator();
        AgeCivSelector selector = new AgeCivSelector(ageID, civID, civNames, ecoUpgrades, language);
        selector.setOnChangeListener(new AgeCivSelector.OnChangeListener() {
            @Override
            public void onAgeChanged(int age) {
                ageID = age;
                calculator.calculateTotalEco();
            }

            @Override
            public void onCivChanged(int civId) {
                civID = civId;
                civ = Database.getCivilization(civID, language);
                calculator.calculateTotalEco();
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                ecoUpgrades = list;
                calculator.calculateTotalEco();
            }
        });
        unitSelectionLayout = new Div();
        unitSelectionLayout.addClassNames("economy-calculator-unit-selection-div");
        addUnitButton = new Button();
        addUnitButton.addClickListener(buttonClickEvent -> calculator.addItem());
        addUnitButton.addClassNames("economy-calculator-button");
        addUnitButton.setText(Database.getString("ec_add_unit", language));
        Div container = new Div(selector, unitSelectionLayout, new Hr(),addUnitButton, new Hr());
        container.addClassNames("economy-calculator-container");
        return container;
    }

    @Override
    protected Div getSecondColumn() {

        ecoStatsLayout = new Div();
        ecoStatsLayout.addClassNames("economy-calculator-stats-div");

        lumberjackRatio = new Label();
        lumberjackRatio.addClassNames("economy-calculator-center-text");
        farmerRatio = new Label();
        farmerRatio.addClassNames("economy-calculator-center-text");
        goldMinerRatio = new Label();
        goldMinerRatio.addClassNames("economy-calculator-center-text");
        totalEcoLumberjackRatio = new Label();
        totalEcoLumberjackRatio.addClassNames("economy-calculator-bold-text");
        totalEcoFarmerRatio = new Label();
        totalEcoFarmerRatio.addClassNames("economy-calculator-bold-text");
        totalEcoGoldMinerRatio = new Label();
        totalEcoGoldMinerRatio.addClassNames("economy-calculator-bold-text");
        numLumberjacks = new Label();
        numLumberjacks.addClassNames("economy-calculator-bold-text");
        numFarmers = new Label();
        numFarmers.addClassNames("economy-calculator-bold-text");
        numGoldMiners = new Label();
        numGoldMiners.addClassNames("economy-calculator-bold-text");

        Label unitsHeader = new Label(Database.getString("ec_units", language));
        unitsHeader.addClassNames("economy-calculator-eco-row-header", "economy-calculator-grid-border");

        Label lumberjackName = new Label(Database.getString("ec_lumberjacks", language));
        lumberjackName.addClassNames("economy-calculator-bold-text");
        Image lumberIcon = new Image();
        lumberIcon.setSrc(Database.getImage("r_wood"));
        lumberIcon.addClassNames("economy-calculator-res-image");
        Div lumberBottom = new Div(lumberIcon, lumberjackRatio);
        lumberBottom.addClassNames("economy-calculator-grid-cell-row");
        Div lumberjacks = new Div(lumberjackName, lumberBottom);
        lumberjacks.addClassNames("economy-calculator-grid-cell", "economy-calculator-grid-border");

        Label farmerName = new Label(Database.getString("ec_farmers", language));
        farmerName.addClassNames("economy-calculator-bold-text");
        Image farmerIcon = new Image();
        farmerIcon.setSrc(Database.getImage("r_food"));
        farmerIcon.addClassNames("economy-calculator-res-image");
        Div farmerBottom = new Div(farmerIcon, farmerRatio  );
        farmerBottom.addClassNames("economy-calculator-grid-cell-row");
        Div farmers = new Div(farmerName, farmerBottom);
        farmers.addClassNames("economy-calculator-grid-cell", "economy-calculator-grid-border");

        Label goldMinerName = new Label(Database.getString("ec_gold_miners", language));
        goldMinerName.addClassNames("economy-calculator-bold-text");
        Image goldMinerIcon = new Image();
        goldMinerIcon.setSrc(Database.getImage("r_gold"));
        goldMinerIcon.addClassNames("economy-calculator-res-image");
        Div goldMinerBottom = new Div(goldMinerIcon, goldMinerRatio);
        goldMinerBottom.addClassNames("economy-calculator-grid-cell-row");
        Div goldMiners = new Div(goldMinerName, goldMinerBottom);
        goldMiners.addClassNames("economy-calculator-grid-cell", "economy-calculator-grid-border");



        Label totalHeader = new Label(Database.getString("ec_total", language));
        totalHeader.addClassNames("economy-calculator-eco-row-header", "economy-calculator-grid-border");

        Image villIcon1 = new Image();
        villIcon1.setSrc(Database.getImage("vill_icon"));
        villIcon1.setClassName("economy-calculator-villager-image");
        Div totalLumberTop = new Div(numLumberjacks, villIcon1);
        totalLumberTop.addClassNames("economy-calculator-grid-cell-row");
        Image totalLumberIcon = new Image();
        totalLumberIcon.setSrc(Database.getImage("r_wood"));
        totalLumberIcon.setClassName("economy-calculator-res-image");
        Div totalLumberBottom = new Div(totalLumberIcon, totalEcoLumberjackRatio);
        totalLumberBottom.addClassNames("economy-calculator-grid-cell-row");
        Div totalLumberjacks = new Div(totalLumberTop, totalLumberBottom);
        totalLumberjacks.addClassNames("economy-calculator-grid-cell", "economy-calculator-grid-border");

        Image villIcon2 = new Image();
        villIcon2.setSrc(Database.getImage("vill_icon"));
        villIcon2.setClassName("economy-calculator-villager-image");
        Div totalFarmerTop = new Div(numFarmers, villIcon2);
        totalFarmerTop.addClassNames("economy-calculator-grid-cell-row");
        Image totalFarmerIcon = new Image();
        totalFarmerIcon.setClassName("economy-calculator-res-image");
        totalFarmerIcon.setSrc(Database.getImage("r_food"));
        Div totalFarmerBottom = new Div(totalFarmerIcon, totalEcoFarmerRatio);
        totalFarmerBottom.addClassNames("economy-calculator-grid-cell-row");
        Div totalFarmers = new Div(totalFarmerTop, totalFarmerBottom);
        totalFarmers.addClassNames("economy-calculator-grid-cell", "economy-calculator-grid-border");

        Image villIcon3 = new Image();
        villIcon3.setSrc(Database.getImage("vill_icon"));
        villIcon3.setClassName("economy-calculator-villager-image");
        Div totalGoldMinerTop = new Div(numGoldMiners, villIcon3);
        totalGoldMinerTop.addClassNames("economy-calculator-grid-cell-row");
        Image totalGoldMinerIcon = new Image();
        totalGoldMinerIcon.setClassName("economy-calculator-res-image");
        totalGoldMinerIcon.setSrc(Database.getImage("r_gold"));
        Div totalGoldMinerBottom = new Div(totalGoldMinerIcon, totalEcoGoldMinerRatio);
        totalGoldMinerBottom.addClassNames("economy-calculator-grid-cell-row");
        Div totalGoldMiners = new Div(totalGoldMinerTop, totalGoldMinerBottom);
        totalGoldMiners.addClassNames("economy-calculator-grid-cell", "economy-calculator-grid-border");

        Div gridHeader = new Div(unitsHeader, lumberjacks, farmers, goldMiners);
        gridHeader.addClassNames("economy-calculator-eco-grid-row");
        
        Div totalRow = new Div(totalHeader, totalLumberjacks, totalFarmers, totalGoldMiners);
        totalRow.addClassNames("economy-calculator-eco-grid-row");
        
        Div ecoGrid = new Div(gridHeader, ecoStatsLayout, totalRow);
        ecoGrid.addClassNames("economy-calculator-eco-grid", "economy-calculator-grid-border");
        
        Div container = new Div(ecoGrid);
        container.addClassNames("economy-calculator-container");

        addUnitButton.click();
        return container;
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_economy_calculator", language);
    }

    public class EconomyCalculator {

        public class UnitItem {
            private Unit u;
            private double unitWoodRate, unitFoodRate, unitGoldRate;
            private final IntegerField buildingSelector;
            private final Image buildingIcon, unitIcon;
            private final Div unitSelector, ecoStats;
            private final Label woodRateText, foodRateText, goldRateText;
            private final Label woodVillagersText, foodVillagersText, goldVillagersText;
            private final Label numBuildings, trainingTime;
            private final Div resContainer;

            public UnitItem() {

                woodRateText = new Label();
                woodRateText.addClassNames("economy-calculator-center-text");
                foodRateText = new Label();
                foodRateText.addClassNames("economy-calculator-center-text");
                goldRateText = new Label();
                goldRateText.addClassNames("economy-calculator-center-text");
                woodVillagersText = new Label();
                woodVillagersText.addClassNames("economy-calculator-center-text");
                foodVillagersText = new Label();
                foodVillagersText.addClassNames("economy-calculator-center-text");
                goldVillagersText = new Label();
                goldVillagersText.addClassNames("economy-calculator-center-text");

                unitIcon = new Image();
                unitIcon.addClassNames("economy-calculator-entity-icon");

                numBuildings = new Label();
                numBuildings.addClassNames("economy-calculator-text-over-image", "economy-calculator-white-text");
                trainingTime = new Label();
                trainingTime.addClassNames("economy-calculator-white-text");

                buildingIcon = new Image();
                buildingIcon.addClassNames("economy-calculator-entity-icon");
                buildingSelector = new IntegerField();
                buildingSelector.addClassNames("economy-calculator-building-selector");
                buildingSelector.setHasControls(true);
                buildingSelector.setMin(0);
                buildingSelector.setMax(10);
                buildingSelector.addValueChangeListener(event -> {
                    if (buildingSelector.getValue() < buildingSelector.getMin()) buildingSelector.setValue(0);
                    if (buildingSelector.getValue() > buildingSelector.getMax()) buildingSelector.setValue(4);
                    numBuildings.setText("×" + event.getValue());
                    calculateTotalEco();
                });
                Button removeButton = new Button(VaadinIcon.TRASH.create());
                removeButton.addClassNames("economy-calculator-remove-button");
                removeButton.addClickListener(event -> removeItem(getItemPosition(this)));
                ComboBox<EntityElement> selector = new ComboBox<>();
                selector.addClassNames("economy-calculator-selector");
                selector.setItemLabelGenerator(EntityElement::getName);
                selector.setRenderer(new ComponentRenderer<>(element -> Utils.getEntityItemRow(element, true)));
                selector.setItems(Utils.getEntityElementComboBoxFilter(), unitList);
                selector.getElement().getStyle().set("--vaadin-combo-box-overlay-width","300px");
                selector.addValueChangeListener(event -> {
                    if (event.getValue() != null) {
                        u = Database.getUnit(event.getValue().getId(), language);
                        buildingIcon.setSrc(u.getCreatorElement().getImage());
                        buildingSelector.setValue(1);
                        unitIcon.setSrc(u.getNameElement().getImage());
                        calculateTotalEco();
                    }
                });
                selector.addFocusListener(event -> selector.setValue(null));
                u = Database.getUnit(1, language);
                selector.setValue(u.getNameElement());
                unitSelector = new Div(selector, buildingIcon, buildingSelector, removeButton);
                unitSelector.addClassNames("economy-calculator-selector-row");



                Div unitImageDiv = new Div(unitIcon, numBuildings);
                unitImageDiv.addClassNames("economy-calculator-unit-image-container");
                Image timeIcon = new Image();
                timeIcon.addClassNames("economy-calculator-hourglass-icon");
                timeIcon.setSrc(Database.getImage("hourglass"));

                Div trainingTimeDiv = new Div(timeIcon, trainingTime);
                trainingTimeDiv.addClassNames("economy-calculator-training-time-container");
                resContainer = new Div();
                resContainer.addClassNames("economy-calculator-res-container");
                Div costDiv = new Div(trainingTimeDiv, resContainer);
                costDiv.addClassNames("economy-calculator-cost-container");
                Div unitHeader = new Div(unitImageDiv, costDiv);
                unitHeader.addClassNames("economy-calculator-unit-header", "economy-calculator-grid-border");



                Image villIcon1 = new Image();
                villIcon1.setSrc(Database.getImage("vill_icon"));
                villIcon1.setClassName("economy-calculator-villager-image");
                Div totalLumberTop = new Div(woodVillagersText, villIcon1);
                totalLumberTop.addClassNames("economy-calculator-grid-cell-row");
                Image totalLumberIcon = new Image();
                totalLumberIcon.setSrc(Database.getImage("r_wood"));
                totalLumberIcon.setClassName("economy-calculator-res-image");
                Div totalLumberBottom = new Div(totalLumberIcon, woodRateText);
                totalLumberBottom.addClassNames("economy-calculator-grid-cell-row");
                Div lumberjacks = new Div(totalLumberTop, totalLumberBottom);
                lumberjacks.addClassNames("economy-calculator-grid-cell", "economy-calculator-grid-border");

                Image villIcon2 = new Image();
                villIcon2.setSrc(Database.getImage("vill_icon"));
                villIcon2.setClassName("economy-calculator-villager-image");
                Div totalFarmerTop = new Div(foodVillagersText, villIcon2);
                totalFarmerTop.addClassNames("economy-calculator-grid-cell-row");
                Image totalFarmerIcon = new Image();
                totalFarmerIcon.setClassName("economy-calculator-res-image");
                totalFarmerIcon.setSrc(Database.getImage("r_food"));
                Div totalFarmerBottom = new Div(totalFarmerIcon, foodRateText);
                totalFarmerBottom.addClassNames("economy-calculator-grid-cell-row");
                Div farmers = new Div(totalFarmerTop, totalFarmerBottom);
                farmers.addClassNames("economy-calculator-grid-cell", "economy-calculator-grid-border");

                Image villIcon3 = new Image();
                villIcon3.setSrc(Database.getImage("vill_icon"));
                villIcon3.setClassName("economy-calculator-villager-image");
                Div totalGoldMinerTop = new Div(goldVillagersText, villIcon3);
                totalGoldMinerTop.addClassNames("economy-calculator-grid-cell-row");
                Image totalGoldMinerIcon = new Image();
                totalGoldMinerIcon.setClassName("economy-calculator-res-image");
                totalGoldMinerIcon.setSrc(Database.getImage("r_gold"));
                Div totalGoldMinerBottom = new Div(totalGoldMinerIcon, goldRateText);
                totalGoldMinerBottom.addClassNames("economy-calculator-grid-cell-row");
                Div goldMiners = new Div(totalGoldMinerTop, totalGoldMinerBottom);
                goldMiners.addClassNames("economy-calculator-grid-cell", "economy-calculator-grid-border");

                ecoStats = new Div(unitHeader, lumberjacks, farmers, goldMiners);
                ecoStats.addClassNames("economy-calculator-eco-grid-row");

            }

            public Div getUnitSelector(){
                return unitSelector;
            }

            public Div getEcoStats(){
                return ecoStats;
            }

            public void calculateUnitEco(){
                int woodVillagers = 0, foodVillagers = 0, goldVillagers = 0;
                unitWoodRate = unitFoodRate = unitGoldRate = 0;
                u.calculateStats(ageID, civID, u.getUpgradesIds());
                setUnitCost();
                int numBuildings = buildingSelector.getValue();
                double trainingRate = 60.0 / u.getCalculatedStat(Database.TRAINING_TIME) * numBuildings;
                HashMap<String, Integer> cost = u.getCalculatedCost();
                for(String s: cost.keySet()){
                    switch (s){
                        case Database.WOOD:
                            unitWoodRate = trainingRate * cost.get(s);
                            woodVillagers = (int) Math.ceil(unitWoodRate / ecoWoodRate);
                            break;
                        case Database.FOOD:
                            unitFoodRate = trainingRate * cost.get(s);
                            foodVillagers = (int) Math.ceil(unitFoodRate / ecoFoodRate);
                            break;
                        case Database.GOLD:
                            unitGoldRate = trainingRate * cost.get(s);
                            goldVillagers = (int) Math.ceil(unitGoldRate / ecoGoldRate);
                            break;
                        default:
                            break;

                    }
                }
                woodRateText.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(unitWoodRate, 1)));
                foodRateText.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(unitFoodRate, 1)));
                goldRateText.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(unitGoldRate, 1)));
                woodVillagersText.setText(Utils.getDecimalString(woodVillagers, 1));
                foodVillagersText.setText(Utils.getDecimalString(foodVillagers, 1));
                goldVillagersText.setText(Utils.getDecimalString(goldVillagers, 1));
                

            }

            private void setUnitCost(){
                trainingTime.setText(Utils.getDecimalString(u.getCalculatedStat(Database.TRAINING_TIME), 0));
                LinkedHashMap<String, Integer> b = u.getCalculatedCost();
                resContainer.removeAll();
                for(String res: b.keySet()){
                    Image resIcon = new Image();
                    resIcon.addClassNames("economy-calculator-res-image");
                    String iconPath = Utils.getResourceIcon(res);
                    resIcon.setSrc(iconPath);
                    Label resValue = new Label(Utils.getDecimalString(b.get(res), 0));
                    resValue.addClassNames("economy-calculator-white-text");
                    Div container = new Div(resIcon, resValue);
                    container.addClassNames("economy-calculator-resource-div");
                    resContainer.add(container);
                }

            }
        }


        List<UnitItem> items;
        double ecoWoodRate, ecoFoodRate, ecoGoldRate;
        double totalWoodRate, totalFoodRate, totalGoldRate;
        int lumberjacks, farmers, miners;
        int numItems = 0;

        public EconomyCalculator(){
            items = new ArrayList<>();
            ecoWoodRate = 1;
            ecoFoodRate = 1;
            ecoGoldRate = 1;
        }

        public void addItem(){
            if(numItems < 5) {
                UnitItem i = new UnitItem();
                unitSelectionLayout.add(i.getUnitSelector());
                ecoStatsLayout.add(i.getEcoStats());
                items.add(i);
                ++numItems;
                calculateTotalEco();

            }
        }

        public int getItemPosition(UnitItem it){
            for(int i = 0; i < items.size(); ++i) if (items.get(i) == it) return i;
            return -1;
        }

        public void removeItem(int i){
            if (numItems > 1) {
                UnitItem ui = items.get(i);
                items.remove(i);
                unitSelectionLayout.remove(ui.getUnitSelector());
                ecoStatsLayout.remove(ui.getEcoStats());
                --numItems;
                calculateTotalEco();
            }
        }

        private void calculateGatherRates(){
            civ.calculateStats(ageID, civID, ecoUpgrades);
            ecoWoodRate = civ.getCalculatedStat(Database.LUMBERJACK);
            ecoFoodRate = civ.getCalculatedStat(Database.FARMER);
            ecoGoldRate = civ.getCalculatedStat(Database.GOLD_MINER);
        }

        public void calculateTotalEco(){
            totalWoodRate = totalFoodRate = totalGoldRate = 0;
            calculateGatherRates();
            for(UnitItem u: items){
                u.calculateUnitEco();
                totalWoodRate += u.unitWoodRate;
                totalFoodRate += u.unitFoodRate;
                totalGoldRate += u.unitGoldRate;
            }
            lumberjacks = (int) Math.ceil(totalWoodRate / ecoWoodRate);
            farmers = (int) Math.ceil(totalFoodRate / ecoFoodRate);
            miners = (int) Math.ceil(totalGoldRate / ecoGoldRate);

            lumberjackRatio.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(ecoWoodRate, 1)));
            farmerRatio.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(ecoFoodRate, 1)));
            goldMinerRatio.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(ecoGoldRate, 1)));

            numLumberjacks.setText(Utils.getDecimalString(lumberjacks, 1));
            numFarmers.setText(Utils.getDecimalString(farmers, 1));
            numGoldMiners.setText(Utils.getDecimalString(miners, 1));

            totalEcoLumberjackRatio.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(totalWoodRate, 1)));
            totalEcoFarmerRatio.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(totalFoodRate, 1)));
            totalEcoGoldMinerRatio.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(totalGoldRate, 1)));

        }
    }

}
