package com.aoedb.views;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

@CssImport("./themes/aoe2database/layout-view.css")
public abstract class FullContentView extends BaseView{

    @Override
    public void initView() {
        Div mainContainer = new Div();
        mainContainer.addClassNames("layout-grid-container");
        Div content = getContent();
        content.addClassNames("layout-section");
        mainContainer.add(content);
        add(mainContainer);
        addClassNames("layout-view");
        removeClassNames("view-main-layout");


    }

    protected abstract Div getContent();

}
