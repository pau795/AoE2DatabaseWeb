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
public class DoubleAgeCivSelector extends Div {

    int ageID, civID1, civID2;
    Select<EntityElement> civSelector1, civSelector2;
    Button darkAgeButton, feudalAgeButton, castleAgeButton, imperialAgeButton, upgradesButton1, upgradesButton2;
    Hr darkFeudalDivider, feudalCastleDivider, castleImperialDivider;
    UpgradesPopup upgradesPopup1, upgradesPopup2;

    OnAgeChangeListener ageListener;
    OnEntityChangeListener entityListener1, entityListener2;

    String language;
    Popup popup1, popup2;

    public DoubleAgeCivSelector(String language){
        this.language = language;
        popup1 = new Popup();
        popup2 = new Popup();
        addClassNames("selector-container");
        ageID = 0;
        civID1 = civID2 = 1;

        //FIRST ROW
        Div firstRow = new Div();
        firstRow.addClassNames("row");
        civSelector1 = new Select<>();
        civSelector2 = new Select<>();
        Div div1 = new Div();
        Div div2 = new Div();
        div1.addClassNames("div");
        div2.addClassNames("div");
        civSelector1.addClassNames("selector");
        civSelector2.addClassNames("selector");
        upgradesButton1 = new Button(Database.getString("upgrades", language).toUpperCase(Locale.ROOT));
        upgradesButton1.setId("upgrades1");
        upgradesButton1.addClassNames("upgrades-button");
        upgradesButton2 = new Button(Database.getString("upgrades", language).toUpperCase(Locale.ROOT));
        upgradesButton2.setId("upgrades2");
        upgradesButton2.addClassNames("upgrades-button");

        civSelector1.setRenderer(new ComponentRenderer<>(item-> Utils.getEntityItemRow(item, false)));
        civSelector2.setRenderer(new ComponentRenderer<>(item-> Utils.getEntityItemRow(item, false)));
        civSelector1.setItemLabelGenerator(EntityElement::getName);
        civSelector2.setItemLabelGenerator(EntityElement::getName);

        popup1.setFor(upgradesButton1.getId().orElse(""));
        popup2.setFor(upgradesButton2.getId().orElse(""));
        div1.add(civSelector1);
        div2.add(civSelector2);
        firstRow.add(div1, div2);

        //SECOND ROW
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

        //THIRD ROW
        Div thirdRow = new Div();
        thirdRow.addClassNames("row", "padding-row");
        Div div3 = new Div();
        Div div4 = new Div();
        div3.addClassNames("div");
        div4.addClassNames("div");
        div3.add(upgradesButton1, popup1);
        div4.add(upgradesButton2, popup2);
        thirdRow.add(div3, div4);



        add(firstRow, new Hr(), secondRow, new Hr(), thirdRow);
        setupAgeButtons();

    }

    public void setUnit1Civ(EntityElement civElement, List<EntityElement> civNames){
        civID1 = civElement.getId();
        setupSelector(civSelector1, civElement, civNames);
    }

    public void setUnit2Civ(EntityElement civElement, List<EntityElement> civNames){
        civID2 = civElement.getId();
        setupSelector(civSelector2, civElement, civNames);
    }

    private void setupSelector(Select<EntityElement> selector, EntityElement civElement, List<EntityElement> civNames){
        selector.clear();
        selector.setItems(civNames);
        if (civNames.contains(civElement)) selector.setValue(civElement);
        else selector.setValue(civNames.get(0));
        selector.addValueChangeListener(event->{
            if (!event.getHasValue().isEmpty()) {
                if (selector == civSelector1) {
                    civID1 = event.getValue().getId();
                    if (entityListener1 != null) entityListener1.onCivChanged(civID1);
                } else {
                    civID2 = event.getValue().getId();
                    if (entityListener2 != null) entityListener2.onCivChanged(civID2);
                }
                if (upgradesPopup1 != null && upgradesPopup1.isEnabled()) upgradesPopup1.filterList(ageID, civID1);
                if (upgradesPopup2 != null && upgradesPopup2.isEnabled()) upgradesPopup2.filterList(ageID, civID2);
            }
        });

    }

    private void setupAgeButtons() {
        darkAgeButton.addClickListener(event -> {
            ageID = 0;
            darkAgeButton.addClassNames("button-active");
            feudalAgeButton.removeClassName("button-active");
            castleAgeButton.removeClassNames("button-active");
            imperialAgeButton.removeClassNames("button-active");
            darkAgeButton.removeClassNames("button-inactive");
            feudalAgeButton.addClassName("button-inactive");
            castleAgeButton.addClassNames("button-inactive");
            imperialAgeButton.addClassNames("button-inactive");
            if (ageListener != null) ageListener.onAgeChanged(ageID);
            if (upgradesPopup1 != null && upgradesPopup1.isEnabled()) upgradesPopup1.filterList(ageID, civID1);
            if (upgradesPopup2 != null && upgradesPopup2.isEnabled()) upgradesPopup2.filterList(ageID, civID2);
        });
        feudalAgeButton.addClickListener(event -> {
            ageID = 1;
            darkAgeButton.removeClassNames("button-active");
            feudalAgeButton.addClassName("button-active");
            castleAgeButton.removeClassNames("button-active");
            imperialAgeButton.removeClassNames("button-active");
            darkAgeButton.addClassNames("button-inactive");
            feudalAgeButton.removeClassName("button-inactive");
            castleAgeButton.addClassNames("button-inactive");
            imperialAgeButton.addClassNames("button-inactive");
            if (ageListener != null) ageListener.onAgeChanged(ageID);
            if (upgradesPopup1 != null && upgradesPopup1.isEnabled()) upgradesPopup1.filterList(ageID, civID1);
            if (upgradesPopup2 != null && upgradesPopup2.isEnabled()) upgradesPopup2.filterList(ageID, civID2);
        });
        castleAgeButton.addClickListener(event -> {
            ageID = 2;
            darkAgeButton.removeClassNames("button-active");
            feudalAgeButton.removeClassName("button-active");
            castleAgeButton.addClassNames("button-active");
            imperialAgeButton.removeClassNames("button-active");
            darkAgeButton.addClassNames("button-inactive");
            feudalAgeButton.addClassName("button-inactive");
            castleAgeButton.removeClassNames("button-inactive");
            imperialAgeButton.addClassNames("button-inactive");
            if (ageListener != null) ageListener.onAgeChanged(ageID);
            if (upgradesPopup1 != null && upgradesPopup1.isEnabled()) upgradesPopup1.filterList(ageID, civID1);
            if (upgradesPopup2 != null && upgradesPopup2.isEnabled()) upgradesPopup2.filterList(ageID, civID2);
        });
        imperialAgeButton.addClickListener(event -> {
            ageID = 3;
            darkAgeButton.removeClassNames("button-active");
            feudalAgeButton.removeClassName("button-active");
            castleAgeButton.removeClassNames("button-active");
            imperialAgeButton.addClassNames("button-active");
            darkAgeButton.addClassNames("button-inactive");
            feudalAgeButton.addClassName("button-inactive");
            castleAgeButton.addClassNames("button-inactive");
            imperialAgeButton.removeClassNames("button-inactive");
            if (ageListener != null) ageListener.onAgeChanged(ageID);
            if (upgradesPopup1 != null && upgradesPopup1.isEnabled()) upgradesPopup1.filterList(ageID, civID1);
            if (upgradesPopup2 != null && upgradesPopup2.isEnabled()) upgradesPopup2.filterList(ageID, civID2);
        });
    }

    public void setupUpgrade1Selector(List<Integer> list){
        upgradesPopup1 = new UpgradesPopup(Database.getUpgradeElementList(list, language), language);
        upgradesPopup1.filterList(ageID, civID1);
        upgradesPopup1.setOnItemChangedListener(list1 -> {
            if (entityListener1 != null) entityListener1.onUpgradesChanged(list1);
        });
        popup1.removeAll();
        popup1.add(upgradesPopup1);
    }

    public void setupUpgrade2Selector(List<Integer> list){
        upgradesPopup2 = new UpgradesPopup(Database.getUpgradeElementList(list, language), language);
        upgradesPopup2.filterList(ageID, civID2);
        upgradesPopup2.setOnItemChangedListener(list1 -> {
            if (entityListener2 != null) entityListener2.onUpgradesChanged(list1);
        });
        popup2.removeAll();
        popup2.add(upgradesPopup2);
    }

    public void showDarkAge(){
        darkAgeButton.setVisible(true);
        darkFeudalDivider.setVisible(true);
        feudalAgeButton.setVisible(true);
        feudalCastleDivider.setVisible(true);
        castleAgeButton.setVisible(true);
        castleImperialDivider.setVisible(true);
    }

    public void showFeudalAge(){
        darkAgeButton.setVisible(false);
        darkFeudalDivider.setVisible(false);
        feudalAgeButton.setVisible(true);
        feudalCastleDivider.setVisible(true);
        castleAgeButton.setVisible(true);
        castleImperialDivider.setVisible(true);
    }

    public void showCastleAge(){
        darkAgeButton.setVisible(false);
        darkFeudalDivider.setVisible(false);
        feudalAgeButton.setVisible(false);
        feudalCastleDivider.setVisible(false);
        castleAgeButton.setVisible(true);
        castleImperialDivider.setVisible(true);
    }

    public void showImperialAge(){
        darkAgeButton.setVisible(false);
        darkFeudalDivider.setVisible(false);
        feudalAgeButton.setVisible(false);
        feudalCastleDivider.setVisible(false);
        castleAgeButton.setVisible(false);
        castleImperialDivider.setVisible(false);
    }

    public void selectInitialAge(int age){
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

    public void setOnAgeChangeListener(OnAgeChangeListener l){
        ageListener = l;
    }

    public void setOnEntity1ChangeListener(OnEntityChangeListener l){
        entityListener1 = l;
    }

    public void setOnEntity2ChangeListener(OnEntityChangeListener l){
        entityListener2 = l;
    }

    public interface OnAgeChangeListener {
        void onAgeChanged(int age);
    }

    public interface OnEntityChangeListener{
        void onCivChanged(int civ);

        void onUpgradesChanged(List<Integer> list);
    }
}
