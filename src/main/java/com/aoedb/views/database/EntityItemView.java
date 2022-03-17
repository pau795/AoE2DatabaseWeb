package com.aoedb.views.database;

import com.aoedb.views.components.ExpandableList;
import com.aoedb.views.components.ExpandableTypeList;
import com.aoedb.data.Item;
import com.vaadin.flow.component.Component;

public abstract class EntityItemView extends EntityView{

    ExpandableTypeList attacks, armors;

    protected Component attackView() {
        attacks = new ExpandableTypeList(((Item) e).getAttackValues(), language);
        return attacks;
    }

    protected Component armorView() {
        armors = new ExpandableTypeList(((Item) e).getArmorValues(), language);
        return armors;
    }

    protected Component upgradesView() {
        ExpandableList list = new ExpandableList(e.getUpgrades(), civID, language);
        list.setEntityView(this);
        return list;
    }

    @Override
    protected void resetViews(){
        attacks = null;
        armors = null;
    }

    @Override
    protected void updateStats(){
        super.updateStats();
        if (attacks != null && armors != null) {
            attacks.updateStats(((Item) e).getAttackValues());
            armors.updateStats(((Item) e).getArmorValues());
        }
    }
}
