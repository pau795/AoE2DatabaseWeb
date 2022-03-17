package com.aoedb.views.tools;

import com.aoedb.data.Civilization;
import com.aoedb.data.EcoElement;
import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.aoedb.views.OneColumnView;
import com.aoedb.views.components.AgeCivSelector;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RoutePrefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RoutePrefix("gathering_rates")
@Route(value = ":language?", layout = MainLayout.class)
@CssImport("./themes/aoe2database/gathering-rates.css")
public class GatheringRatesView extends OneColumnView {

    int civID, ageID;
    Civilization civ;
    List<Integer> ecoUpgrades;
    Div ratesList;
    List<EcoElement> gatheringRates;
    HashMap<Integer, String> statNames;

    @Override
    protected Div getColumn() {
        civID = 1;
        ageID = 0;
        civ = Database.getCivilization(civID, language);
        ecoUpgrades = civ.getUpgradesIds();
        statNames = Database.getEcoList(language);
        gatheringRates = Database.getGatheringRates(language);
        List<EntityElement> civNames = new ArrayList<>(Database.getList(Database.CIVILIZATION_LIST, language));
        civNames.sort(EntityElement.getAlphabeticalComparator());
        AgeCivSelector selector = new AgeCivSelector(ageID, civID, civNames, ecoUpgrades, language);
        selector.setOnChangeListener(new AgeCivSelector.OnChangeListener() {
            @Override
            public void onAgeChanged(int age) {
                ageID = age;
                calculateEco();
            }

            @Override
            public void onCivChanged(int civId) {
                civID = civId;
                civ = Database.getCivilization(civID, language);
                calculateEco();
            }

            @Override
            public void onUpgradesChanged(List<Integer> list) {
                ecoUpgrades = list;
                calculateEco();
            }
        });
        ratesList = new Div();
        ratesList.addClassNames("gathering-rates-list");
        Div container = new Div(selector, ratesList);
        container.addClassNames("gathering-rates-container");
        calculateEco();
        return container;

    }

    private void calculateEco(){
        civ.calculateStats(ageID, civID, ecoUpgrades);
        ratesList.removeAll();
        for (int i = 0; i < gatheringRates.size(); ++i){
            EcoElement element = gatheringRates.get(i);
            double stat = civ.getCalculatedStat(statNames.get(element.getStat()));
            if (i == 13) stat *= 1.75; //deep fish
            else if ( i == 14) stat *= 1.25; // fish trap
            element.setGatheringRate(stat);
            if (!Double.isNaN(element.getGatheringRate())){
                ratesList.add(getGatheringRate(element));
            }
        }
    }

    private Div getGatheringRate(EcoElement element){
        Image icon = new Image();
        icon.addClassNames("gathering-rates-icon");
        icon.setSrc(element.getStatIcon());
        Label statName = new Label();
        statName.addClassNames("gathering-rates-stat-name");
        statName.setText(element.getStatName());
        Image resIcon = new Image();
        resIcon.addClassNames("gathering-rates-resource-icon");
        resIcon.setSrc(element.getResourceIcon());
        Label gatheringRate = new Label();
        gatheringRate.addClassNames("gathering-rates-gathering-rate");
        gatheringRate.setText(String.format(Database.getString("ec_min", language), Utils.getDecimalString(element.getGatheringRate(), 1)));
        Div gatheringRateContainer = new Div(resIcon, gatheringRate);
        gatheringRateContainer.addClassNames("gathering-rates-resource-container");
        Div cellContainer = new Div(icon, statName, gatheringRateContainer);
        cellContainer.addClassNames("gathering-rates-cell");
        return cellContainer;
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + Database.getString("title_gathering_rates", language);
    }
}