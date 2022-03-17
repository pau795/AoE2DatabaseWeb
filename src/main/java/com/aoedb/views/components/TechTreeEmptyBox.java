package com.aoedb.views.components;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

@CssImport("./themes/aoe2database/components/tech-tree-box.css")
public class TechTreeEmptyBox extends Div {

    Div line;
    Div topRightLine;
    Div topLeftLine;

    public TechTreeEmptyBox(boolean hasLine){
        addClassNames("tech-tree-empty-box");
        line = new Div();
        line.addClassNames("tech-tree-box-vertical-line");
        topRightLine = new Div();
        topRightLine.addClassNames("tech-tree-box-horizontal-line");
        topLeftLine = new Div();
        topLeftLine.addClassNames("tech-tree-box-horizontal-line");

        add(topLeftLine, line, topRightLine);

        if (hasLine) line.removeClassName("tech-tree-box-line-not-active");
        else line.addClassNames("tech-tree-box-line-not-active");

        topRightLine.addClassName("tech-tree-box-line-not-active");
        topLeftLine.addClassName("tech-tree-box-line-not-active");
    }

    public void setTopRightLine(){
        topRightLine.removeClassName("tech-tree-box-line-not-active");
    }

    public void setTopLeftLine(){
        topLeftLine.removeClassName("tech-tree-box-line-not-active");
    }
}
