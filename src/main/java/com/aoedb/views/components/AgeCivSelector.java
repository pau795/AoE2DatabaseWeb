package com.aoedb.views.components;

import com.aoedb.data.EntityElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.vaadin.componentfactory.Popup;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;
import java.util.Locale;

@CssImport("./themes/aoe2database/components/age-civ-selector.css")
public class AgeCivSelector extends Div {

    int ageID, civID;
    List<EntityElement> civNames;
    Select<EntityElement> civSelector;
    Button darkAgeButton, feudalAgeButton, castleAgeButton, imperialAgeButton, upgradesButton;
    Hr darkFeudalDivider, feudalCastleDivider, castleImperialDivider;
    UpgradesPopup upgradesPopup;
    OnChangeListener listener;
    String language;
    Popup popup;


    public AgeCivSelector(int ageID, int civID, List<EntityElement> civNames, List<Integer> upgradeList, String language){
        this.ageID = ageID;
        this.civID = civID;
        this.civNames = civNames;
        this.language = language;
        popup = new Popup();
        setupUpgradeSelector(upgradeList);
        addClassNames("selector-container");
        Div firstRow = new Div();
        firstRow.addClassNames("row");

        civSelector = new Select<>();
        Div div1 = new Div();
        Div div2 = new Div();
        div1.addClassNames("div");
        div2.addClassNames("div");
        civSelector.addClassNames("selector");
        upgradesButton = new Button(Database.getString("upgrades", language).toUpperCase(Locale.ROOT));
        upgradesButton.setId("upgrades");
        upgradesButton.addClassNames("upgrades-button");
        popup.setFor(upgradesButton.getId().orElse(""));
        div1.add(civSelector);
        div2.add(upgradesButton, popup);
        firstRow.add(div1, div2);
        Div secondRow = new Div();
        secondRow.addClassNames("row");
        darkAgeButton = new Button(Database.getString("menu_dark_age", language));
        darkAgeButton.addClassNames("age-button", "button-inactive");
        feudalAgeButton = new Button(Database.getString("menu_feudal_age", language));
        feudalAgeButton.addClassNames("age-button", "button-inactive");
        castleAgeButton = new Button(Database.getString("menu_castle_age", language));
        castleAgeButton.addClassNames("age-button", "button-inactive");
        imperialAgeButton = new Button(Database.getString("menu_imperial_age", language));
        imperialAgeButton.addClassNames("age-button", "button-inactive");
        darkFeudalDivider = new Hr();
        feudalCastleDivider = new Hr();
        castleImperialDivider = new Hr();
        secondRow.add(darkAgeButton, darkFeudalDivider, feudalAgeButton, feudalCastleDivider, castleAgeButton, castleImperialDivider, imperialAgeButton);
        add(firstRow, new Hr(), secondRow, new Hr());
        setupAgeButtons();
        setupSelector();
        selectInitialAge(ageID);

    }

    private void setupAgeButtons(){
        darkAgeButton.addClickListener(event ->{
           ageID = 0;
           darkAgeButton.addClassNames("button-active");
           feudalAgeButton.removeClassName("button-active");
           castleAgeButton.removeClassNames("button-active");
           imperialAgeButton.removeClassNames("button-active");
           darkAgeButton.removeClassNames("button-inactive");
           feudalAgeButton.addClassName("button-inactive");
           castleAgeButton.addClassNames("button-inactive");
           imperialAgeButton.addClassNames("button-inactive");
           if (listener != null) listener.onAgeChanged(ageID);
           if (upgradesPopup.isEnabled()) upgradesPopup.filterList(ageID, civID);
        });
        feudalAgeButton.addClickListener(event ->{
            ageID = 1;
            darkAgeButton.removeClassNames("button-active");
            feudalAgeButton.addClassName("button-active");
            castleAgeButton.removeClassNames("button-active");
            imperialAgeButton.removeClassNames("button-active");
            darkAgeButton.addClassNames("button-inactive");
            feudalAgeButton.removeClassName("button-inactive");
            castleAgeButton.addClassNames("button-inactive");
            imperialAgeButton.addClassNames("button-inactive");
            if (listener != null) listener.onAgeChanged(ageID);
            if (upgradesPopup.isEnabled()) upgradesPopup.filterList(ageID, civID);
        });
        castleAgeButton.addClickListener(event ->{
            ageID = 2;
            darkAgeButton.removeClassNames("button-active");
            feudalAgeButton.removeClassName("button-active");
            castleAgeButton.addClassNames("button-active");
            imperialAgeButton.removeClassNames("button-active");
            darkAgeButton.addClassNames("button-inactive");
            feudalAgeButton.addClassName("button-inactive");
            castleAgeButton.removeClassNames("button-inactive");
            imperialAgeButton.addClassNames("button-inactive");
            if (listener != null) listener.onAgeChanged(ageID);
            if (upgradesPopup.isEnabled()) upgradesPopup.filterList(ageID, civID);
        });
        imperialAgeButton.addClickListener(event ->{
            ageID = 3;
            darkAgeButton.removeClassNames("button-active");
            feudalAgeButton.removeClassName("button-active");
            castleAgeButton.removeClassNames("button-active");
            imperialAgeButton.addClassNames("button-active");
            darkAgeButton.addClassNames("button-inactive");
            feudalAgeButton.addClassName("button-inactive");
            castleAgeButton.addClassNames("button-inactive");
            imperialAgeButton.removeClassNames("button-inactive");
            if (listener != null) listener.onAgeChanged(ageID);
            if (upgradesPopup.isEnabled()) upgradesPopup.filterList(ageID, civID);
        });
    }

    private void setupSelector(){
        civSelector.setRenderer(new ComponentRenderer<>(item-> Utils.getEntityItemRow(item, false, language)));
        civSelector.setItemLabelGenerator(entityElement -> entityElement.getName().getTranslatedString(language));
        civSelector.setItems(civNames);
        civSelector.setValue(Database.getElement(Database.CIVILIZATION_LIST, civID));
        civSelector.addValueChangeListener(event->{
            civID = event.getValue().getId();
            if (listener != null) listener.onCivChanged(civID);
            if (upgradesPopup.isEnabled()) upgradesPopup.filterList(ageID, civID);
        });

    }


    private void selectInitialAge(int age){
        switch (age){
            case 0:
                darkAgeButton.click();
                break;
            case 1:
                feudalAgeButton.click();
                break;
            case 2:
                castleAgeButton.click();
                break;
            case 3:
                imperialAgeButton.click();
                break;
        }
    }

    private void setupUpgradeSelector(List<Integer> list){
        upgradesPopup = new UpgradesPopup(Database.getUpgradeElementList(list), language);
        upgradesPopup.filterList(ageID, civID);
        upgradesPopup.setOnItemChangedListener(list1 -> {
            if (listener != null) listener.onUpgradesChanged(list1);
        });
        popup.removeAll();
        popup.add(upgradesPopup);
    }

    public void hideDarkAge(){
        darkAgeButton.setVisible(false);
        darkFeudalDivider.setVisible(false);
    }

    public void hideFeudalAge(){
        hideDarkAge();
        feudalAgeButton.setVisible(false);
        feudalCastleDivider.setVisible(false);
    }

    public void hideCastleAge(){
        hideFeudalAge();
        castleAgeButton.setVisible(false);
        castleImperialDivider.setVisible(false);
    }

    public void setOnChangeListener(OnChangeListener listener){
        this.listener = listener;
    }

    public interface OnChangeListener {
        void onAgeChanged(int age);

        void onCivChanged(int civ);

        void onUpgradesChanged(List<Integer> list);

    }
}
