package com.aoedb.views.database;

import com.aoedb.views.components.AudioPlayer;
import com.aoedb.views.components.CivilizationEntityButton;
import com.aoedb.data.Civilization;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.aoedb.views.MainLayout;
import com.aoedb.views.tech_tree.TechTreeLoader;
import com.aoedb.views.TwoColumnView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.*;

import java.util.HashMap;
import java.util.Map;

@RoutePrefix("civ")
@Route(value = ":entityID?/:language?", layout = MainLayout.class)
@CssImport("./themes/aoe2database/civilization-view.css")
public class CivilizationView extends TwoColumnView {

    int civID;
    Civilization civ;


    @Override
    protected Div getFirstColumn() {
        Div profileLayout = new Div();
        profileLayout.addClassNames("section");
        Label civName = new Label(civ.getName());
        civName.addClassNames("title");
        Div imagesLayout = new Div();
        imagesLayout.addClassNames("civ-images-layout");
        Image icon = new Image();
        icon.setSrc(civ.getNameElement().getImage());
        icon.addClassNames("entity_icon");
        Label civStyle = new Label(civ.getCivStyle());
        civStyle.addClassNames("civ-style");
        imagesLayout.add(icon, civStyle);

        Label themeLabel = new Label(Database.getString("civilization_theme", language));
        themeLabel.addClassNames("civ-label");
        AudioPlayer themePlayer = new AudioPlayer();
        themePlayer.setSrc(civ.getCivThemeString());
        themePlayer.addClassNames("civ-audio");
        Div themeLayout = new Div();
        themeLayout.add(themeLabel, themePlayer);
        themeLayout.addClassNames("civ-panel-layout");

        Label techTreeLabel = new Label(Database.getString("civilization_tech_tree", language));
        techTreeLabel.addClassNames("civ-label");
        Icon techTreeImage = new Icon(VaadinIcon.FILE_TREE);
        techTreeImage.addClassNames("civ-image");
        Div techTreeLayout = new Div();
        techTreeLayout.add(techTreeLabel, techTreeImage);
        techTreeLayout.addClassNames("panel-hover", "civ-panel-layout");

        Map<String, String> params = new HashMap<>();
        params.put("language", language);
        params.put("civID", String.valueOf(civID));
        techTreeLayout.addClickListener(event->
            getUI().ifPresent(ui->ui.navigate(TechTreeLoader.class, new RouteParameters(params)))
        );

        Div linkImages = new Div();
        linkImages.add(themeLayout, techTreeLayout);
        linkImages.addClassNames("links-layout");

        Label civBonus = new Label(Database.getString("civilization_bonuses", language));
        civBonus.addClassNames("title");
        Span bonusText = new Span();
        bonusText.addClassNames("plain-text");
        bonusText.getElement().setProperty("innerHTML", civ.writeCivBonuses());

        profileLayout.add(civName, new Hr(), imagesLayout, new Hr(), linkImages, civBonus, new Hr(), bonusText, new Hr());
        return profileLayout;
    }

    public void setCivID(int id){
        this.civID = id;
    }

    @Override
    protected Div getSecondColumn() {
        Div entitiesLayout = new Div();
        entitiesLayout.addClassNames("section");

        Label uniqueUnits = new Label(Database.getString("civilization_unique_units", language));
        uniqueUnits.addClassNames("title");
        entitiesLayout.add(uniqueUnits, new Hr());
        for (int i : civ.getUniqueUnitList()){
            CivilizationEntityButton uniqueUnit = new CivilizationEntityButton(Database.getUnit(i, language), civID, language);
            entitiesLayout.add(uniqueUnit);
        }

        Label uniqueTechs = new Label(Database.getString("civilization_unique_technologies", language));
        uniqueTechs.addClassNames("title");
        CivilizationEntityButton uniqueTech1 = new CivilizationEntityButton(Database.getTechnology(civ.getUniqueTechList().get(0), language), civID, language);
        CivilizationEntityButton uniqueTech2 = new CivilizationEntityButton(Database.getTechnology(civ.getUniqueTechList().get(1), language), civID, language);
        entitiesLayout.add(uniqueTechs, new Hr(), uniqueTech1, uniqueTech2);

        if (civ.getUniqueBuildingList().size() > 0){
            Label uniqueBuildings = new Label(Database.getString("civilization_unique_buildings", language));
            uniqueBuildings.addClassNames("title");
            CivilizationEntityButton uniqueBuilding1 = new CivilizationEntityButton(Database.getBuilding(civ.getUniqueBuildingList().get(0), language), civID, language);
            entitiesLayout.add(uniqueBuildings, new Hr(), uniqueBuilding1);
        }
        entitiesLayout.add(new Hr());
        return entitiesLayout;
    }

    @Override
    public String getPageTitle() {
        return Database.getString("app_name", language) + " - " + civ.getName();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        parameters = beforeEnterEvent.getRouteParameters();
        language = Utils.checkLanguage(parameters.get("language").orElse(Database.DEFAULT_LANGUAGE));
        civID = Integer.parseInt(getParameters().get("entityID").orElse("1"));
        civ = Database.getCivilization(civID, language);
        if (!init){
            initView();
            init = true;
        }

    }
}
