package com.aoedb.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

import java.util.Set;

@CssImport("./themes/aoe2database/tech-tree-view.css")
public class TechTreeSlotLayout extends Div {

    Div firstRow;
    Div secondRow;

    Set<TechTreeBox> boxSet;

    public TechTreeSlotLayout(Set<TechTreeBox> set){
        this.boxSet = set;
        addClassNames("tech-tree-age-section");
        firstRow = new Div();
        firstRow.addClassNames("tech-tree-age-row");
        secondRow = new Div();
        secondRow.addClassNames("tech-tree-age-row");
        add(firstRow, secondRow);
    }

    public void addFistRow(Component... components){
        firstRow.add(components);
        addTechTreeBoxes(components);
    }

    public void addSecondRow(Component... components){
        secondRow.add(components);
        addTechTreeBoxes(components);
    }

    private void addTechTreeBoxes(Component... components){
        for(Component c: components)
            if (c instanceof TechTreeBox) boxSet.add((TechTreeBox) c);
    }

}
