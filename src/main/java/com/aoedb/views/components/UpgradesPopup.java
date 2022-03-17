package com.aoedb.views.components;

import com.aoedb.data.Technology;
import com.aoedb.data.UpgradeElement;
import com.aoedb.database.Database;
import com.aoedb.database.Utils;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CssImport("./themes/aoe2database/components/upgrades-popup.css")
public class UpgradesPopup extends Div {

    List<UpgradeElement> upgradeList;
    HashMap<Checkbox, UpgradeElement> checkMap;
    HashMap<Integer, UpgradeElement> indexMap;
    boolean enabled;
    boolean ignoreUpdates;

    OnItemChangedListener listener;
    String language;

    public UpgradesPopup(List<UpgradeElement> list, String language){
        upgradeList = list;
        enabled = false;
        ignoreUpdates = false;
        this.language = language;
        checkMap = new HashMap<>();
        indexMap = new HashMap<>();
        for (UpgradeElement u: list) indexMap.put(u.getId(), u);
        addClassNames("upgrades-list");
        for(UpgradeElement u : upgradeList){
            Div row = new Div();
            row.addClassNames("upgrades-row");
            Checkbox checkbox = new Checkbox();
            checkbox.addClassNames("upgrades-checkbox");
            Label name = new Label(u.getName());
            name.addClassNames("upgrades-name");
            Image image = new Image();
            image.setSrc(u.getImage());
            image.addClassNames("upgrades-image");
            checkMap.put(checkbox, u);
            checkbox.addValueChangeListener(event ->{
                if (!ignoreUpdates) {
                    setSelectedItems(checkMap.get(checkbox), event.getValue());
                    updateContent();
                    if (listener != null) listener.onItemChanged(getSelectedElements());
                }
            });
            row.addClickListener(event -> checkbox.setValue(!checkbox.getValue()));
            row.add(checkbox, name, image);
            add(row);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void updateContent(){
        for(int i = 0; i < getComponentCount(); ++i){
            Div row = (Div)getComponentAt(i);
            Checkbox checkbox = (Checkbox) row.getComponentAt(0);
            UpgradeElement u = checkMap.get(checkbox);
            Label name = (Label) row.getComponentAt(1);
            Image image = (Image) row.getComponentAt(2);
            checkbox.setValue(u.isSelected());
            if (!u.isEnabled()) {
                checkbox.setEnabled(false);
                name.setEnabled(false);
                image.setEnabled(false);
                image.addClassNames("upgrades-image-disabled");
                row.addClassNames("upgrades-row-disabled");
                row.setEnabled(false);
            }
            else{
                checkbox.setEnabled(true);
                name.setEnabled(true);
                row.removeClassNames("upgrades-row-disabled");
                image.removeClassNames("upgrades-image-disabled");
                row.setEnabled(true);
            }
        }
    }

    public void filterList(int age, int civID){
        for(UpgradeElement u: upgradeList){
            Technology t = Database.getTechnology(u.getId(), language);
            boolean b = Utils.mapAgeID(t.getAgeElement().getId()) <= age && t.isAvailableTo(civID);
            u.setEnabled(b);
            u.setSelected(b);
        }
        ignoreUpdates = true;
        updateContent();
        ignoreUpdates = false;
        enabled = true;
        if (listener != null) listener.onItemChanged(getSelectedElements());
    }


    private void setSelectedItems(UpgradeElement u, boolean selected){
        Technology t = Database.getTechnology(u.getId(), language);
        u.setSelected(selected);
        int techID;
        if (selected) techID = t.getRequiredTechElement().getId();
        else techID = t.getNextUpgradeElement().getId();
        if (techID != 0 && indexMap.containsKey(techID) && indexMap.get(techID).isEnabled()) setSelectedItems(indexMap.get(techID), selected);
    }

    private List<Integer> getSelectedElements(){
        List<Integer> list = new ArrayList<>();
        for(UpgradeElement u: upgradeList) if (u.isEnabled() && u.isSelected()) list.add(u.getId());
        return list;
    }


    public void setOnItemChangedListener(OnItemChangedListener l){
        this.listener = l;
    }

    public interface OnItemChangedListener{
        void onItemChanged(List<Integer> list);
    }
}
