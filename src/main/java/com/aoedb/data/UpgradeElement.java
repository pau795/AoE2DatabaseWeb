package com.aoedb.data;

import com.aoedb.database.Database;

import java.util.Comparator;
import java.util.HashMap;

public class UpgradeElement {
    private final int id;
    private final StringKey name;
    private final String image;
    private boolean selected;
    private boolean enabled;

    public UpgradeElement(EntityElement e) {
        this.id = e.getId();
        this.name = e.getName();
        this.image = e.getImage();
        this.selected = false;
        this.enabled = true;
    }

    public int getId() {
        return id;
    }


    public StringKey getName() {
        return name;
    }


    public String getImage() {
        return image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static Comparator<UpgradeElement> getListElementComparator(final String file, final int index){

        return new Comparator<UpgradeElement>() {
            final HashMap<Integer, Integer> order =  Database.getOrderMap(file, index);
            @Override
            public int compare(UpgradeElement o1, UpgradeElement o2) {
                return order.get(o1.getId()).compareTo(order.get(o2.getId()));
            }
        };
    }
}
