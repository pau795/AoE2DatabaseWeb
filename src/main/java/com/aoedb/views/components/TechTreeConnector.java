package com.aoedb.views.components;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

@CssImport("./themes/aoe2database/components/tech-tree-box.css")
public class TechTreeConnector extends Div{

    public TechTreeConnector(int width, boolean left){
        setWidth(width + "px");
        addClassNames("tech-tree-connector");
        Div top = new Div();
        top.addClassNames("tech-tree-connector-half");
        Div bottom = new Div();
        bottom.addClassNames("tech-tree-connector-half", "tech-tree-connector-bottom");
        Div bottomEmpty = new Div();
        bottomEmpty.addClassNames("tech-tree-connector-half");
        Div verticalLine = new Div();
        verticalLine.addClassNames("tech-tree-box-vertical-line");
        Div horizontalLine = new Div();
        horizontalLine.addClassNames("tech-tree-box-horizontal-line");
        if(left) bottom.add(bottomEmpty, verticalLine, horizontalLine);
        else bottom.add(horizontalLine, verticalLine, bottomEmpty);
        add(top, bottom);
    }

}
