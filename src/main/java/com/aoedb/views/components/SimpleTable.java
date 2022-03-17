package com.aoedb.views.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class SimpleTable extends Div {


    public SimpleTable(int columns){
        getStyle().set("display", "grid");
        getStyle().set("grid-template-columns", "repeat("+columns+", minmax(0, 1fr)");
        getStyle().set("border", "1px solid lightGray");
        getStyle().set("width", "fit-content");
        getStyle().set("align-self", "center");
        getStyle().set("margin-top", "15px");
        getStyle().set("margin-bottom", "15px");
    }

    public void addItem(Span... items){
        for(Span c: items){
            c.getStyle().set("border", "1px solid lightGray");
            c.getStyle().set("display", "flex");
            c.getStyle().set("align-items", "center");
            c.getStyle().set("text-align", "center");
            c.getStyle().set("justify-content", "center");
            c.getStyle().set("padding-left", "10px");
            c.getStyle().set("padding-right", "10px");
            add(c);
        }
    }
}
